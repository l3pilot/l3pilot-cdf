#include "hdf5_data_buffer.hpp"

cHDF5DataBuffer::cHDF5DataBuffer()
{
    toMS =      1000;
}

bool cHDF5DataBuffer::configureDataBuffer(std::string filename, std::string folder)
{
    m_fileName = filename;
    m_folder = folder;
    m_complete_file << folder << "\\" << filename << ".h5";

    // Create the HDF5 file
    m_file = H5Fcreate(m_complete_file.str().c_str(), H5F_ACC_TRUNC, H5P_DEFAULT, H5P_DEFAULT);

    // Configure the meta data
    configureMetaDataStructure();

    // Define the enum for defining functions active or not
    active_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(active_tid, H5T_SGN_2);
    ActiveEnum::Enum val_act;
    val_act = ActiveEnum::NOT_APPLICABLE;
    H5Tenum_insert(active_tid, "NOT_APPLICABLE",    &val_act);
    val_act = ActiveEnum::NOT_ACTIVE;
    H5Tenum_insert(active_tid, "NOT_ACTIVE",        &val_act);
    val_act = ActiveEnum::ACTIVE;
    H5Tenum_insert(active_tid, "ACTIVE",            &val_act);
    val_act = ActiveEnum::UNKNOWN;
    H5Tenum_insert(active_tid, "UNKNOWN",           &val_act);

    // Define the enum for defining availabilities
    ava_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(ava_tid, H5T_SGN_2);
    AvailableEnum::Enum val_ava;
    val_ava = AvailableEnum::NOT_APPLICABLE;
    H5Tenum_insert(ava_tid, "NOT_APPLICABLE",   &val_ava);
    val_ava = AvailableEnum::NOT_AVAILABLE;
    H5Tenum_insert(ava_tid, "NOT_AVAILABLE",    &val_ava);
    val_ava = AvailableEnum::AVAILABLE;
    H5Tenum_insert(ava_tid, "AVAILABLE",        &val_ava);
    val_ava = AvailableEnum::UNKNOWN;
    H5Tenum_insert(ava_tid, "UNKNOWN",          &val_ava);

    // Define the enum for the brake light
    brake_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(brake_tid, H5T_SGN_2);
    BrakeEnum::Enum val_bra;
    val_bra = BrakeEnum::NOT_APPLICABLE;
    H5Tenum_insert(brake_tid, "NOT_APPLICABLE", &val_bra);
    val_bra = BrakeEnum::OFF;
    H5Tenum_insert(brake_tid, "OFF",            &val_bra);
    val_bra = BrakeEnum::ON;
    H5Tenum_insert(brake_tid, "ON",             &val_bra);
    val_bra = BrakeEnum::FLASHING;
    H5Tenum_insert(brake_tid, "FLASHING",       &val_bra);
    val_bra = BrakeEnum::UNKNOWN;
    H5Tenum_insert(brake_tid, "UNKNOWN",        &val_bra);

    // Define the enum for the indicator status
    indicator_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(indicator_tid, H5T_SGN_2);
    IndicatorEnum::Enum val_ind;
    val_ind = IndicatorEnum::NOT_APPLICABLE;
    H5Tenum_insert(indicator_tid, "NOT_APPLICABLE", &val_ind);
    val_ind = IndicatorEnum::OFF;
    H5Tenum_insert(indicator_tid, "OFF",            &val_ind);
    val_ind = IndicatorEnum::LEFT;
    H5Tenum_insert(indicator_tid, "LEFT",           &val_ind);
    val_ind = IndicatorEnum::RIGHT;
    H5Tenum_insert(indicator_tid, "RIGHT",          &val_ind);
    val_ind = IndicatorEnum::HAZARD;
    H5Tenum_insert(indicator_tid, "HAZARD",         &val_ind);
    val_ind = IndicatorEnum::UNKNOWN;
    H5Tenum_insert(indicator_tid, "UNKNOWN",        &val_ind);

    // Define the enum for hands on detection 
    hands_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(hands_tid, H5T_SGN_2);
    HandsEnum::Enum val_han;
    val_han = HandsEnum::NOT_APPLICABLE;
    H5Tenum_insert(hands_tid, "NOT_APPLICABLE", &val_han);
    val_han = HandsEnum::HANDS_OFF;
    H5Tenum_insert(hands_tid, "HANDS_OFF",      &val_han);
    val_han = HandsEnum::ONE_HAND;
    H5Tenum_insert(hands_tid, "ONE_HAND",       &val_han);
    val_han = HandsEnum::TWO_HANDS;
    H5Tenum_insert(hands_tid, "TWO_HANDS",      &val_han);
    val_han = HandsEnum::UNKNOWN;
    H5Tenum_insert(hands_tid, "UNKNOWN",        &val_han);

    // Define the enum for the wiper state
    wiper_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(wiper_tid, H5T_SGN_2);
    WiperEnum::Enum val_wip;
    val_wip = WiperEnum::NOT_APPLICABLE;
    H5Tenum_insert(wiper_tid, "NOT_APPLICABLE", &val_wip);
    val_wip = WiperEnum::OFF;
    H5Tenum_insert(wiper_tid, "OFF",            &val_wip);
    val_wip = WiperEnum::SLOW_INTERVAL;
    H5Tenum_insert(wiper_tid, "SLOW_INTERVAL",  &val_wip);
    val_wip = WiperEnum::FAST_INTERVAL;
    H5Tenum_insert(wiper_tid, "FAST_INTERVAL",  &val_wip);
    val_wip = WiperEnum::SLOW;
    H5Tenum_insert(wiper_tid, "SLOW",           &val_wip);
    val_wip = WiperEnum::FAST;
    H5Tenum_insert(wiper_tid, "FAST",           &val_wip);
    val_wip = WiperEnum::UNKNOWN;
    H5Tenum_insert(wiper_tid, "UNKNOWN",        &val_wip);

    // Define the enum for the on off state
    on_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(on_tid, H5T_SGN_2);
    OnEnum::Enum val_on;
    val_on = OnEnum::NOT_APPLICABLE;
    H5Tenum_insert(on_tid, "NOT_APPLICABLE",    &val_on);
    val_on = OnEnum::OFF;
    H5Tenum_insert(on_tid, "OFF",               &val_on);
    val_on = OnEnum::ON;
    H5Tenum_insert(on_tid, "ON",                &val_on);
    val_on = OnEnum::UNKNOWN;
    H5Tenum_insert(on_tid, "UNKNOWN",           &val_on);

    // Define the enum for TOR status
    tor_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(tor_tid, H5T_SGN_2);
    TOREnum::Enum tor_sb;
    tor_sb = TOREnum::NOT_APPLICABLE;
    H5Tenum_insert(tor_tid, "NOT_APPLICABLE", &tor_sb);
    tor_sb = TOREnum::NOT_ACTIVE;
    H5Tenum_insert(tor_tid, "NOT_ACTIVE", &tor_sb);
    tor_sb = TOREnum::ACTIVE;
    H5Tenum_insert(tor_tid, "ACTIVE", &tor_sb);
    tor_sb = TOREnum::SHORT_ACTIVE;
    H5Tenum_insert(tor_tid, "SHORT_ACTIVE", &tor_sb);
    tor_sb = TOREnum::UNKNOWN;
    H5Tenum_insert(tor_tid, "UNKNOWN", &tor_sb);

    // Create the ego compound
    m_ego_tid = H5Tcreate(H5T_COMPOUND, sizeof(ego_t));
    // Add the members of the ego compound
    H5Tinsert(m_ego_tid, "UTCTime",                     HOFFSET(ego_t, UTCTime),                    H5T_STD_I64LE);
    H5Tinsert(m_ego_tid, "FileTime",                    HOFFSET(ego_t, FileTime),                   H5T_IEEE_F64LE);
    H5Tinsert(m_ego_tid, "ABSIntervention",             HOFFSET(ego_t, ABSIntervention),            active_tid);
    H5Tinsert(m_ego_tid, "ADFunctionActive",            HOFFSET(ego_t, ADFunctionActive),           active_tid);
    H5Tinsert(m_ego_tid, "ADFunctionAvailable",         HOFFSET(ego_t, ADFunctionAvailable),        ava_tid);
    H5Tinsert(m_ego_tid, "AmbientLightLevel",           HOFFSET(ego_t, AmbientLightLevel),          H5T_IEEE_F64LE);
    H5Tinsert(m_ego_tid, "AmbientTemperature",          HOFFSET(ego_t, AmbientTemperature),         H5T_IEEE_F64LE);
    H5Tinsert(m_ego_tid, "BaselineADASActive",          HOFFSET(ego_t, BaselineADASActive),         H5T_STD_I32LE);
    H5Tinsert(m_ego_tid, "BaselineADASIntervention",    HOFFSET(ego_t, BaselineADASIntervention),   H5T_STD_I32LE);
    H5Tinsert(m_ego_tid, "BrakeLight",                  HOFFSET(ego_t, BrakeLight),                 brake_tid);
    H5Tinsert(m_ego_tid, "BrakePedalPos",               HOFFSET(ego_t, BrakePedalPos),              H5T_STD_I32LE);
    H5Tinsert(m_ego_tid, "DirectionIndicator",          HOFFSET(ego_t, DirectionIndicator),         indicator_tid);
    H5Tinsert(m_ego_tid, "EnergyConsumption",           HOFFSET(ego_t, EnergyConsumption),          H5T_IEEE_F64LE);
    H5Tinsert(m_ego_tid, "ESCIntervention",             HOFFSET(ego_t, ESCIntervention),            active_tid);
    H5Tinsert(m_ego_tid, "FrontFogLightStatus",         HOFFSET(ego_t, FrontFogLightStatus),        on_tid);
    H5Tinsert(m_ego_tid, "FrontWiperStatus",            HOFFSET(ego_t, FrontWiperStatus),           wiper_tid);
    H5Tinsert(m_ego_tid, "FuelConsumption",             HOFFSET(ego_t, FuelConsumption),            H5T_IEEE_F64LE);
    H5Tinsert(m_ego_tid, "HandsOnDetection",            HOFFSET(ego_t, HandsOnDetection),           hands_tid);
    H5Tinsert(m_ego_tid, "LatAcceleration",             HOFFSET(ego_t, LatAcceleration),            H5T_IEEE_F64LE);
    H5Tinsert(m_ego_tid, "LongAcceleration",            HOFFSET(ego_t, LongAcceleration),           H5T_IEEE_F64LE);
    H5Tinsert(m_ego_tid, "Odometer",                    HOFFSET(ego_t, Odometer),                   H5T_IEEE_F64LE);
    H5Tinsert(m_ego_tid, "RearFogLightStatus",          HOFFSET(ego_t, RearFogLightStatus),         on_tid);
    H5Tinsert(m_ego_tid, "SteeringAngle",               HOFFSET(ego_t, SteeringAngle),              H5T_IEEE_F64LE);
    H5Tinsert(m_ego_tid, "SteeringAngleADF",            HOFFSET(ego_t, SteeringAngleADF),           H5T_IEEE_F64LE);
    H5Tinsert(m_ego_tid, "ThrottlePedalPos",            HOFFSET(ego_t, ThrottlePedalPos),           H5T_STD_I32LE);
    H5Tinsert(m_ego_tid, "TOR",                         HOFFSET(ego_t, TOR),                        tor_tid);
    H5Tinsert(m_ego_tid, "TorsionBarTorque",            HOFFSET(ego_t, TorsionBarTorque),           H5T_IEEE_F64LE);
    H5Tinsert(m_ego_tid, "VehicleSpeed",                HOFFSET(ego_t, VehicleSpeed),               H5T_IEEE_F64LE);
    H5Tinsert(m_ego_tid, "YawRate",                     HOFFSET(ego_t, YawRate),                    H5T_IEEE_F64LE);

    // Define the dimension of the ego dataset
    ego_dim[0] =        1;
    max_ego_dim[0] =    H5S_UNLIMITED;
    // Create the ego space
    m_ego_space =       H5Screate_simple(1, ego_dim, max_ego_dim);
    m_prop_ds =         H5Pcreate(H5P_DATASET_CREATE);
    // Chunk the ego dataset every 500 samples
    ego_chunk_dim[0] =  500;
    m_ego_status =      H5Pset_chunk(m_prop_ds, 1, ego_chunk_dim);
    // Activate the deflation
    m_ego_status =      H5Pset_deflate(m_prop_ds, 9);
    // Set the fill value
    m_ego_status =      H5Pset_fill_value(m_prop_ds, m_ego_tid, &cEgoFill);
    // Create the dataset
    m_ego_ds =          H5Dcreate2(m_file, "egoVehicle", m_ego_tid, m_ego_space, H5P_DEFAULT, m_prop_ds, H5P_DEFAULT);
    ego_first = true;

    // Write the ego vehicle attributes
    writeDSAttributeSignal("/egoVehicle", "UTCTime",                    "GNSS time (UTC) in microseconds since epoch", "ms");
    writeDSAttributeSignal("/egoVehicle", "FileTime",                   "Time since beginning of recording", "s");
    writeDSAttributeSignal("/egoVehicle", "ABSIntervention",            "Whether the ABS system is intervening. 0=no intervention;1=ABS intervening", "-");
    writeDSAttributeSignal("/egoVehicle", "ADFunctionActive",           "Status of the autonomous driving function;0 = not active;1 = active", "-");
    writeDSAttributeSignal("/egoVehicle", "ADFunctionAvailable",        "Whether the preconditions are met to enable the autonomous driving function;0 = preconditions not met;1 = preconditions met", "-");
    writeDSAttributeSignal("/egoVehicle", "AmbientLightLevel",          "Ambient light level at the location of the ego vehicle", "ln(lux)");
    writeDSAttributeSignal("/egoVehicle", "AmbientTemperature",         "Ambient temperature recorded by the ego vehicles temperature sensor", "�C");
    writeDSAttributeSignal("/egoVehicle", "BaselineADASActive",         "The combination of ADAS active in baseline mode, bit-wise encoding", "-");
    writeDSAttributeSignal("/egoVehicle", "BaselineADASIntervention",   "The combination of ADAS intervening in baseline mode, bit-wise encoding", "-");
    writeDSAttributeSignal("/egoVehicle", "BrakeLight",                 "Status of the brake light.0 = off;1 = on;2 = flashing", "-");
    writeDSAttributeSignal("/egoVehicle", "BrakePedalPos",              "The position of the brake pedal in %.0 = not pressed;100 = fully pressed", "%");
    writeDSAttributeSignal("/egoVehicle", "BrakePressure",              "Brake system pressure", "%");
    writeDSAttributeSignal("/egoVehicle", "DirectionIndicator",         "The status of the turn indicator. 0=none;1=left;2=right;3=hazard", "-");
    writeDSAttributeSignal("/egoVehicle", "EnergyConsumption",          "Current energy consumption", "kWh/100km");
    writeDSAttributeSignal("/egoVehicle", "ESCIntervention",            "Whether the Electronic Slip Control / Traction Control system is intervening;0=no intervention;1=ESC intervening", "-");
    writeDSAttributeSignal("/egoVehicle", "FrontFogLightStatus",        "The status of the front fog light", "-");
    writeDSAttributeSignal("/egoVehicle", "FrontWiper",                 "The state of the front wiper", "-");
    writeDSAttributeSignal("/egoVehicle", "FuelConsumption",            "Current fuel consumption", "l/100km");
    writeDSAttributeSignal("/egoVehicle", "HandsOnDetection",           "Whether the driver has the hands on the steering wheel;0=hands off;1=one hand;2=two hands;3=one or more hands", "-");
    writeDSAttributeSignal("/egoVehicle", "LatAcceleration",            "lateral acceleration (Y axis)", "m/s�");
    writeDSAttributeSignal("/egoVehicle", "LongAcceleration",           "longitudinal acceleration (X axis)", "m/s�");
    writeDSAttributeSignal("/egoVehicle", "Odometer",                   "The vehicle odometer reading", "km");
    writeDSAttributeSignal("/egoVehicle", "RearFogLightStatus",         "The status of the rear fog light", "-");
    writeDSAttributeSignal("/egoVehicle", "SteeringAngle",              "Steering wheel angle; Turning left (counterclockwise) is positive; Turning right (clockwise) is negative", "rad");
    writeDSAttributeSignal("/egoVehicle", "SteeringAngleADF",           "Steering wheel angle as desired by the ADF", "rad");
    writeDSAttributeSignal("/egoVehicle", "ThrottlePedalPos",           "The position of the throttle pedal in %. 0=not pressed; 100=fully pressed", "%");
    writeDSAttributeSignal("/egoVehicle", "TOR",                        "Whether the take-over-request to return control to the driver is active: TOR at known system limits (1), TOR at unknown/unexpected system limits (2); 0 = TOR not active; 1 = TOR active; 2 = short TOR active", "-");
    writeDSAttributeSignal("/egoVehicle", "TorsionBarTorque",           "Steering force applied by the driver on the torsion bar", "Nm");
    writeDSAttributeSignal("/egoVehicle", "VehicleSpeed",               "Speed of the ego vehicle", "m/s");
    writeDSAttributeSignal("/egoVehicle", "YawRate",                    "The yaw rate of the ego vehicle; Towards left is positive; Towards right is negative", "rad/s");

    // Create the positioning compound
    m_pos_tid = H5Tcreate(H5T_COMPOUND, sizeof(pos_t));
    // Add the members of the positioning compound
    H5Tinsert(m_pos_tid, "UTCTime",             HOFFSET(pos_t, UTCTime),            H5T_STD_I64LE);
    H5Tinsert(m_pos_tid, "FileTime",            HOFFSET(pos_t, FileTime),           H5T_IEEE_F64LE);
    H5Tinsert(m_pos_tid, "Altitude",            HOFFSET(pos_t, Altitude),           H5T_IEEE_F64LE);
    H5Tinsert(m_pos_tid, "GNSSSpeed",           HOFFSET(pos_t, GNSSSpeed),          H5T_IEEE_F64LE);
    H5Tinsert(m_pos_tid, "GNSSTime",            HOFFSET(pos_t, GNSSTime),           H5T_STD_I64LE);
    H5Tinsert(m_pos_tid, "Heading",             HOFFSET(pos_t, Heading),            H5T_IEEE_F64LE);
    H5Tinsert(m_pos_tid, "Latitude",            HOFFSET(pos_t, Latitude),           H5T_IEEE_F64LE);
    H5Tinsert(m_pos_tid, "Longitude",           HOFFSET(pos_t, Longitude),          H5T_IEEE_F64LE);
    H5Tinsert(m_pos_tid, "NumberOfSatellites",  HOFFSET(pos_t, NumberOfSatellites), H5T_STD_I32LE);

    // Define the size of the positioning dataset
    pos_dim[0] =        1;
    max_pos_dim[0] =    H5S_UNLIMITED;
    // Create the positioning space
    m_pos_space =       H5Screate_simple(1, pos_dim, max_pos_dim);
    m_prop_ds =         H5Pcreate(H5P_DATASET_CREATE);
    // Chunk the positioning dataset every 1000 samples
    pos_chunk_dim[0] =  1000;
    m_pos_status =      H5Pset_chunk(m_prop_ds, 1, pos_chunk_dim);
    // Activate the deflation
    m_pos_status =      H5Pset_deflate(m_prop_ds, 9);
    // Set the fill value
    m_pos_status =      H5Pset_fill_value(m_prop_ds, m_pos_tid, &cPosFill);
    // Create the dataset
    m_pos_ds =          H5Dcreate2(m_file, "positioning", m_pos_tid, m_pos_space, H5P_DEFAULT, m_prop_ds, H5P_DEFAULT);
    pos_first =         true;

    // Write the positioning struct attributes
    writeDSAttributeSignal("/positioning", "UTCTime",               "GNSS time (UTC) in microseconds since epoch", "ms");
    writeDSAttributeSignal("/positioning", "FileTime",              "Time since beginning of recording", "s");
    writeDSAttributeSignal("/positioning", "Altitude",              "Altitude of the ego vehicle(WGS84)", "m");
    writeDSAttributeSignal("/positioning", "GNSSSpeed",             "Speed of the ego vehicle as reported by the GPS module / IMU", "m/s");
    writeDSAttributeSignal("/positioning", "GNSSTime",              "Time as reported by the GPS module", "ms");
    writeDSAttributeSignal("/positioning", "Heading",               "Heading of the ego vehicle;0=north, 90=east, 180=south, 270=west", "deg");
    writeDSAttributeSignal("/positioning", "Latitude",              "Latitude of the ego vehicle(WGS84)", "deg");
    writeDSAttributeSignal("/positioning", "Longitude",             "Longitude of the ego vehicle(WGS84)", "deg");
    writeDSAttributeSignal("/positioning", "NumberOfSatellites",    "The number of used satellites", "-");

    // Define the enum for classifying the object
    obj_cla_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(obj_cla_tid, H5T_SGN_2);
    ObjClassification::Enum val_obj;
    val_obj = ObjClassification::NOT_APPLICABLE;
    H5Tenum_insert(obj_cla_tid, "NOT_APPLICABLE",   &val_obj);
    val_obj = ObjClassification::OTHER;
    H5Tenum_insert(obj_cla_tid, "OTHER",            &val_obj);
    val_obj = ObjClassification::CAR;
    H5Tenum_insert(obj_cla_tid, "CAR",              &val_obj);
    val_obj = ObjClassification::TRUCK;
    H5Tenum_insert(obj_cla_tid, "TRUCK",            &val_obj);
    val_obj = ObjClassification::MOTORCYCLE;
    H5Tenum_insert(obj_cla_tid, "MOTORCYCLE",       &val_obj);
    val_obj = ObjClassification::BICYCLE;
    H5Tenum_insert(obj_cla_tid, "BICYCLE",          &val_obj);
    val_obj = ObjClassification::PEDESTRIAN;
    H5Tenum_insert(obj_cla_tid, "PEDESTRIAN",       &val_obj);
    val_obj = ObjClassification::UNKNOWN;
    H5Tenum_insert(obj_cla_tid, "UNKNOWN",          &val_obj);

    // Create the object objects compound
    m_obj_obj_tid = H5Tcreate(H5T_COMPOUND, sizeof(obj_obj_t));
    // Add the members of the objects
    H5Tinsert(m_obj_obj_tid, "Classification",      HOFFSET(obj_obj_t, Classification),         obj_cla_tid);
    H5Tinsert(m_obj_obj_tid, "Height",              HOFFSET(obj_obj_t, Height),                 H5T_IEEE_F64LE);
    H5Tinsert(m_obj_obj_tid, "ID",                  HOFFSET(obj_obj_t, ID),                     H5T_STD_I32LE);
    H5Tinsert(m_obj_obj_tid, "LatPosition",         HOFFSET(obj_obj_t, LatPosition),            H5T_IEEE_F64LE);
    H5Tinsert(m_obj_obj_tid, "LatVelocity",         HOFFSET(obj_obj_t, LatVelocity),            H5T_IEEE_F64LE);
    H5Tinsert(m_obj_obj_tid, "Length",              HOFFSET(obj_obj_t, Length),                 H5T_IEEE_F64LE);
    H5Tinsert(m_obj_obj_tid, "LongPosition",        HOFFSET(obj_obj_t, LongPosition),           H5T_IEEE_F64LE);
    H5Tinsert(m_obj_obj_tid, "LongVelocity",        HOFFSET(obj_obj_t, LongVelocity),           H5T_IEEE_F64LE);
    H5Tinsert(m_obj_obj_tid, "Width",               HOFFSET(obj_obj_t, Width),                  H5T_IEEE_F64LE);
    H5Tinsert(m_obj_obj_tid, "YawAngle",            HOFFSET(obj_obj_t, YawAngle),               H5T_IEEE_F64LE);
    H5Tinsert(m_obj_obj_tid, "YawRate",             HOFFSET(obj_obj_t, YawRate),                H5T_IEEE_F64LE);
    // Set the size of the objects array
    hsize_t obj_arr_dim[] = { OBJECT_NUMBER };
    // Create the objects array
    m_obj_array =           H5Tarray_create2(m_obj_obj_tid, 1, obj_arr_dim);
    // Create the object compound
    m_obj_tid =             H5Tcreate(H5T_COMPOUND, sizeof(obj_t));
    // Add the members to the object compound
    H5Tinsert(m_obj_tid, "UTCTime",         HOFFSET(obj_t, UTCTime),            H5T_STD_I64LE);
    H5Tinsert(m_obj_tid, "FileTime",        HOFFSET(obj_t, FileTime),           H5T_IEEE_F64LE);
    H5Tinsert(m_obj_tid, "LeadVehicleID",   HOFFSET(obj_t, LeadVehicleID),      H5T_STD_I32LE);
    H5Tinsert(m_obj_tid, "NumberOfObjects", HOFFSET(obj_t, NumberOfObjects),    H5T_STD_I32LE);
    // Add the array of objects
    H5Tinsert(m_obj_tid, "sObject",         HOFFSET(obj_t, sObject),            m_obj_array);
    // TODO find better solution here
    for (int i = 0; i < OBJECT_NUMBER; i++)
    {
        cObjFill.sObject[i] = cObjObjFill;
    }

    // Define the size of the object dataset
    obj_dim[0] =        1;
    max_obj_dim[0] =    H5S_UNLIMITED;
    // Create the object space
    m_obj_space =       H5Screate_simple(1, obj_dim, max_obj_dim);
    m_prop_ds =         H5Pcreate(H5P_DATASET_CREATE);
    // Chunk the dataset every 20 samples
    obj_chunk_dim[0] =  20;
    m_obj_status =      H5Pset_chunk(m_prop_ds, 1, obj_chunk_dim);
    // Activate deflation
    m_obj_status =      H5Pset_deflate(m_prop_ds, 9);
    // Set the fill value
    m_pos_status =      H5Pset_fill_value(m_prop_ds, m_obj_tid, &cObjFill);
    // Create the dataset
    m_obj_ds =          H5Dcreate2(m_file, "objects", m_obj_tid, m_obj_space, H5P_DEFAULT, m_prop_ds, H5P_DEFAULT);
    obj_first =         true;

    // Write the object struct attributes
    writeDSAttributeSignal("/objects", "UTCTime",               "GNSS time (UTC) in microseconds since epoch", "ms");
    writeDSAttributeSignal("/objects", "FileTime",              "Time since beginning of recording", "s");
    writeDSAttributeSignal("/objects", "LeadVehicleID",         "ID of the vehicle currently selected as lead vehicle", "-");
    writeDSAttributeSignal("/objects", "NumberOfObjects",       "Number of objects in timestep", "-");
    writeDSAttributeSignal("/objects", "Classification",        "Classification of the object, 0=other;1=car;2=truck;3=motorcycle;4=bicycle;5=pedestrian;9=unknown", "-");
    writeDSAttributeSignal("/objects", "Height",                "The height of the object", "m");
    writeDSAttributeSignal("/objects", "ID",                    "Unique ID of the object", "-");
    writeDSAttributeSignal("/objects", "LatPosition",           "Position in lateral direction", "m");
    writeDSAttributeSignal("/objects", "LatVelocity",           "Relative velocity in lateral direction", "m/s");
    writeDSAttributeSignal("/objects", "Length",                "Length of the object", "m");
    writeDSAttributeSignal("/objects", "LongPosition",          "Position in longitudinal direction", "m");
    writeDSAttributeSignal("/objects", "LongVelocity",          "Relative velocity in longitudinal direction", "m/s");
    writeDSAttributeSignal("/objects", "Width",                 "Width of the object", "m");
    writeDSAttributeSignal("/objects", "YawAngle",              "Relative yaw angle of the object", "rad");
    writeDSAttributeSignal("/objects", "YawRate",               "Relative yaw angle rate of the object", "rad/s");

    // Define the enum for defining the lane markings type
    lan_typ_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(lan_typ_tid, H5T_SGN_2);
    LaneType::Enum val_lan;
    val_lan = LaneType::NOT_APPLICABLE;
    H5Tenum_insert(lan_typ_tid, "NOT_APPLICABLE",   &val_lan);
    val_lan = LaneType::NONE;
    H5Tenum_insert(lan_typ_tid, "NONE",             &val_lan);
    val_lan = LaneType::CONTINUOUS;
    H5Tenum_insert(lan_typ_tid, "CONTINUOUS",       &val_lan);
    val_lan = LaneType::DASHED;
    H5Tenum_insert(lan_typ_tid, "DASHED",           &val_lan);
    val_lan = LaneType::DOTS;
    H5Tenum_insert(lan_typ_tid, "DOTS",             &val_lan);
    val_lan = LaneType::DOUBLE;
    H5Tenum_insert(lan_typ_tid, "DOUBLE",           &val_lan);
    val_lan = LaneType::OTHER;
    H5Tenum_insert(lan_typ_tid, "OTHER",            &val_lan);
    val_lan = LaneType::UNKNOWN;
    H5Tenum_insert(lan_typ_tid, "UNKNOWN",          &val_lan);

    // Create the lane lanes compound
    m_lan_lan_tid = H5Tcreate(H5T_COMPOUND, sizeof(lan_lan_t));
    // Insert the members of the compound
    H5Tinsert(m_lan_lan_tid, "Curvature",       HOFFSET(lan_lan_t, Curvature),      H5T_IEEE_F64LE);
    H5Tinsert(m_lan_lan_tid, "CurvatureDx",     HOFFSET(lan_lan_t, CurvatureDx),    H5T_IEEE_F64LE);
    H5Tinsert(m_lan_lan_tid, "Dy",              HOFFSET(lan_lan_t, Dy),             H5T_IEEE_F64LE);
    H5Tinsert(m_lan_lan_tid, "QualityIndex",    HOFFSET(lan_lan_t, QualityIndex),   H5T_STD_I32LE);
    H5Tinsert(m_lan_lan_tid, "Type",            HOFFSET(lan_lan_t, Type),           lan_typ_tid);
    H5Tinsert(m_lan_lan_tid, "YawAngle",        HOFFSET(lan_lan_t, YawAngle),       H5T_IEEE_F64LE);
    cLanLanFill.YawAngle =                      NAN;
    // Define the size of the lanes array
    hsize_t lan_arr_dim[] = { LANE_NUMBER };
    // Create the lanes array
    m_lan_array =           H5Tarray_create2(m_lan_lan_tid, 1, lan_arr_dim);
    // Create the lane compound
    m_lan_tid = H5Tcreate(H5T_COMPOUND, sizeof(lan_t));
    // Insert the members of the compound
    H5Tinsert(m_lan_tid, "UTCTime",         HOFFSET(lan_t, UTCTime),        H5T_STD_I64LE);
    H5Tinsert(m_lan_tid, "FileTime",        HOFFSET(lan_t, FileTime),       H5T_IEEE_F64LE);
    H5Tinsert(m_lan_tid, "EgoLaneWidth",    HOFFSET(lan_t, EgoLaneWidth),   H5T_IEEE_F64LE);
    // Add the array of lanes
    H5Tinsert(m_lan_tid, "sLaneLine",       HOFFSET(lan_t, sLaneLine),      m_lan_array);
    // TODO find better solution here
    for (int i = 0; i < LANE_NUMBER; i++)
    {
        cLanFill.sLaneLine[i] = cLanLanFill;
    }

    // Define the size of the lanes dataset
    lan_dim[0] =        1;
    max_lan_dim[0] =    H5S_UNLIMITED;
    // Create the lanes space
    m_lan_space =       H5Screate_simple(1, lan_dim, max_lan_dim);
    m_prop_ds =         H5Pcreate(H5P_DATASET_CREATE);
    // Chunk the dataset every 500 samples
    lan_chunk_dim[0] =  500;
    m_lan_status =      H5Pset_chunk(m_prop_ds, 1, lan_chunk_dim);
    // Activate deflation
    m_lan_status =      H5Pset_deflate(m_prop_ds, 9);
    // Set fill value
    m_lan_status =      H5Pset_fill_value(m_prop_ds, m_lan_tid, &cLanFill);
    // Create the dataset
    m_lan_ds =          H5Dcreate2(m_file, "laneLines", m_lan_tid, m_lan_space, H5P_DEFAULT, m_prop_ds, H5P_DEFAULT);
    lan_first =         true;

    // Write the lane attributes
    writeDSAttributeSignal("/laneLines", "UTCTime",         "GNSS time (UTC) in microseconds since epoch", "us");
    writeDSAttributeSignal("/laneLines", "FileTime",        "Time since beginning of recording", "s");
    writeDSAttributeSignal("/laneLines", "EgoLaneWidth",    "Width of the current lane", "s");
    writeDSAttributeSignal("/laneLines", "Curvature",       "Curvature of the lane markings", "1/m");
    writeDSAttributeSignal("/laneLines", "CurvatureDx",     "Differential curvature of the lane markings", "1/m�");
    writeDSAttributeSignal("/laneLines", "Dy",              "Lateral position of the lane markings", "m");
    writeDSAttributeSignal("/laneLines", "QualityIndex",    "Quality index for lane detection", "-");
    writeDSAttributeSignal("/laneLines", "Type",            "Type of lane marking; -1=not applicable; 0=none; 1=continuous; 2=dashed; 3=dots; 4=double; 9=unknown", "-");
    writeDSAttributeSignal("/laneLines", "YawAngle",        "Yaw angle of the lane markings", "rad");

    return true;
}

