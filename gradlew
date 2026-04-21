#!/usr/bin/env sh

executedir=$(dirname "$0")

if [ -x "$executedir/gradle/wrapper/gradle-wrapper.jar" ]; then
    if [ "$OS" = "Windows_NT" ]; then
        "$executedir/gradle/wrapper/gradle-wrapper.jar" "$@"
    else
        java -jar "$executedir/gradle/wrapper/gradle-wrapper.jar" "$@"
    fi
else
    echo "Error: gradle-wrapper.jar not found"
    exit 1
fi