#!/usr/bin/env python

"""  Contains the definitions of the structures """

import numpy as np
import h5py

__author__ = "Johannes Hiller"
__version__ = "0.8"
__maintainer__ = "Johannes Hiller"
__email__ = "johannes.hiller@ika.rwth-aachen.de"
__status__ = "development"

str_ego = "egoVehicle"
str_lan = "laneLines"
str_lan_obj = "sLaneLine"
str_obj = "objects"
str_obj_obj = "sObject"
str_pos = "positioning"
str_ed = "externalData"
str_ed_weather = "weather"
str_ed_map = "map"


class EgoTop:
    """ Class for the signals on the top-level of the ego dataset

    name
        Name of the signal
    data_type
        Datatype of the signal, e.g. np.double
    description
        Brief description of the signal. This is used for the attributes
    unit
        Unit of the signal. This is used for the attributes
    fill_value
        Fill value for the signal, if no value is recorded. E.g. NaN for doubles
    """
    def __init__(self, name, data_type, description, unit, fill_value):
        self.name = name
        self.type = data_type
        self.description = description
        self.unit = unit
        self.fill_value = fill_value

    def __str__(self):
        return "Name: {0}; Type: {1}; Description: {2}; Unit: {3}; Fill value: {4}".format(
            self.name, self.type, self.description, self.unit, self.fill_value
        )


class LaneTop:
    """ Class for the signals on the top-level of the lanes dataset

    name
        Name of the signal
    data_type
        Datatype of the signal, e.g. np.double
    description
        Brief description of the signal. This is used for the attributes
    unit
        Unit of the signal. This is used for the attributes
    fill_value
        Fill value for the signal, if no value is recorded. E.g. NaN for doubles
    """
    def __init__(self, name, data_type, description, unit, fill_value):
        self.name = name
        self.type = data_type
        self.description = description
        self.unit = unit
        self.fill_value = fill_value

    def __str__(self):
        return "Name: {0}; Type: {1}; Description: {2}; Unit: {3}; Fill value: {4}".format(
                self.name, self.type, self.description, self.unit, self.fill_value
        )


class LaneSObject:
    """ Class for the respective lanes in the lanes dataset

    name
        Name of the signal
    data_type
        Datatype of the signal, e.g. np.double
    description
        Brief description of the signal. This is used for the attributes
    unit
        Unit of the signal. This is used for the attributes
    fill_value
        Fill value for the signal, if no value is recorded. E.g. NaN for doubles
    """
    def __init__(self, name, data_type, description, unit, fill_value):
        self.name = name
        self.type = data_type
        self.description = description
        self.unit = unit
        self.fill_value = fill_value

    def __str__(self):
        return "Name: {0}; Type: {1}; Description: {2}; Unit: {3}; Fill value: {4}".format(
            self.name, self.type, self.description, self.unit, self.fill_value
        )


class ObjectTop:
    """ Class for the signals on the top-level of the objects dataset

    name
        Name of the signal
    data_type
        Datatype of the signal, e.g. np.double
    description
        Brief description of the signal. This is used for the attributes
    unit
        Unit of the signal. This is used for the attributes
    fill_value
        Fill value for the signal, if no value is recorded. E.g. NaN for doubles
    """
    def __init__(self, name, data_type, description, unit, fill_value):
        self.name = name
        self.type = data_type
        self.description = description
        self.unit = unit
        self.fill_value = fill_value

    def __str__(self):
        return "Name: {0}; Type: {1}; Description: {2}; Unit: {3}; Fill value: {4}".format(
                self.name, self.type, self.description, self.unit, self.fill_value
        )


class ObjectSObject:
    """ Class fot the respective object signals in the objects dataset

    name
        Name of the signal
    data_type
        Datatype of the signal, e.g. np.double
    description
        Brief description of the signal. This is used for the attributes
    unit
        Unit of the signal. This is used for the attributes
    fill_value
        Fill value for the signal, if no value is recorded. E.g. NaN for doubles
    """
    def __init__(self, name, data_type, description, unit, fill_value):
        self.name = name
        self.type = data_type
        self.description = description
        self.unit = unit
        self.fill_value = fill_value

    def __str__(self):
        return "Name: {0}; Type: {1}; Description: {2}; Unit: {3}; Fill value: {4}".format(
                self.name, self.type, self.description, self.unit, self.fill_value
        )


