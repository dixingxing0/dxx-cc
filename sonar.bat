@echo off
echo [Pre-Requirement] Makesure install JDK 6.0+ and set the JAVA_HOME.
echo [Pre-Requirement] Makesure install Maven 3.0.4+ and set the PATH.

set MVN=mvn
set MAVEN_OPTS=%MAVEN_OPTS% -XX:MaxPermSize=128m

echo [cc-core] 
cd cc-core
call %MVN% sonar:sonar 
if errorlevel 1 goto error

echo [cc-db] 
cd ../cc-db
call %MVN% sonar:sonar 
if errorlevel 1 goto error


echo [cc-ioc] 
cd ../cc-ioc
call %MVN% sonar:sonar 
if errorlevel 1 goto error

echo [cc-tx] 
cd ../cc-tx
call %MVN% sonar:sonar 
if errorlevel 1 goto error


echo [cc-web] 
cd ../cc-web
call %MVN% sonar:sonar 
if errorlevel 1 goto error


goto end
:error
echo Error Happen!!!
:end
pause