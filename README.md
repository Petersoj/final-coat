# FinalCoat

[![Maven Central Version](https://img.shields.io/maven-central/v/net.jacobpeterson/final-coat)](https://central.sonatype.com/artifact/net.jacobpeterson/final-coat)
[![GitHub License](https://img.shields.io/github/license/Petersoj/final-coat)](https://github.com/Petersoj/final-coat/blob/main/LICENSE.txt)

An [Error Prone](https://errorprone.info/) plugin check for `final` keyword usage on effectively final variables.

This plugin is for enforcing the following rule in a codebase: any variable that can be marked with `final`, should be
marked with `final`.

## Installation

For `build.gradle.kts`:

```kotlin
dependencies {
    // ...
    errorprone("net.jacobpeterson:final-coat:1.0.0")
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
    errorprone "net.jacobpeterson:final-coat:1.0.0"
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
                        <version>1.0.0</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```
