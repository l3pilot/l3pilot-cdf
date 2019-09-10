function Data = read_example (file)
%READ_EXAMPLE An example for reading a L3Pilot Common Data Format File
%   This funciton simply iterates over all available datasets and reads
%   them to the output struct. In this case, top-level functions can be
%   used.
%
%   Example usage:
%   - Data = read_example('hdf5_filled_example_matlab_v02.h5');
%
% Copyright Institut fuer Kraftfahrzeuge, Johannes Hiller, 2018
%
%SEE ALSO h5read, h5readatt, h5info

% Get all the infos about the HDF5 file
file_infos = h5info(file);
% Iterate over all datasets and read them
for jj = 1:length(file_infos.Datasets)
    disp(['Found dataset ' file_infos.Datasets(jj).Name ' with a size of '...
        num2str(file_infos.Datasets(jj).Datatype.Size) ' bytes per timestamp and ' ...
        num2str(length(file_infos.Datasets(jj).Datatype.Type.Member)) ...
        ' members'])
    Data.(file_infos.Datasets(jj).Name) = ...
        h5read(file, ['/' file_infos.Datasets(jj).Name]);
end

% Iterate over all datasets in groups
for kk = 1:length(file_infos.Groups)
    disp(['Found group ' file_infos.Groups(kk).Name]);

    
    
    for jj = 1:length(file_infos.Groups(kk).Datasets)
        disp(['Found dataset ' file_infos.Groups(kk).Datasets(jj).Name ' with a size of '...
            num2str(file_infos.Groups(kk).Datasets(jj).Datatype.Size) ' bytes per timestamp and ' ...
            num2str(length(file_infos.Groups(kk).Datasets(jj).Datatype.Type.Member)) ...
            ' members']);
        Data.(file_infos.Groups(1).Name(2:length(file_infos.Groups(1).Name))).(file_infos.Groups(kk).Datasets(jj).Name) = ...
            h5read(file, [file_infos.Groups(kk).Name '/' file_infos.Groups(1).Datasets(jj).Name]);
    end
        
    

end


% Iterate over the top-level attributes and read them
for kk = 1:length(file_infos.Attributes)
    Data.metadata.(file_infos.Attributes(kk).Name) = ...
        h5readatt(file, '/', file_infos.Attributes(kk).Name);
end
end
