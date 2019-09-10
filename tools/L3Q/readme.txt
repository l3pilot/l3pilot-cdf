// L3Q is a software that assists pilot sites in validating collected log files in EU project L3Pilot.
//
// Usage: 1. Place log files in ./logs folder or set the directory in ./config/l3q.properties file.
//        2. Run l3q.bat (requires 64-bit Java installation)
//        3. Reports are stored in .html format in foder ./reports (or as set in the l3q.properties)
//
// Instructions for Ubuntu: HDF5 libraries 1.10.4 are required, especially the latest libhdf5_java.so 
// in /usr/lib/x86_64-linux-gnu/jni and libhdf5.so.103 in /usr/lib/x86_64-linux-gnu
// Please also modify the Java command accordingly: -cp "l3q.jar:jarhdf..."
//
// Contact information: Sami Koskinen, VTT, sami.koskinen@vtt.fi