class ExternalDataTop:
    """ Class for the signals on the top-level of the external data dataset

    name
        Name of the signal
    data_type
        Datatype of the signal, e.g. np.double
    description
        Brief description of the signal. This is used for the attributes
    unit
        Unit of the signal. This is used for the attributes
    fill_value
        Fill value for the signal, if no value is recorded. E.g. NaN for doubles
    """
    def __init__(self, name, data_type, description, unit, fill_value):
        self.name = name
        self.type = data_type
        self.description = description
        self.unit = unit
        self.fill_value = fill_value

    def __str__(self):
        return "Name: {0}; Type: {1}; Description: {2}; Unit: {3}; Fill value: {4}".format(
                self.name, self.type, self.description, self.unit, self.fill_value
        )


class ExternalData:
    """ Class for the respective signals for the weather in the external data dataset

    name
        Name of the signal
    data_type
        Datatype of the signal, e.g. np.double
    description
        Brief description of the signal. This is used for the attributes
    unit
        Unit of the signal. This is used for the attributes
    fill_value
        Fill value for the signal, if no value is recorded. E.g. NaN for doubles
    """
    def __init__(self, name, data_type, description, unit, fill_value):
        self.name = name
        self.type = data_type
        self.description = description
        self.unit = unit
        self.fill_value = fill_value

    def __str__(self):
        return "Name: {0}; Type: {1}; Description: {2}; Unit: {3}; Fill value: {4}".format(
                self.name, self.type, self.description, self.unit, self.fill_value
        )


class PosTop:
    """ Class for the signals on the top-level of the positioning dataset

        name
            Name of the signal
        data_type
            Datatype of the signal, e.g. np.double
        description
            Brief description of the signal. This is used for the attributes
        unit
            Unit of the signal. This is used for the attributes
        fill_value
            Fill value for the signal, if no value is recorded. E.g. NaN for doubles
        """
    def __init__(self, name, data_type, description, unit, fill_value):
        self.name = name
        self.type = data_type
        self.description = description
        self.unit = unit
        self.fill_value = fill_value

    def __str__(self):
        return "Name: {0}; Type: {1}; Description: {2}; Unit: {3}; Fill value: {4}".format(
            self.name, self.type, self.description, self.unit, self.fill_value
        )


class MetaGeneral:
    """ Class for the signals of the general meta data

    name
        Name of the signal
    data_type
        Datatype of the signal, e.g. np.double
    description
        Brief description of the signal. This is used for the attributes
    unit
        Unit of the signal. This is used for the attributes
    """
    def __init__(self, name, data_type, description, unit):
        self.name = name
        self.type = data_type
        self.description = description
        self.attr_value = unit

    def __str__(self):
        return "Name: {0}; Type: {1}; Description: {2}; Attribute: {3}".format(
            self.name, self.type, self.description, self.attr_value
        )


class MetaDriver:
    """ Class for the signals of the driver meta data

    name
        Name of the signal
    data_type
        Datatype of the signal, e.g. np.double
    description
        Brief description of the signal. This is used for the attributes
    unit
        Unit of the signal. This is used for the attributes
    """
    def __init__(self, name, data_type, description, unit):
        self.name = name
        self.type = data_type
        self.description = description
        self.attr_value = unit

    def __str__(self):
        return "Name: {0}; Type: {1}; Description: {2}; Attribute: {3}".format(
            self.name, self.type, self.description, self.attr_value
        )


class MetaCar:
    """ Class for the signals of the car meta data

    name
        Name of the signal
    data_type
        Datatype of the signal, e.g. np.double
    description
        Brief description of the signal. This is used for the attributes
    unit
        Unit of the signal. This is used for the attributes
    """
    def __init__(self, name, data_type, description, unit):
        self.name = name
        self.type = data_type
        self.description = description
        self.attr_value = unit

    def __str__(self):
        return "Name: {0}; Type: {1}; Description: {2}; Attribute: {3}".format(
            self.name, self.type, self.description, self.attr_value
        )