void cHDF5DataBuffer::updateEgoData(ego_t ego_struct)
{
    cEgo = ego_struct;
}

void cHDF5DataBuffer::updatePositioningData(pos_t pos_struct)
{
    cPos = pos_struct;
}

void cHDF5DataBuffer::updateLaneData(lan_t lan_struct)
{
    cLan = lan_struct;
}

void cHDF5DataBuffer::updateObjectData(obj_t obj_struct)
{
    cObj = obj_struct;
}

void cHDF5DataBuffer::updateGeneralMetaData(met_gen_t met_gen_struct)
{
    cMetGen = met_gen_struct;
}

void cHDF5DataBuffer::updateDriverMetaData(met_dri_t met_dri_struct)
{
    cMetDri = met_dri_struct;
}

void cHDF5DataBuffer::updateCarMetaData(met_car_t met_car_struct)
{
    cMetCar = met_car_struct;
}

void cHDF5DataBuffer::updateExperimentMetaData(met_exp_t met_exp_struct)
{
    cMetExp = met_exp_struct;
}

bool cHDF5DataBuffer::writeAttributeString(const char* location, const char* attrname, const char* attrvalue)
{
    // Define the needed types
    hid_t space_attr =          H5Screate(H5S_SCALAR);
    hid_t attr_obj =            H5Oopen(m_file, location, H5P_DEFAULT);
    hid_t attr_type =           H5Tcopy(H5T_C_S1);
    // Set the size of the attribute to fit the string
    H5Tset_size(attr_type,      strlen(attrvalue) + 1);
    H5Tset_strpad(attr_type,    H5T_STR_NULLTERM);
    // Create the attribute
    hid_t attr1 =               H5Acreate2(attr_obj, attrname, attr_type, space_attr, H5P_DEFAULT, H5P_DEFAULT);
    // Write the string to the attribute
    herr_t attr_status =        H5Awrite(attr1, attr_type, attrvalue);
    // Close all the types used
    H5Aclose(attr1);
    H5Tclose(attr_type);
    H5Oclose(attr_obj);
    H5Sclose(space_attr);
    // Return true if successful
    return (attr_status >= 0) ? true : false;
}

