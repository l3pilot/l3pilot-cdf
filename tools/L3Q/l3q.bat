@echo off
title L3Pilot log file quality check
:start
echo Starting processing.
echo.
java -Xmx4096m -cp l3q.jar;jarhdf-4.2.14.jar;jarhdf5-1.10.4.jar;slf4j-api-1.7.25.jar;slf4j-simple-1.7.25.jar;jfreechart-1.0.19.jar;jcommon-1.0.23.jar l3q.L3Q
echo Processing finished
echo.
pause