class MetaExperiment:
    """ Class for the signals of the experiment meta data

    name
        Name of the signal
    data_type
        Datatype of the signal, e.g. np.double
    description
        Brief description of the signal. This is used for the attributes
    unit
        Unit of the signal. This is used for the attributes
    """
    def __init__(self, name, data_type, description, unit):
        self.name = name
        self.type = data_type
        self.description = description
        self.attr_value = unit

    def __str__(self):
        return "Name: {0}; Type: {1}; Description: {2}; Attribute: {3}".format(
            self.name, self.type, self.description, self.attr_value
        )


active_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                     "NOT_ACTIVE": 0,
                                                     "ACTIVE": 1,
                                                     "UNKNOWN": 9}))
available_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                        "NOT_AVAILABLE": 0,
                                                        "AVAILABLE": 1,
                                                        "UNKNOWN": 9}))
brake_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                    "OFF": 0,
                                                    "ON": 1,
                                                    "FLASHING": 2,
                                                    "UNKNOWN": 9}))
indicator_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                        "OFF": 0,
                                                        "LEFT": 1,
                                                        "RIGHT": 2,
                                                        "HAZARD": 3,
                                                        "UNKNOWN": 9}))
hands_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                    "HANDS_OFF": 0,
                                                    "ONE_HAND": 1,
                                                    "TWO_HANDS": 2,
                                                    "UNKNOWN": 9}))
wiper_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                    "OFF": 0,
                                                    "SLOW_INTERVAL": 1,
                                                    "FAST_INTERVAL": 2,
                                                    "SLOW": 3,
                                                    "FAST": 4,
                                                    "UNKNOWN": 9}))
on_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                 "OFF": 0,
                                                 "ON": 1,
                                                 "UNKNOWN": 9}))
obj_classification_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                                 "OTHER": 0,
                                                                 "CAR": 1,
                                                                 "TRUCK": 2,
                                                                 "MOTORCYCLE": 3,
                                                                 "BICYCLE": 4,
                                                                 "PEDESTRIAN": 5,
                                                                 "UNKNOWN": 9}))
lane_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                   "NONE": 0,
                                                   "CONTINUOUS": 1,
                                                   "DASHED": 2,
                                                   "DOTS": 3,
                                                   "DOUBLE": 4,
                                                   "OTHER": 5,
                                                   "UNKNOWN": 9}))
precipitation_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                            "NONE": 0,
                                                            "LIGHT": 1,
                                                            "MODERATE": 2,
                                                            "HEAVY": 3,
                                                            "VIOLENT": 4,
                                                            "UNKNOWN": 9}))
precipitation_type_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                                 "NONE": 0,
                                                                 "RAIN": 1,
                                                                 "SNOW": 2,
                                                                 "HAIL": 3,
                                                                 "UNKNOWN": 9}))
weather_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                      "OTHER": 0,
                                                      "NO_ADVERSE_CONDITIONS": 1,
                                                      "WIND_GUSTS": 2,
                                                      "FOG": 3,
                                                      "MIST_OR_LIGHT_RAIN": 4,
                                                      "RAINING": 5,
                                                      "SNOWING": 6,
                                                      "SLEETING": 7,
                                                      "RAIN_AND_FOG": 8,
                                                      "SNOW_OR_SLEET_AND_FOG": 9,
                                                      "UNKNOWN": 99}))

road_surface_condition_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                                     "DRY": 0,
                                                                     "WET": 1,
                                                                     "ICE": 2,
                                                                     "SNOW": 3,
                                                                     "AQUAPLANE": 9}))
road_type_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                        "OTHER": 0,
                                                        "DUAL_CARRIAGEWAY_MULTIPLE_LANES": 1,
                                                        "DUAL_CARRIAGEWAY_2_LANES": 2,
                                                        "DUAL_CARRIAGEWAY_2_1_LANES": 3,
                                                        "DUAL_CARRIAGEWAY_SINGLE_LANES": 4,
                                                        "SINGLE_CARRIAGEWAY_MULTIPLE_LANES": 5,
                                                        "SINGLE_CARRIAGEWAY_4_LANES": 6,
                                                        "SINGLE_CARRIAGEWAY_3_LANES": 7,
                                                        "SINGLE_CARRIAGEWAY_2_LANES": 8,
                                                        "WIDE_LANE_ROAD": 9,
                                                        "SINGLE_TRACK_ROAD": 10,
                                                        "SINGLE_TRACK_ROAD_ONE_WAY": 11,
                                                        "PARKING_LOT_OR_RAMP": 12,
                                                        "ENTRANCE_OR_EXIT_RAMP": 13,
                                                        "DRIVEWAY_OR_ALLEY": 14,
                                                        "GRAVEL_ROAD": 15,
                                                        "INTERSECTION": 16,
                                                        "UNKNOWN": 99}))