bool cHDF5DataBuffer::writeDSAttributeSignal(const char* location, const char* attrname, const char* description, const char* unit)
{
    // Define the array of the attribute
    const char* attr_data[2][2] =   { "Description", description, "Unit", unit };
    // Set the dimensions of the new attribute
    hsize_t attr_dim[2] =           { 2, 2 };
    // Define the needed types
    hid_t space_attr =              H5Screate_simple(2, attr_dim, attr_dim);
    hid_t attr_obj =                H5Oopen(m_file, location, H5P_DEFAULT);
    hid_t attr_type =               H5Tcopy(H5T_C_S1);
    // Set the size of the attribute
    H5Tset_size(attr_type,          H5T_VARIABLE);
    H5Tset_strpad(attr_type,        H5T_STR_NULLTERM);
    // Create the attribute
    hid_t attr1 =                   H5Acreate2(attr_obj, attrname, attr_type, space_attr, H5P_DEFAULT, H5P_DEFAULT);
    // Write the array of strings to the attribute
    herr_t attr_status =            H5Awrite(attr1, attr_type, attr_data);
    // Close all the types used
    H5Aclose(attr1);
    H5Tclose(attr_type);
    H5Oclose(attr_obj);
    H5Sclose(space_attr);
    // Return true if successful
    return (attr_status >= 0) ? true : false;
}

