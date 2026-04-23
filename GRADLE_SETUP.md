# Gradle Wrapper 8.11.1 Setup

## Problem
The current system Gradle 9.4.1 is incompatible with NeoForge plugins due to Groovy version mismatch.

## Solution

### Step 1: Manually download Gradle 8.11.1
1. Go to: https://services.gradle.org/distributions/gradle-8.11.1-bin.zip
2. Download the zip file
3. Extract it to: `~/.gradle/wrapper/dists/gradle-8.11.1-bin/`

### Step 2: Verify the installation
```bash
ls -la ~/.gradle/wrapper/dists/gradle-8.11.1-bin/gradle-8.11.1/
```

### Step 3: Update gradlew script
The script is already configured to use the wrapper, but we need the proper gradle-wrapper.jar file.

### Step 4: Run the build
```bash
./gradlew build
```

## Alternative: Use Gradle 8.11.1 directly
If you have Gradle 8.11.1 installed, you can run:
```bash
gradle-8.11.1 build
```
