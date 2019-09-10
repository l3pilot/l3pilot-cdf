/**
*
*   @brief A data buffer for writing data to an HDF5 file
*
*   This data buffer writes data to files specified by the structure definition in the L3Pilot Common Data Format
*
*   @file
*   Copyright &copy; Institut fuer Kraftfahrzeuge, Johannes Hiller, 2018
*
*   @author Johannes Hiller
*   @version 0.2
*   @see structure_definitions.h
*/

#include <ctime>
#include <sstream>
#include <string.h>
#include <structure_definitions.h>

/*! 
*   @brief Class for buffering data before writing it to the HDF5 file
*
*   This class is configured using the name and location of the file (configureDataBuffer())
*   The data is written to the file, every time the writing function is called. This can be used to vary the cycle time.
*   
*   @see configureDataBuffer()
*/
class cHDF5DataBuffer 
{
public:
    /*!
    *   @brief The constructor. Any initialization of values can be done here.
    *
    */
    cHDF5DataBuffer();

    /*!
    *   @brief Function for configuring the data buffer
    *
    *   Here the HDF5 file is created and the structure is defined.
    *
    *   @param filename [in] : The name of the HDF5 file that is created
    *   @param folder [in] : The location for saving the HDF5 file
    *
    *   @return true if successful
    */
    bool configureDataBuffer(std::string filename, std::string folder);

    /*!
    *   @brief Updates the struct with the ego data
    *
    *   Update the internal struct, which holds the values to write in the ego dataset
    *
    *   @param ego_struct [in] : The struct containing the new ego data
    */
    void updateEgoData(ego_t ego_struct);

    /*!
    *   @brief Updates the struct with the positioning data
    *
    *   Update the internal struct, which holds the values to write in the positioning dataset
    *
    *   @param pos_struct [in] : The struct containing the new positioning data
    */
    void updatePositioningData(pos_t pos_struct);

    /*!
    *   @brief Updates the struct with the lane data
    *
    *   Update the internal struct, which holds the values to write in the lanes dataset
    *
    *   @param lan_struct [in] : The struct containing the new lane data
    */
    void updateLaneData(lan_t lan_struct);

    /*!
    *   @brief Updates the struct with the object data
    *
    *   Update the internal struct, which holds the values to write in the object dataset
    *
    *   @param obj_struct [in] : The struct containing the new object data
    */
    void updateObjectData(obj_t obj_struct);

    /*!
    *   @brief Updates the general meta data
    *
    *   Update the internal struct, which holds the values to write in the general meta data   
    *
    *   @param met_gen_struct [in] : The struct containing the general meta data
    */
    void updateGeneralMetaData(met_gen_t met_gen_struct);

    /*!
    *   @brief Updates the driver meta data
    *
    *   Update the internal struct, which holds the values to write in the driver meta data   
    *
    *   @param met_dri_struct [in] : The struct containing the driving meta data
    */
    void updateDriverMetaData(met_dri_t met_dri_struct);

    /*!
    *   @brief Updates the car meta data
    *
    *   Update the internal struct, which holds the values to write in the car meta data   
    *
    *   @param met_car_struct [in] : The struct containing the car meta data
    */
    void updateCarMetaData(met_car_t met_car_struct);

    /*!
    *   @brief Updates the experiment meta data
    *
    *   Update the internal struct, which holds the values to write in the experiment meta data   
    *
    *   @param met_exp_struct [in] : The struct containing the experiment meta data
    */
    void updateExperimentMetaData(met_exp_t met_exp_struct);

    /*!
    *   @brief Writes the current data in the buffer to the HDF5 file
    *
    *   Function that takes the current data in the buffer and writes it to the HDF5 file.
    *   The data has to be passed to the buffer by updateEgoData(), updatePositioningData(), updateLaneData(), updateObjectData().
    *
    *   @see updateEgoData, updatePositioningData, updateLaneData, updateObjectData
    *
    *   @return true if all write functions were successful
    */
    bool writeDataBuffer();

    /*!
    *   @brief Writes the meta data struct to the HDF5 file
    *
    *   This function adds the meta data to the file.
    *   The meta data structs are General, Driver, Car and Experiment.
    *
    *   @see met_gen_t, met_dri_t, met_car_t, met_exp_t
    *
    *   @return true if all write functions were successful
    */
    bool writeMetaData();

