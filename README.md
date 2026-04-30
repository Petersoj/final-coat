# FinalCoat

[![Maven Central](https://img.shields.io/badge/Maven_Central-1.2.3-blue?logo=apachemaven)](https://central.sonatype.com/artifact/net.jacobpeterson/final-coat)
[![Java Version](https://img.shields.io/badge/Java_Version-25-orange?logo=java)](https://openjdk.org/projects/jdk/25)
[![GitHub License](https://img.shields.io/github/license/Petersoj/final-coat)](https://github.com/Petersoj/final-coat/blob/main/LICENSE.txt)

An [Error Prone](https://errorprone.info/) plugin check for `final` keyword usage on effectively final variables.

This plugin is for enforcing the following rule in a codebase: any variable that can be marked with `final`, should be
marked with `final`.

## Installation

For `build.gradle.kts`:

```kotlin
dependencies {
    // ...
    errorprone("net.jacobpeterson:final-coat:1.2.3")
}
// ...
tasks.withType(JavaCompile::class) {
    options.errorprone {
        check("FinalCoat", WARN)
    }
}
```

For `build.gradle`:

```groovy
dependencies {
    // ...
    errorprone "net.jacobpeterson:final-coat:1.2.3"
}
// ...
tasks.withType(JavaCompile) {
    options.errorprone {
        check("FinalCoat", WARN)
    }
}
```

For `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <!-- ... -->
            <configuration>
                <!-- ... -->
                <compilerArgs>
                    <!-- ... -->
                    <arg>-Xep:FinalCoat:WARN</arg>
                </compilerArgs>
                <annotationProcessorPaths>
                    <!-- ... -->
                    <path>
                        <groupId>net.jacobpeterson</groupId>
                        <artifactId>final-coat</artifactId>
                        <version>1.2.3</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```
