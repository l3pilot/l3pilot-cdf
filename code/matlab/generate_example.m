function generate_example(name, niterations)
%GENERATE_EXAMPLE Creates an example file of the L3Pilot common data format
%   Low-level Matlab functions for creating the file and the datasets are
%   used, since the high-level functions only support unique datatypes per
%   dataset.
%
% Copyright Institut fuer Kraftfahrzeuge, Johannes Hiller, 2018
%
% SEE ALSO H5WRITEATTFILE, H5WRITEATTRIBUTEARRAY

% Number of timesteps to use
iterations = 1000;
if nargin == 2 && ~isempty(niterations)
    iterations = niterations;
end

if ~strcmp(name(end-1:end), 'h5')
    name = [name '.h5'];
end
%% Open file
if exist(name)
    delete(name)
    file = H5F.create(name);
    cu_file = onCleanup(@()H5F.close(file));
else
    file = H5F.create(name);
    cu_file = onCleanup(@()H5F.close(file));
end

%% Define the structures and data types
structure_definitions;

disp(['Creating a L3Pilot common data format file in version ' ...
    num2str(VERSION)])
script_start = datestr(now);

%% Set the attributes of the file
% See the attached function for details
h5writeattfile(file, '/', 'creation_script', [mfilename '.m']);
h5writeattfile(file, '/', 'creation_date', script_start);
h5writeattfile(file, '/', 'creation_script_version', num2str(VERSION));
h5writeattfile(file, '/', 'author', 'Johannes Hiller');
h5writeattfile(file, '/', 'comment', 'example of a hdf5 file for the L3Pilot project');
h5writeattfile(file, '/', 'institution', 'Institut fuer Kraftfahrzeuge (ika)');
[I,II,III] = H5.get_libversion;
h5writeattfile(file, '/', 'hdf5_version', [num2str(I) '.' num2str(II) '.' num2str(III)]);
ii = ver('matlab');
h5writeattfile(file, '/', 'matlab_version', ii.Release);

% Generate meta data attributes
meta_data.General.ADFVersion = -1;
meta_data.General.FormatVersion = VERSION;
meta_data.General.Partner = 'ika';
meta_data.General.RecordDate = script_start;
meta_data.General.UTCOffset = int32(2);
meta_data.Driver.DriverID = '4AED8067';
meta_data.Driver.DriverType = int8(1);
meta_data.Car.DriveType = int8(4);
meta_data.Car.FuelType = int8(2);
meta_data.Car.NumberOfOccupants = int32(3);
meta_data.Car.PositionFrontBumper = 2.73;
meta_data.Car.PositionRearBumper = 1.13;
meta_data.Car.TransmissionType = int8(1);
meta_data.Car.VehicleLength = 4.87;
meta_data.Car.VehicleWeight = int32(1472);
meta_data.Car.VehicleWidth = 1.903;
meta_data.Experiment.AnalysisEligible = int8(1);
meta_data.Experiment.Baseline = int8(0);
meta_data.Experiment.Country = 'DE';
meta_data.Experiment.TestEndOdo = int32(42424);
meta_data.Experiment.TestEndTime = int32(-1);
meta_data.Experiment.TestSiteType = int8(2);
meta_data.Experiment.TestStartOdo = int32(31313);
meta_data.Experiment.TestStartTime = int32(-1);
meta_data.Experiment.TripID = '6AA62C4A';

% Write the attribute arrays
h5WriteAttributeArray(file, '/', 'metaData', meta_data, type_meta);

%% Create the datasets
% Create the data space for the ego data
space_ego = H5S.create_simple(1, 1, unlimited);
cu_space_ego = onCleanup(@()H5S.close(space_ego));
% Create the dataset properties
dcpl = H5P.create('H5P_DATASET_CREATE');
% In order to activate the deflation filter, chunking needs to be enabled
H5P.set_chunk(dcpl, 1);
% Enable compression by deflating
H5P.set_deflate(dcpl, 9);
% Set the fill values
H5P.set_fill_value(dcpl, type_ego_compound, ego_fill);
% Create the ego dataset
dset_ego = H5D.create(file, 'egoVehicle', type_ego_compound, space_ego, dcpl);
cu_dset_ego = onCleanup(@()H5D.close(dset_ego));

