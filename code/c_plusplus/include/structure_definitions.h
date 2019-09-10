/**
*
*   @brief L3Pilot Common Data Format Definition Header
*
*   This header file provides the structures for the different datasets that are needed to create the L3Pilot Common Data Format HDF5 files.
*
*   @file
*   Copyright &copy; Institut fuer Kraftfahrzeuge, Johannes Hiller, 2018
*
*   @author Johannes Hiller
*   @version 0.8
*/

#include <hdf5.h>
#define _USE_MATH_DEFINES // for C++  
#include <math.h>

#define OBJECT_NUMBER 32
#define LANE_NUMBER 4

//! Namespace for hosting the enum for activity of systems
namespace ActiveEnum {
    //! Enum for the different states of a system which can be active or not
    enum Enum {
        //! Not applicable to the system or was not applied
        NOT_APPLICABLE =    -1,
        //! System is not active
        NOT_ACTIVE =        0,
        //! System is active
        ACTIVE =            1,
        //! System state is unknown
        UNKNOWN =           9
    };
}

//! Namespace for hosting the enum for availability of systems
namespace AvailableEnum {
    //! Enum for defining the availability of a system
    enum Enum {
        //! Availability is not applicable to the system or was not applied
        NOT_APPLICABLE =    -1,
        //! System is not available
        NOT_AVAILABLE =     0,
        //! System is available
        AVAILABLE =         1,
        //! System availability is unknown
        UNKNOWN =           9
    };
}

//! Namespace for hosting the enum for the brake light status
namespace BrakeEnum {
    //! Enum for the different states of the brake light
    enum Enum {
        //! Definition is not applicable or was not applied
        NOT_APPLICABLE =    -1,
        //! Brake light is off
        OFF =               0,
        //! Brake light is on
        ON =                1,
        //! Brake light is flashing, e.g. during emergency brakings
        FLASHING =          2,
        //! Brake light status is unknown
        UNKNOWN =           9
    };
}

//! Namespace for hosting the enum for the direction indicator status
namespace IndicatorEnum {
    //! Enum for the different states of the direction indicator
    enum Enum {
        //! Definition is not applicable or was not applied
        NOT_APPLICABLE =    -1,
        //! Direction indicator is off
        OFF =               0,
        //! Direction indicator left is on
        LEFT =              1,
        //! Direction indicator right is on
        RIGHT =             2,
        //! Hazard lights are on i.e. direction indicator left and right are both on
        HAZARD =            3,
        //! Direction indicator status is unknown
        UNKNOWN =           9
    };
}

//! Namespace for hosting the enum for the number of hands on the steering wheel
namespace HandsEnum {
    //! Enum for the different possibilities of hands on the steering wheel
    enum Enum {
        //! Definition is not applicable or was not applied
        NOT_APPLICABLE =    -1,
        //! No hands are on the wheel
        HANDS_OFF =         0,
        //! One hand is on the wheel
        ONE_HAND =          1,
        //! Two hands are on the wheel
        TWO_HANDS =         2,
        //! The number of hands on the wheel is unknown
        UNKNOWN =           9
    };
}

//! Namespace for hosting the enum for the wiper status
namespace WiperEnum {
    //! Enum for the different wiper states
    enum Enum {
        //! Definition is not applicable or was not applied
        NOT_APPLICABLE = -  1,
        //! The wiper is off (not used)
        OFF =               0,
        //! The wiper is set to a slow interval
        SLOW_INTERVAL =     1,
        //! The wiper is set to a fast interval
        FAST_INTERVAL =     2,
        //! The wiper is continuously active in slow mode
        SLOW =              3,
        //! The wiper is continuously active in fast mode
        FAST =              4,
        //! The status of the wiper is unknown
        UNKNOWN =           9
    };
}
//! Namespace for hosting the enum for an on and off state
namespace OnEnum {
    //! Enum for the on and off state
    enum Enum {
        //! Definition is not applicable or was not applied
        NOT_APPLICABLE =    -1,
        //! The system is off
        OFF =               0,
        //! The system is on
        ON =                1,
        //! The system state is unknown
        UNKNOWN =           9
    };
}
//! Namespace for hosting the enum for the TOR status
namespace TOREnum
{
    //! Enum for the TOR status
    enum Enum
    {
        //! Definition is not applicable or was not applied
        NOT_APPLICABLE =    -1,
        //! TOR is not active
        NOT_ACTIVE =        0,
        //! A TOR is active, i.e. a planned TOR
        ACTIVE =            1,
        //! A short TOR is active, i.e. an unplanned TOR
        SHORT_ACTIVE =      2,
        //! TOR status is unknown
        UNKNOWN =           9
    };
}