bool cHDF5DataBuffer::writeAttributeArray(const char * location, const char * attrname, void * array, hid_t datatype)
{
    // Define the dimension of the attribute array
    hsize_t attr_dim[1] =   { 1 };
    // Create the space of the attribute
    hid_t space_attr =      H5Screate_simple(1, attr_dim, attr_dim);
    // Open the object for storing the attribute
    hid_t attr_obj =        H5Oopen(m_file, location, H5P_DEFAULT);
    // Create the attribute
    hid_t attr1 =           H5Acreate2(attr_obj, attrname, datatype, space_attr, H5P_DEFAULT, H5P_DEFAULT);
    // Write the array to the attribute
    herr_t attr_status =    H5Awrite(attr1, datatype, array);
    // Close all the types used
    H5Aclose(attr1);
    H5Oclose(attr_obj);
    H5Sclose(space_attr);
    // Return true if successful
    return (attr_status >= 0) ? true : false;
}

const std::string cHDF5DataBuffer::getCurrentTime()
{
    // Define the needed variables
    time_t rawtime;
    struct tm *timeinfo;
    char buffer[80];

    // Get the time
    time(&rawtime);
    // Convert the time to the local time
    timeinfo = localtime(&rawtime);
    // Format the time to fit the pattern
    strftime(buffer, sizeof(buffer), "%d-%b-%Y %H:%M:%S", timeinfo);
    // Return the buffer
    return buffer;
}

