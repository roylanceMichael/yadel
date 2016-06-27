@REM ----------------------------------------------------------------------------
@REM Copyright 2001-2004 The Apache Software Foundation.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM      http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM ----------------------------------------------------------------------------
@REM

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\org\roylance\yadel.api\0.43-SNAPSHOT\yadel.api-0.43-20160625.225453-1.jar;"%REPO%"\commons-io\commons-io\2.5\commons-io-2.5.jar;"%REPO%"\commons-codec\commons-codec\1.10\commons-codec-1.10.jar;"%REPO%"\com\typesafe\akka\akka-cluster_2.11\2.4.7\akka-cluster_2.11-2.4.7.jar;"%REPO%"\org\scala-lang\scala-library\2.11.8\scala-library-2.11.8.jar;"%REPO%"\com\typesafe\akka\akka-remote_2.11\2.4.7\akka-remote_2.11-2.4.7.jar;"%REPO%"\com\typesafe\akka\akka-actor_2.11\2.4.7\akka-actor_2.11-2.4.7.jar;"%REPO%"\com\typesafe\config\1.3.0\config-1.3.0.jar;"%REPO%"\org\scala-lang\modules\scala-java8-compat_2.11\0.7.0\scala-java8-compat_2.11-0.7.0.jar;"%REPO%"\com\typesafe\akka\akka-protobuf_2.11\2.4.7\akka-protobuf_2.11-2.4.7.jar;"%REPO%"\io\netty\netty\3.10.3.Final\netty-3.10.3.Final.jar;"%REPO%"\org\uncommons\maths\uncommons-maths\1.2.2a\uncommons-maths-1.2.2a.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-core\8.0.28\tomcat-embed-core-8.0.28.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-logging-juli\8.0.28\tomcat-embed-logging-juli-8.0.28.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-jasper\8.0.28\tomcat-embed-jasper-8.0.28.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-el\8.0.28\tomcat-embed-el-8.0.28.jar;"%REPO%"\org\eclipse\jdt\core\compiler\ecj\4.4.2\ecj-4.4.2.jar;"%REPO%"\org\apache\tomcat\tomcat-jasper\8.0.28\tomcat-jasper-8.0.28.jar;"%REPO%"\org\apache\tomcat\tomcat-servlet-api\8.0.28\tomcat-servlet-api-8.0.28.jar;"%REPO%"\org\apache\tomcat\tomcat-juli\8.0.28\tomcat-juli-8.0.28.jar;"%REPO%"\org\apache\tomcat\tomcat-el-api\8.0.28\tomcat-el-api-8.0.28.jar;"%REPO%"\org\apache\tomcat\tomcat-api\8.0.28\tomcat-api-8.0.28.jar;"%REPO%"\org\apache\tomcat\tomcat-util-scan\8.0.28\tomcat-util-scan-8.0.28.jar;"%REPO%"\org\apache\tomcat\tomcat-util\8.0.28\tomcat-util-8.0.28.jar;"%REPO%"\org\apache\tomcat\tomcat-jasper-el\8.0.28\tomcat-jasper-el-8.0.28.jar;"%REPO%"\org\apache\tomcat\tomcat-jsp-api\8.0.28\tomcat-jsp-api-8.0.28.jar;"%REPO%"\org\roylance\chaperapp.models\0.33-SNAPSHOT\chaperapp.models-0.33-20160412.210150-1.jar;"%REPO%"\com\squareup\retrofit\converter-jackson\1.9.0\converter-jackson-1.9.0.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-databind\2.4.3\jackson-databind-2.4.3.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-annotations\2.4.0\jackson-annotations-2.4.0.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-core\2.4.3\jackson-core-2.4.3.jar;"%REPO%"\com\squareup\okhttp\okhttp\2.6.0\okhttp-2.6.0.jar;"%REPO%"\com\squareup\okio\okio\1.6.0\okio-1.6.0.jar;"%REPO%"\com\squareup\okhttp\okhttp-urlconnection\2.6.0\okhttp-urlconnection-2.6.0.jar;"%REPO%"\org\roylance\yaorm\0.58-SNAPSHOT\yaorm-0.58-20160623.181725-1.jar;"%REPO%"\com\google\code\gson\gson\2.6.2\gson-2.6.2.jar;"%REPO%"\org\jetbrains\kotlin\kotlin-stdlib\1.0.1\kotlin-stdlib-1.0.1.jar;"%REPO%"\org\jetbrains\kotlin\kotlin-runtime\1.0.1\kotlin-runtime-1.0.1.jar;"%REPO%"\org\quartz-scheduler\quartz\2.2.1\quartz-2.2.1.jar;"%REPO%"\c3p0\c3p0\0.9.1.1\c3p0-0.9.1.1.jar;"%REPO%"\org\slf4j\slf4j-api\1.6.6\slf4j-api-1.6.6.jar;"%REPO%"\org\quartz-scheduler\quartz-jobs\2.2.1\quartz-jobs-2.2.1.jar;"%REPO%"\junit\junit\4.12\junit-4.12.jar;"%REPO%"\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar;"%REPO%"\org\xerial\sqlite-jdbc\3.8.10.1\sqlite-jdbc-3.8.10.1.jar;"%REPO%"\org\glassfish\jersey\containers\jersey-container-servlet\2.22.2\jersey-container-servlet-2.22.2.jar;"%REPO%"\org\glassfish\jersey\containers\jersey-container-servlet-core\2.22.2\jersey-container-servlet-core-2.22.2.jar;"%REPO%"\org\glassfish\hk2\external\javax.inject\2.4.0-b34\javax.inject-2.4.0-b34.jar;"%REPO%"\org\glassfish\jersey\core\jersey-common\2.22.2\jersey-common-2.22.2.jar;"%REPO%"\javax\annotation\javax.annotation-api\1.2\javax.annotation-api-1.2.jar;"%REPO%"\org\glassfish\jersey\bundles\repackaged\jersey-guava\2.22.2\jersey-guava-2.22.2.jar;"%REPO%"\org\glassfish\hk2\hk2-api\2.4.0-b34\hk2-api-2.4.0-b34.jar;"%REPO%"\org\glassfish\hk2\hk2-utils\2.4.0-b34\hk2-utils-2.4.0-b34.jar;"%REPO%"\org\glassfish\hk2\external\aopalliance-repackaged\2.4.0-b34\aopalliance-repackaged-2.4.0-b34.jar;"%REPO%"\org\glassfish\hk2\hk2-locator\2.4.0-b34\hk2-locator-2.4.0-b34.jar;"%REPO%"\org\javassist\javassist\3.18.1-GA\javassist-3.18.1-GA.jar;"%REPO%"\org\glassfish\hk2\osgi-resource-locator\1.0.1\osgi-resource-locator-1.0.1.jar;"%REPO%"\org\glassfish\jersey\core\jersey-server\2.22.2\jersey-server-2.22.2.jar;"%REPO%"\org\glassfish\jersey\core\jersey-client\2.22.2\jersey-client-2.22.2.jar;"%REPO%"\org\glassfish\jersey\media\jersey-media-jaxb\2.22.2\jersey-media-jaxb-2.22.2.jar;"%REPO%"\javax\validation\validation-api\1.1.0.Final\validation-api-1.1.0.Final.jar;"%REPO%"\javax\ws\rs\javax.ws.rs-api\2.0.1\javax.ws.rs-api-2.0.1.jar;"%REPO%"\com\sun\jersey\jersey-json\1.19.1\jersey-json-1.19.1.jar;"%REPO%"\org\codehaus\jettison\jettison\1.1\jettison-1.1.jar;"%REPO%"\com\sun\xml\bind\jaxb-impl\2.2.3-1\jaxb-impl-2.2.3-1.jar;"%REPO%"\javax\xml\bind\jaxb-api\2.2.2\jaxb-api-2.2.2.jar;"%REPO%"\javax\xml\stream\stax-api\1.0-2\stax-api-1.0-2.jar;"%REPO%"\javax\activation\activation\1.1\activation-1.1.jar;"%REPO%"\org\codehaus\jackson\jackson-core-asl\1.9.2\jackson-core-asl-1.9.2.jar;"%REPO%"\org\codehaus\jackson\jackson-mapper-asl\1.9.2\jackson-mapper-asl-1.9.2.jar;"%REPO%"\org\codehaus\jackson\jackson-jaxrs\1.9.2\jackson-jaxrs-1.9.2.jar;"%REPO%"\org\codehaus\jackson\jackson-xc\1.9.2\jackson-xc-1.9.2.jar;"%REPO%"\com\sun\jersey\jersey-core\1.19.1\jersey-core-1.19.1.jar;"%REPO%"\javax\ws\rs\jsr311-api\1.1.1\jsr311-api-1.1.1.jar;"%REPO%"\redis\clients\jedis\2.6.0\jedis-2.6.0.jar;"%REPO%"\org\apache\commons\commons-pool2\2.0\commons-pool2-2.0.jar;"%REPO%"\com\intellij\annotations\12.0\annotations-12.0.jar;"%REPO%"\com\squareup\retrofit\retrofit\1.9.0\retrofit-1.9.0.jar;"%REPO%"\org\apache\httpcomponents\httpclient\4.5.1\httpclient-4.5.1.jar;"%REPO%"\org\apache\httpcomponents\httpcore\4.4.3\httpcore-4.4.3.jar;"%REPO%"\commons-logging\commons-logging\1.2\commons-logging-1.2.jar;"%REPO%"\com\google\protobuf\protobuf-java\3.0.0-beta-3\protobuf-java-3.0.0-beta-3.jar;"%REPO%"\org\roylance\yadel.report\0.1-SNAPSHOT\yadel.report-0.1-SNAPSHOT.jar
set EXTRA_JVM_ARGUMENTS=
goto endInit

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS% %EXTRA_JVM_ARGUMENTS% -classpath %CLASSPATH_PREFIX%;%CLASSPATH% -Dapp.name="webapp" -Dapp.repo="%REPO%" -Dbasedir="%BASEDIR%" launch.Main %CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@endlocal

:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