rules_intersection_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                                 "OTHER": 0,
                                                                 "REGULATED_BY_LAW_ONLY": 1,
                                                                 "TRAFFIC_SIGNS_AND_ROAD_MARKINGS": 2,
                                                                 "TRAFFIC_LIGHTS_ALLOWING_PARTIAL_CONFLICTS": 3,
                                                                 "TRAFFIC_LIGHTS_NOT_ALLOWING_PARTIAL_CONFLICTS": 4,
                                                                 "UNKNOWN": 9}))
intersection_type_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                                "NONE": 0,
                                                                "X_INTERSECTION": 1,
                                                                "T_INTERSECTION_RIGHT": 2,
                                                                "T_INTERSECTION_LEFT": 3,
                                                                "T_INTERSECTION_BY_ROAD": 4,
                                                                "Y_INTERSECTION": 5,
                                                                "ROUNDABOUT": 6,
                                                                "5_OR_MORE_LEGS": 7,
                                                                "MERGING_LANE": 8,
                                                                "PASSING_BY_MERGING_LANE": 9,
                                                                "EXIT_OR_TURNING_LANE": 10,
                                                                "COMPLEX_INTERSECTION": 11,
                                                                "UNKNOWN": 99}))
type_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                   "UNKNOWN": 0,
                                                   "PROFESSIONAL": 1,
                                                   "COMPANY_EMPLOYEE": 2,
                                                   "ORDINARY_SUPERVISION": 3,
                                                   "ORDINARY": 4,
                                                   "NAIVE_PASSENGER": 5}))
drive_type_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                         "UNKNOWN": 0,
                                                         "ICE": 1,
                                                         "ELECTRIC_MOTOR": 2,
                                                         "HYBRID": 3,
                                                         "HORSE": 4}))
fuel_type_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                        "NA_EV": 0,
                                                        "GASOLINE": 1,
                                                        "DIESEL": 2,
                                                        "H2": 3,
                                                        "FOSSIL_GAS": 4,
                                                        "OTHER": 5}))
transmission_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                           "NONE": 0,
                                                           "MANUAL": 1,
                                                           "AUTOMATIC": 2,
                                                           "OTHER": 3}))
analysis_eligible_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                                "NOT_ELIGIBLE": 0,
                                                                "ELIGIBLE": 1,
                                                                "UNKNOWN": 2}))
baseline_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                       "NOT_BASELINE": 0,
                                                       "BASELINE": 1,
                                                       "UNKNOWN": 2}))
test_site_enum = h5py.h5t.special_dtype(enum=(np.int8, {"NOT_APPLICABLE": -1,
                                                        "UNKNOWN": 0,
                                                        "TEST_TRACK": 1,
                                                        "PUBLIC_ROAD": 2,
                                                        "PRIVATE_PARKING": 3,
                                                        "PUBLIC_PARKING": 4}))
str_dtype = h5py.h5t.C_S1.copy()
str_dtype.set_size(h5py.h5t.VARIABLE)
str_dtype.set_strpad(h5py.h5t.STR_NULLTERM)
# def gen_str_len():
#     temp_dtype = h5py.h5t.C_S1.copy()
#     temp_dtype.set_size()
#     temp_dtype.set.set_strpad(h5py.h5t.STR_NULLTERM)
#     return temp_dtype

