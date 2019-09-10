#!/usr/bin/env python

""" Generates an example file showing the hdf5 structure
    For all signals, dummy data are used. The signals are in no way related to each other but generated mainly by
    random functions
"""

import os
from random import randint
from random import random
import datetime
import time
from structure_definitions import *
from h5py import Dataset

__author__ = "Johannes Hiller"
__version__ = "0.8"
__maintainer__ = "Johannes Hiller"
__email__ = "johannes.hiller@ika.rwth-aachen.de"
__status__ = "development"


def fill_ego_data(utc_time: np.int64, time_step: np.double):
    """
    This method takes a row of the csv and writes the values concerning the ego object to an array. This array is
    then returned and written to the hdf5 file

    :param utc_time: utc time
    :param time_step: time step
    :return: An array containing all the ego information for the current timestamp
    """

    # Create the array for the data
    ego_array = np.array(np.zeros(1, ), dtype=c_ego)
    # Fill the array with random data
    ego_array["UTCTime"][0] = utc_time
    ego_array["FileTime"][0] = time_step
    ego_array["ABSIntervention"][0] = np.int8(randint(0, 2))
    ego_array["ADFunctionActive"][0] = np.int8(1)
    ego_array["ADFunctionAvailable"][0] = np.int8(1)
    ego_array["AmbientLightLevel"][0] = (random() - 0.5) * 20
    ego_array["AmbientTemperature"][0] = 20.3
    ego_array["BaselineADASActive"][0] = randint(0, 1024)
    ego_array["BaselineADASIntervention"][0] = randint(0, 1024)
    ego_array["BrakeLight"][0] = np.int8(randint(0, 3))
    ego_array["BrakePedalPos"][0] = np.int32(randint(0, 101))
    ego_array["BrakePressure"][0] = np.int32(randint(0, 101))
    ego_array["DirectionIndicator"][0] = randint(0, 3)
    ego_array["EnergyConsumption"][0] = random() * 40
    ego_array["ESCIntervention"][0] = np.int8(randint(0, 2))
    ego_array["FrontFogLightStatus"][0] = np.int8(randint(0, 2))
    ego_array["FrontWiperStatus"][0] = np.int8(randint(0, 5))
    ego_array["FuelConsumption"][0] = np.nan
    ego_array["HandsOnDetection"][0] = np.int8(randint(0, 3))
    ego_array["LatAcceleration"][0] = (random() - 0.5) * 5
    ego_array["LongAcceleration"][0] = (random() - 0.5) * 10
    ego_array["Odometer"][0] = time_step * 26 + 4242
    ego_array["RearFogLightStatus"][0] = np.int8(randint(0, 2))
    ego_array["SteeringAngle"][0] = (random() - 0.5) * np.pi * 10
    ego_array["SteeringAngleADF"][0] = (random() - 0.5) * np.pi * 2
    ego_array["ThrottlePedalPos"][0] = np.int32(randint(0, 101))
    ego_array["TOR"][0] = np.int8(randint(0, 2))
    ego_array["TorsionBarTorque"][0] = random() * 100
    ego_array["VehicleSpeed"][0] = random() * 40
    ego_array["YawRate"][0] = (random() - 0.5) * np.pi * 0.25
    return ego_array


def fill_lane_objects(utc_time: np.int64, time_step: np.double):
    """
    This method takes a row of the csv and writes the values concerning the lane markings to an array. This array is
    then returned and written to the hdf5 file

    :param utc_time: utc time
    :param time_step: time step
    :return: An array containing all the lane marking information for the current timestamp
    """

    # Create the array for the data
    lane_array = np.array(np.zeros(1, ), dtype=c_lane)
    # Fill the array with random data
    lane_array["UTCTime"][0] = utc_time
    lane_array["FileTime"][0] = time_step
    lane_array["EgoLaneWidth"][0] = random() * 5

    """ Find the lane markings closest to the ego vehicle
    0 = right, 2 = second closeset right
    1 = left, 3 = second closest left
    """
    for ii in range(0, 4):
        lane_array[str_lan_obj]["Curvature"][0][ii] = (random() - 0.5) * 0.02
        lane_array[str_lan_obj]["CurvatureDx"][0][ii] = (random() - 0.5) * 0.0002
        lane_array[str_lan_obj]["Dy"][0][ii] = (random() - 0.5) * 15
        lane_array[str_lan_obj]["QualityIndex"][0] = randint(0, 4)
        lane_array[str_lan_obj]["Type"][0] = np.int8(randint(0, 6))
        lane_array[str_lan_obj]["YawAngle"][0][ii] = (random() - 0.5) * np.pi / 2
    return lane_array