bool cHDF5DataBuffer::finalizeDataFile()
{
    // Close ego ids
    H5Dflush(m_ego_ds);
    H5Tclose(active_tid);
    H5Tclose(ava_tid);
    H5Tclose(brake_tid);
    H5Tclose(indicator_tid);
    H5Tclose(hands_tid);
    H5Tclose(wiper_tid);
    H5Tclose(on_tid);
    H5Tclose(m_ego_tid);
    H5Sclose(m_ego_space);
    H5Sclose(m_ego_fspace);
    H5Dclose(m_ego_ds);

    // CLose positioning ids
    H5Dflush(m_pos_ds);
    H5Tclose(m_pos_tid);
    H5Sclose(m_pos_space);
    H5Sclose(m_pos_fspace);
    H5Dclose(m_pos_ds);

    // Close object ids
    H5Dflush(m_obj_ds);
    H5Tclose(obj_cla_tid);
    H5Tclose(m_obj_obj_tid);
    H5Tclose(m_obj_array);
    H5Tclose(m_obj_tid);
    H5Sclose(m_obj_space);
    H5Sclose(m_obj_fspace);
    H5Dclose(m_obj_ds);

    // CLose lane ids
    H5Dflush(m_lan_ds);
    H5Tclose(lan_typ_tid);
    H5Tclose(m_lan_lan_tid);
    H5Tclose(m_lan_array);
    H5Tclose(m_lan_tid);
    H5Sclose(m_lan_space);
    H5Sclose(m_lan_fspace);
    H5Dclose(m_lan_ds);

    // Close the meta data ids
    H5Tclose(m_met_gen);
    H5Tclose(driver_tid);
    H5Tclose(m_met_dri);
    H5Tclose(drive_tid);
    H5Tclose(fuel_tid);
    H5Tclose(transmission_tid);
    H5Tclose(m_met_car);
    H5Tclose(analysis_tid);
    H5Tclose(baseline_tid);
    H5Tclose(test_tid);
    H5Tclose(m_met_exp);

    // Close the property identifier
    H5Pclose(m_prop_ds);

    // Close file
    herr_t file_status = H5Fclose(m_file);

    return (file_status >= 0) ? true : false;
}