//! The structure of the ego data in the HDF5 file
typedef struct ego_t {
    //! GNSS time (UTC) in microseconds since epoch
    long long UTCTime =                         -1;
    //! Time since beginning of recording
    double FileTime =                           NAN;
    //! Whether the ABS system is intervening @see ActiveEnum
    ActiveEnum::Enum ABSIntervention =          ActiveEnum::NOT_APPLICABLE;
    //! Status of the autonomous driving function @see ActiveEnum
    ActiveEnum::Enum ADFunctionActive =         ActiveEnum::NOT_APPLICABLE;
    //! Whether the preconditions are met to enable the autonomous driving function @see AvailableEnum
    AvailableEnum::Enum ADFunctionAvailable =   AvailableEnum::NOT_APPLICABLE;
    //! Ambient light level at the location of the ego vehicle
    double AmbientLightLevel =                  NAN;
    //! Ambient temperature recorded by the ego vehicles temperature sensor
    double AmbientTemperature =                 NAN;
    //! The combination of ADAS active in baseline mode, bit-wise encoding
    int BaselineADASActive =                    -1;
    //! The combination of ADAS intervening in baseline mode, bit-wise encoding
    int BaselineADASIntervention =              -1;
    //! Status of the brake light @see BrakeEnum
    BrakeEnum::Enum BrakeLight =                BrakeEnum::NOT_APPLICABLE;
    //! The position of the brake pedal in %
    int BrakePedalPos =                         -1;
    //! Brake system pressure
    int BrakePressure =                         -1;
    //! The status of the turn indicator @see IndicatorEnum
    IndicatorEnum::Enum DirectionIndicator =    IndicatorEnum::NOT_APPLICABLE;
    //! Current energy consumption
    double EnergyConsumption =                  NAN;
    //! Whether the electronic slip control / traction control system is intervening @see ActiveEnum
    ActiveEnum::Enum ESCIntervention =          ActiveEnum::NOT_APPLICABLE;
    //! The status of the front fog light
    OnEnum::Enum FrontFogLightStatus =          OnEnum::NOT_APPLICABLE;
    //! State of the front wiper
    WiperEnum::Enum FrontWiperStatus =          WiperEnum::NOT_APPLICABLE;
    //! Current fuel consumption
    double FuelConsumption =                    NAN;
    //! Whether the driver has their hand on the steering wheel @see HandsEnum
    HandsEnum::Enum HandsOnDetection =          HandsEnum::NOT_APPLICABLE;
    //! Lateral acceleration (Y axis)
    double LatAcceleration =                    NAN;
    //! Longitudinal acceleration (X axis)
    double LongAcceleration =                   NAN;
    //! The vehicle odometer reading
    double Odometer =                           NAN;
    //! The status of the rear fog light
    OnEnum::Enum RearFogLightStatus =           OnEnum::NOT_APPLICABLE;
    //! The steering wheel angle
    double SteeringAngle =                      NAN;
    //! The turning rate of the steering wheel
    double SteeringAngleADF =                   NAN;
    //! The position of the throttle pedal in %
    int ThrottlePedalPos =                      -1;
    //! Whether the take-over-request to return control to the driver is active @see ActiveEnum
    TOREnum::Enum TOR =                         TOREnum::NOT_APPLICABLE;
    //! Steering force applied by the driver on the torsion bar
    double TorsionBarTorque =                   NAN;
    //! Speed of the ego vehicle as reported by the ABS / wheel sensing module
    double VehicleSpeed =                       NAN;
    //! The yaw rate of the ego vehicle
    double YawRate =                            NAN;
} ego_t;


