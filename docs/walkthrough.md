# Gradle Java 21 Compatibility Fix Walkthrough

This document records the context and resolution for the Gradle build failure encountered when using Java 21 with the Gradle 7.6 wrapper.

## Issue Description
When executing Gradle tasks, the build failed with the following error:
```
Could not open cp_init generic class cache for initialization script 'C:\Users\sahuk\AppData\Local\Temp\01f6c43ec0866d9d7133b2ce4aacdec9e2d7ba372cd5f7b3884dcb53c04da8f4.gradle' (C:\Users\sahuk\.gradle\caches\7.6\scripts\f1iusddrx2jvd7d7rz5v613c1).
> BUG! exception in phase 'semantic analysis' in source unit '_BuildScript_' Unsupported class file major version 65
```

### Analysis
- **Class File Major Version 65** corresponds to **Java 21**.
- The project is configured with Gradle 7.6 (`gradle-wrapper.properties`), which does not support running on Java 21.
- While upgrading Gradle to 8.x is an option, it breaks compatibility with the Palantir docker plugins configured on some recipes.
- Therefore, the Gradle daemon must be executed using a compatible Java version (JDK 17 or lower) while compiling the codebase itself with the correct toolchain.

## Changes Made
### [MODIFY] [gradle.properties](file:///c:/Users/sahuk/IdeaProjects/spring-6-recipes/gradle.properties)
Configured `org.gradle.java.home` to point to a compatible JDK 17 installation found on the machine:
```diff
+ org.gradle.java.home=C:/Program Files/Java/jdk-17.0.4.1
```

## Verification Results
We ran the compilation target for the active project module:
```cmd
./gradlew :ch05:recipe_5_1_ii:compileJava
```
### Output:
```cmd
Starting a Gradle Daemon, 1 busy and 1 incompatible and 4 stopped Daemons could not be reused...
> Task :shared-resources:compileJava UP-TO-DATE
> Task :ch05:recipe_5_shared:compileJava UP-TO-DATE
> Task :ch05:recipe_5_1_ii:compileJava

BUILD SUCCESSFUL in 37s
3 actionable tasks: 1 executed, 2 up-to-date
```
The Gradle daemon successfully executed using JDK 17, resolving the startup and initialization script issues.
