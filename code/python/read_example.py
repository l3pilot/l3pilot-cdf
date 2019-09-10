#!/usr/bin/env python

""" Reads the example file to a dict

    This file shows, how to read the data into a dict, that can be used as any other Python dict.
    Also it shows how to read the attributes available in the file and in a dataset.
 """

from structure_definitions import *

__author__ = "Johannes Hiller"
__version__ = "0.8"
__maintainer__ = "Johannes Hiller"
__email__ = "johannes.hiller@ika.rwth-aachen.de"
__status__ = "development"

# The file to read
h5_file = "l3pilot_cdf_filled_example.h5"
# Initialize the dict for storing the data
h5_dict = dict()
# Open the file
with h5py.File(h5_file, "r") as file:
    # Initialize the ego part of the dict
    h5_dict[str_ego] = dict()
    for item in [list1 for list1 in ego_struct if list1.__class__.__name__ == "EgoTop"]:
        h5_dict[str_ego][item.name] = file[str_ego][item.name]
    # Initialize the lane part of the dict
    h5_dict[str_lan] = dict()
    for item in [list1 for list1 in lane_struct if list1.__class__.__name__ == "LaneTop"]:
        h5_dict[str_lan][item.name] = file[str_lan][item.name]
    # Initialize the dict for the different lanes
    h5_dict[str_lan][str_lan_obj] = dict()
    for item in [list1 for list1 in lane_struct if list1.__class__.__name__ == "LaneSObject"]:
        h5_dict[str_lan][str_lan_obj][item.name] = file[str_lan][str_lan_obj][item.name]
    # Initialize the object part of the dict
    h5_dict[str_obj] = dict()
    for item in [list1 for list1 in object_struct if list1.__class__.__name__ == "ObjectTop"]:
        h5_dict[str_obj][item.name] = file[str_obj][item.name]
    # Initialize the dict for the different objects
    h5_dict[str_obj][str_obj_obj] = dict()
    for item in [list1 for list1 in object_struct if list1.__class__.__name__ == "ObjectSObject"]:
        h5_dict[str_obj][str_obj_obj][item.name] = file[str_obj][str_obj_obj][item.name]
    # Initialize the positioning part of the dict
    h5_dict[str_pos] = dict()
    for item in [list1 for list1 in pos_struct if list1.__class__.__name__ == "PosTop"]:
        h5_dict[str_pos][item.name] = file[str_pos][item.name]

    # Print some information about the file
    print("The file is in version {} of the L3Pilot Common Data Format and was created by {}, {} at {}"
          .format(float(file.attrs["metaData"]["General"]["FormatVersion"]), file.attrs["author"],
                  file.attrs["institution"], file.attrs["creation_date"]))
    # Print some information on a signal in the egoVehicle dataset
    print("The description of the UTCTime is : \"{}\" and its unit is \"{}\""
          .format(file["egoVehicle"].attrs["UTCTime"][0][1], file["egoVehicle"].attrs["UTCTime"][1][1]))

# Print the number of timestamps contained in the file
print("The loaded h5 file contains {} timestamps".format(len(h5_dict["egoVehicle"]["UTCTime"])))

