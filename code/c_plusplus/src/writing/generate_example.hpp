/**
*   
*   @brief This small program creates a dummy file with exemplary data
*   
*   @file
*   Copyright &copy; Institut fuer Kraftfahrzeuge, Johannes Hiller, 2018
*
*   @author Johannes Hiller
*   @version 0.2
*/

#include <cstdlib>
#include <stdio.h>
#include <stdlib.h>
#include <chrono>
#include <time.h>
#include "hdf5_data_buffer.hpp"

//! The data buffer for buffering the data
cHDF5DataBuffer m_dataBuffer;
//! The struct for writing the ego data
ego_t           cEgo;
//! The struct for writing the object data
obj_t           cObj;
//! The struct for writing the lanes data
lan_t           cLan;
//! The struct for writing the positioning data
pos_t           cPos;

/*!
*   @brief  Generates a random double between the specified numbers
*
*   Generates a random number in the limits specified by lower and upper
*   Take care to initialize the random number generator with srand() before using this function   
*
*   @param lower [in] : The lower limit of the range for the random number
*   @param upper [in] : The upper limit of the range for the random number
*
*   @return The random number
*/
double randomDouble(double lower, double upper);

/*!
*   @brief Function for generating random data
*
*   This function generates random data for the ego data struct
*
*   @see updateEgoData()
*/
void randomEgoData(long long &utc_time, double &file_time);

/*!
*   @brief Function for generating random data
*
*   This function generates random data for the object data struct
*
*   @see updateObjectData()
*/
void randomObjectData(long long &utc_time, double &file_time);

/*!
*   @brief Function for generating random data
*
*   This function generates random data for the lanes data struct
*
*   @see updateLaneData()
*/
void randomLaneData(long long &utc_time, double &file_time);

/*!
*   @brief Function for generating random data
*
*   This function generates random data for the positioning data struct
*
*   @see updatePositioningData()
*/
void randomPositioningData(long long &utc_time, double &file_time);

/*!
*   @brief The main function of the program
*
*   @todo add detailed description
*
*   @return 0 if successful
*/
int main(int argc, char* argv[]);