//! The structure of the positioning data in the HDF5 file
typedef struct pos_t {
    //! GNSS time (UTC) in microseconds since epoch
    long long UTCTime =         -1;
    //! Time since beginning of recording
    double FileTime =           NAN;
    //! Altitude of the ego vehicle (WGS84)
    double Altitude =           NAN;
    //! The speed of the ego vehicle according to GNSS / IMU
    double GNSSSpeed =          NAN;
    //! The time according to GNSS
    long long GNSSTime =        NAN;
    //! Heading of the ego vehicle
    double Heading =            NAN;
    //! Latitude of the ego vehicle (WGS84)
    double Latitude =           NAN;
    //! Longitude of the ego vehicle (WGS84)
    double Longitude =          NAN;
    //! The number of used satellites
    int NumberOfSatellites =    -1;
} pos_t;


//! Namespace for hosting the enum for the classification of dynamic objects
namespace ObjClassification {
    //! Enum for the classification of dynamic objects
    enum Enum {
        //! The definition is not applicable or was not applied
        NOT_APPLICABLE =    -1,
        //! The object classification is known but does not fit the given categories
        OTHER =             0,
        //! The object is classified as car. This also includes SUVs and vans
        CAR =               1,
        //! The object is classified as truck. This also includes buses
        TRUCK =             2,
        //! The object is classified as motorcycle
        MOTORCYCLE =        3,
        //! The object is classified as bicycle
        BICYCLE =           4,
        //! The object is classified as pedestrian or as group of pedestrians
        PEDESTRIAN =        5,
        //! The classification of the object is unknown
        UNKNOWN =           9
    };
}

//! The structure of each object in the HDF5 file
typedef struct obj_obj_t {
    //! The classification of the object. @see ObjClassification
    ObjClassification::Enum Classification =    ObjClassification::NOT_APPLICABLE;
    //! Height of the object
    double Height =                             NAN;
    //! Unique ID of the object
    int ID =                                    -1;
    //! Position in lateral direction in the ego coordinate system
    double LatPosition =                        NAN;
    //! Relative velocity in lateral direction
    double LatVelocity =                        NAN;
    //! Length of the object
    double Length =                             NAN;
    //! Position in longitudinal direction in the ego coordinate system
    double LongPosition =                       NAN;
    //! Relative velocity in longitudinal direction
    double LongVelocity =                       NAN;
    //! Width of the object
    double Width =                              NAN;
    //! Relative yaw angle of the object
    double YawAngle =                           NAN;
    //! Relative yaw rate of the object
    double YawRate =                            NAN;
} obj_obj_t;

//! The structure of all objects in the HDF5 file
typedef struct obj_t {
    //! GNSS time (UTC) in microseconds since epoch
    long long UTCTime =     -1;
    //! Time since beginning of recording
    double FileTime =       NAN;
    //! ID of the object currently selected as lead vehicle
    int LeadVehicleID =     -1;
    //! Number of objects in timestamp
    int NumberOfObjects =   -1;
    //! Array containing all object information
    obj_obj_t sObject[OBJECT_NUMBER];
} obj_t;

//! Namespace for hosting the enum for the different lane marking types
namespace LaneType {
    //! Enum for the different lane marking types
    enum Enum {
        //! The definition is not applicable or was not applied
        NOT_APPLICABLE =    -1,
        //! The type of lane marking is not a lane marking
        NONE =              0,
        //! The type of lane marking is continuous
        CONTINUOUS =        1,
        //! The type of lane marking is dashed
        DASHED =            2,
        //! The type of lane marking is dots
        DOTS =              3,
        //! The type of lane marking is double
        DOUBLE =            4,
        //! The type of lane marking is known but doesn't fit the given categories
        OTHER =             5,
        //! The type of lane marking is unknown
        UNKNOWN =           9
    };
}