bool cHDF5DataBuffer::wrtiteEgoTimeStamp()
{
    // No data has been written yet, no need to extend the space
    if (ego_first)
    {
        // Set the extent of the ego dataspace
        m_ego_status =      H5Dset_extent(m_ego_ds, ego_dim);
        // Get an identifier for the ego space
        m_ego_fspace =      H5Dget_space(m_ego_ds);
        // Start at the beginning of the space
        m_ego_offset[0] =   0;
        // Select the hyperslab that is used for writing
        m_ego_status =      H5Sselect_hyperslab(m_ego_fspace, H5S_SELECT_SET, m_ego_offset, NULL, ego_dim, NULL);
        // Write the structure with the ego data to the dataspace
        m_ego_status =      H5Dwrite(m_ego_ds, m_ego_tid, m_ego_space, m_ego_fspace, H5P_DEFAULT, &cEgo);
        ego_first =         false;
        // Return true if successful
        return (m_ego_status >= 0) ? true : false;
    }
    else
    {
        // This works well if one timestep at a time is written. If more than one timestep is to be written
        // per iteration, you will have to use a bigger m_ego_space, e.g. for 10 timesteps:
        //  // Define the dimension of the ego dataset
        //  ego_dim[0] = 10;
        //  max_ego_dim[0] = H5S_UNLIMITED;
        //  // Create the ego space
        //  m_ego_space = H5Screate_simple(1, ego_dim, max_ego_dim);

        // Get an identifier for the ego space
        m_ego_fspace =      H5Dget_space(m_ego_ds);
        // Get the size of the ego space
        m_ego_status =      H5Sget_simple_extent_dims(m_ego_fspace, m_ego_osize, NULL);
        m_ego_nsize[0] =    m_ego_osize[0] + ego_dim[0];
        // Extend the ego space by the dimension of the ego data
        m_ego_status =      H5Dset_extent(m_ego_ds, m_ego_nsize);
        // Get the new, extended ego space
        m_ego_fspace =      H5Dget_space(m_ego_ds);
        // Define the new offset
        m_ego_offset[0] =   m_ego_nsize[0] - ego_dim[0];
        // Select the hyperslab that is used for writing
        m_ego_status =      H5Sselect_hyperslab(m_ego_fspace, H5S_SELECT_SET, m_ego_offset, NULL, ego_dim, NULL);
        // Write the structure with ego data to the dataspace
        m_ego_status =      H5Dwrite(m_ego_ds, m_ego_tid, m_ego_space, m_ego_fspace, H5P_DEFAULT, &cEgo);
        // Return true if successful
        return (m_ego_status >= 0) ? true : false;
    }
}

bool cHDF5DataBuffer::writeObjectTimeStamp()
{
    // No data has been written yet, no need to extend the space
    if (obj_first)
    {
        // Set the extent of the object dataspace
        m_obj_status =      H5Dset_extent(m_obj_ds, obj_dim);
        // Get an identifier for the object space
        m_obj_fspace =      H5Dget_space(m_obj_ds);
        // Start at the beginning of the space
        m_obj_offset[0] =   0;
        // Select the hyperslab that is used for writing
        m_obj_status =      H5Sselect_hyperslab(m_obj_fspace, H5S_SELECT_SET, m_obj_offset, NULL, obj_dim, NULL);
        // Write the structure with the object data to the dataspace
        m_obj_status =      H5Dwrite(m_obj_ds, m_obj_tid, m_obj_space, m_obj_fspace, H5P_DEFAULT, &cObj);
        obj_first =         false;
        // Return true if successful
        return (m_obj_status >= 0) ? true : false;
    }
    else
    {
        // Get an identifier for the object space
        m_obj_fspace =      H5Dget_space(m_obj_ds);
        // Get the size of the object space
        m_obj_status =      H5Sget_simple_extent_dims(m_obj_fspace, m_obj_osize, NULL);
        m_obj_nsize[0] =    m_obj_osize[0] + obj_dim[0];
        // Extend the object space by the dimension of the object data
        m_obj_status =      H5Dset_extent(m_obj_ds, m_obj_nsize);
        // Get the new, extended object space
        m_obj_fspace =      H5Dget_space(m_obj_ds);
        // Define the new offset
        m_obj_offset[0] =   m_obj_nsize[0] - obj_dim[0];
        // Select the hyperslab that is used for writing
        m_obj_status =      H5Sselect_hyperslab(m_obj_fspace, H5S_SELECT_SET, m_obj_offset, NULL, obj_dim, NULL);
        // Write the structure with the object data to the dataspace
        m_obj_status =      H5Dwrite(m_obj_ds, m_obj_tid, m_obj_space, m_obj_fspace, H5P_DEFAULT, &cObj);
        // Return true if successful
        return (m_obj_status >= 0) ? true : false;
    }
}

bool cHDF5DataBuffer::writeLaneTimeStamp()
{
    // No data has been written yet, no need to extend the space
    if (lan_first)
    {
        // Set the extent for the lane space
        m_lan_status =      H5Dset_extent(m_lan_ds, lan_dim);
        // Get an identifier for the lane space
        m_lan_fspace =      H5Dget_space(m_lan_ds);
        // Start at the beginning of the space
        m_lan_offset[0] =   0;
        // Select the hyperslab that is used for writing
        m_lan_status =      H5Sselect_hyperslab(m_lan_fspace, H5S_SELECT_SET, m_lan_offset, NULL, lan_dim, NULL);
        // Write the structure with the lane data to the dataspace
        m_lan_status =      H5Dwrite(m_lan_ds, m_lan_tid, m_lan_space, m_lan_fspace, H5P_DEFAULT, &cLan);
        lan_first =         false;
        // Return true if successful
        return (m_lan_status >= 0) ? true : false;
    }
    else
    {
        // Get an identifier for the lane space
        m_lan_fspace =      H5Dget_space(m_lan_ds);
        // Get the size of the lane space
        m_lan_status =      H5Sget_simple_extent_dims(m_lan_fspace, m_lan_osize, NULL);
        m_lan_nsize[0] =    m_lan_osize[0] + lan_dim[0];
        // Extend the object space by the dimension of lane data
        m_lan_status =      H5Dset_extent(m_lan_ds, m_lan_nsize);
        // Get the new, extended lane space
        m_lan_fspace =      H5Dget_space(m_lan_ds);
        // Define the new offset
        m_lan_offset[0] =   m_lan_nsize[0] - lan_dim[0];
        // Select the hyperslab that is used for writing
        m_lan_status =      H5Sselect_hyperslab(m_lan_fspace, H5S_SELECT_SET, m_lan_offset, NULL, lan_dim, NULL);
        // Write the structure with the lane data to the dataspace
        m_lan_status =      H5Dwrite(m_lan_ds, m_lan_tid, m_lan_space, m_lan_fspace, H5P_DEFAULT, &cLan);
        // Return true if successful
        return (m_lan_status >= 0) ? true : false;
    }
}