def fill_objects(utc_time: np.int64, time_step: np.double):
    """
    This methods takes a row of the csv and writes the values concerning the objects to an array. This array is then
    returned and written to the hdf5 file.

    :param utc_time: utc time
    :param time_step: time step
    :return: An array containing all the object information for the current timestamp
    """

    # Create the array for the data
    obj_array = np.array(np.zeros(1, ), dtype=c_objects)
    # Fill the array with random data
    obj_array["UTCTime"][0] = utc_time
    obj_array["FileTime"][0] = time_step
    obj_array["LeadVehicleID"][0] = randint(0, 4242)
    obj_array["NumberOfObjects"][0] = randint(0, 32)

    for ii in range(0, 32):
        obj_array[str_obj_obj]["Classification"][0][ii] = np.int8(randint(0, 7))
        obj_array[str_obj_obj]["ID"][0][ii] = randint(0, 4242)
        obj_array[str_obj_obj]["LatPosition"][0][ii] = (random() - 0.5) * 80
        obj_array[str_obj_obj]["LatVelocity"][0][ii] = (random() - 0.5) * 20
        obj_array[str_obj_obj]["Length"][0][ii] = (random() - 0.5) * 15 + 10.5
        obj_array[str_obj_obj]["LongPosition"][0][ii] = (random() - 0.5) * 320
        obj_array[str_obj_obj]["LongVelocity"][0][ii] = (random() - 0.5) * 60
        obj_array[str_obj_obj]["Width"][0][ii] = (random() - 0.5) + 2.5
        obj_array[str_obj_obj]["YawAngle"][0][ii] = (random() - 0.5) * 2 * np.pi
        obj_array[str_obj_obj]["YawRate"][0][ii] = (random() - 0.5) * 20
    return obj_array


def fill_positioning(utc_time: np.int64, time_step: np.double):
    """
    This method takes a row of the csv and writes the values concerning the positioning to an array. This array is
    then returned and written to the hdf5 file

    :param utc_time: utc time
    :param time_step: time step
    :return: An array containing all the ego information for the current timestamp
    """

    # Create the array for the data
    pos_array = np.array(np.zeros(1, ), dtype=c_pos)
    # Fill the array with random data
    pos_array["UTCTime"][0] = utc_time
    pos_array["FileTime"][0] = time_step
    pos_array["Altitude"][0] = 8848
    pos_array["GNSSSpeed"][0] = random() * 40
    pos_array["Heading"][0] = random() * 2 * np.pi
    pos_array["Latitude"][0] = 50.7862653
    pos_array["Longitude"][0] = 6.0456767
    pos_array["NumberOfSatellites"][0] = 17
    return pos_array


def generate_random_content(iterations: np.int32):
    """
    This method iterates by the amount defined when calling the function. In each step, the datasets are resized and
    filled with random data.

    :param iterations: Number of iterations. Gives the time in seconds, when divided by 10
    :return: No return value.
    """
    if iterations < 1:
        print("Please specify a iteration number, that is larger or equal to 1")
        return
    start_time = time.time() * 1e3
    # Iterate iterations times
    for ii in range(0, iterations):
        # Resize the datasets
        d_ego.resize((ii + 1,))
        d_lane.resize((ii + 1,))
        d_objects.resize((ii + 1, ))
        d_positioning.resize((ii + 1, ))
        # Fill the datasets with random data
        d_ego[ii] = fill_ego_data(np.int64(start_time + ii * 1e2), np.double(ii / 10))
        d_lane[ii] = fill_lane_objects(np.int64(start_time + ii * 1e2), np.double(ii / 10))
        d_objects[ii] = fill_objects(np.int64(start_time + ii * 1e2), np.double(ii / 10))
        d_positioning[ii] = fill_positioning(np.int64(start_time + ii * 1e2), np.double(ii / 10))


def generate_attributes(dataset, struct_selection: str):
    """
    This method writes the description and the unit of the variables to the attributes of the datasets

    :param dataset: The dataset, to which the attributes are added
    :param struct_selection: String used for the selection of the correct attribute structure
    :return:
    """
    # Define the datatype for the attributes
    dtype = h5py.h5t.special_dtype(vlen=str)
    if struct_selection == str_ego:
        struct = ego_struct
    elif struct_selection == str_obj:
        struct = object_struct
    elif struct_selection == str_lan:
        struct = lane_struct
    elif struct_selection == str_pos:
        struct = pos_struct
    elif struct_selection == str_ed_weather:
        struct = weather_struct
    elif struct_selection == str_ed_map:
        struct = map_struct
    else:
        return dataset
    # Generate the attributes for the ego variables
    for variable in struct:
        dataset.attrs.create(variable.name, [("Description", variable.description), ("Unit", variable.unit)],
                             dtype=dtype)
    return dataset


