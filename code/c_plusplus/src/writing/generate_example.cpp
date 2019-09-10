#include "generate_example.hpp"

double randomDouble(double lower, double upper)
{
    // Generate a random number with the specified lower and upper limit
    return lower + static_cast<double>(rand()) / (static_cast<double>(RAND_MAX / (upper - lower)));
}

void randomEgoData(long long & utc_time, double & file_time)
{
    cEgo.UTCTime =                  utc_time;
    cEgo.FileTime =                 file_time;
    cEgo.ABSIntervention =          static_cast<ActiveEnum::Enum>(rand() % 2);
    cEgo.ADFunctionActive =         static_cast<ActiveEnum::Enum>(1);
    cEgo.ADFunctionAvailable =      static_cast<AvailableEnum::Enum>(1);
    cEgo.AmbientLightLevel =        randomDouble(-12., 12.);
    cEgo.AmbientTemperature =       randomDouble(-30., 50.);
    cEgo.BaselineADASActive =       rand() % 1024;
    cEgo.BaselineADASIntervention = rand() % 1024;
    cEgo.BrakeLight =               static_cast<BrakeEnum::Enum>(rand() % 3);
    cEgo.BrakePedalPos =            rand() % 101;
    cEgo.BrakePressure =            randomDouble(0., 10.);
    cEgo.DirectionIndicator =       static_cast<IndicatorEnum::Enum>(rand() % 4);
    cEgo.ESCIntervention =          static_cast<ActiveEnum::Enum>(rand() % 2);
    cEgo.FrontFogLightStatus =      static_cast<OnEnum::Enum>(rand() % 2);
    cEgo.FrontWiperStatus =         static_cast<WiperEnum::Enum>(rand() % 5);
    cEgo.FuelConsumption =          randomDouble(0., 100.);
    cEgo.HandsOnDetection =         static_cast<HandsEnum::Enum>(rand() % 3);
    cEgo.LatAcceleration =          randomDouble(-10., 10.);
    cEgo.LongAcceleration =         randomDouble(-10., 10.);
    cEgo.Odometer =                 file_time * 26 + 4242;
    cEgo.RearFogLightStatus =       static_cast<OnEnum::Enum>(rand() % 2);
    cEgo.SteeringAngle =            randomDouble(-4 * M_PI, 4 * M_PI);
    cEgo.SteeringAngleADF =         randomDouble(-4 * M_PI, 4 * M_PI);
    cEgo.ThrottlePedalPos =         rand() % 101;
    cEgo.TOR =                      static_cast<TOREnum::Enum>(rand() % 2);
    cEgo.TorsionBarTorque =         randomDouble(-100., 100.);
    cEgo.VehicleSpeed =             26.;
    cEgo.YawRate =                  randomDouble(-10., 10.);
}

void randomObjectData(long long & utc_time, double & file_time)
{
    cObj.UTCTime =          utc_time;
    cObj.FileTime =         file_time;
    cObj.LeadVehicleID =    rand() % 10000;
    cObj.NumberOfObjects =  rand() % (OBJECT_NUMBER + 1);
    for (int i = 0; i < OBJECT_NUMBER; i++)
    {
        cObj.sObject[i].Classification =        static_cast<ObjClassification::Enum>(rand() % 6);
        cObj.sObject[i].Height =                randomDouble(1.5, 4.);
        cObj.sObject[i].ID =                    rand() % 10000;
        cObj.sObject[i].LatPosition =           randomDouble(-100., 100.);
        cObj.sObject[i].LatVelocity =           randomDouble(-100., 100.);
        cObj.sObject[i].Length =                randomDouble(0., 20.);
        cObj.sObject[i].LongPosition =          randomDouble(-300., 300.);
        cObj.sObject[i].LongVelocity =          randomDouble(-100., 100.);
        cObj.sObject[i].Width =                 randomDouble(0., 5.);
        cObj.sObject[i].YawAngle =              randomDouble(-M_PI, M_PI);
        cObj.sObject[i].YawRate =               randomDouble(-10., 10.);
    }
}

void randomLaneData(long long & utc_time, double & file_time)
{
    cLan.UTCTime =      utc_time;
    cLan.FileTime =     file_time;
    cLan.EgoLaneWidth = randomDouble(0., 6.);
    for (int i = 0; i < LANE_NUMBER; i++)
    {
        cLan.sLaneLine[i].Curvature =       randomDouble(-1., 1.);
        cLan.sLaneLine[i].CurvatureDx =     randomDouble(-1., 1.);
        cLan.sLaneLine[i].Dy =              randomDouble(-20., 20.);
        cLan.sLaneLine[i].QualityIndex =    rand() % 6;
        cLan.sLaneLine[i].Type =            static_cast<LaneType::Enum>(rand() % 6);
        cLan.sLaneLine[i].YawAngle =        randomDouble(-M_PI, M_PI);
    }
}

void randomPositioningData(long long & utc_time, double & file_time)
{
    cPos.UTCTime =              utc_time;
    cPos.FileTime =             file_time;
    cPos.Altitude =             randomDouble(-50., 4000.);
    cPos.GNSSSpeed =            randomDouble(-7., 70.);
    cPos.GNSSTime =             utc_time;
    cPos.Heading =              randomDouble(0., 2 * M_PI);
    cPos.Latitude =             randomDouble(36., 61.);
    cPos.Longitude =            randomDouble(-10., 26.);
    cPos.NumberOfSatellites =   rand() % 37;
}

int main(int argc, char *argv[])
{
    // Initialize the random generator with a seed
    srand(static_cast<unsigned>(time(NULL)));
    // Create the HDF5 data buffer
    m_dataBuffer = cHDF5DataBuffer();
    // Configure the data buffer
    m_dataBuffer.configureDataBuffer("l3pilot_cdf_filled_example_v0.8", "D:\\");
    // Write the meta data to the file. Here, simply the default meta data is used
    m_dataBuffer.writeMetaData();

    // The UTC time in us at the beginning of the recording
    long long start_time =  std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now().time_since_epoch()).count();
    long long utc_time =    -1;
    double file_time =      -1;
    // Create the sample file with 1001 timestamps, i.e. 100 seconds
    for (int i = 0; i < 1001; i++)
    {
        utc_time = start_time + i * (long long)1e2;
        file_time = (double)i / 10;
        // Generate random ego data
        randomEgoData(utc_time, file_time);
        m_dataBuffer.updateEgoData(cEgo);
        // Generate random object data
        randomObjectData(utc_time, file_time);
        m_dataBuffer.updateObjectData(cObj);
        // Generate random lane data
        randomLaneData(utc_time, file_time);
        m_dataBuffer.updateLaneData(cLan);
        // Generate random positioning data
        randomPositioningData(utc_time, file_time);
        m_dataBuffer.updatePositioningData(cPos);
        // Write the data buffer to the file
        m_dataBuffer.writeDataBuffer();
    }
    // Write all data to the 
    m_dataBuffer.finalizeDataFile();
    return 0;
}