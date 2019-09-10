# Templates for Saving and Loading in Python

This repository contains templates for saving and loading the L3Pilot common data format in Python.
All files are written and tested with [Python 3.6.4](https://www.python.org/downloads/release/python-364/).
In order to use the definitions provided in this repository, two additional python libraries are of essence:

* [NumPy](http://www.numpy.org/), a scientific computing package for Python. The tested version is 1.17.0
* [H5py](https://www.h5py.org/), a Pythonic interface to the HDF5 binary format. The test version is 2.9.0

Specific versions of the format templates for Python can be downloaded under the [Tags](https://gitlab.ika.rwth-aachen.de/l3pilot-sp5/l3pilot-cdf/python/tags).

## Creating L3Pilot Common Data Format Files

In order to create L3Pilot common data format HDF5 files, the definitions in [_structure\_definitions.py_](https://gitlab.ika.rwth-aachen.de/l3pilot-sp5/l3pilot-cdf/python/blob/master/structure_definitions.py) can be used.
Be sure to use the latest version available.

For that purpose, you can base your work on the script that is used to generate the example.
The full details can be found in [_generate\_example.py_](https://gitlab.ika.rwth-aachen.de/l3pilot-sp5/l3pilot-cdf/python/blob/master/generate_example.py)
The general steps in Python of creating a file with the egoVehicle dataset as well as some data are now described:

1. The HDF5 file is created. In this case this is done with
    ```python
    with h5py.file("egoVehicle_example.h5", "w") as file:
    ```

2. **This section has changed since the first version to include the usage of fill values.**  
    This section is now similar to how it is done in C/C++ and Matlab.
    * First, a space needs to be created.
        This space is extendible, but initialized with a size of `1`.

        ```python
        ego_space = h5py.h5s.create_simple((1, ), (h5py.h5s.UNLIMITED, ))
        ```
    * In a next step, the properties for the dataset are set.

        ```python
        ego_plist = h5py.h5p.create(h5py.h5p.DATASET_CREATE)
        ego_plist.set_chunk((500, ))
        ego_plist.set_deflate(9)
        ego_plist.set_fill_value(c_ego_fill)
        ```
        In this case, the size of the chunks is set to `500`, as this is about 10MB per timestamp.
        The deflation is set to the maximum.
        `c_ego_fill` is an numpy array with same datatype as the dataset and contains the values that should be used, if a signal is not availbale.
    * Using the `dtype` of the array, the datatype of the dataset is derived.

        ```python
        ego_type = h5py.h5t.py_create(c_ego, logical=1)
        ```
    * With the creation properties,the space and the datatype, the dataset can be created.
        For easier handling, a high-level dataset object is created from the ID with `h5hl.Dataset`.
        This gives easier access to many function like attributes.

        ```python
        d_ego_id = h5py.h5d.create(file.id, str_ego.encode(), ego_type, ego_space, ego_plist)
        d_ego = h5hl.Dataset(d_ego_id)
        ```
        `str_ego.encode()` takes the string containing the name for the ego dataset and encodes it as a bytes object.
        This is needed by the low-level function, as it does not handle normal strings.
        `ego_type` is the datatype of the dataset which was created in the previous step and `ego_space` is the space used for storing the data.
        The creation properties are specified by the `ego_plist`.
        As described above, this contains the information about compression, fill values and other stuff.
        Using the `h5hl.Dataset` function, a wrapper is then put around the dataset ID for easier access.

3. In order to fill the file with data, the dataset needs to be resized in order to hold the data.
    For the egoVehicle dataset, this would be
    ```python
    d_ego_data.resize((ii + 1, ))
    ```
    if you have an iterator running over your data.
    Otherwise you could access the current size of the dataset with `len(d_ego_data)` and increment it when writing new data.
    However, for ease of use, an iterator is recommended, as it also makes data handling on Python side easier.

4. Now the data can be written to the dataset.
    All data for one timestamp for each dataset are written at once.
    Therefore, some data accumulation needs to be done first.
    * Create a NumPy array with the datatype of the dataset:

        ```python
        ego_array = np.array(np.zeros(1, ), dtype=c_ego_data)
        ```
    * Write the data to the file, e.g.

        ```python
        ego_array["FuelConsumption"][0] = 4.2
        ```
    * If using an iterator, write the data to the dataset with

        ```python
        d_ego_data[ii] = ego_array
        ```

5. Repeat steps 3 and 4 until all timestamps are written to the dataset.

6. Using the `with` statement, the file is closed as soon as the scope is left.

## Reading L3Pilot Common Data Format Files

Reading HDF5 files is more simple.
Using the high-level functions, all parts of the file can be read into a `dict`.
An example can be found in [_read\_example.py](https://gitlab.ika.rwth-aachen.de/l3pilot-sp5/l3pilot-cdf/python/blob/master/read_example.py).

The needed steps are described in the following:

1. The dict for storing the read data has to be initialized:

    ```python
    h5_dict = dict()
    ```

2. In a next step, the HDF5 file is open in read mode.
    Using the `with` method, opening and closing is handled automatically.

    ```python
    with h5py.File(h5_file, "r") as file:
    ```

3. For each of the datasets in the file, a subdict is created.
    This is done using the strings from the _structure\_definitions.py_.
    In this case `str_ego` is used.
    Another possibility here would be to query the file for available datasets, but since we know the dataset names, this is not necessary.

    ```python
    h5_dict[str_ego] = dict()
    ```

4. Using the list of items of each dataset from which the datatypes are created, the signals can then be read into the dict.

    ```python
    for item in [list1 for list1 in ego_struct if list1.__class__.__name__ == "EgoTop"]:
        h5_dict[str_ego][item.name] = file[str_ego][item.name]
    ```

5. Using default python dict options, the data can now be used.

## Generating an Example File

Also contained in this repository is a Python script (generate_example.py) that can generate an example file filled with random data.
It uses the general definitions given in this repository and fills the signals with data.

### Prerequisites

For the example to run, the following libraries are additionally needed:

* os
* random
* datetime

### Output

If you run generate_example.py, it will generate a file called _h5\_filled\_example.h5_ in the directory it is executed.
Currently contained in that file are the meta data as well as the following datasets:

* egoVehicle
* lanes
* objects
* positioning

### Distinct steps

1. The HDF5 file is created. In this case this is done with
    ```python
    with h5py.file(raw_file, "w") as file:
    ```

2. In order to save data and signals, datasets need to be created.
    This is done with the `file.create_dataset()` method.
    E.g. for the _egoVehicle_ dataset, the command is as follows:
    ```python
    d_ego_id = h5py.h5d.create(file.id, str_ego.encode(), ego_type, ego_space, ego_plist)
    d_ego = h5hl.Dataset(d_ego_id)
    ```
    This command has two parts.
    In the first part, the actual dataset is created.
    This is done using the low-level function `h5py.h5d.create()`.
    Using low-level functions, we have access to more options during creation.
    For more details, please refer to the details above.
    Using the `h5hl.Dataset` function, a wrapper is then put around the dataset ID for easier access.

3. In a next step, the attributes for the dataset are created.
    For this case, the function `generate_attributes()` is used.
    ```python
    d_ego_data = generate_attributes(d_ego_data, "egoVehicle")
    ```
    The function takes the description and unit specified in _structure\_definitions_ and writes them as attributes.

4. For the data, the function `generate_random_content()` is used.
    It takes the number of time steps as input and generates random data.

5. Since the `with` statement in Python is used, the file is automatically closed, as soon as the scope is left.