def generate_top_level_attributes(f):
    """
    This functions adds the top level attributes to the trip file
    The entries of the array should be filled accordingly

    :param f: The hdf5 file, to which the attributes are added
    :return: The file with the attached attributes
    """
    f.attrs["creation_script"] = os.path.basename(__file__)
    f.attrs["creation_date"] = datetime.datetime.now().strftime("%d-%b-%Y %H:%M:%S")
    f.attrs["author"] = __maintainer__
    f.attrs["comment"] = "example of a hdf5 file for the L3Pilot project".encode()
    f.attrs["institution"] = "Institut fuer Kraftfahrzeuge (ika)".encode()
    f.attrs["hdf5_version"] = str(h5py.version.hdf5_version).encode()
    f.attrs["h5py_version"] = str(h5py.version.version).encode()

    meta_array = np.array(np.zeros(1,), dtype=c_meta_data)
    meta_array["General"]["ADFVersion"][0] = 0.2
    meta_array["General"]["FormatVersion"][0] = cdf_version
    meta_array["General"]["Partner"][0] = "ika".encode()
    meta_array["General"]["RecordDate"][0] = datetime.datetime.now().strftime("%d-%b-%Y %H:%M:%S").encode()
    meta_array["General"]["UTCOffset"][0] = 2

    meta_array["Driver"]["DriverID"][0] = "42".encode()
    meta_array["Driver"]["DriverType"][0] = np.int8(2)

    meta_array["Car"]["DriveType"][0] = 4
    meta_array["Car"]["FuelType"][0] = 5
    meta_array["Car"]["NumberOfOccupants"][0] = 1
    meta_array["Car"]["PositionFrontBumper"][0] = 2.37
    meta_array["Car"]["PositionRearBumper"][0] = 1.37
    meta_array["Car"]["Transmission"][0] = np.int8(1)
    meta_array["Car"]["VehicleID"][0] = "42".encode()
    meta_array["Car"]["VehicleLength"][0] = 4.902
    meta_array["Car"]["VehicleWeight"][0] = 1515
    meta_array["Car"]["VehicleWidth"][0] = 2.090

    meta_array["Experiment"]["AnalysisEligible"][0] = np.int8(0)
    meta_array["Experiment"]["Baseline"][0] = np.int8(0)
    meta_array["Experiment"]["Country"][0] = "DE".encode()
    meta_array["Experiment"]["TestEndOdo"][0] = 42
    meta_array["Experiment"]["TestEndTime"][0] = time.time()
    meta_array["Experiment"]["TestSiteType"][0] = np.int8(3)
    meta_array["Experiment"]["TestStartOdo"][0] = 17
    meta_array["Experiment"]["TestStartTime"][0] = time.time() - 100
    meta_array["Experiment"]["TripID"][0] = "52074".encode()
    f.attrs.create("metaData", meta_array, (1, ), c_meta_data)
    return f


