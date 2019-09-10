% This file contains all the structures needed for the L3Pilot Common Data
% Format. You can call this script in any function or script where you need
% the definitions. Be sure to call the cleanup script afterwards for
% closing all the datatypes again. If not done, the file can not be
% correctly closed and Matlab will not close the file.
%
% Copyright Institut fuer Kraftfahrzeuge, Johannes Hiller, 2018

% Version of the format
VERSION = 0.8;
% Number of lanes
NUMBER_LANES = 4;
% Number of objects
NUMBER_OBJECTS = 32;

%% General datatypes
% Initialize the datatypes for easier access later
unlimited = H5ML.get_constant_value('H5S_UNLIMITED');
type_str = H5T.copy('H5T_C_S1');
H5T.set_size(type_str, 'H5T_VARIABLE');
H5T.set_strpad(type_str, 'H5T_STR_NULLTERM');

% Create a string type with 7 bytes length
type_str_s = H5T.copy('H5T_C_S1');
H5T.set_size(type_str_s, 7);
H5T.set_strpad(type_str_s, 'H5T_STR_NULLTERM');

% Create a string type with 21 bytes length
type_str_t = H5T.copy('H5T_C_S1');
H5T.set_size(type_str_t, 21);
H5T.set_strpad(type_str_t, 'H5T_STR_NULLTERM');

% Create a string type with 3 bytes length
type_str_3 = H5T.copy('H5T_C_S1');
H5T.set_size(type_str_3, 3);
H5T.set_strpad(type_str_3, 'H5T_STR_NULLTERM');

% Create a string type with 8 bytes length
type_str_8 = H5T.copy('H5T_C_S1');
H5T.set_size(type_str_8, 8);
H5T.set_strpad(type_str_8, 'H5T_STR_NULLTERM');

