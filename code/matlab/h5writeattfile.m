function h5writeattfile( file_handle, location, attrname, attrvalue )
%H5WRITEATTFILE Writes an attribute to a file specified by file_handle
%   Basically the same functionality as h5writeatt, however it takes file
%   handles as input
%   Doesn't contain much handliung of error cases
%
% Copyright Institut fuer Kraftfahrzeuge, Johannes Hiller, 2018
%
%SEE ALSO h5writeatt, h5writeattributearray, generate_example

space_attr  = H5S.create('H5S_SCALAR');
cu_space    = onCleanup(@()H5S.close(space_attr));
objId       = H5O.open(file_handle, location, 'H5P_DEFAULT');
cu_objId    = onCleanup(@()H5O.close(objId));
acpl_id     = H5P.create('H5P_ATTRIBUTE_CREATE');
cu_acpl     = onCleanup(@()H5P.close(acpl_id));
type_temp   = createDatatypeId(attrvalue);
cu_type     = onCleanup(@()H5T.close(type_temp));
attr_id     = H5A.create(objId, attrname, type_temp, space_attr, acpl_id);
cu_attr     = onCleanup(@()H5A.close(attr_id));
H5A.write(attr_id, type_temp, attrvalue);

end

function datatype_id = createDatatypeId ( attvalue )
% We need to choose an appropriate HDF5 datatype based upon the attribute
% data.
switch class(attvalue)
	case 'double'
	    datatype_id = H5T.copy('H5T_NATIVE_DOUBLE');
	case 'single'
	    datatype_id = H5T.copy('H5T_NATIVE_FLOAT');
	case 'int64'
	    datatype_id = H5T.copy('H5T_NATIVE_LLONG');
	case 'uint64'
	    datatype_id = H5T.copy('H5T_NATIVE_ULLONG');
	case 'int32'
	    datatype_id = H5T.copy('H5T_NATIVE_INT');
	case 'uint32'
	    datatype_id = H5T.copy('H5T_NATIVE_UINT');
	case 'int16'
	    datatype_id = H5T.copy('H5T_NATIVE_SHORT');
	case 'uint16'
	    datatype_id = H5T.copy('H5T_NATIVE_USHORT');
	case 'int8'
	    datatype_id = H5T.copy('H5T_NATIVE_SCHAR');
	case 'uint8'
	    datatype_id = H5T.copy('H5T_NATIVE_UCHAR');
	case 'char'
	    datatype_id = H5T.copy('H5T_C_S1');
        if ~isempty(attvalue)
            % Don't do this when working with empty strings.
            H5T.set_size(datatype_id,numel(attvalue));
        end
		H5T.set_strpad(datatype_id,'H5T_STR_NULLTERM');
    otherwise
		error(message('MATLAB:imagesci:h5writeatt:unsupportedAttributeDatatype', class( attvalue )));
end
end