% Create the data space for the lane data
space_lane = H5S.create_simple(1, 1, unlimited);
cu_space_lane = onCleanup(@()H5S.close(space_lane));
% Create the dataset properties
dcpl = H5P.create('H5P_DATASET_CREATE');
% In order to activate the deflation filter, chunking needs to be enabled
H5P.set_chunk(dcpl, 1);
% Enable compression by deflating
H5P.set_deflate(dcpl, 9);
% Set the fill values
H5P.set_fill_value(dcpl, type_lane_compound, lane_fill);
% Create the lane dataset
dset_lane = H5D.create(file, 'laneLines', type_lane_compound, space_lane, dcpl);
cu_dset_lane = onCleanup(@()H5D.close(dset_lane));

% Create the data space for the object data
space_object = H5S.create_simple(1, 1, unlimited);
cu_space_object = onCleanup(@()H5S.close(space_object));
% Create the dataspace properties
dcpl = H5P.create('H5P_DATASET_CREATE');
% In order to activate the deflation filter, chunking needs to be enabled
H5P.set_chunk(dcpl, 1);
% Enable compression by deflating
H5P.set_deflate(dcpl, 9);
% Set the fill values
H5P.set_fill_value(dcpl, type_object_compound, object_fill);
% Create the object dataset
dset_object = H5D.create(file, 'objects', type_object_compound, space_object, dcpl);
cu_dset_object = onCleanup(@()H5D.close(dset_object));

% Create the data space for the positioning data
space_pos = H5S.create_simple(1, 1, unlimited);
cu_space_pos = onCleanup(@()H5S.close(space_pos));
% Create the dataspace properties
dcpl = H5P.create('H5P_DATASET_CREATE');
% In order to activate the deflation filter, chunking needs to be enabled
H5P.set_chunk(dcpl, 1);
% Enable compression by deflating
H5P.set_deflate(dcpl, 9);
% Set the fill values
H5P.set_fill_value(dcpl, type_positioning_compound, positioning_fill);
% Create the positioning dataset
dset_positioning = H5D.create(file, 'positioning', type_positioning_compound, space_pos, dcpl);
cu_dset_positioning = onCleanup(@()H5D.close(dset_positioning));

%% External data
% Create the group for the external data
ext_group = H5G.create(file, 'externalData', 'H5P_DEFAULT', 'H5P_DEFAULT', 'H5P_DEFAULT');
% % Create the data space for the external weather data
% % Since weather data is not part of SP3 Glossary this section is only
% % left here as a reference if you have the data available
% space_ext_wea = H5S.create_simple(1, 1, unlimited);
% cu_space_ext = onCleanup(@()H5S.close(space_ext_wea));
% % Create the dataspace properties
% dcpl = H5P.create('H5P_DATASET_CREATE');
% % In order to activate the deflation filter, chunking needs to be enabled
% H5P.set_chunk(dcpl, 1);
% % Enable compression by deflating
% H5P.set_deflate(dcpl, 9);
% % Set the fill values
% H5P.set_fill_value(dcpl, type_weather_compound, weather_fill);
% % Create the external weather data dataset
% dset_external_wea = H5D.create(ext_group, 'weather', type_weather_compound, space_ext_wea, dcpl);
% cu_dset_external_wea = onCleanup(@()H5D.close(dset_external_wea));

% Create the data space for the external map data
space_ext_map = H5S.create_simple(1, 1, unlimited);
cu_space_ext = onCleanup(@()H5S.close(space_ext_map));
% Create the dataspace properties
dcpl = H5P.create('H5P_DATASET_CREATE');
% In order to activate the deflation filter, chunking needs to be enabled
H5P.set_chunk(dcpl, 1);
% Enable compression by deflating
H5P.set_deflate(dcpl, 9);
% Set the fill values
H5P.set_fill_value(dcpl, type_map_compound, map_fill);
% Create the external map data dataset
dset_external_map = H5D.create(ext_group, 'map', type_map_compound, space_ext_map, dcpl);
cu_dset_external_map = onCleanup(@()H5D.close(dset_external_map));