bool cHDF5DataBuffer::writePositioningTimeStamp()
{
    // No data has been written yet, no need to extend the space
    if (pos_first)
    {
        // Set the extent for the positioning space
        m_pos_status =      H5Dset_extent(m_pos_ds, pos_dim);
        // Get an identifier for the positioning space
        m_pos_fspace =      H5Dget_space(m_pos_ds);
        // Start at the beginning of the space
        m_pos_offset[0] =   0;
        // Select the hyperslab that is used for writing
        m_pos_status =      H5Sselect_hyperslab(m_pos_fspace, H5S_SELECT_SET, m_pos_offset, NULL, pos_dim, NULL);
        // Write the structure with the positioning data to the dataspace
        m_pos_status =      H5Dwrite(m_pos_ds, m_pos_tid, m_pos_space, m_pos_fspace, H5P_DEFAULT, &cPos);
        pos_first =         false;
        // Return true if successful
        return (m_pos_status >= 0) ? true : false;
    }
    else
    {
        // Get an identifier for the positioning space
        m_pos_fspace =      H5Dget_space(m_pos_ds);
        // Get the size of the positioning space
        m_pos_status =      H5Sget_simple_extent_dims(m_pos_fspace, m_pos_osize, NULL);
        m_pos_nsize[0] =    m_pos_osize[0] + pos_dim[0];
        // Extend the positioning space by the dimension of positioning data
        m_pos_status =      H5Dset_extent(m_pos_ds, m_pos_nsize);
        // Get the new, extended positioning space
        m_pos_fspace =      H5Dget_space(m_pos_ds);
        // Define the new offset
        m_pos_offset[0] =   m_pos_nsize[0] - pos_dim[0];
        // Select the hyperslab that is used for writing
        m_pos_status =      H5Sselect_hyperslab(m_pos_fspace, H5S_SELECT_SET, m_pos_offset, NULL, pos_dim, NULL);
        // Write the structure with the positioning data to the dataspace
        m_pos_status =      H5Dwrite(m_pos_ds, m_pos_tid, m_pos_space, m_pos_fspace, H5P_DEFAULT, &cPos);
        // Return true if successful
        return (m_pos_status >= 0) ? true : false;
    }
}