    /*!
    *   @brief Flushes the datasets and closes the file
    *
    *   First a flush of all datasets is done, in order to ensure that all data are written to the file.
    *   Afterwards, all open HDF5 data types, that were created are closed. This ensures, that no file dependencies are still open.
    *   In a final step, the file is closed.
    *   This procedure is a necessity with HDF5 files.
    *
    *   @return true if successful
    */
    bool finalizeDataFile();

protected:
    //! Identifier for the HDF5 file
    hid_t m_file;
    //! Status of the HDF5 file operations
    herr_t m_file_status;

    //! Ego-vehicle identifiers for the enums
    hid_t active_tid, ava_tid, brake_tid, indicator_tid, hands_tid, wiper_tid, ndra_tid, on_tid, tor_tid;
    //! Identifiers for the ego dataset
    hid_t m_ego_tid, m_ego_ds, m_ego_space, m_ego_fspace;
    //! Status of the ego dataset
    herr_t m_ego_status;
    //! Dimensions and sizes for the ego dataset
    hsize_t ego_dim[1], max_ego_dim[1], ego_chunk_dim[1];
    //! Sizes for the extension of the ego dataset
    hsize_t m_ego_osize[1], m_ego_nsize[1], m_ego_offset[1];

    //! Identifiers for the positioning dataset
    hid_t m_pos_tid, m_pos_ds, m_pos_space, m_pos_fspace;
    //! Status of the positioning dataset
    herr_t m_pos_status;
    //! Dimensions and sizes of the positioning dataset
    hsize_t pos_dim[1], max_pos_dim[1], pos_chunk_dim[1];
    //! Sizes for the extension of the positioning dataset
    hsize_t m_pos_osize[1], m_pos_nsize[1], m_pos_offset[1];

    //! Objects classification enum type
    hid_t obj_cla_tid;
    //! Identifiers for the objects dataset
    hid_t m_obj_tid, m_obj_obj_tid, m_obj_array, m_obj_ds, m_obj_space, m_obj_fspace;
    //! Status of the objects dataset
    herr_t m_obj_status;
    //! Dimensions and sizes of the objects dataset
    hsize_t obj_dim[1], max_obj_dim[1], obj_chunk_dim[1];
    //! Sizes for the extension of the objects dataset
    hsize_t m_obj_osize[1], m_obj_nsize[1], m_obj_offset[1];
    
    //! Lane marking type enum type
    hid_t lan_typ_tid;
    //! Identifiers for the lanes dataset
    hid_t m_lan_tid, m_lan_lan_tid, m_lan_array, m_lan_ds, m_lan_space, m_lan_fspace;
    //! Status of the lanes dataset
    herr_t m_lan_status;
    //! Dimensions and sizes of the lanes dataset
    hsize_t lan_dim[1], max_lan_dim[1], lan_chunk_dim[1];
    //! Sizes for the extension of the lanes dataset
    hsize_t m_lan_osize[1], m_lan_nsize[1], m_lan_offset[1];

    //! Identifier for the general meta data
    hid_t m_met_gen;
    //! Driver meta data identifiers for the enums
    hid_t skill_tid, driver_tid;
    //! Identifier for the driver meta data
    hid_t m_met_dri;
    //! Car meta data identifiers for the enums
    hid_t drive_tid, fuel_tid, transmission_tid;
    //! Identifier for the car meta data
    hid_t m_met_car;
    //! Experiment meta data identifiers for the enums
    hid_t analysis_tid, baseline_tid, test_tid;
    //! Identifier for the experiment meta data
    hid_t m_met_exp;
    //! Identifier for all meta data
    hid_t m_met;

    //! The properties for creating datasets
    hid_t m_prop_ds;

    //! Booleans for the determination of the first run on datasets
    bool ego_first, pos_first, obj_first, lan_first;

private:
    //! The name of the HDF5 file
    std::string         m_fileName;
    //! The location for the HDF5 file
    std::string         m_folder;
    //! The complete name and location of the HDF5 file
    std::stringstream   m_complete_file;
    //! The struct for the general meta data
    met_gen_t           cMetGen;
    //! The struct for the driver meta data
    met_dri_t           cMetDri;
    //! The struct for the car meta data
    met_car_t           cMetCar;
    //! The struct for the experiment meta data
    met_exp_t           cMetExp;
    //! The struct for the overall metadata
    met_t               cMet;
    //! The struct for writing the ego data
    ego_t               cEgo;
    //! The struct with the fill values for the ego dataset
    ego_t               cEgoFill;
    //! The struct for writing the object data
    obj_t               cObj;
    //! The struct with the fill values for object dataset
    obj_t               cObjFill;
    //! The struct with the fill values for the objects
    obj_obj_t           cObjObjFill;
    //! The struct for writing the lanes data
    lan_t               cLan;
    //! The struct with the fill values for the lane dataset
    lan_t               cLanFill;
    //! The struct with the fill values for the lanes
    lan_lan_t           cLanLanFill;
    //! The struct for writing the positioning data
    pos_t               cPos;
    //! The struct with the fill values for the positioning dataset
    pos_t               cPosFill;