%% Enums
% Enum for the activation of systems
active_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(active_enum,      'H5T_ORDER_LE');
H5T.set_sign(active_enum,       'H5T_SGN_2');
H5T.enum_insert(active_enum,    'NOT_APPLICABLE', -1);
H5T.enum_insert(active_enum,    'NOT_ACTIVE', 0);
H5T.enum_insert(active_enum,    'ACTIVE', 1);
H5T.enum_insert(active_enum,    'UNKNOWN', 9);
% Enum for the availability of systems
available_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(available_enum,   'H5T_ORDER_LE');
H5T.set_sign(available_enum,    'H5T_SGN_2');
H5T.enum_insert(available_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(available_enum, 'NOT_AVAILABLE', 0);
H5T.enum_insert(available_enum, 'AVAILABLE', 1);
H5T.enum_insert(available_enum, 'UNKNOWN', 9);
% Enum for brake light status
brake_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(brake_enum,   'H5T_ORDER_LE');
H5T.set_sign(brake_enum,    'H5T_SGN_2');
H5T.enum_insert(brake_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(brake_enum, 'OFF', 0);
H5T.enum_insert(brake_enum, 'ON', 1);
H5T.enum_insert(brake_enum, 'FLASHING', 2);
H5T.enum_insert(brake_enum, 'UNKNOWN', 9);
% Enum for the indicator status
indicator_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(indicator_enum,   'H5T_ORDER_LE');
H5T.set_sign(indicator_enum,    'H5T_SGN_2');
H5T.enum_insert(indicator_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(indicator_enum, 'OFF', 0);
H5T.enum_insert(indicator_enum, 'LEFT', 1);
H5T.enum_insert(indicator_enum, 'RIGHT', 2);
H5T.enum_insert(indicator_enum, 'HAZARD', 3);
H5T.enum_insert(indicator_enum, 'UNKNOWN', 9);
% Enum for the hands-on detection
hands_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(hands_enum,   'H5T_ORDER_LE');
H5T.set_sign(hands_enum,    'H5T_SGN_2');
H5T.enum_insert(hands_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(hands_enum, 'HANDS_OFF', 0);
H5T.enum_insert(hands_enum, 'ONE_HAND', 1);
H5T.enum_insert(hands_enum, 'TWO_HANDS', 2);
H5T.enum_insert(hands_enum, 'UNKNOWN', 9);
% Enum for the front wiper status
wiper_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(wiper_enum,   'H5T_ORDER_LE');
H5T.set_sign(wiper_enum,    'H5T_SGN_2');
H5T.enum_insert(wiper_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(wiper_enum, 'OFF', 0);
H5T.enum_insert(wiper_enum, 'SLOW_INTERVAL', 1);
H5T.enum_insert(wiper_enum, 'FAST_INTERVAL', 2);
H5T.enum_insert(wiper_enum, 'SLOW', 3);
H5T.enum_insert(wiper_enum, 'FAST', 4);
H5T.enum_insert(wiper_enum, 'UNKNOWN', 9);
% Enum for a system that can be on and off
on_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(on_enum,      'H5T_ORDER_LE');
H5T.set_sign(on_enum,       'H5T_SGN_2');
H5T.enum_insert(on_enum,    'NOT_APPLICABLE', -1);
H5T.enum_insert(on_enum,    'OFF', 0);
H5T.enum_insert(on_enum,    'ON', 1);
H5T.enum_insert(on_enum,    'UNKNOWN', 9);
% Enum for the TOR
tor_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(tor_enum, 'H5T_ORDER_LE');
H5T.set_sign(tor_enum, 'H5T_SGN_2');
H5T.enum_insert(tor_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(tor_enum, 'NOT_ACTIVE', 0);
H5T.enum_insert(tor_enum, 'ACTIVE', 1);
H5T.enum_insert(tor_enum, 'SHORT_ACTIVE', 2);
H5T.enum_insert(tor_enum, 'UNKNOWN', 9);
% Enum for the classification of objects
obj_classification_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(obj_classification_enum,      'H5T_ORDER_LE');
H5T.set_sign(obj_classification_enum,       'H5T_SGN_2');
H5T.enum_insert(obj_classification_enum,    'NOT_APPLICABLE', -1);
H5T.enum_insert(obj_classification_enum,    'OTHER', 0);
H5T.enum_insert(obj_classification_enum,    'CAR', 1);
H5T.enum_insert(obj_classification_enum,    'TRUCK', 2);
H5T.enum_insert(obj_classification_enum,    'MOTORCYCLE', 3);
H5T.enum_insert(obj_classification_enum,    'BICYCLE', 4);
H5T.enum_insert(obj_classification_enum,    'PEDESTRIAN', 5);
H5T.enum_insert(obj_classification_enum,    'UNKNOWN', 9);
% Enum for the type of lane marking
lane_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(lane_enum,    'H5T_ORDER_LE');
H5T.set_sign(lane_enum,     'H5T_SGN_2');
H5T.enum_insert(lane_enum,  'NOT_APPLICABLE', -1);
H5T.enum_insert(lane_enum,  'NONE', 0);
H5T.enum_insert(lane_enum,  'CONTINUOUS', 1);
H5T.enum_insert(lane_enum,  'DASHED', 2);
H5T.enum_insert(lane_enum,  'DOTS', 3);
H5T.enum_insert(lane_enum,  'DOUBLE', 4);
H5T.enum_insert(lane_enum,  'OTHER', 5);
H5T.enum_insert(lane_enum,  'UNKNOWN', 9);
% Enum for the amount of precipitation
precipitation_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(precipitation_enum,   'H5T_ORDER_LE');
H5T.set_sign(precipitation_enum,    'H5T_SGN_2');
H5T.enum_insert(precipitation_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(precipitation_enum, 'NONE', 0);
H5T.enum_insert(precipitation_enum, 'LIGHT', 1);
H5T.enum_insert(precipitation_enum, 'HEAVY', 2);
H5T.enum_insert(precipitation_enum, 'UNKNOWN', 9);
% Enum for precipitation type
precipitation_type_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(precipitation_type_enum,      'H5T_ORDER_LE');
H5T.set_sign(precipitation_type_enum,       'H5T_SGN_2');
H5T.enum_insert(precipitation_type_enum,    'NOT_APPLICABLE', -1);
H5T.enum_insert(precipitation_type_enum,    'NONE', 0);
H5T.enum_insert(precipitation_type_enum,    'RAIN', 1);
H5T.enum_insert(precipitation_type_enum,    'SNOW', 2);
H5T.enum_insert(precipitation_type_enum,    'HAIL', 3);
H5T.enum_insert(precipitation_type_enum,    'UNKNOWN', 9);
% Enum f0or the weather conditions
weather_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(weather_enum,     'H5T_ORDER_LE');
H5T.set_sign(weather_enum,      'H5T_SGN_2');
H5T.enum_insert(weather_enum,   'NOT_APPLICABLE', -1);
H5T.enum_insert(weather_enum,   'OTHER', 0);
H5T.enum_insert(weather_enum,   'NO_ADVERSE_CONDITIONS', 1);
H5T.enum_insert(weather_enum,   'WIND_GUSTS', 2);
H5T.enum_insert(weather_enum,   'FOG', 3);
H5T.enum_insert(weather_enum,   'MIST_OR_LIGHT_RAIN', 4);
H5T.enum_insert(weather_enum,   'RAINING', 5);
H5T.enum_insert(weather_enum,   'SNOWING', 6);
H5T.enum_insert(weather_enum,   'SLEETING', 7);
H5T.enum_insert(weather_enum,   'RAIN_AND_FOG', 8);
H5T.enum_insert(weather_enum,   'SNOW_OR_SLEET_AND_FOG', 9);
H5T.enum_insert(weather_enum,   'UNKNOWN', 99);
% Enum for the road surface conditions
road_surface_condition_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(road_surface_condition_enum,      'H5T_ORDER_LE');
H5T.set_sign(road_surface_condition_enum,       'H5T_SGN_2');
H5T.enum_insert(road_surface_condition_enum,    'NOT_APPLICABLE', -1);
H5T.enum_insert(road_surface_condition_enum,    'DRY', 0);
H5T.enum_insert(road_surface_condition_enum,    'WET', 1);
H5T.enum_insert(road_surface_condition_enum,    'ICE', 2);
H5T.enum_insert(road_surface_condition_enum,    'SNOW', 3);
H5T.enum_insert(road_surface_condition_enum,    'UNKNOWN', 9);
% Enum for construction site description
construction_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(construction_enum,    'H5T_ORDER_LE');
H5T.set_sign(construction_enum,     'H5T_SGN_2');
H5T.enum_insert(construction_enum,  'NOT_APPLICABLE', -1);
H5T.enum_insert(construction_enum,  'OTHER', 0);
H5T.enum_insert(construction_enum,  'NOT_CONSTRUCTION_ZONE_RELATED', 1);
H5T.enum_insert(construction_enum,  'CONSTRUCTION_ZONE', 2);
H5T.enum_insert(construction_enum,  'CONSTRUCTION_ZONE_RELATED', 3);
H5T.enum_insert(construction_enum,  'UNKNOWN', 9);
% Enum for the type of road the ego is travelling on
road_type_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(road_type_enum,   'H5T_ORDER_LE');
H5T.set_sign(road_type_enum,    'H5T_SGN_2');
H5T.enum_insert(road_type_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(road_type_enum, 'OTHER', 0);
H5T.enum_insert(road_type_enum, 'DUAL_CARRIAGEWAY_MULTIPLE_LANES', 1);
H5T.enum_insert(road_type_enum, 'DUAL_CARRIAGEWAY_2_LANES', 2);
H5T.enum_insert(road_type_enum, 'DUAL_CARRIAGEWAY_2_1_LANES', 3);
H5T.enum_insert(road_type_enum, 'DUAL_CARRIAGEWAY_SINGLE_LANES', 4);
H5T.enum_insert(road_type_enum, 'SINGLE_CARRIAGEWAY_MULTIPLE_LANES', 5);
H5T.enum_insert(road_type_enum, 'SINGLE_CARRIAGEWAY_4_LANES', 6);
H5T.enum_insert(road_type_enum, 'SINGLE_CARRIAGEWAY_3_LANES', 7);
H5T.enum_insert(road_type_enum, 'SINGLE_CARRIAGEWAY_2_LANES', 8);
H5T.enum_insert(road_type_enum, 'WIDE_LANE_ROAD', 9);
H5T.enum_insert(road_type_enum, 'SINGLE_TRACK_ROAD', 10);
H5T.enum_insert(road_type_enum, 'SINGLE_TRACK_ROAD_ONE_WAY', 11);
H5T.enum_insert(road_type_enum, 'PARKING_LOT_OR_RAMP', 12);
H5T.enum_insert(road_type_enum, 'ENTRANCE_OR_EXIT_RAMP', 13);
H5T.enum_insert(road_type_enum, 'DRIVEWAY_OR_ALLEY', 14);
H5T.enum_insert(road_type_enum, 'GRAVEL_ROAD', 15);
H5T.enum_insert(road_type_enum, 'INTERSECTION', 16);
H5T.enum_insert(road_type_enum, 'UNKNOWN', 99);
% Enum for the rules at an intersection
rules_intersection_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(rules_intersection_enum,      'H5T_ORDER_LE');
H5T.set_sign(rules_intersection_enum,       'H5T_SGN_2');
H5T.enum_insert(rules_intersection_enum,    'NOT_APPLICABLE', -1);
H5T.enum_insert(rules_intersection_enum,    'OTHER', 0);
H5T.enum_insert(rules_intersection_enum,    'REGULATED_BY_LAW_ONLY', 1);
H5T.enum_insert(rules_intersection_enum,    'TRAFFIC_SIGNS_AND_ROAD_MARKINGS', 2);
H5T.enum_insert(rules_intersection_enum,    'TRAFFIC_LIGHTS_ALLOWING_PARTIAL_CONFLICTS', 3);
H5T.enum_insert(rules_intersection_enum,    'TRAFFIC_LIGHTS_NOT_ALLOWING_PARTIAL_CONFLICTS', 4);
H5T.enum_insert(rules_intersection_enum,    'UNKNOWN', 9);
% Enum for the type of intersction
intersection_type_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(intersection_type_enum,   'H5T_ORDER_LE');
H5T.set_sign(intersection_type_enum,    'H5T_SGN_2');
H5T.enum_insert(intersection_type_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(intersection_type_enum, 'NONE', 0);
H5T.enum_insert(intersection_type_enum, 'X_INTERSECTION', 1);
H5T.enum_insert(intersection_type_enum, 'T_INTERSECTION_RIGHT', 2);
H5T.enum_insert(intersection_type_enum, 'T_INTERSECTION_LEFT', 3);
H5T.enum_insert(intersection_type_enum, 'T_INTERSECTION_BY_ROAD', 4);
H5T.enum_insert(intersection_type_enum, 'Y_INTERSECTION', 5);
H5T.enum_insert(intersection_type_enum, 'ROUNDABOUT', 6);
H5T.enum_insert(intersection_type_enum, '5_OR_MORE_LEGS', 7);
H5T.enum_insert(intersection_type_enum, 'MERGING_LANE', 8);
H5T.enum_insert(intersection_type_enum, 'PASSING_BY_MERGING_LANES', 9);
H5T.enum_insert(intersection_type_enum, 'EXIT_OR_TURNING_LANE', 10);
H5T.enum_insert(intersection_type_enum, 'COMPLEX_INTERSECTION', 11);
H5T.enum_insert(intersection_type_enum, 'UNKNOWN', 99);
% Enum for the type of subject
driver_type_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(driver_type_enum,    'H5T_ORDER_LE');
H5T.set_sign(driver_type_enum,     'H5T_SGN_2');
H5T.enum_insert(driver_type_enum,  'NOT_APPLICABLE', -1);
H5T.enum_insert(driver_type_enum,  'UNKNOWN', 0);
H5T.enum_insert(driver_type_enum,  'PROFESSIONAL', 1);
H5T.enum_insert(driver_type_enum,  'COMPANY_EMPLOYEE', 2);
H5T.enum_insert(driver_type_enum,  'ORDINARY_SUPERVISION', 3);
H5T.enum_insert(driver_type_enum,  'ORDINARY', 4);
H5T.enum_insert(driver_type_enum,  'NAIVE_PASSENGER', 5);
% Enum for the type of drive
drive_type_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(drive_type_enum,      'H5T_ORDER_LE');
H5T.set_sign(drive_type_enum,       'H5T_SGN_2');
H5T.enum_insert(drive_type_enum,    'NOT_APPLICABLE', -1);
H5T.enum_insert(drive_type_enum,    'UNKNOWN', 0);
H5T.enum_insert(drive_type_enum,    'ICE', 1);
H5T.enum_insert(drive_type_enum,    'ELECTRIC_MOTOR', 2);
H5T.enum_insert(drive_type_enum,    'HYBRID', 3);
H5T.enum_insert(drive_type_enum,    'HORSE', 4);
% Enum for the fuel used
fuel_type_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(fuel_type_enum,   'H5T_ORDER_LE');
H5T.set_sign(fuel_type_enum,    'H5T_SGN_2');
H5T.enum_insert(fuel_type_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(fuel_type_enum, 'NA_EV', 0);
H5T.enum_insert(fuel_type_enum, 'GASOLINE', 1);
H5T.enum_insert(fuel_type_enum, 'DIESEL', 2);
H5T.enum_insert(fuel_type_enum, 'H2', 3);
H5T.enum_insert(fuel_type_enum, 'FOSSIL_GAS', 4);
H5T.enum_insert(fuel_type_enum, 'OTHER', 5);
% Enum for the type of transmission
transmission_type_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(transmission_type_enum,    'H5T_ORDER_LE');
H5T.set_sign(transmission_type_enum,     'H5T_SGN_2');
H5T.enum_insert(transmission_type_enum,  'NOT_APPLICABLE', -1);
H5T.enum_insert(transmission_type_enum,  'NONE', 0);
H5T.enum_insert(transmission_type_enum,  'MANUAL', 1);
H5T.enum_insert(transmission_type_enum,  'AUTOMATIC', 2);
H5T.enum_insert(transmission_type_enum,  'OTHER', 3);
% Enum for the eligiability of the analysis
analysis_eligible_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(analysis_eligible_enum,   'H5T_ORDER_LE');
H5T.set_sign(analysis_eligible_enum,    'H5T_SGN_2');
H5T.enum_insert(analysis_eligible_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(analysis_eligible_enum, 'NOT_ELIGIBLE', 0);
H5T.enum_insert(analysis_eligible_enum, 'ELIGIBLE', 1);
H5T.enum_insert(analysis_eligible_enum, 'UNKNOWN', 2);
% Enum to determine baseline trips
baseline_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(baseline_enum,    'H5T_ORDER_LE');
H5T.set_sign(baseline_enum,     'H5T_SGN_2');
H5T.enum_insert(baseline_enum,  'NOT_APPLICABLE', -1);
H5T.enum_insert(baseline_enum,  'NOT_BASELINE', 0);
H5T.enum_insert(baseline_enum,  'BASELINE', 1);
H5T.enum_insert(baseline_enum,  'UNKNOWN', 2);
% Enum for the test site
test_site_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(test_site_enum,   'H5T_ORDER_LE');
H5T.set_sign(test_site_enum,    'H5T_SGN_2');
H5T.enum_insert(test_site_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(test_site_enum, 'UNKNOWN', 0);
H5T.enum_insert(test_site_enum, 'TEST_TRACK', 1);
H5T.enum_insert(test_site_enum, 'PUBLIC_ROAD', 2);
H5T.enum_insert(test_site_enum, 'PRIVATE_PARKING', 3);
H5T.enum_insert(test_site_enum, 'PUBLIC_PARKING', 4);

%% Create the ego compound
% The ego compound
type_ego_compound = H5T.create('H5T_COMPOUND', 143);
% Insert the data members
H5T.insert(type_ego_compound, 'UTCTime', 0,                     'H5T_STD_I64LE');
ego_fill.UTCTime = int64(-1);
H5T.insert(type_ego_compound, 'FileTime', 8,                    'H5T_IEEE_F64LE');
ego_fill.FileTime = NaN;
H5T.insert(type_ego_compound, 'ABSIntervention', 16,            active_enum);
ego_fill.ABSIntervention = int8(-1);
H5T.insert(type_ego_compound, 'ADFunctionActive', 17,           active_enum);
ego_fill.ADFunctionActive = int8(-1);
H5T.insert(type_ego_compound, 'ADFunctionAvailable', 18,        available_enum);
ego_fill.ADFunctionAvailable = int8(-1);
H5T.insert(type_ego_compound, 'AmbientLightLevel', 19,          'H5T_IEEE_F64LE');
ego_fill.AmbientLightLevel = NaN;
H5T.insert(type_ego_compound, 'AmbientTemperature', 27,         'H5T_IEEE_F64LE');
ego_fill.AmbientTemperature = NaN;
H5T.insert(type_ego_compound, 'BaselineADASActive', 35,         'H5T_STD_I32LE');
ego_fill.BaselineADASActive = int32(-1);
H5T.insert(type_ego_compound, 'BaselineADASIntervention', 39,    'H5T_STD_I32LE');
ego_fill.BaselineADASIntervention = int32(-1);
H5T.insert(type_ego_compound, 'BrakeLight', 43,                 brake_enum);
ego_fill.BrakeLight = int8(-1);
H5T.insert(type_ego_compound, 'BrakePedalPos', 44,              'H5T_STD_I32LE');
ego_fill.BrakePedalPos = int32(-1);
H5T.insert(type_ego_compound, 'BrakePressure', 48,              'H5T_STD_I32LE');
ego_fill.BrakePressure = int32(-1);
H5T.insert(type_ego_compound, 'DirectionIndicator', 52,         indicator_enum);
ego_fill.DirectionIndicator = int8(-1);
H5T.insert(type_ego_compound, 'EnergyConsumption', 53,          'H5T_IEEE_F64LE');
ego_fill.EnergyConmsumption = NaN;
H5T.insert(type_ego_compound, 'ESCIntervention', 61,             active_enum);
ego_fill.ESCIntervention = int8(-1);
H5T.insert(type_ego_compound, 'FrontFogLightStatus', 62,        on_enum);
ego_fill.FrontFogLightStatus = int8(-1);
H5T.insert(type_ego_compound, 'FrontWiperStatus', 63,           wiper_enum);
ego_fill.FrontWiper = int8(-1);
H5T.insert(type_ego_compound, 'FuelConsumption', 64,            'H5T_IEEE_F64LE');
ego_fill.FuelConsumption = NaN;
H5T.insert(type_ego_compound, 'HandsOnDetection', 72,           hands_enum);
ego_fill.HandsOnDetection = int8(-1);
H5T.insert(type_ego_compound, 'LatAcceleration', 73,            'H5T_IEEE_F64LE');
ego_fill.LatAcceleration = NaN;
H5T.insert(type_ego_compound, 'LongAcceleration', 81,          'H5T_IEEE_F64LE');
ego_fill.LongAcceleration = NaN;
H5T.insert(type_ego_compound, 'Odometer', 89,                  'H5T_IEEE_F64LE');
ego_fill.Odometer = NaN;
H5T.insert(type_ego_compound, 'RearFogLightStatus', 97,         on_enum);
ego_fill.RearFogLightStatus = int8(-1);
H5T.insert(type_ego_compound, 'SteeringAngle', 98,              'H5T_IEEE_F64LE');
ego_fill.SteeringAngle = NaN;
H5T.insert(type_ego_compound, 'SteeringAngleADF', 106,          'H5T_IEEE_F64LE');
ego_fill.SteeringAngleRate = NaN;
H5T.insert(type_ego_compound, 'ThrottlePedalPos', 114,          'H5T_STD_I32LE');
ego_fill.ThrottlePedalPos = int32(-1);
H5T.insert(type_ego_compound, 'TOR', 118,                       tor_enum);
ego_fill.TOR = int8(-1);
H5T.insert(type_ego_compound, 'TorsionBarTorque', 119,          'H5T_IEEE_F64LE');
ego_fill.TorsionBarTorque = NaN;
H5T.insert(type_ego_compound, 'VehicleSpeed', 127,              'H5T_IEEE_F64LE');
ego_fill.VehicleSpeed = NaN;
H5T.insert(type_ego_compound, 'YawRate', 135,                   'H5T_IEEE_F64LE');
ego_fill.YawRate = NaN;

%% Create the lane compound
% The lane compound
type_lane_compound = H5T.create('H5T_COMPOUND', 172);
% Insert all the members
H5T.insert(type_lane_compound, 'UTCTime', 0,        'H5T_STD_I64LE');
lane_fill.UTCTime = int64(-1);
H5T.insert(type_lane_compound, 'FileTime', 8,       'H5T_IEEE_F64LE');
lane_fill.FileTime = NaN;
H5T.insert(type_lane_compound, 'EgoLaneWidth', 16,  'H5T_IEEE_F64LE');
lane_fill.Width = NaN;
% Create inner lane compound
type_lane_slane = H5T.create('H5T_COMPOUND', 37);
% Insert the members
H5T.insert(type_lane_slane, 'Curvature', 0,         'H5T_IEEE_F64LE');
lane_fill.sLane.Curvature = NaN(1, NUMBER_LANES);
H5T.insert(type_lane_slane, 'CurvatureDx', 8,      'H5T_IEEE_F64LE');
lane_fill.sLane.CurvatureDer = NaN(1, NUMBER_LANES);
H5T.insert(type_lane_slane, 'Dy', 16,               'H5T_IEEE_F64LE');
lane_fill.sLane.Dy = NaN(1, NUMBER_LANES);
H5T.insert(type_lane_slane, 'QualityIndex', 24,     'H5T_STD_I32LE');
lane_fill.sLane.QualityIndex = int32(-1 * ones(1, NUMBER_LANES));
H5T.insert(type_lane_slane, 'MarkingType', 28,             lane_enum);
lane_fill.sLane.Type = int8(-1 * ones(1, NUMBER_LANES));
H5T.insert(type_lane_slane, 'YawAngle', 29,         'H5T_IEEE_F64LE');
lane_fill.sLane.YawAngle = NaN(1, NUMBER_LANES);
% Create the array for the lanes
type_lane_array = H5T.array_create(type_lane_slane, 4);
H5T.insert(type_lane_compound, 'sLaneLine', 24,         type_lane_array);

%% Create the object compound
% The object compound
type_object_compound = H5T.create('H5T_COMPOUND', 2488);
% Insert the members
H5T.insert(type_object_compound, 'UTCTime', 0,          'H5T_STD_I64LE');
object_fill.UTCTime = int64(-1);
H5T.insert(type_object_compound, 'FileTime', 8,         'H5T_IEEE_F64LE');
object_fill.FileTime = NaN;
H5T.insert(type_object_compound, 'LeadVehicleID', 16,     'H5T_STD_I32LE');
object_fill.LeadVehicleID = int32(-1);
H5T.insert(type_object_compound, 'NumberOfObjects', 20, 'H5T_STD_I32LE');
object_fill.NumberOfObjects = int32(-1);
% Create inner object compound
type_object_sobject = H5T.create('H5T_COMPOUND', 77);
% Insert the members
H5T.insert(type_object_sobject, 'Classification', 0, obj_classification_enum);
object_fill.sObject.Classification = int8(-1 * ones(1, NUMBER_OBJECTS));
H5T.insert(type_object_sobject, 'Height', 1,            'H5T_IEEE_F64LE');
object_fill.sObject.Height = NaN(1, NUMBER_OBJECTS);
H5T.insert(type_object_sobject, 'ID', 9,                'H5T_STD_I32LE');
object_fill.sObject.ID = int32(-1 * ones(1, NUMBER_OBJECTS));
H5T.insert(type_object_sobject, 'LatPosition', 13,      'H5T_IEEE_F64LE');
object_fill.sObject.LatPosition = NaN(1, NUMBER_OBJECTS);
H5T.insert(type_object_sobject, 'LatVelocity', 21,      'H5T_IEEE_F64LE');
object_fill.sObject.LatVelocity = NaN(1, NUMBER_OBJECTS);
H5T.insert(type_object_sobject, 'Length', 29,           'H5T_IEEE_F64LE');
object_fill.sObject.Length = NaN(1, NUMBER_OBJECTS);
H5T.insert(type_object_sobject, 'LongPosition', 37,     'H5T_IEEE_F64LE');
object_fill.sObject.LongPosition = NaN(1, NUMBER_OBJECTS);
H5T.insert(type_object_sobject, 'LongVelocity', 45,     'H5T_IEEE_F64LE');
object_fill.sObject.LongVelocity = NaN(1, NUMBER_OBJECTS);
H5T.insert(type_object_sobject, 'Width', 53,            'H5T_IEEE_F64LE');
object_fill.sObject.Width = NaN(1, NUMBER_OBJECTS);
H5T.insert(type_object_sobject, 'YawAngle', 61,         'H5T_IEEE_F64LE');
object_fill.sObject.YawAngle = NaN(1, NUMBER_OBJECTS);
H5T.insert(type_object_sobject, 'YawRate', 69,          'H5T_IEEE_F64LE');
object_fill.sObject.YawRate = NaN(1, NUMBER_OBJECTS);
% Create the array for the respective objects
type_object_array = H5T.array_create(type_object_sobject, 32);
H5T.insert(type_object_compound, 'sObject', 24,         type_object_array);

%% Create the positioning compound
% Create positioning compound
type_positioning_compound = H5T.create('H5T_COMPOUND', 68);
% Insert the members
H5T.insert(type_positioning_compound, 'UTCTime', 0,             'H5T_STD_I64LE');
positioning_fill.UTCTime = int64(-1);
H5T.insert(type_positioning_compound, 'FileTime', 8,            'H5T_IEEE_F64LE');
positioning_fill.FileTime = NaN;
H5T.insert(type_positioning_compound, 'Altitude', 16,           'H5T_IEEE_F64LE');
positioning_fill.Altitude = NaN;
H5T.insert(type_positioning_compound, 'GNSSSpeed', 24,          'H5T_IEEE_F64LE');
positioning_fill.GNSSSpeed = NaN;
H5T.insert(type_positioning_compound, 'GNSSTime', 32,           'H5T_STD_I64LE');
positioning_fill.GNSSTime = int64(-1);
H5T.insert(type_positioning_compound, 'Heading', 40,                    'H5T_IEEE_F64LE');
positioning_fill.Heading = NaN;
H5T.insert(type_positioning_compound, 'Latitude', 48,           'H5T_IEEE_F64LE');
positioning_fill.Latitude = NaN;
H5T.insert(type_positioning_compound, 'Longitude', 56,          'H5T_IEEE_F64LE');
positioning_fill.Longitude = NaN;
H5T.insert(type_positioning_compound, 'NumberOfSatellites', 64, 'H5T_STD_I32LE');
positioning_fill.NumberOfSatellites = int32(-1);


% %% Create the external data compound
% % Create the external data weather compound
% type_weather_compound = H5T.create('H5T_COMPOUND', 28);
% % Insert the members
% H5T.insert(type_weather_compound, 'UTCTime', 0,                 'H5T_STD_I64LE');
% weather_fill.UTCTime = int64(-1);
% H5T.insert(type_weather_compound, 'FileTime', 8,                'H5T_IEEE_F64LE');
% weather_fill.FileTime = NaN;
% H5T.insert(type_weather_compound, 'Precipitation', 16,          precipitation_enum);
% weather_fill.Precipitation = int8(-1);
% H5T.insert(type_weather_compound, 'PrecipitationType', 17,      precipitation_type_enum);
% weather_fill.PrecipitationType = int8(-1);
% H5T.insert(type_weather_compound, 'RoadSurfaceCondition', 18,   road_surface_condition_enum);
% weather_fill.RoadSurfaceCondition = int8(-1);
% H5T.insert(type_weather_compound, 'Temperature', 19,            'H5T_IEEE_F64LE');
% weather_fill.Temperature = NaN;
% H5T.insert(type_weather_compound, 'Weather', 27,                weather_enum);
% weather_fill.Weather = int8(-1);

% Create the external data map compound
type_map_compound = H5T.create('H5T_COMPOUND', 35);
% Insert the members
H5T.insert(type_map_compound, 'UTCTime', 0,            'H5T_STD_I64LE');
map_fill.UTCTime = int64(-1);
H5T.insert(type_map_compound, 'FileTime', 8,           'H5T_IEEE_F64LE');
map_fill.FileTime = NaN;
H5T.insert(type_map_compound, 'DistIntersection', 16,   'H5T_IEEE_F64LE');
map_fill.DistIntersection = NaN;
H5T.insert(type_map_compound, 'NumberOfLanes', 24,        'H5T_STD_I32LE');
map_fill.NumOfLanes = int32(-1);
H5T.insert(type_map_compound, 'RoadType', 28,          road_type_enum);
map_fill.RoadType = int8(-1);
H5T.insert(type_map_compound, 'RulesIntersection', 29, rules_intersection_enum);
map_fill.RulesIntersection = int8(-1);
H5T.insert(type_map_compound, 'SpeedLimit', 30,        'H5T_STD_I32LE');
map_fill.SpeedLimit = int32(-2);
H5T.insert(type_map_compound, 'TypeIntersection', 34,  intersection_type_enum);
map_fill.TypeIntersection = int8(-1);

%% Create the meta data compounds
% Create the general meta data compound
type_meta_general = H5T.create('H5T_COMPOUND', 48);
% Insert the members
H5T.insert(type_meta_general, 'ADFVersion', 0,      'H5T_IEEE_F64LE');
H5T.insert(type_meta_general, 'FormatVersion', 8,   'H5T_IEEE_F64LE');
H5T.insert(type_meta_general, 'Partner', 16,        type_str_s);
H5T.insert(type_meta_general, 'RecordDate', 23,     type_str_t);
H5T.insert(type_meta_general, 'UTCOffset', 44,      'H5T_STD_I32LE');

% Create the driver meta data compound
type_meta_driver = H5T.create('H5T_COMPOUND', 9);
% Insert the members
H5T.insert(type_meta_driver, 'DriverID', 0,       type_str_8);
H5T.insert(type_meta_driver, 'DriverType', 8,     driver_type_enum);

% Create the car meta data compound
type_meta_car = H5T.create('H5T_COMPOUND', 43);
% Insert the members
H5T.insert(type_meta_car, 'DriveType', 0,               drive_type_enum);
H5T.insert(type_meta_car, 'FuelType', 1,                fuel_type_enum);
H5T.insert(type_meta_car, 'NumberOfOccupants', 2,       'H5T_STD_I32LE');
H5T.insert(type_meta_car, 'PositionFrontBumper', 6,    'H5T_IEEE_F64LE');
H5T.insert(type_meta_car, 'PositionRearBumper', 14,     'H5T_IEEE_F64LE');
H5T.insert(type_meta_car, 'TransmissionType', 22,       transmission_type_enum);
H5T.insert(type_meta_car, 'VehicleLength', 23,          'H5T_IEEE_F64LE');
H5T.insert(type_meta_car, 'VehicleWeight', 31,          'H5T_STD_I32LE');
H5T.insert(type_meta_car, 'VehicleWidth', 35,           'H5T_IEEE_F64LE');

% Create the experiment meta data compound
type_meta_experiment = H5T.create('H5T_COMPOUND', 30);
% Insert the members
H5T.insert(type_meta_experiment, 'AnalysisEligible', 0,     analysis_eligible_enum);
H5T.insert(type_meta_experiment, 'Baseline', 1,             baseline_enum);
H5T.insert(type_meta_experiment, 'Country', 2,              type_str_3);
H5T.insert(type_meta_experiment, 'TestEndOdo', 5,           'H5T_STD_I32LE');
H5T.insert(type_meta_experiment, 'TestEndTime', 9,          'H5T_STD_I32LE');
H5T.insert(type_meta_experiment, 'TestSiteType', 13,        test_site_enum);
H5T.insert(type_meta_experiment, 'TestStartOdo', 14,        'H5T_STD_I32LE');
H5T.insert(type_meta_experiment, 'TestStartTime', 18,       'H5T_STD_I32LE');
H5T.insert(type_meta_experiment, 'TripID', 22,              type_str_8);

type_meta = H5T.create('H5T_COMPOUND', 130);
% Insert the members
H5T.insert(type_meta, 'General', 0, type_meta_general);
H5T.insert(type_meta, 'Driver', 48, type_meta_driver);
H5T.insert(type_meta, 'Car', 57, type_meta_car);
H5T.insert(type_meta, 'Experiment', 100, type_meta_experiment);