# The struct with the definition of the ego signals
ego_struct = [
    EgoTop("UTCTime", np.int64, "System time in milliseconds since epoch", "ms", np.int64(-1)),
    EgoTop("FileTime", np.double, "Time since beginning of recording", "s", np.nan),
    EgoTop("ABSIntervention", active_enum, "Whether the ABS system is intervening", "-", np.int8(-1)),
    EgoTop("ADFunctionActive", active_enum, "Status of the autonomous driving function", "-", np.int8(-1)),
    EgoTop("ADFunctionAvailable", available_enum, "Whether the preconditions are met", "-", np.int8(-1)),
    EgoTop("AmbientLightLevel", np.double, "Ambient light level at the location of the ego vehicle", "ln(lux)", np.nan),
    EgoTop("AmbientTemperature", np.double, "Ambient temperature recorded by the ego vehicles temperature sensor",
           "°C", np.nan),
    EgoTop("BaselineADASActive", np.int32, "The combination of ADAS active in baseline mode, bit-wise encoding", "-",
           np.int32(-1)),
    EgoTop("BaselineADASIntervention", np.int32,
           "The combination of ADAS intervening showing warnings in baseline mode, bit-wise encoding", "-",
           np.int32(-1)),
    EgoTop("BrakeLight", brake_enum, "Status of the brake light", "-", np.int8(-1)),
    EgoTop("BrakePedalPos", np.int32, "The position of the brake pedal in %", "%", np.int32(-1)),
    EgoTop("BrakePressure", np.int32, "Brake system pressure", "%", np.int32(-1)),
    EgoTop("DirectionIndicator", indicator_enum, "The status of the turn indicator", "-", np.int8(-1)),
    EgoTop("EnergyConsumption", np.double, "Current energy consumption", "kWh/100 km", np.nan),
    EgoTop("ESCIntervention", active_enum, "Whether the electronic slip control / traction control system is "
                                           "intervening", "-", np.int8(-1)),
    EgoTop("FrontFogLightStatus", on_enum, "The status of the front fog light", "-", np.int(-1)),
    EgoTop("FrontWiperStatus", wiper_enum, "The operation state of the front wiper", "-", np.int8(-1)),
    EgoTop("FuelConsumption", np.double, "Current fuel consumption", "l/100km", np.nan),
    EgoTop("HandsOnDetection", hands_enum, "Whether the driver has hands on the steering wheel", "-", np.int8(-1)),
    EgoTop("LatAcceleration", np.double, "Lateral acceleration (Y axis)", "m/s²", np.nan),
    EgoTop("LongAcceleration", np.double, "Longitudinal acceleration (X axis)", "m/s²", np.nan),
    EgoTop("Odometer", np.double, "The vehicle odometer reading", "m", np.nan),
    EgoTop("RearFogLightStatus", on_enum, "The status of the rear fog light", "-", np.int8(-1)),
    EgoTop("SteeringAngle", np.double, "The steering wheel angle", "rad", np.nan),
    EgoTop("SteeringAngleADF", np.double, "The desired steering wheel angle by the ADF", "rad", np.nan),
    EgoTop("ThrottlePedalPos", np.int32, "The position of the throttle pedal in %", "%", np.int32(-1)),
    EgoTop("TOR", active_enum, "Whether the take-over request to return control to the driver is active", "-",
           np.int8(-1)),
    EgoTop("TorsionBarTorque", np.double, "Steering force applied by the driver on the torsion bar", "Nm", np.nan),
    EgoTop("VehicleSpeed", np.double, "Speed of the ego vehicle as reported by ABS / wheel sensing module", "m/s",
           np.nan),
    EgoTop("YawRate", np.double, "The yaw rate of the ego vehicle", "rad/s", np.nan)
]
# The struct with the definitions of the lanes signals
lane_struct = [
    LaneTop("UTCTime", np.int64, "System time in milliseconds since epoch", "ms", np.int64(-1)),
    LaneTop("FileTime", np.double, "Time since beginning of recording", "s", np.nan),
    LaneTop("EgoLaneWidth", np.double, "Width of the current lane", "m", np.nan),
    LaneSObject("Curvature", np.double, "The curvature of the lane markings", "1/m", np.nan),
    LaneSObject("CurvatureDx", np.double, "Differential curvature of the lane markings", "1/m²", np.nan),
    LaneSObject("Dy", np.double, "Lateral position of lane markings", "m", np.nan),
    LaneSObject("QualityIndex", np.int32, "Quality index for lane detection", "-", np.int32(-1)),
    LaneSObject("Type", lane_enum, "Type of lane marking", "-", np.int8(-1)),
    LaneSObject("YawAngle", np.double, "Yaw angle between ego heading and lane markings", "rad", np.nan)
]
# The struct with the definitions of the objects signals
object_struct = [
    ObjectTop("UTCTime", np.int64, "System time in milliseconds since epoch", "ms", np.int64(-1)),
    ObjectTop("FileTime", np.double, "Time since beginning of recording", "s", np.nan),
    ObjectTop("LeadVehicleID", np.int32, "ID of the objects currently selected as lead vehicle", "-", np.int32(-1)),
    ObjectTop("NumberOfObjects", np.int32, "Number of objects in timestep", "-", np.int32(-1)),
    ObjectSObject("Classification", obj_classification_enum, "Classification of the object", "-", np.int8(-1)),
    ObjectSObject("Height", np.double, "Height of the object", "m", np.nan),
    ObjectSObject("ID", np.int32, "Unique ID of the object", "-", np.int32(-1)),
    ObjectSObject("LatPosition", np.double, "Position in lateral direction in the ego coordinate system", "m", np.nan),
    ObjectSObject("LatVelocity", np.double, "Relative velocity in lateral direction", "m/s", np.nan),
    ObjectSObject("Length", np.double, "Length of the object", "m", np.nan),
    ObjectSObject("LongPosition", np.double, "Position in longitudinal direction in the ego coordinate system", "m",
                  np.nan),
    ObjectSObject("LongVelocity", np.double, "Relative velocity in longitudinal direction", "m/s", np.nan),
    ObjectSObject("Width", np.double, "Width of the object", "m", np.nan),
    ObjectSObject("YawAngle", np.double, "Relative yaw angle of the object", "rad", np.nan),
    ObjectSObject("YawRate", np.double, "Relative yaw angle rate of the object", "rad/s", np.nan)
]
# The struct with the definitions of the positioning signals
pos_struct = [
    PosTop("UTCTime", np.int64, "System time in milliseconds since epoch", "ms", np.int64(-1)),
    PosTop("FileTime", np.double, "Time since beginning of recording", "s", np.nan),
    PosTop("Altitude", np.double, "Altitude of the ego vehicle", "m", np.nan),
    PosTop("GNSSSpeed", np.double, "Speed of the ego vehicle as reported by the GNSS module / IMU", "m/s", np.nan),
    PosTop("GNSSTime", np.int64, "GNSS time (UTC) in milliseconds since epoch", "ms", np.int64(-1)),
    PosTop("Heading", np.double, "Heading of the ego vehicle", "rad", np.nan),
    PosTop("Latitude", np.double, "Latitude of the ego vehicle", "deg", np.nan),
    PosTop("Longitude", np.double, "Longitude of the ego vehicle", "deg", np.nan),
    PosTop("NumberOfSatellites", np.int32, "The number of used satellites", "-", np.int32(-1)),
]
# The struct with the definitions of the external data signals
weather_struct = [
    ExternalData("UTCTime", np.int64, "System time in milliseconds since epoch", "ms", np.int64(-1)),
    ExternalData("FileTime", np.double, "Time since beginning of recording", "s", np.nan),
    ExternalData("Precipitation", precipitation_enum, "The amount of precipitation at the current time step",
                 "mm", np.nan),
    ExternalData("PrecipitationType", precipitation_type_enum, "Th type of precipitation", "-", np.int8(-1)),
    ExternalData("RoadSurfaceCondition", road_surface_condition_enum, "The condition of the road surface", "-",
                        np.int8(-1)),
    ExternalData("Temperature", np.double, "Ambient temperature at location of ego vehicle", "°C", np.nan),
    ExternalData("Weather", weather_enum, "The weather conditions", "-", np.int8(-1))
]
map_struct = [
    ExternalData("UTCTime", np.int64, "System time in milliseconds since epoch", "ms", np.int64(-1)),
    ExternalData("FileTime", np.double, "Time since beginning of recording", "s", np.nan),
    ExternalData("DistIntersection", np.double, "Distance to next intersection", "m", np.nan),
    ExternalData("NumberOfLanes", np.int32, "The number of lanes on the current road section", "-", np.int32(-1)),
    ExternalData("RoadType", road_type_enum, "The type of road", "-", np.int8(-1)),
    ExternalData("RulesIntersection", rules_intersection_enum, "Type of priority rules at intersection", "-",
                 np.int(-1)),
    ExternalData("SpeedLimit", np.int32, "The speed limit at the current location of the ego vehicle in m/s; "
                                         "-1 = unlimited; 0 = no information", "m/s", np.int32(-2)),
    ExternalData("TypeIntersection", intersection_type_enum, "The type of the next intersection", "-", np.int8(-1)),
]
# The struct with the definitions of the meta data
meta_struct = [
    MetaGeneral("ADFVersion", np.double, "Version of the ADF", "-"),
    MetaGeneral("FormatVersion", np.double, "Version of the L3Pilot data format", "-"),
    MetaGeneral("Partner", str_dtype, "Partner that recorded the file", "-"),
    MetaGeneral("RecordDate", str_dtype, "Date and time of the start of recording", "-"),
    MetaGeneral("UTCOffset", np.int32, "Offset from the UTC time", "h"),
    MetaDriver("DriverID", str_dtype, "ID of the driver", "-"),
    MetaDriver("DriverType", type_enum, "Whether the driver is a professional", "-"),
    MetaCar("DriveType", drive_type_enum, "Whether ego vehicle is EV or powered by ICE", "-"),
    MetaCar("FuelType", fuel_type_enum, "Type of fuel or propellant for the vehicle", "-"),
    MetaCar("NumberOfOccupants", np.int32, "The number of occupants in the vehicle", "-"),
    MetaCar("PositionFrontBumper", np.double, "Distance rear axle front bumper", "m"),
    MetaCar("PositionRearBumper", np.double, "Distance rear axle rear bumper", "m"),
    MetaCar("Transmission", transmission_enum, "The type of transmission used", "-"),
    MetaCar("VehicleID", str_dtype, "The vehicle ID. Coding is up to the OEM", "-"),
    MetaCar("VehicleLength", np.double, "The length of the vehicle", "m"),
    MetaCar("VehicleWeight", np.int32, "Estimated weight of the vehicle. Constant for trip", "kg"),
    MetaCar("VehicleWidth", np.double, "Width of the vehicle", "m"),
    MetaExperiment("AnalysisEligible", analysis_eligible_enum, "Whether the logfile should be used for analysis",
                   "-"),
    MetaExperiment("Baseline", baseline_enum, "Whether the trip was a baseline test", "-"),
    MetaExperiment("Country", str_dtype, "The country of the test site", "-"),
    MetaExperiment("TestEndOdo", np.int32, "Odometer reading at end of test", "km"),
    MetaExperiment("TestEndTime", np.int64, "End time of test as UTC timestamp", "s"),
    MetaExperiment("TestSiteType", test_site_enum, "Type of test site for the trip", "-"),
    MetaExperiment("TestStartOdo", np.int32, "Odometer reading at start of test", "km"),
    MetaExperiment("TestStartTime", np.int64, "Start time of test as UTC timestamp", "s"),
    MetaExperiment("TripID", str_dtype, "ID of the trip", "-")
]