//! The structure of each lane in the HDF5 file
typedef struct lan_lan_t {
    //! The curvature of the lane markings
    double Curvature =      NAN;
    //! The curvature derivative of the lane markings
    double CurvatureDx =    NAN;
    //! The lateral position of the lane markings
    double Dy =             NAN;
    //! Quality index for lane detection
    int QualityIndex =      -1;
    //! Type of lane marking
    LaneType::Enum Type =   LaneType::NOT_APPLICABLE;
    //! Yaw angle between ego heading and lane markings
    double YawAngle =       NAN;
} lan_lan_t;

//! The structure of all lanes in the HDF5 file
typedef struct lan_t {
    //! GNSS time (UTC) in microseconds since epoch
    long long UTCTime =     -1;
    //! Time since beginning of recording
    double FileTime =       NAN;
    //! Width of the current lane
    double EgoLaneWidth =   NAN;
    //! Array of lane markings; 0=right;1=left;2=right of right lane;3=left of left lane
    lan_lan_t sLaneLine[LANE_NUMBER];
} lan_t;

//! The structure for the general meta data
typedef struct met_gen_t {
    //! Version of the ADF
    double ADFVersion =     NAN;
    //! Version of the L3Pilot data format
    double FormatVersion =  NAN;
    //! Partner that recorded the file
    char * Partner =        "DEFAULT";
    //! Date and time of the start of recording. For easy access
    char * RecordDate =     "06-Dec-2018 12:14:36";
    //! Offset from the UTC time
    int UTCOffset =         -99;
} met_gen_t;

//! Namespace for hosting the driver type
namespace DriverEnum {
    //! Whether the driver is a professional or naive driver
    enum Enum {
        //! The definition is not applicable or was not applied
        NOT_APPLICABLE =        -1,
        //! The type of driver is unknown
        UNKNOWN =               0,
        //! Professional driver
        PROFESSIONAL =          1,
        //! Company employee (not professional) driver
        COMPANY_EMPLOYEE =      2,
        //! Ordinary driver with supervision
        ORDINARY_SUPERVISION =  3,
        //! Ordinary driver without supervision
        ORDINARY =              4,
        //! Naive subject as a passenger
        NAIVE_PASSENGER =       5,
    };
}

//! The structure for the driver related meta data
typedef struct met_dri_t {
    //! ID of the driver
    char * DriverID =               "someHexValue";
    //! Whether driver is a professional or naive driver
    DriverEnum::Enum DriverType =   DriverEnum::NOT_APPLICABLE;
} met_dri_t;

//! Namespace for hosting drive type
namespace DriveTypeEnum {
    //! Whether ego vehicle is EV or powered by ICE
    enum Enum {
        //! The definition is not applicable or was not applied
        NOT_APPLICABLE =    -1,
        //! The drive type is unknown
        UNKNOWN =           0,
        //! The ego vehicle is powered by an internal combustion engine
        ICE =               1,
        //! The ego vehicle is powered by an electric motor
        EV =                2,
        //! The ego vehicle is a hybrid vehicle
        HYBRID =            3,
        //! The ego vehicle is pulled by a horse
        HORSE =             4
    };
}

//! Namespace for hosting the fuel type
namespace FuelTypeEnum {
    //! Type of fuel or propellant for the vehicle
    enum Enum {
        //! The definition is not applicable or was not applied
        NOT_APPLICABLE =    -1,
        //! The propellant is not available (EV only)
        NA =                0,
        //! Gasoline
        GASOLINE =          1,
        //! Diesel
        DIESEL =            2,
        //! H2
        H2 =                3,
        //! Fossil gas
        FOSSIL_GAS =        4,
        //! Other propellant
        OTHER =             5
    };
}

//! Namespace for hosting the transmission type
namespace TransmissionEnum {
    //! The type of transmission used
    enum Enum {
        //! The definition is not applicable or was not applied
        NOT_APPLICABLE =    -1,
        //! None (e.g. for EVs)
        NONE =              0,
        //! Manual transmission
        MANUAL =            1,
        //! Automatic transmission
        AUTOMATIC =         2,
        //! Other form of transmission
        OTHER =             3
    };
}