%% Generate random data
presize = [iterations, 1];
presize_obj = [iterations, NUMBER_OBJECTS];
presize_lane = [iterations, NUMBER_LANES];
% Ego data
ego_data.UTCTime = int64(posixtime(datetime(script_start)) * 1e3 + ...
    (0:(iterations - 1))' * 1e2);
ego_data.FileTime = (0:0.1:(iterations - 1) / 10)';
ego_data.ABSIntervention = int8(randi([0, 1], presize));
ego_data.ADFunctionActive = int8(ones(presize));
ego_data.ADFunctionAvailable = int8(ones(presize));
ego_data.AmbientLightLevel = (rand(presize) - 0.5) * 20;
ego_data.AmbientTemperature = 20.3 * ones(presize);
ego_data.BaselineADASActive = int32(randi([0, 1024], presize));
ego_data.BaselineADASIntervention = int32(randi([0, 1024], presize));
ego_data.BrakeLight = int8(randi([0, 2], presize));
ego_data.BrakePedalPos = int32(randi([0, 100], presize));
ego_data.BrakePressue = int32(randi([0, 100], presize));
ego_data.DirectionIndicator = int8(randi([0, 2], presize));
ego_data.EnergyConsumption = rand(presize) * 40;
ego_data.ESCIntervention = int8(randi([0, 1], presize));
ego_data.FrontFogLightStatus = int8(randi([0, 1], presize));
ego_data.FrontWiperStatus = int8(randi([0, 4], presize));
ego_data.FuelConsumption = rand(presize) * 6 + 4.2;
ego_data.HandsOnDetection = int8(randi([0, 2], presize));
ego_data.LatAcceleration = (rand(presize) - 0.5) * 5;
ego_data.LongAcceleration = (rand(presize) - 0.5) * 10;
ego_data.Odometer = linspace(42, 4242, iterations)';
ego_data.RearFogLightStatus = int8(randi([0, 1], presize));
ego_data.SteeringAngle = (rand(presize) - 0.5) * 10 * pi;
ego_data.SteeringAngleADF = (rand(presize) - 0.5) * 10 * pi;
ego_data.ThrottlePedalPos = int32(randi([0, 100], presize));
ego_data.TOR = int8(randi([0, 1], presize));
ego_data.TorsionBarTorque = rand(presize) * 100;
ego_data.VehicleSpeed = rand(presize) * 40;
ego_data.YawRate = (rand(presize) - 0.5) * 0.25 * pi;

% Lane data
lane_data.UTCTime = int64(posixtime(datetime(script_start)) * 1e3 + ...
    (0:(iterations - 1))' * 1e2);
lane_data.FileTime = (0:0.1:(iterations - 1) / 10)';
lane_data.EgoLaneWidth = rand(presize) * 5;
lane_data.sLaneLine.Curvature = (rand(presize_lane) - 0.5) * 0.02;
lane_data.sLaneLine.CurvatureDx = (rand(presize_lane) - 0.5) * 0.0002;
lane_data.sLaneLine.Dy = (rand(presize_lane) - 0.5) * 15;
lane_data.sLaneLine.QualityIndex = int32(randi([0, 3], presize_lane));
lane_data.sLaneLine.MarkingType = int8(randi([0, 5], presize_lane));
lane_data.sLaneLine.YawAngle = (rand(presize_lane) - 0.5) * 0.5 * pi;

% Object data
object_data.UTCTime = int64(posixtime(datetime(script_start)) * 1e3 + ...
    (0:(iterations - 1))' * 1e2);
object_data.FileTime = (0:0.1:(iterations - 1) / 10)';
object_data.LeadVehicleID = int32(randi([0, 4242], presize));
object_data.NumberOfObjects = int32(randi([0, 32], presize));
object_data.sObject.Classification = int8(randi([0, 6], presize_obj));
object_data.sObject.Height = (rand(presize_obj) - 0.5) + 2.5;
object_data.sObject.ID = int32(randi([0, 4242], presize_obj));
object_data.sObject.LatPosition = (rand(presize_obj) - 0.5) * 80;
object_data.sObject.LatVelocity = (rand(presize_obj) - 0.5) * 20;
object_data.sObject.Length = (rand(presize_obj) - 0.5) * 15 + 10.5;
object_data.sObject.LongPosition = (rand(presize_obj) - 0.5) * 320;
object_data.sObject.LongVelocity = (rand(presize_obj) - 0.5) * 60;
object_data.sObject.Width = (rand(presize_obj) - 0.5) + 2.5;
object_data.sObject.YawAngle = (rand(presize_obj) - 0.5) * 2 * pi;
object_data.sObject.YawRate = (rand(presize_obj) - 0.5) * 20;

% Positioning data
positioning_data.UTCTime = int64(posixtime(datetime(script_start)) * 1e3 + ...
    (0:(iterations - 1))' * 1e2);
positioning_data.FileTime = (0:0.1:(iterations - 1) / 10)';
positioning_data.Altitude = 8848 * ones(presize);
positioning_data.GNSSSpeed = rand(presize) * 40;
positioning_data.GNSSTime = int64(posixtime(datetime(script_start)) * 1e3 + ...
    (0:(iterations - 1))' * 1e2);
positioning_data.Heading = rand(presize) * 2 * pi;
positioning_data.Latitude = 50.7862653 * ones(presize);
positioning_data.Longitude = 6.0456767 * ones(presize);
positioning_data.NumberOfSatellites = int32(17 * ones(presize));


% Map data
map_data.UTCTime = int64(posixtime(datetime(script_start)) * 1e3 + ...
    (0:(iterations - 1))' * 1e2);
map_data.FileTime = (0:0.1:(iterations - 1) / 10)';

%create three different segments at least 100 samples, max 333 (first two)
%there is an intersection in the beginning of the third segment
slimitlength(1) = round(100+(333)*rand);
slimitlength(2) = round(100+(333)*rand);
sum2Seg = sum(slimitlength);
slimitlength(3) = 1000-sum2Seg;
slimits = int32(round(1 + (12)*rand(3,1)));
nofLanes = round(1 + (4)*rand(3,1));
roadType = round(1 + (4)*rand(3,1));

map_data.DistIntersection = zeros(presize);
map_data.DistIntersection (sum2Seg-99:sum2Seg) = cumsum(randi([1,100],100,1)/10,1,'reverse');
map_data.NumberOfLanes = [int32(ones(slimitlength(1),1)*nofLanes(1)); int32(ones(slimitlength(2),1)*nofLanes(2)); int32(ones(slimitlength(3),1)*nofLanes(3))];
map_data.RoadType = [int8(ones(slimitlength(1),1)*roadType(1)); int8(ones(slimitlength(2),1)*roadType(2)); int8(ones(slimitlength(3),1)*roadType(3))];
map_data.RulesIntersection = int8(zeros(presize)-1);
map_data.RulesIntersection(sum2Seg:sum2Seg+50) = int8(round(0 + (4)*rand(1,1)));
map_data.SpeedLimit = [int32(ones(slimitlength(1),1))*slimits(1); int32(ones(slimitlength(2),1))*slimits(2); int32(ones(slimitlength(3),1))*slimits(3)];
map_data.TypeIntersection = int8(zeros(presize));
map_data.TypeIntersection(sum2Seg:sum2Seg+50) = int8(round(1 + (16)*rand(1,1)));

% % weather data
% % Since weather data is not part of SP3 Glossary this section is only
% % left here as a reference if you have the data available
% weather_data.UTCTime = int64(posixtime(datetime(script_start)) * 1e3 + ...
%     (0:(iterations - 1))' * 1e2);
% weather_data.FileTime = (0:0.1:(iterations - 1) / 10)';
% weather_data.Precipitation = int8(round(0 + (2)*rand(presize)));
% 
% weather_data.PrecipitationType = int8(round(0 + (1)*rand(presize)));
% weather_data.RoadSurfaceCondition = int8(round(0 + (1)*rand(presize)));
% weather_data.Temperature = round(15 + (7)*rand(presize));
% weather_data.Weather = int8(round(0 + (5)*rand(presize)));


%% Write data to the file
% Write data to the ego dataset
% Extend the dataset to include all the data
H5D.set_extent(dset_ego, length(ego_data.FileTime));
% Write the datastruct to the dataset
H5D.write(dset_ego, type_ego_compound, 'H5S_ALL', 'H5S_ALL', ...
    'H5P_DEFAULT', ego_data);

% Write data to the lanes dataset
% Extend the dataset to include all the data
H5D.set_extent(dset_lane, length(lane_data.FileTime));
% Write the datastruct to the dataset
H5D.write(dset_lane, type_lane_compound, 'H5S_ALL', 'H5S_ALL', ...
    'H5P_DEFAULT', lane_data);

% Write data to the objects dataset
% Extend the dataset to include all the data
H5D.set_extent(dset_object, length(object_data.FileTime));
% Write the datastruct to the dataset
H5D.write(dset_object, type_object_compound, 'H5S_ALL', 'H5S_ALL', ...
    'H5P_DEFAULT', object_data);

% Write data to the positioning dataset
% Extend the dataset to include all the data
H5D.set_extent(dset_positioning, length(positioning_data.FileTime));
% Write the datastruct to the dataset
H5D.write(dset_positioning, type_positioning_compound, 'H5S_ALL', ...
    'H5S_ALL', 'H5P_DEFAULT', positioning_data);


% Write data to the external map dataset
% Extend the dataset to include all the data
H5D.set_extent(dset_external_map, length(map_data.FileTime));
% Write the datastruct to the dataset
H5D.write(dset_external_map, type_map_compound, 'H5S_ALL', ...
    'H5S_ALL', 'H5P_DEFAULT', map_data);

% % <Write data to the external weather dataset>
% % Extend the dataset to include all the data
% % Since weather data is not part of SP3 Glossary this section is only
% % left here as a reference if you have the data available
% H5D.set_extent(dset_external_wea, length(weather_data.FileTime));
% % Write the datastruct to the dataset
% H5D.write(dset_external_wea, type_weather_compound, 'H5S_ALL', ...
%     'H5S_ALL', 'H5P_DEFAULT', weather_data);
% % </Write data to the external weather dataset>

%% Write data to the file piecewise
% Write data to the ego dataset
ego_fspace = H5D.get_space(dset_ego);
cu_ego_space = onCleanup(@()H5S.close(ego_fspace));
% Get the extent of the current space
[~, ego_dims] = H5S.get_simple_extent_dims(ego_fspace);
% Increase the size
new_ego_dims = ego_dims + 1;
% Increase the dataspace
H5D.set_extent(dset_ego, new_ego_dims);
% Get the new increased datasapce
ego_fspace = H5D.get_space(dset_ego);
% The offset for writing
ego_offset = new_ego_dims - 1;
% Select the hyperslab used for writing
H5S.select_hyperslab(ego_fspace, 'H5S_SELECT_SET', ego_offset, [], 1, []);
ego_fields = fieldnames(ego_data);
for jj = 1:length(ego_fields)
    single_ego_data.(ego_fields{jj}) = ego_data.(ego_fields{jj})(42);
end
% Write the data to the dataset
H5D.write(dset_ego, type_ego_compound, space_ego, ego_fspace, 'H5P_DEFAULT', single_ego_data);

%% Write bigger slabs
ego_fields = fieldnames(ego_data);
for jj = 1:length(ego_fields)
    single_ego_data.(ego_fields{jj}) = ego_data.(ego_fields{jj})(42:51);
end
pieces = 0:10:100;
% Get the datasapce
ego_fspace = H5D.get_space(dset_ego);
for idx = 1:length(pieces)-1
    % Get the extent of the current space
    [~, ego_dims] = H5S.get_simple_extent_dims(ego_fspace);
    % Increase the size
    new_ego_dims = ego_dims + (pieces(idx+1) - pieces(idx));
    % Increase the dataspace
    H5D.set_extent(dset_ego, new_ego_dims);
    % Get the new increased datasapce
    ego_fspace = H5D.get_space(dset_ego);
    % Create the slab data space for the ego data
    slab_space_ego = H5S.create_simple(1, pieces(idx+1) - pieces(idx), unlimited);
    cu_slab_space_ego = onCleanup(@()H5S.close(slab_space_ego));
    % The offset for writing
    ego_offset = ego_dims(1);
    % Select the hyperslab used for writing
    H5S.select_hyperslab(ego_fspace, 'H5S_SELECT_SET', ego_offset, [], [pieces(idx+1) - pieces(idx)], []);
    % Write the data to the dataset
    H5D.write(dset_ego, type_ego_compound, slab_space_ego, ego_fspace, 'H5P_DEFAULT', single_ego_data);
end

%% Housekeeping
% Close all opened data types
cleanup_structure;
end