def generate_ego_struct():
    """ Generate the datatype for the ego dataset

    :return: The datatype for the ego dataset and the fill values for the ego dataset
    """
    ego_top_list = []
    for item in [list1 for list1 in ego_struct]:
        ego_top_list.append((item.name, item.type))
    d_ego = np.dtype(ego_top_list)
    ego_fill = np.zeros((len(ego_top_list), ), dtype=d_ego)
    for item in [list1 for list1 in ego_struct]:
        ego_fill[item.name] = item.fill_value
    return d_ego, ego_fill


def generate_lane_struct():
    """ Generate the datatype for the lanes dataset

    :return: The datatype for the lanes dataset and the fill values for the lanes dataset
    """
    lane_top_list = []
    for item in [list1 for list1 in lane_struct if list1.__class__.__name__ == "LaneTop"]:
        lane_top_list.append((item.name, item.type))
    lane_list = []
    for item in [list1 for list1 in lane_struct if list1.__class__.__name__ == "LaneSObject"]:
        lane_list.append((item.name, item.type))
    lane_top_list.append((str_lan_obj, lane_list, 4))
    d_lane = np.dtype(lane_top_list)
    lane_fill = np.zeros((len(lane_top_list), ), dtype=d_lane)
    for item in [list1 for list1 in lane_struct if list1.__class__.__name__ == "LaneTop"]:
        lane_fill[item.name] = item.fill_value
    for item in [list1 for list1 in lane_struct if list1.__class__.__name__ == "LaneSObject"]:
        lane_fill[str_lan_obj][item.name] = item.fill_value
    return d_lane, lane_fill


