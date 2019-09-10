CMAKE_MINIMUM_REQUIRED(VERSION 3.2.2)

PROJECT(L3Pilot_CDF_EXAMPLE)

SET (L3Pilot_CDF_EXAMPLE_VERSION_MAJOR 0)
SET (L3Pilot_CDF_EXAMPLE_VERSION_MINOR 2)

##########################################################
#
# setup solution and cmake path
#  - name: L3Pilot_CDF_EXAMPLE
#
##########################################################
IF(MSVC)
	# enable folders for visual studio projects
	SET_PROPERTY( GLOBAL PROPERTY USE_FOLDERS ON )
	set(CMAKE_CONFIGURATION_TYPES Debug Release DebugRead ReleaseRead CACHE TYPE  INTERNAL FORCE )
	#SET( CMAKE_C_FLAGS_DEBUGREAD ${CMAKE_C_FLAGS_DEBUG} CACHE STRING "" FORCE)
	SET( CMAKE_C_FLAGS_DEBUGREAD "/MDd /Zi /Ob0 /Od /RTC1" CACHE STRING "" FORCE)
	SET( CMAKE_C_FLAGS_RELEASEREAD ${CMAKE_C_FLAGS_RELEASE} CACHE STRING "" FORCE)
	#SET( CMAKE_CXX_FLAGS_DEBUGREAD ${CMAKE_CXX_FLAGS_DEBUG} CACHE STRING "" FORCE)
	SET( CMAKE_CXX_FLAGS_DEBUGREAD "/MDd /Zi /Ob0 /Od /RTC1" CACHE STRING "" FORCE)
	SET( CMAKE_CXX_FLAGS_RELEASEREAD ${CMAKE_CXX_FLAGS_RELEASE} CACHE STRING "" FORCE)
	#SET( CMAKE_EXE_LINKER_FLAGS_DEBUGREAD ${CMAKE_EXE_LINKER_FLAGS_DEBUG} CACHE STRING "" FORCE)
	SET( CMAKE_EXE_LINKER_FLAGS_DEBUGREAD "/debug /INCREMENTAL" CACHE STRING "" FORCE)
	SET( CMAKE_EXE_LINKER_FLAGS_RELEASEREAD ${CMAKE_EXE_LINKER_FLAGS_RELEASE} CACHE STRING "" FORCE)
ENDIF()
SET( CMAKE_MODULE_PATH ${PROJECT_SOURCE_DIR}/cmake )
SET ( L3P_EXAMPLE_SRC_DIR ${PROJECT_SOURCE_DIR}/src )
SET ( L3P_EXAMPLE_INCLUDE_DIR ${PROJECT_SOURCE_DIR}/include)

OPTION( BUILD_DOXYGEN "set to true to build documentation with doxygen" FALSE )

OPTION (BUILD_READ_EXAMPLE  "Build example for reading L3Pilot CDF files" OFF)

OPTION (BUILD_WRITE_EXAMPLE  "Build example for writing L3Pilot CDF files" OFF)

###########################################################
#
# setup HDF5 pathes and libraries if they were not found
#
#
############################################################
IF(NOT $ENV{HDF5_DIR})
    SET( HDF5_DIR "D:/Resources/HDF5/HDF5-1.10.1-win64/cmake" CACHE PATH "HDF5 1.10" )
    MESSAGE(STATUS "Set HDF5 directory to " ${HDF5_DIR})
ELSE()
    SET( HDF5_DIR $ENV{HDF5_DIR} CACHE PATH "HDF5 1.10" )
    MESSAGE(STATUS "Set HDF5 directory to " ${HDF5_DIR})
ENDIF()

##########################################################
#
# generate user options for Visual Studio
# configure the template file .vcxproj.user file to setup MSVC specific configuration settings
#
##########################################################
if ( MSVC )
	message( STATUS "=======================" )
	message( STATUS "configuring directories" )
	file(TO_NATIVE_PATH ${CMAKE_BINARY_DIR} CMAKE_BINARY_DIR_PATH)
	file(TO_NATIVE_PATH ${CMAKE_SOURCE_DIR} CMAKE_SOURCE_DIR_PATH)
	
	set(VS_DEBUG_TARGET_COMMAND ${CMAKE_BINARY_DIR}\\src\\writing\\Debug\\GenerateExample.exe)
	set(VS_DEBUG_READ_TARGET_COMMAND ${CMAKE_BINARY_DIR}\\src\\reading\\DebugRead\\ReadExample.exe)
	set(VS_RELEASE_TARGET_COMMAND ${CMAKE_BINARY_DIR}\\src\\writing\\Release\\GenerateExample.exe)
	set(VS_RELEASE_READ_TARGET_COMMAND ${CMAKE_BINARY_DIR}\\src\\reading\\ReleaseRead\\ReadExample.exe)
	
	set(USER_FILE ALL_BUILD.vcxproj.user)
	set(OUTPUT_PATH ${CMAKE_CURRENT_BINARY_DIR}/${USER_FILE})
	configure_file(cmake/ALL_BUILD_TEMPLATE.vcxproj.user ${OUTPUT_PATH} @ONLY)
	message( STATUS "configuring directories ... finished" )
	message( STATUS "=======================" )
endif( MSVC )

ADD_SUBDIRECTORY(${PROJECT_SOURCE_DIR}/src)

if(BUILD_DOXYGEN)
	message( STATUS "=======================" )
	message( STATUS "adding Doxygen components" )
	add_subdirectory( doc )
	message( STATUS "adding Doxygen components ... finished" )
	message( STATUS "=======================" )
endif()