    //! Member variable for the conversion between seconds and milliseconds
    int                 toMS;

    /*!
    *   @brief Function that returns the current time as string
    *
    *   @return The current time
    */
    const std::string getCurrentTime();

    /*!
    *   @brief Function for writing a string to an attribute
    *
    *   Function for writing a single string to an attribute in the HDF5 file used in the data buffer.
    *   This function takes care of all the types, spaces and objects needed.
    *
    *   @param location [in] : The location in the file to write the attribute to
    *   @param attrname [in] : The name of the attribute
    *   @param attrvalue [in] : The value of the attribute
    *
    *   @return true if successful
    */
    bool writeAttributeString(const char* location, const char* attrname, const char* attrvalue);

    /*!
    *   @brief Function for writing a 2x2 array of attributes to a dataset.
    *
    *   This is used in the datasets to describe the signals contained and to define the units of the signals.
    *   This function takes care of all the types, spaces and objects needed.
    *
    *   @param location [in] : The location in the file to write the attribute to
    *   @param attrname [in] : The name of the attribute
    *   @param description [in] : The description of the signal
    *   @param unit [in] : The unit of the signal
    *
    *   @return true if successful
    */
    bool writeDSAttributeSignal(const char* location, const char* attrname, const char* description, const char* unit);
    
    /*!
    *   @brief Writes an array to the attributes
    *
    *   This function writes an array (struct) of values under an attribute, that is added to the HDF5 file in the specified location.
    *   In order for this to work, it is important, that the datatype of the array is specified and that it matches that of the array.
    *   The location specified must be an object that is already contained in the file. Therefore, take care to create the object first.
    *
    *   @param location [in] : The location in the HDF5 file, where the attribute array should be places
    *   @param attrname [in] : The name of the attribute, under which the attribute is placed
    *   @param array [in] : The array that is used as attribute
    *   @param datatype [in] : The datatype of the attribute array
    *   
    *   @return true if successful
    */
    bool writeAttributeArray(const char* location, const char* attrname, void* array, hid_t datatype);

    /*!
    *   @brief Writes the current ego data into the HDF5 file
    *
    *   Takes the data from the internal storage struct for the ego data and writes it to the HDF5 file.
    *   The internal struct can be updated with the updateEgoData() method.
    *
    *   @see updateEgoData()
    *
    *   @return true if successful
    */
    bool wrtiteEgoTimeStamp();

    /*!
    *   @brief Writes the current object data into the HDF5 file
    *
    *   Takes the data from the internal storage struct for the object data and writes it to the HDF5 file.
    *   The internal struct can be updated with the updateObjectData() method.
    *
    *   @see updateObjectData()
    *
    *   @return true if successful
    */
    bool writeObjectTimeStamp();

    /*!
    *   @brief Writes the current lane data into the HDF5 file
    *
    *   Takes the data from the internal storage struct for the lane data and writes it to the HDF5 file.
    *   The internal struct can be updated with the updateLaneData() method.
    *
    *   @see updateLaneData()
    *
    *   @return true if successful
    */
    bool writeLaneTimeStamp();

    /*!
    *   @brief Writes the current position data into the HDF5 file
    *
    *   Takes the data from the internal storage struct for the positioning data and writes it to the HDF5 file.
    *   The internal struct can be updated with the updatePositioningData() method.
    *
    *   @see updatePositioningData()
    *
    *   @return true if successful
    */
    bool writePositioningTimeStamp();

    /*!
    *   @brief Configures the data types for the meta data
    *
    *   Configures all the structures and data types that are used for meta data
    *   This function is called in the configureDataBuffer() function
    *
    *
    *   @see configureDataBuffer()
    */
    void configureMetaDataStructure();
};