def generate_object_struct():
    """ Generate the datatype for the objects dataset

    :return: The datatype for the objects dataset and the fill values for the objects dataset
    """
    object_top_list = []
    for item in [list1 for list1 in object_struct if list1.__class__.__name__ == "ObjectTop"]:
        object_top_list.append((item.name, item.type))
    object_list = []
    for item in [list1 for list1 in object_struct if list1.__class__.__name__ == "ObjectSObject"]:
        object_list.append((item.name, item.type))
    object_top_list.append((str_obj_obj, object_list, 32))
    d_object = np.dtype(object_top_list)
    object_fill = np.zeros((len(object_top_list), ), dtype=d_object)
    for item in [list1 for list1 in object_struct if list1.__class__.__name__ == "ObjectTop"]:
        object_fill[item.name] = item.fill_value
    for item in [list1 for list1 in object_struct if list1.__class__.__name__ == "ObjectSObject"]:
        object_fill[str_obj_obj][item.name] = item.fill_value
    return d_object, object_fill


def generate_pos_struct():
    """ Generate the datatype for the positioning dataset

    :return: The datatype for the positioning dataset and the fill values for the positioning dataset
    """
    pos_top_list = []
    for item in [list1 for list1 in pos_struct]:
        pos_top_list.append((item.name, item.type))
    d_pos = np.dtype(pos_top_list)
    pos_fill = np.zeros((len(pos_top_list), ), dtype=d_pos)
    for item in [list1 for list1 in pos_struct]:
        pos_fill[item.name] = item.fill_value
    return d_pos, pos_fill


