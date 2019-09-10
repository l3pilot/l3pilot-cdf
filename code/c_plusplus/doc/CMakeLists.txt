FIND_PACKAGE(Doxygen)

IF(DOXYGEN_FOUND)
	# setup doxygen input file and output path
	# all doxygen specific settings are set in the inputfile doxygenConfig.in

	CONFIGURE_FILE(doxygenConfig.in ${CMAKE_BINARY_DIR}/doc/doxygenConfig @ONLY)

	SET(DOXYGEN_INPUT_FILE ${CMAKE_BINARY_DIR}/doc/doxygenConfig)
	SET(DOXYGEN_OUTPUT ${CMAKE_BINARY_DIR}/doc)
	
	ADD_CUSTOM_TARGET(doc
		COMMAND ${DOXYGEN_EXECUTABLE} ${DOXYGEN_INPUT_FILE}
		WORKING DIRECTORY ${DOXYGEN_OUTPUT}
		COMMENT "Gerating documentation with Doxygen"
		VERBATIM)
ENDIF()
