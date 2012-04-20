@echo off
echo [Pre-Requirement] Makesure install JDK 6.0+ and set the JAVA_HOME.
echo [Pre-Requirement] Makesure install Maven 3.0.4+ and set the PATH.

set MVN=mvn
set MAVEN_OPTS=%MAVEN_OPTS% -XX:MaxPermSize=128m

echo [Step 1] Install all cc modules to local maven repository.
call %MVN% clean install -pl org.cc:cc-parent,org.cc:cc-core,org.cc:cc-db,org.cc:cc-ioc,,org.cc:cc-transaction,org.cc:cc-web -Dmaven.test.skip=true
if errorlevel 1 goto error

goto end
:error
echo Error Happen!!!
:end
pause