#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

if [ -z "${-##*i*}" ]; then
    set -e
fi

dirname="$(cd "$(dirname "$0")" && pwd)"

if [ -f "$dirname/gradle/wrapper/gradle-wrapper.jar" ]; then
    exec java "-Dorg.gradle.appname=gradlew" -jar "$dirname/gradle/wrapper/gradle-wrapper.jar" "$@"
else
    echo "Error: Gradle wrapper jar not found."
    exit 1
fi
