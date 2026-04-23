#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Set local scope for the variables with windows NT shell
if [ -z "${-##*i*}" ]; then
    set -e
fi

dirname="$(cd "$(dirname "$0")" && pwd)"

# Set Java 25
export JAVA_HOME="/opt/homebrew/Cellar/openjdk/25.0.2/libexec/openjdk.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

# Use Gradle Wrapper
if [ -f "$dirname/gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "Using Gradle Wrapper"
    exec java "-Dorg.gradle.appname=gradlew" -jar "$dirname/gradle/wrapper/gradle-wrapper.jar" "$@"
else
    echo "Error: Gradle wrapper jar not found."
    exit 1
fi