# The name of the output file that is created
raw_file = "l3pilot_cdf_filled_example.h5"
print("Generating an example file with version {0} of the L3Pilot common data format.".format(cdf_version))
# Open and initialize the file
with h5py.File(raw_file, "w") as file:
    # Add some attributes to the file.
    file = generate_top_level_attributes(file)
    """ Here the datasets are created, that are then filled with random data. The structures used here are defined in 
        the structure_definitions.py file. """

    # Create the ego dataset
    ego_space = h5py.h5s.create_simple((1, ), (h5py.h5s.UNLIMITED, ))
    ego_plist = h5py.h5p.create(h5py.h5p.DATASET_CREATE)
    ego_plist.set_chunk((500, ))
    ego_plist.set_deflate(9)
    ego_plist.set_fill_value(c_ego_fill)
    ego_type = h5py.h5t.py_create(c_ego, logical=1)
    d_ego_id = h5py.h5d.create(file.id, str_ego.encode(), ego_type, ego_space, ego_plist)
    d_ego = Dataset(d_ego_id)
    print("Created egoVehicle dataset with a size of {0} bytes per timestamp".format(c_ego.itemsize))
    d_ego = generate_attributes(d_ego, str_ego)

    # Create the lane dataset
    lane_space = h5py.h5s.create_simple((1, ), (h5py.h5s.UNLIMITED, ))
    lane_plist = h5py.h5p.create(h5py.h5p.DATASET_CREATE)
    lane_plist.set_chunk((500, ))
    lane_plist.set_deflate(9)
    lane_plist.set_fill_value(c_lane_fill)
    lane_type = h5py.h5t.py_create(c_lane, logical=1)
    d_lane_id = h5py.h5d.create(file.id, str_lan.encode(), lane_type, lane_space, lane_plist)
    d_lane = Dataset(d_lane_id)
    print("Created lanes dataset with a size of {0} bytes per timestamp".format(c_lane.itemsize))
    d_lane = generate_attributes(d_lane, str_lan)

    # Create the objects dataset
    objects_space = h5py.h5s.create_simple((1, ), (h5py.h5s.UNLIMITED, ))
    objects_plist = h5py.h5p.create(h5py.h5p.DATASET_CREATE)
    objects_plist.set_chunk((20, ))
    objects_plist.set_deflate(9)
    objects_plist.set_fill_value(c_objects_fill)
    objects_type = h5py.h5t.py_create(c_objects, logical=1)
    d_objects_id = h5py.h5d.create(file.id, str_obj.encode(), objects_type, objects_space, objects_plist)
    d_objects = Dataset(d_objects_id)
    print("Created objects dataset with a size of {0} bytes per timestamp".format(c_objects.itemsize))
    d_objects = generate_attributes(d_objects, str_obj)

    # Create the positioning dataset
    pos_space = h5py.h5s.create_simple((1, ), (h5py.h5s.UNLIMITED, ))
    pos_plist = h5py.h5p.create(h5py.h5p.DATASET_CREATE)
    pos_plist.set_chunk((1000, ))
    pos_plist.set_deflate(9)
    pos_plist.set_fill_value(c_pos_fill)
    pos_type = h5py.h5t.py_create(c_pos, logical=1)
    d_positioning_id = h5py.h5d.create(file.id, str_pos.encode(), pos_type, pos_space, pos_plist)
    d_positioning = Dataset(d_positioning_id)
    print("Created positioning dataset with a size of {0} bytes per timestamp".format(c_pos.itemsize))
    d_positioning = generate_attributes(d_positioning, str_pos)

    # Create the external data group
    # For demonstration purposes here!
    ext_group = h5py.h5g.create(file.id, str_ed.encode(), h5py.h5p.DEFAULT, h5py.h5p.DEFAULT)
    # # Add the weather dataset
    # weather_space = h5py.h5s.create_simple((1, ), (h5py.h5s.UNLIMITED, ))
    # weather_plist = h5py.h5p.create(h5py.h5p.DATASET_CREATE)
    # weather_plist.set_chunk((1000, ))
    # weather_plist.set_deflate(9)
    # weather_plist.set_fill_value(c_weather_fill)
    # weather_type = h5py.h5t.py_create(c_ext_weather, logical=1)
    # d_weather_id = h5py.h5d.create(ext_group, str_ed_weather.encode(), weather_type, weather_space, weather_plist)
    # d_weather = h5hl.Dataset(d_weather_id)
    # print("Created weather dataset with a size of {0} bytes per timestamp".format(c_ext_weather.itemsize))
    # d_weather = generate_attributes(d_weather, str_ed_weather)
    # Add the map dataset
    map_space = h5py.h5s.create_simple((1, ), (h5py.h5s.UNLIMITED, ))
    map_plist = h5py.h5p.create(h5py.h5p.DATASET_CREATE)
    map_plist.set_chunk((1000, ))
    map_plist.set_deflate(9)
    map_plist.set_fill_value(c_map_fill)
    map_type = h5py.h5t.py_create(c_ext_map, logical=1)
    d_map_id = h5py.h5d.create(ext_group, str_ed_map.encode(), map_type, map_space, map_plist)
    d_map = Dataset(d_map_id)
    print("Created map dataset with a size of {0} bytes per timestamp".format(c_ext_map.itemsize))
    d_map = generate_attributes(d_map, str_ed_map)

    # Print some information
    print("The maximum size per timestamp is {0} bytes".format(c_ego.itemsize + c_lane.itemsize
                                                               + c_objects.itemsize + c_pos.itemsize))
    print("The size for 1001 timestamps would therefore be {0} bytes or approx. {1} bytes including "
          "the metadata".format((c_ego.itemsize + c_lane.itemsize + c_objects.itemsize + c_pos.itemsize)
                                * 1001, (c_ego.itemsize + c_lane.itemsize + c_objects.itemsize
                                         + c_pos.itemsize) * 1001 + c_meta_data.itemsize))

    # Generate random content to fill the structs
    generate_random_content(np.int32(1001))

print("Using compression, the actual size is {} bytes or {:3.1f} % of the raw data"
      .format(os.path.getsize(raw_file), (os.path.getsize(raw_file) / ((c_ego.itemsize + c_lane.itemsize
                                                                        + c_objects.itemsize + c_pos.itemsize) * 1001
                                                                       + c_meta_data.itemsize)) * 100))
