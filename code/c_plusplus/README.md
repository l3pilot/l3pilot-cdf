# Templates for Saving and Loading in C/C++

This repository contains templates for saving and loading the L3Pilot common data format in C/C++.

## Prerequisites

In order to produce HDF5 files in C/C++, some prerequisites need to be fulfilled.

### HDF5 Binaries and Sources

The most important is, that HDF5 must be installed or available on your machine.
It can either be downloaded as [pre-built binaries](https://www.hdfgroup.org/downloads/hdf5/) or as [source code](https://support.hdfgroup.org/ftp/HDF5/releases/hdf5-1.10/hdf5-1.10.1/src/) for compiling the libraries with CMake.

In both cases, you will get a folder, that contains the libraries and the binaries.
Using the Windows Installer, this would be e.g. `C:\Program Files\HDF_Group\HDF5\1.10.1`.

The folder will be as follows under Windows (left out files for brevity):

```bash
.
+-- bin
|   +-- ...
|   +-- hdf5.dll
|   +-- ...
|   +-- szip.dll
|   +-- zlib.dll
+-- cmake
|   +-- hdf5-config.cmake
|   +-- hdf5-config-version.cmake
|   +-- ...
+-- include
|   +-- ...
|   +-- hdf5.h
|   +-- ...
+-- lib
|   +-- hdf5.lib
|   +-- ...
|   +-- szip.lib
|   +-- zlib.lib
+-- ...
+-- USING_HDF5_CMAKE.txt
+-- USING_HDF5_VS.txt
```

This is also the folder configuration that will be created in a zip file, when the sources are built using CMake.
In order for CMake to find the HDF5 package in your software, the environment variable `HDF5_DIR` must be set.
It should point to the `cmake` folder, e.g. `HDF5_DIR=C:\Program Files\HDF_Group\HDF5\1.10.1\cmake`

### Integrating HDF5 into a CMake project

The following explanation is based upon the document `USING_HDF5_CMAKE.txt` that can be found in the folder of the installation or [here](https://support.hdfgroup.org/ftp/HDF5/releases/hdf5-1.10/hdf5-1.10.1/src/unpacked/release_docs/USING_HDF5_CMake.txt).

If the environment variable `HDF5_DIR` is set, CMake should have no problems finding your HDF5 installation.
Otherwise you can also set it in CMake itself with `SET(HDF5_DIR "C:/Program Files/HDF_Group/HDF5/1.10.1/cmake")`.

For the following explanations, the code from the example project for generating HDF5 files is used.

1. The type of library used for linking is defined.
    This can be either `SHARED` or `STATIC`.
    Additionally a string is generated from that for searching for the right config.

    ```cmake
    SET(LIB_TYPE SHARED) # or STATIC
    STRING(TOLOWER ${LIB_TYPE} SEARCH_TYPE)
    ```

    Note, that if you build your libraries from source and you want to link against the shared libraries, you need configure to CMake to compile the shared libraries before compiling.

2. Next, the HDF5 package is added to the project.
    In this case, only the C library is used in the project.

    ```cmake
    FIND_PACKAGE(HDF5 NAMES hdf5 COMPONENTS C ${SEARCH_TYPE})
    ```

    Other options for the used components would be e.g. `CXX` or `HL`.

3. Now, the include directories for HDF5 need to be added to the project.

    ```cmake
    INCLUDE_DIRECTORIES(${HDF5_INCLUDE_DIR})
    ```

4. For the HDF5 library to be used in an executable, the libraries need to be linked.
    The libraries used for linking are set using

    ```cmake
    set (LINK_LIBS ${LINK_LIBS} ${HDF5_C_${LIB_TYPE}_LIBRARY})
    ```

5. The executable is added using

    ```cmake
    ADD_EXECUTABLE (${example} ${PROJECT_SOURCE_DIR}/${example}.c)
    ```

6. In a final step, the libraries are linked against this executable

    ```cmake
    TARGET_LINK_LIBRARIES(${EXAMPLE} ${LINK_LIBS} )
    ```

The following CMakeLists.txt snippet puts all that together.
Note, that this code snippet includes some additional information needed for the example project.

```cmake
# Set the type of library used
SET(LIB_TYPE SHARED)
# Set the name of the executable
SET(EXE_NAME GenerateExample)
STRING(TOLOWER ${LIB_TYPE} SEARCH_TYPE)
# Find the HDF5 directory
FIND_PACKAGE(HDF5 NAMES hdf5 COMPONENTS C ${SEARCH_TYPE})
# Add the include directories
INCLUDE_DIRECTORIES("../include" ${HDF5_INCLUDE_DIR} )
# Set the libraries for linking
SET(LINK_LIBS ${LINK_LIBS} ${HDF5_C_${LIB_TYPE}_LIBRARY})
# Add the executable
ADD_EXECUTABLE(${EXE_NAME} generate_example.cpp hdf5_data_buffer.cpp )
# Link the libraries
TARGET_LINK_LIBRARIES(${EXE_NAME} ${LINK_LIBS} )
```

## Creating L3Pilot Common Data Format Files

If you want to generate HDF5 files in the L3Pilot Common Data Format, you can use the header provided: [structure_definitions.h](https://gitlab.ika.rwth-aachen.de/l3pilot-sp5/l3pilot-cdf/c_cplusplus/blob/master/include/structure_definitions.h).
Include it using `#include <structure_definitions.h>`.
This header defines the structure and the default values for the signals.
As an example, consider the top level of the objects dataset:

```cpp
//! The structure of all objects in the HDF5 file
typedef struct obj_t {
    //! System time (UTC) in microseconds since epoch
    long long UTCTime =     -1;
    //! Time since beginning of recording
    double FileTime =       -1;
    //! ID of the object currently selected as lead vehicle
    int LeadVehicle =       -1;
    //! Number of objects in timestamp
    int NumberOfObjects =   -1;
    //! Array containing all object information
    obj_obj_t sObject[OBJECT_NUMBER];
} obj_t;
```

Based on these structures, the datatypes are used.
These datatypes are needed for creating the datasets.
To stay with the example of the objects dataset, the top level for the datatype is created with the following lines of code

```cpp
// Create the object compound
m_obj_tid = H5Tcreate(H5T_COMPOUND, sizeof(obj_t));
// Add the members to the object compound
H5Tinsert(m_obj_tid, "UTCTime",         HOFFSET(obj_t, UTCTime),            H5T_STD_I64LE);
H5Tinsert(m_obj_tid, "FileTime",        HOFFSET(obj_t, FileTime),           H5T_IEEE_F64LE);
H5Tinsert(m_obj_tid, "LeadVehicle",     HOFFSET(obj_t, LeadVehicle),        H5T_STD_I32LE);
H5Tinsert(m_obj_tid, "NumberOfObjects", HOFFSET(obj_t, NumberOfObjects),    H5T_STD_I32LE);
// Add the array of objects
H5Tinsert(m_obj_tid, "sObject",      HOFFSET(obj_t, sObject),      m_obj_array);
```

`m_obj_tid` is the identifier for the datatype that describes the top-level of the objects dataset.
Using `H5Tcreate(...)`, it is created as a compound datatype with the size of the struct defined in the header.
`H5Tinsert(...)` adds a new member to the newly created datatype.
`HOFFSET(obj_t, UTCTime)` specifies the offset in bytes from the beginning of the struct / datatype.
The last argument, e.g. `H5T_STD_I64LE`, specifies the datatype of the member.
These macros are the same over all platforms and languages, therefore use them.
In case of the object array, an array is created beforehand for the signals concerning the single objects (shortened for brevity).

```cpp
// Create the object objects compound
m_obj_obj_tid = H5Tcreate(H5T_COMPOUND, sizeof(obj_obj_t));
// Add the members of the objects
...
H5Tinsert(m_obj_obj_tid, "Distance", HOFFSET(obj_obj_t, Distance), H5T_IEEE_F64LE);
...
// Set the size of the objects array
hsize_t obj_arr_dim[] = { OBJECT_NUMBER };
// Create the objects array
m_obj_array =           H5Tarray_create2(m_obj_obj_tid, 1, obj_arr_dim);
```

`m_obj_obj_tid` is the identifier for the datatype defining the signals and their types for the single objects.
As for `m_obj_tid`, all the members are added (not fully shown here).
`obj_arr_dim` defines the size of the array and thereby limits the number of objects.
`m_obj_array` finally is the identifier of the array created with `H5Tarray_create2(...)`.
It takes the base type (`m_obj_obj_tid`), the number of dimensions (`1`) and the dimension of the array (`obj_arr_dim`) as input arguments.

Now that the datatype is fully defined, the dataset can be created.
Again, the objects dataset is considered as an example.

```cpp
// Define the size of the object dataset
obj_dim[0] =       1;
max_obj_dim[0] =   H5S_UNLIMITED;
// Create the object space
m_obj_space =      H5Screate_simple(1, obj_dim, max_obj_dim);
m_prop_ds =        H5Pcreate(H5P_DATASET_CREATE);
// Chunk the dataset every 20 samples
obj_chunk_dim[0] = 20;
m_obj_status =     H5Pset_chunk(m_prop_ds, 1, obj_chunk_dim);
// Activate deflation
m_obj_status =     H5Pset_deflate(m_prop_ds, 9);
// Set the fill value
m_pos_status =     H5Pset_fill_value(m_prop_ds, m_obj_tid, &cObjFill);
// Create the dataset
m_obj_ds =         H5Dcreate2(m_file, "objects", m_obj_tid, m_obj_space, H5P_DEFAULT, m_prop_ds, H5P_DEFAULT);
```

`obj_dim` specifies the dimension of the dataset.
In the beginning this is `1` as no data is yet added.
This can also be initially set to a higher value, but then you need to keep track of how much data you have already written to the dataset and extend it later on.
The maximum size, `max_obj_dim`, is set to be unlimited with `H5S_UNLIMITED`.
In order to store data in a dataset, a dataspace must be created.
In this case, a simple dataspace is used which is created with `H5Screate_simple`.
The number of dimensions is `1`, as only the compound is added to the dataset.
Additionally, the dimensions of that array (`obj_dim` and `max_obj_dim`) are given.
Next, the different properties of the dataset are set.
For this, a property identifier, `m_prop_ds` is initialized with `H5Pcreate`.
As properties, chunking is set to 20 samples (approx. 1 MB).
Depending on the dataset used, this is set to different sizes, but the size of a chunk should be approx. 1 MB.
The deflate compression is set to maximum and the fill values (i.e. the default values) are set.
In a last step, the dataset is create with `H5Dcreate2(...)`.
The file to create the dataset in is specified with `m_file` in this case, the name of the dataset is `"objects"`.
Caution: There can be no two datasets with the same name!
The base datatype of the dataset is specified with `m_obj_tid` and the space with `m_obj_space`.
Also, the properties specified earlier are set.
The other two options stay at the default.

Now, that the dataset is created, data can be written on it.

```cpp
// Get an identifier for the object space
m_obj_fspace =    H5Dget_space(m_obj_ds);
// Get the size of the object space
m_obj_status =    H5Sget_simple_extent_dims(m_obj_fspace, m_obj_osize, NULL);
m_obj_nsize[0] =  m_obj_osize[0] + obj_dim[0];
// Extend the object space by the dimension of the object data
m_obj_status =    H5Dset_extent(m_obj_ds, m_obj_nsize);
// Get the new, extended object space
m_obj_fspace =    H5Dget_space(m_obj_ds);
// Define the new offset
m_obj_offset[0] = m_obj_nsize[0] - obj_dim[0];
// Select the hyperslab that is used for writing
m_obj_status =    H5Sselect_hyperslab(m_obj_fspace, H5S_SELECT_SET, m_obj_offset, NULL, obj_dim, NULL);
// Write the structure with the object data to the dataspace
m_obj_status =    H5Dwrite(m_obj_ds, m_obj_tid, m_obj_space, m_obj_fspace, H5P_DEFAULT, &cObj);
```

First, the current space of the dataset `m_obj_fspace` is queried with `H5Dget_space(...)`.
Its dimensions are then accessed via `H5Sget_simple_extent_dims(...)`.
The new size of the data space `m_obj_nsize` is calculated from the old size `m_obj_osize` and the dimension of the object struct `obj_dim` by addition.
With `H5Dset_extent(...)` the space is extended and the new extended space queried again with `H5Dget_space(...)`.
For writing on the dataspace, the offset from the beginning is important.
This is calculated from the new size of the space `m_obj_nsize` and the dimension of the object struct `obj_dim` by subtraction.
Using the offset and the new dataspace, a hyperslab is selected with `H5Sselect_hyperslab(...)`.
This hyperslab can be used for writing the data.
In a final step, the data `cObj` (which is of type `obj_t`) is written to the dataset with `H5Dwrite(...)`.
This process is repeated for each timestamp.

After all data is written to the file, the datatypes and the file need to be closed.

```cpp
// Close object ids
H5Dflush(m_obj_ds);
H5Tclose(m_obj_obj_tid);
H5Tclose(m_obj_array);
H5Tclose(m_obj_tid);
H5Sclose(m_obj_space);
H5Sclose(m_obj_fspace);
H5Dclose(m_obj_ds);
...
// Close file
herr_t file_status = H5Fclose(m_file);
```

This closes all the types with `H5Tclose(...)`, the spaces with `H5Sclose(...)` and the dataset with `H5Dclose(...)`.
After all types, space and datasets are closed, the file is closed with `H5Fclose(...)`.

## Reading L3Pilot Common Data Format Files

The example provided reads out the datasets contained in a HDF5 file and writes their size to the console.

This is done by traversing the complete file and writing the infos back.

So far this is example is quite generic and does not rely on the names of the dataset to be known.