//! The struct for the car related meta data
typedef struct met_car_t {
    //! Whether ego vehicle is EV or powered by ICE
    DriveTypeEnum::Enum DriveType =          DriveTypeEnum::NOT_APPLICABLE;
    //! Type of fuel or propellant for the vehicle
    FuelTypeEnum::Enum FuelType =            FuelTypeEnum::NOT_APPLICABLE;
    //! The number of occupants in the vehicle (including test drivers and operators)
    int NumberOfOccupants =                 -1;
    //! Position of the front bumper from the rear axle
    double PositionFrontBumper =            NAN;
    //! Position of the rear bumper from the rear axle
    double PositionRearBumper =             NAN;
    //! The type of transmission used
    TransmissionEnum::Enum Transmission =   TransmissionEnum::NOT_APPLICABLE;
    //! The ID of the vehicle
    char * VehicleID =                      "someHexValue";
    //! Length of the vehicle
    double VehicleLength =                  NAN;
    //! Estimated weight of the vehicle. Constant for trip
    int VehicleWeight =                     NAN;
    //! Width of the vehicle
    double VehicleWidth =                   NAN;
} met_car_t;

//! Namespace for hosting the analysis eligible type
namespace AnalysisEnum {
    //! Whether the logfile should be used for analysis
    enum Enum {
        //! The definition is not applicable or was not applied
        NOT_APPLICABLE =    -1,
        //! The logfile is not eligible for analysis
        NOT_ELIGIBLE =      0,
        //! The logfile is eligible for analysis
        ELIGIBLE =          1,
        //! The eligibility is unknown
        UNKNOWN =           2,
    };
}

//! Namespace for hosting the baseline type
namespace BaselineEnum {
    //! Whether the trip was a baseline test
    enum Enum {
        //! The definition is not applicable or was not applied
        NOT_APPLICABLE = -1,
        //! Not baseline
        NOT_BASELINE = 0,
        //! Baseline
        BASELINE = 1,
        //! The use of the trip is unknown
        UNKNOWN = 2
    };
}

//! Namespace for hosting the test site type
namespace TestSiteEnum {
    //! The type of test site used for the current test
    enum Enum {
        //! The definition is not applicable or was not applied
        NOT_APPLICABLE = -1,
        //! The type of test site is unknown (not recommended!)
        UNKNOWN = 0,
        //! Test track / proving ground
        TEST_TRACK = 1,
        //! Public road
        PUBLIC_ROAD = 2,
        //! Private parking
        PRIVATE_PARKING = 3,
        //! Public parking
        PUBLIC_PARKING = 4
    };
}

//! The struct for the experiment related meta data
typedef struct met_exp_t {
    //! Whether the logfile should be used for analysis
    AnalysisEnum::Enum AnalysisEligible =   AnalysisEnum::NOT_APPLICABLE;
    //! Whether this trip was a baseline trip
    BaselineEnum::Enum Baseline =           BaselineEnum::NOT_APPLICABLE;
    //! The country of the test site
    char * Country =                        "XX";
    //! Odometer reading at end of test
    int TestEndOdo =                        -1;
    //! End time of test as UTC timestamp
    int TestEndTime =                 -1;
    //! Type of test site for the trip
    TestSiteEnum::Enum TestSiteType =       TestSiteEnum::NOT_APPLICABLE;
    //! Odometer reading at start of test
    int TestStartOdo =                      -1;
    //! Start time of test as UTC timestamp
    int TestStartTime =               -1;
    //! ID of the trip
    char * TripID =                         "someHexValue";
} met_exp_t;

//! The struct for the overall meta data
typedef struct met_t {
    //! The struct for the general meta data
    met_gen_t General;
    //! The struct for the driver meta data
    met_dri_t Driver;
    //! The struct for the car meta data
    met_car_t Car;
    //! The struct for the experiment meta data
    met_exp_t Experiment;
} met_t;