def generate_external_data_struct():
    """ Generate the datatypes for the external data datasets

    :return: The datatype for the external data datasets and the fill values for the external data datasets
    """
    external_weather = []
    for item in [list1 for list1 in weather_struct]:
        external_weather.append((item.name, item.type))
    d_ext_weather = np.dtype(external_weather)
    external_map = []
    for item in [list1 for list1 in map_struct]:
        external_map.append((item.name, item.type))
    d_ext_map = np.dtype(external_map)
    ext_weather_fill = np.zeros((len(external_weather), ), dtype=d_ext_weather)
    for item in [list1 for list1 in weather_struct]:
        ext_weather_fill[item.name] = item.fill_value
    ext_map_fill = np.zeros((len(external_map),), dtype=d_ext_map)
    for item in [list1 for list1 in map_struct]:
        ext_map_fill[item.name] = item.fill_value

    return d_ext_weather, d_ext_map, ext_weather_fill, ext_map_fill


def generate_meta_struct():
    """ Generate the datatype for the meta data

    :return: The datatype for the meta data
    """
    meta_top_list = []
    meta_general_list = []
    for item in [list1 for list1 in meta_struct if list1.__class__.__name__ == "MetaGeneral"]:
        meta_general_list.append((item.name, item.type))
    meta_top_list.append(("General", meta_general_list))
    meta_driver_list = []
    for item in [list1 for list1 in meta_struct if list1.__class__.__name__ == "MetaDriver"]:
        meta_driver_list.append((item.name, item.type))
    meta_top_list.append(("Driver", meta_driver_list))
    meta_car_list = []
    for item in [list1 for list1 in meta_struct if list1.__class__.__name__ == "MetaCar"]:
        meta_car_list.append((item.name, item.type))
    meta_top_list.append(("Car", meta_car_list))
    meta_experiment_list = []
    for item in [list1 for list1 in meta_struct if list1.__class__.__name__ == "MetaExperiment"]:
        meta_experiment_list.append((item.name, item.type))
    meta_top_list.append(("Experiment", meta_experiment_list))
    d_meta = np.dtype(meta_top_list)
    return d_meta


# Some general definitions and the datatypes
cdf_version = __version__
c_ego, c_ego_fill = generate_ego_struct()
c_lane, c_lane_fill = generate_lane_struct()
c_objects, c_objects_fill = generate_object_struct()
c_pos, c_pos_fill = generate_pos_struct()
c_ext_weather, c_ext_map, c_weather_fill, c_map_fill = generate_external_data_struct()
c_meta_data = generate_meta_struct()
