@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.\
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

if exist "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" (
    set GRADLE_WRAPPER_JAR=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
) else (
    echo Error: Gradle wrapper jar not found.
    exit /b 1
)

if not defined JAVA_HOME (
    echo Error: JAVA_HOME is not set.
    exit /b 1
)

set JAVA_EXE=%JAVA_HOME%\bin\java.exe

if not exist "%JAVA_EXE%" (
    echo Error: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
    exit /b 1
)

exec "%JAVA_EXE%" "-Dorg.gradle.appname=%APP_BASE_NAME%" -jar "%GRADLE_WRAPPER_JAR%" %*
