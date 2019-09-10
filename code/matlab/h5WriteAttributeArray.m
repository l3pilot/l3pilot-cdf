function h5WriteAttributeArray( file_handle, location, attr_name, array, datatype )
%H5WRITEATTRIBUTEARRAY Writes an array of attributes to a file
%   This function takes an array of different datatypes defined by datatype
%   and writes it to the file (defined by file_handle) at the location.
%   attr_name is used as name of the new attribute.
%
% Copyright Institut fuer Kraftfahrzeuge, Johannes Hiller, 2018
%
%SEE ALSO H5WRITEATTFILE, GENERATE_EXAMPLE

% Create a space for the attribute
attr_space = H5S.create_simple(1, 1, 1);
cu_attr_space = onCleanup(@()H5S.close(attr_space));
% Open an object for writing the attribute
attr_obj = H5O.open(file_handle, location, 'H5P_DEFAULT');
cu_attr_obj = onCleanup(@()H5O.close(attr_obj));
% Create the attribute
attr1 = H5A.create(attr_obj, attr_name, datatype, attr_space, 'H5P_DEFAULT', 'H5P_DEFAULT');
cu_attr1 = onCleanup(@()H5A.close(attr1));
% Write the array to the attribute
H5A.write(attr1, datatype, array);
end

