@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  sapi startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and SAPI_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\sapi-0.147.jar;%APP_HOME%\lib\jetty-server-9.3.14.v20161028.jar;%APP_HOME%\lib\jetty-servlet-9.3.14.v20161028.jar;%APP_HOME%\lib\jetty-webapp-9.3.14.v20161028.jar;%APP_HOME%\lib\jersey-server-2.22.2.jar;%APP_HOME%\lib\jersey-container-servlet-core-2.22.2.jar;%APP_HOME%\lib\jersey-container-servlet-2.22.2.jar;%APP_HOME%\lib\jersey-media-multipart-2.22.2.jar;%APP_HOME%\lib\httpclient-4.5.1.jar;%APP_HOME%\lib\akka-cluster_2.11-2.4.7.jar;%APP_HOME%\lib\capi-0.147.jar;%APP_HOME%\lib\javax.servlet-api-3.1.0.jar;%APP_HOME%\lib\jetty-http-9.3.14.v20161028.jar;%APP_HOME%\lib\jetty-io-9.3.14.v20161028.jar;%APP_HOME%\lib\jetty-security-9.3.14.v20161028.jar;%APP_HOME%\lib\jetty-xml-9.3.14.v20161028.jar;%APP_HOME%\lib\jersey-common-2.22.2.jar;%APP_HOME%\lib\jersey-client-2.22.2.jar;%APP_HOME%\lib\javax.ws.rs-api-2.0.1.jar;%APP_HOME%\lib\jersey-media-jaxb-2.22.2.jar;%APP_HOME%\lib\javax.annotation-api-1.2.jar;%APP_HOME%\lib\hk2-api-2.4.0-b34.jar;%APP_HOME%\lib\javax.inject-2.4.0-b34.jar;%APP_HOME%\lib\hk2-locator-2.4.0-b34.jar;%APP_HOME%\lib\validation-api-1.1.0.Final.jar;%APP_HOME%\lib\mimepull-1.9.6.jar;%APP_HOME%\lib\httpcore-4.4.3.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-codec-1.9.jar;%APP_HOME%\lib\scala-library-2.11.8.jar;%APP_HOME%\lib\akka-remote_2.11-2.4.7.jar;%APP_HOME%\lib\retrofit-2.1.0.jar;%APP_HOME%\lib\common-0.9.jar;%APP_HOME%\lib\api-0.147.jar;%APP_HOME%\lib\jetty-util-9.3.14.v20161028.jar;%APP_HOME%\lib\jersey-guava-2.22.2.jar;%APP_HOME%\lib\osgi-resource-locator-1.0.1.jar;%APP_HOME%\lib\hk2-utils-2.4.0-b34.jar;%APP_HOME%\lib\aopalliance-repackaged-2.4.0-b34.jar;%APP_HOME%\lib\javassist-3.18.1-GA.jar;%APP_HOME%\lib\akka-actor_2.11-2.4.7.jar;%APP_HOME%\lib\akka-protobuf_2.11-2.4.7.jar;%APP_HOME%\lib\netty-3.10.3.Final.jar;%APP_HOME%\lib\uncommons-maths-1.2.2a.jar;%APP_HOME%\lib\okhttp-3.3.0.jar;%APP_HOME%\lib\protobuf-java-3.0.0.jar;%APP_HOME%\lib\kotlin-stdlib-1.0.5.jar;%APP_HOME%\lib\commons-io-2.5.jar;%APP_HOME%\lib\joda-time-2.9.4.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\config-1.3.0.jar;%APP_HOME%\lib\scala-java8-compat_2.11-0.7.0.jar;%APP_HOME%\lib\okio-1.8.0.jar;%APP_HOME%\lib\kotlin-runtime-1.0.5.jar

@rem Execute sapi
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %SAPI_OPTS%  -classpath "%CLASSPATH%" org.roylance.yadel.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable SAPI_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%SAPI_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
