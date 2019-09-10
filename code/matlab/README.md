# Templates for Saving and Loading in Matlab

This repository contains templates for saving and loading the L3Pilot common data format in Matlab.

Matlab has built-in functions for working with HDF5 files since R2011a. These examples assume the reader has this version or later available.

[High level Matlab HDF5 functions](http://uk.mathworks.com/help/matlab/high-level-functions.html)
These high-level functions can be used for reading the data. 
For creating the data, we need to use the [low-level functions](http://de.mathworks.com/help/matlab/low-level-functions.html).

## Creating L3Pilot Common Data Format Files

In order to create the L3Pilot Common Data Format Files, we need to use the low-level functions implemented within Matlab.
Only these functions provide the possibility to create a dataset with different datatypes.

The needed datatypes are included in the file `structure_definitions.m`.
That script needs to be executed in your function or script for creating the data files.
Be sure to also execute `cleanup_structure.m` at the end of your function or script, i.e.

```matlab
structure_definitions;
...
... (a lot of code)
...
cleanup_structure;
```

### Datatype definitions

This section will give a short overview, over how the datatypes are defined in Matlab.

First, some general variables are defined, such as version or the number of lanes:

```matlab
% Version of the format
VERSION = 0.2;
% Number of lanes
NUMBER_LANES = 4;
% Number of objects
NUMBER_OBJECTS = 32;
```

This allows easy access and changing later on.

In order to assure the compatibility over different platforms and also languages, the HDF5 library comes with predefined datatypes that are used.
These are used in the following and are defined on the [confluence page](https://confluence-l3pilot.eict.de/display/SP5/Common+Data+Format).

For different signals, enums are used to set the different states.
Considering, a signal that shows the state of a system that can be active or not, the enum is defined as follows:

```matlab
active_enum = H5T.create('H5T_ENUM', 1);
H5T.set_order(active_enum, 'H5T_ORDER_LE');
H5T.set_sign(active_enum, 'H5T_SGN_2');
H5T.enum_insert(active_enum, 'NOT_APPLICABLE', -1);
H5T.enum_insert(active_enum, 'NOT_ACTIVE', 0);
H5T.enum_insert(active_enum, 'ACTIVE', 1);
H5T.enum_insert(active_enum, 'UNKNOWN', 9);
```

This datatype can now be used later on to define a signal in the dataset.
After all basic datatypes are defined, the datatype of the dataset can be constructed by combining multiple datatypes.
This is done by defining the compound, that defines the dataset:

```matlab
% The ego compound
type_ego_compound = H5T.create('H5T_COMPOUND', 175);
```

In this case, `175` is the size in bytes of the compound, i.e. the amount of data in one timestep.
Into this compound, the different signals are inserted with their datatypes.
As an example, we will consider the signal of the ABS intervention, which can be active or not:

```matlab
...
H5T.insert(type_ego_compound, 'ABSIntervention', 16, active_enum);
...
```

Here, the enum defined above is used again.
`16` is the offset from the beginning of the dataset at which the new member is inserted.
With this, the `egoVehicle` and `positioning` datatypes can be defined.

For `objects` and `lanes` however, nested compound with arrays are used.
Therefore, the creation takes a few more steps.
First, the inner compound is created and filled with signals:

```matlab
% Create inner lane compound
type_lane_slane = H5T.create('H5T_COMPOUND', 53);
% Insert the members
H5T.insert(type_lane_slane, 'Curvature', 0, 'H5T_IEEE_F64LE');
...
```

Since there are four lane markings that are considered in L3Pilot, an array of this compound is created with the size of 4:

```matlab
type_lane_array = H5T.array_create(type_lane_slane, 4);
```

This array is then inserted into the outer compound:

```matlab
% The lane compound
type_lane_compound = H5T.create('H5T_COMPOUND', 236);
...
H5T.insert(type_lane_compound, 'sLane', 24, type_lane_array);
```

Using these functions, all datatypes are defined.
After usage, these datatypes need to be closed, i.e.

```matlab
% Close ego types
H5T.close(type_ego_compound);
H5T.close(active_enum);
...
```

### Writing data to the file

First, a file needs to be created:

```matlab
file = H5F.create('hdf5_filled_example_matlab_v02.h5')
```

After that, the datasets have to be created.
This is done using the datatypes defined in `structure_definitions.m`.

The amount of data saved in a dataset is defined by a dataspace.
This space is created using

```matlab
% Create the data space for the ego data
space_ego = H5S.create_simple(1, 1, unlimited);
```

Next, the properties of the dataset need to be set, i.e. deflation and chunking:

```matlab
% Create the dataset properties
dcpl = H5P.create('H5P_DATASET_CREATE');
% In order to activate the deflation filter, chunking needs to be enabled
H5P.set_chunk(dcpl, 1);
% Enable compression by deflating
H5P.set_deflate(dcpl, 9)
```

Now, the actual dataset can be created:

```matlab
% Create the ego dataset
dset_ego = H5D.create(file, 'egoVehicle', type_ego_compound, space_ego, dcpl);
```

Here, the previously defined datatype `type_ego_compound` is used.

There are now two possible ways to write the data to this dataset.
The first possibility is to write all the data at once.
For this purpose, use a struct that replicates the datatype of the dataset and fill it with data.
To stick with the ABS intervention, this would be:

```matlab
...
ego_data.ABSIntervention = int8(randi([0, 1], [iterations, 1]));
...
```

This data is then written to the dataset via

```matlab
% Extend the dataset to include all the data
H5D.set_extent(dset_ego, length(ego_data.FileTime));
% Write the datastruct to the dataset
H5D.write(dset_ego, type_ego_compound, 'H5S_ALL', 'H5S_ALL', ...
    'H5P_DEFAULT', ego_data);
```

The extension needs to be done, as the size of the space was set to `1` during creation.
It is now extended to the size of the datastruct.
The second function then writes the data to the dataset.

The second possibility is to write data per timestep or in smaller time ranges.
However, bear in mind, that you always need to write a complete datatype.
This second possibility also shows the way to add data to the dataset.
First, get the current space and its size:

```matlab
% Write data to the ego dataset
ego_fspace = H5D.get_space(dset_ego);
% Get the extent of the current space
[~, ego_dims] = H5S.get_simple_extent_dims(ego_fspace);
```

Afterwards increase the size by the number of timesteps to be written, in this case `1`:

```matlab
% Increase the size
new_ego_dims = ego_dims + 1;
% Increase the dataspace
H5D.set_extent(dset_ego, new_ego_dims);
```

Now, get the new increased dataspace

```matlab
% Get the new increased datasapce
ego_fspace = H5D.get_space(dset_ego);
```

Then, calculate the offset in the space where the data should be written to and select the hyperslab from that point on:

```matlab
% The offset for writing
ego_offset = new_ego_dims - 1;
% Select the hyperslab used for writing
H5S.select_hyperslab(ego_fspace, 'H5S_SELECT_SET', ego_offset, [], 1, []);
```

Finally, write your data to the dataset:

```matlab
% Write the data to the dataset
H5D.write(dset_ego, type_ego_compound, space_ego, ego_fspace, 'H5P_DEFAULT', single_ego_data);
```

Thats it, the data is written to the dataset!

## Reading L3Pilot Common Data Format Files

Reading HDF5 files in Matlab is simpler than reading them.
However, reading of complete files is not possible.
Reading always has to be done by dataset.
Using the funciton `h5info` function, all information about a HDF5 file can be accessed.
This information can then be used, to read the complete file.

First, the information about the file is queried

```matlab
% Get all the infos about the HDF5 file
file_infos = h5info('hdf5_filled_example_matlab_v02.h5');
```

`file_infos` contains information about all datasets included in the file as well as the attributes.
Using that information, the datasets can all be read iteratively.

```matlab
% Iterate over all datasets and read them
for jj = 1:length(file_infos.Datasets)
    Data.(file_infos.Datasets(jj).Name) = ...
        h5read(file, ['/' file_infos.Datasets(jj).Name]);
end
```

Another information included in the `file_infos` struct is the name of all attributes.
Specifically, we are interested in the top-level attributes, as they include the needed metadata.
The metadata can also be read iteratively:

```matlab
% Iterate over the top-level attributes and read them
for kk = 1:length(file_infos.Attributes)
    Data.metadata.(file_infos.Attributes(kk).Name) = ...
        h5readatt(file, '/', file_infos.Attributes(kk).Name);
end
```

In this fashion, all important information has been read from the file and can now be used in further Matlab scripts.
