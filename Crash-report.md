Run chmod +x gradlew

Welcome to Gradle 9.4.1!

Here are the highlights of this release:
 - Java 26 support
 - Non-class-based JVM tests
 - Enhanced console progress bar

For more details see https://docs.gradle.org/9.4.1/release-notes.html

Starting a Gradle Daemon (subsequent builds will be faster)

FAILURE: Build failed with an exception.

* Where:
Build file '/home/runner/work/MiniRadar/MiniRadar/build.gradle' line: 3

* What went wrong:
Plugin [id: 'fabric-loom', version: '1.10.34'] was not found in any of the following sources:

- Gradle Core Plugins (not a core plugin. For more available plugins, please refer to https://docs.gradle.org/9.4.1/userguide/plugin_reference.html in the Gradle documentation.)
- Included Builds (No included builds contain this plugin)
- Plugin Repositories (could not resolve plugin artifact 'fabric-loom:fabric-loom.gradle.plugin:1.10.34')
  Searched in the following repositories:
    Fabric(https://maven.fabricmc.net/)
    Gradle Central Plugin Repository

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights from a Build Scan (powered by Develocity).
> Get more help at https://help.gradle.org.

BUILD FAILED in 15s
Error: Process completed with exit code 1.