void cHDF5DataBuffer::configureMetaDataStructure()
{
    // Write the general file attributes
    writeAttributeString("/", "creation_script",            "L3Pilot C/C++ CDF Template");
    writeAttributeString("/", "creation_date",              getCurrentTime().c_str());
    writeAttributeString("/", "creation_script_version",    "0.8");
    writeAttributeString("/", "author",                     "Johannes Hiller");
    writeAttributeString("/", "comment",                    "Generated by an example of an implementation of HDF5 files in  C/C++");
    writeAttributeString("/", "institution",                "Institut fuer Kraftfahrzeuge (ika)");
    writeAttributeString("/", "hdf5_version",               "HDF5 1.10.1");

    // Create the datatype for the partner string
    hid_t strtype =        H5Tcopy(H5T_C_S1);
    // Set the size of the attribute to fit the string
    H5Tset_size(strtype,   H5T_VARIABLE);
    H5Tset_strpad(strtype, H5T_STR_NULLTERM);

    // Create the general meta data compound
    m_met_gen = H5Tcreate(H5T_COMPOUND, sizeof(met_gen_t));
    H5Tinsert(m_met_gen, "ADFVersion",      HOFFSET(met_gen_t, ADFVersion),     H5T_IEEE_F64LE);
    H5Tinsert(m_met_gen, "FormatVersion",   HOFFSET(met_gen_t, FormatVersion),  H5T_IEEE_F64LE);
    H5Tinsert(m_met_gen, "Partner",         HOFFSET(met_gen_t, Partner),        strtype);
    H5Tinsert(m_met_gen, "RecordDate",      HOFFSET(met_gen_t, RecordDate),     strtype);
    H5Tinsert(m_met_gen, "UTCOffset",       HOFFSET(met_gen_t, UTCOffset),      H5T_STD_I32LE);

    // Define the enum for the driver type
    driver_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(driver_tid, H5T_SGN_2);
    DriverEnum::Enum val_dri;
    val_dri = DriverEnum::NOT_APPLICABLE;
    H5Tenum_insert(driver_tid, "NOT_APPLICABLE",        &val_dri);
    val_dri = DriverEnum::UNKNOWN;
    H5Tenum_insert(driver_tid, "UNKNOWN",               &val_dri);
    val_dri = DriverEnum::PROFESSIONAL;
    H5Tenum_insert(driver_tid, "PROFESSIONAL",   &val_dri);
    val_dri = DriverEnum::COMPANY_EMPLOYEE;
    H5Tenum_insert(driver_tid, "COMPANY_EMPLOYEE", &val_dri);
    val_dri = DriverEnum::ORDINARY_SUPERVISION;
    H5Tenum_insert(driver_tid, "ORDINARY_SUPERVISION", &val_dri);
    val_dri = DriverEnum::ORDINARY;
    H5Tenum_insert(driver_tid, "ORDINARY", &val_dri);
    val_dri = DriverEnum::NAIVE_PASSENGER;
    H5Tenum_insert(driver_tid, "NAIVE_PASSENGER",          &val_dri);

    // Create the driver meta data compound
    m_met_dri = H5Tcreate(H5T_COMPOUND, sizeof(met_dri_t));
    H5Tinsert(m_met_dri, "DriverID",    HOFFSET(met_dri_t, DriverID),     strtype);
    H5Tinsert(m_met_dri, "DriverType",  HOFFSET(met_dri_t, DriverType),   driver_tid);

    // Define the enum for the drive type
    drive_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(drive_tid, H5T_SGN_2);
    DriveTypeEnum::Enum val_dty;
    val_dty = DriveTypeEnum::NOT_APPLICABLE;
    H5Tenum_insert(drive_tid, "NOT_APPLICABLE", &val_dty);
    val_dty = DriveTypeEnum::UNKNOWN;
    H5Tenum_insert(drive_tid, "UNKNOWN",        &val_dty);
    val_dty = DriveTypeEnum::ICE;
    H5Tenum_insert(drive_tid, "ICE",            &val_dty);
    val_dty = DriveTypeEnum::EV;
    H5Tenum_insert(drive_tid, "EV",             &val_dty);
    val_dty = DriveTypeEnum::HYBRID;
    H5Tenum_insert(drive_tid, "HYBRID",         &val_dty);
    val_dty = DriveTypeEnum::HORSE;
    H5Tenum_insert(drive_tid, "HORSE",          &val_dty);

    // Define the enum for the fuel type
    fuel_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(fuel_tid, H5T_SGN_2);
    FuelTypeEnum::Enum val_fue;
    val_fue = FuelTypeEnum::NOT_APPLICABLE;
    H5Tenum_insert(fuel_tid, "NOT_APPLICABLE",  &val_fue);
    val_fue = FuelTypeEnum::NA;
    H5Tenum_insert(fuel_tid, "NA",              &val_fue);
    val_fue = FuelTypeEnum::GASOLINE;
    H5Tenum_insert(fuel_tid, "GASOLINE",        &val_fue);
    val_fue = FuelTypeEnum::DIESEL;
    H5Tenum_insert(fuel_tid, "DIESEL",          &val_fue);
    val_fue = FuelTypeEnum::H2;
    H5Tenum_insert(fuel_tid, "H2",              &val_fue);
    val_fue = FuelTypeEnum::FOSSIL_GAS;
    H5Tenum_insert(fuel_tid, "FOSSIL_GAS",      &val_fue);
    val_fue = FuelTypeEnum::OTHER;
    H5Tenum_insert(fuel_tid, "OTHER",           &val_fue);

    // Define the enum for the transmission type
    transmission_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(transmission_tid, H5T_SGN_2);
    TransmissionEnum::Enum val_tra;
    val_tra = TransmissionEnum::NOT_APPLICABLE;
    H5Tenum_insert(transmission_tid, "NOT_APPLICABLE",  &val_tra);
    val_tra = TransmissionEnum::NONE;
    H5Tenum_insert(transmission_tid, "NONE",            &val_tra);
    val_tra = TransmissionEnum::MANUAL;
    H5Tenum_insert(transmission_tid, "MANUAL",          &val_tra);
    val_tra = TransmissionEnum::AUTOMATIC;
    H5Tenum_insert(transmission_tid, "AUTOMATIC",       &val_tra);
    val_tra = TransmissionEnum::OTHER;
    H5Tenum_insert(transmission_tid, "OTHER",           &val_tra);

    // Create the car meta data compound
    m_met_car = H5Tcreate(H5T_COMPOUND, sizeof(met_car_t));
    H5Tinsert(m_met_car, "DriveType",               HOFFSET(met_car_t, DriveType),              drive_tid);
    H5Tinsert(m_met_car, "FuelType",                HOFFSET(met_car_t, FuelType),               fuel_tid);
    H5Tinsert(m_met_car, "NumberOfOccupants",       HOFFSET(met_car_t, NumberOfOccupants),      H5T_STD_I32LE);
    H5Tinsert(m_met_car, "PositionFrontBumper",     HOFFSET(met_car_t, PositionFrontBumper),    H5T_IEEE_F64LE);
    H5Tinsert(m_met_car, "PositionRearBumper",      HOFFSET(met_car_t, PositionRearBumper),     H5T_IEEE_F64LE);
    H5Tinsert(m_met_car, "Transmission",            HOFFSET(met_car_t, Transmission),           transmission_tid);
    H5Tinsert(m_met_car, "VehicleID",               HOFFSET(met_car_t, VehicleID),              strtype);
    H5Tinsert(m_met_car, "VehicleLength",           HOFFSET(met_car_t, VehicleLength),          H5T_IEEE_F64LE);
    H5Tinsert(m_met_car, "VehicleWeight",           HOFFSET(met_car_t, VehicleWeight),          H5T_STD_I32LE);
    H5Tinsert(m_met_car, "VehicleWidth",            HOFFSET(met_car_t, VehicleWidth),           H5T_IEEE_F64LE);
    

    // Define the enum for the analysis enum
    analysis_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(analysis_tid, H5T_SGN_2);
    AnalysisEnum::Enum val_ana;
    val_ana = AnalysisEnum::NOT_APPLICABLE;
    H5Tenum_insert(analysis_tid, "NOT_APPLICABLE",  &val_ana);
    val_ana = AnalysisEnum::NOT_ELIGIBLE;
    H5Tenum_insert(analysis_tid, "NOT_ELIGIBLE",    &val_ana);
    val_ana = AnalysisEnum::ELIGIBLE;
    H5Tenum_insert(analysis_tid, "ELIGIBLE",        &val_ana);
    val_ana = AnalysisEnum::UNKNOWN;
    H5Tenum_insert(analysis_tid, "UNKNOWN",         &val_ana);

    // Define the enum for the baseline enum
    baseline_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(baseline_tid, H5T_SGN_2);
    BaselineEnum::Enum val_bas;
    val_bas = BaselineEnum::NOT_APPLICABLE;
    H5Tenum_insert(baseline_tid, "NOT_APPLICABLE",  &val_bas);
    val_bas = BaselineEnum::NOT_BASELINE;
    H5Tenum_insert(baseline_tid, "NOT_BASELINE",    &val_bas);
    val_bas = BaselineEnum::BASELINE;
    H5Tenum_insert(baseline_tid, "BASELINE",        &val_bas);
    val_bas = BaselineEnum::UNKNOWN;
    H5Tenum_insert(baseline_tid, "UNKNOWN",         &val_bas);

    // Create the enum for the test site enum
    test_tid = H5Tcreate(H5T_ENUM, sizeof(signed char));
    H5Tset_sign(test_tid, H5T_SGN_2);
    TestSiteEnum::Enum val_tes;
    val_tes = TestSiteEnum::NOT_APPLICABLE;
    H5Tenum_insert(test_tid, "NOT_APPLICABLE",  &val_tes);
    val_tes = TestSiteEnum::UNKNOWN;
    H5Tenum_insert(test_tid, "UNKNOWN",         &val_tes);
    val_tes = TestSiteEnum::TEST_TRACK;
    H5Tenum_insert(test_tid, "TEST_TRACK",      &val_tes);
    val_tes = TestSiteEnum::PUBLIC_ROAD;
    H5Tenum_insert(test_tid, "PUBLIC_ROAD",     &val_tes);
    val_tes = TestSiteEnum::PRIVATE_PARKING;
    H5Tenum_insert(test_tid, "PRIVATE_PARKING", &val_tes);
    val_tes = TestSiteEnum::PUBLIC_PARKING;
    H5Tenum_insert(test_tid, "PUBLIC_PARKING",  &val_tes);

    // Create the experiment meta data compound
    m_met_exp = H5Tcreate(H5T_COMPOUND, sizeof(met_exp_t));
    H5Tinsert(m_met_exp, "AnalysisEligible",    HOFFSET(met_exp_t, AnalysisEligible),   analysis_tid);
    H5Tinsert(m_met_exp, "Baseline",            HOFFSET(met_exp_t, Baseline),           baseline_tid);
    H5Tinsert(m_met_exp, "TestCountry",         HOFFSET(met_exp_t, Country),            strtype);
    H5Tinsert(m_met_exp, "TestEndOdometer",     HOFFSET(met_exp_t, TestEndOdo),         H5T_STD_I32LE);
    H5Tinsert(m_met_exp, "TestEndTime",         HOFFSET(met_exp_t, TestEndTime),        H5T_STD_I32LE);
    H5Tinsert(m_met_exp, "TestSizeType",        HOFFSET(met_exp_t, TestSiteType),       test_tid);
    H5Tinsert(m_met_exp, "TestStartOdometer",   HOFFSET(met_exp_t, TestStartOdo),       H5T_STD_I32LE);
    H5Tinsert(m_met_exp, "TestStartTime",       HOFFSET(met_exp_t, TestStartTime),      H5T_STD_I32LE);
    H5Tinsert(m_met_exp, "TripID",              HOFFSET(met_exp_t, TripID),             strtype);

    // Create the overall meta data compound
    m_met = H5Tcreate(H5T_COMPOUND, sizeof(met_t));
    H5Tinsert(m_met, "General", HOFFSET(met_t, General), m_met_gen);
    H5Tinsert(m_met, "Driver", HOFFSET(met_t, Driver), m_met_dri);
    H5Tinsert(m_met, "Car", HOFFSET(met_t, Car), m_met_car);
    H5Tinsert(m_met, "Experiment", HOFFSET(met_t, Experiment), m_met_exp);
}

bool cHDF5DataBuffer::writeDataBuffer()
{
    // Write the positioning data to the file
    bool pos_bool = writePositioningTimeStamp();
    if (!pos_bool)
    {
        printf("Failed while writing positioning dataset");
    }
    // Write the ego data to the file
    bool ego_bool = wrtiteEgoTimeStamp();
    if (!ego_bool)
    {
        printf("Failed while writing ego dataset");
    }
    // Write the lane data to the file
    bool lan_bool = writeLaneTimeStamp();
    if (!lan_bool)
    {
        printf("Failed while writing lane dataset");
    }
    // Write the object data to the file
    bool obj_bool = writeObjectTimeStamp();
    if (!obj_bool)
    {
        printf("Failed while writing object dataset");
    }
    return pos_bool & ego_bool & lan_bool & obj_bool;
}

bool cHDF5DataBuffer::writeMetaData()
{
    // Write the overall meta data to the file
    bool met_bool = writeAttributeArray("/", "metaData", &cMet, m_met);
    if (!met_bool)
    {
        printf("Failed while writing meta data");
    }
    return met_bool;
}
