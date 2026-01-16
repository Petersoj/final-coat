import org.gradle.api.JavaVersion.VERSION_25
import org.jreleaser.model.Active.ALWAYS
import org.jreleaser.model.Active.NEVER

plugins {
    `java-library`
    `maven-publish`
    id("com.github.ben-manes.versions") version "0.53.0"
    id("org.jreleaser") version "1.22.0"
}

group = "net.jacobpeterson"
version = "1.2.0"

java {
    sourceCompatibility = VERSION_25
    targetCompatibility = VERSION_25
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
    implementation("com.google.errorprone:error_prone_core:2.46.0")
}

tasks.compileJava {
    options.compilerArgs.addAll(setOf(
            "--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
            "--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
            "--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
            "--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED"))
}

tasks.javadoc {
    isFailOnError = false
}

val jreleaserDeployDirectory = file("build/jreleaser-deploy/")

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name = artifactId
                description = "An Error Prone plugin check for `final` keyword usage on effectively final variables."
                val githubRepoPath = "Petersoj/final-coat"
                url = "https://github.com/${githubRepoPath}"
                inceptionYear = "2025"
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }
                developers {
                    developer {
                        id = "Petersoj"
                        name = "Jacob Peterson"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/${githubRepoPath}.git"
                    developerConnection = "scm:git:ssh://github.com:${githubRepoPath}.git"
                    url = pom.url
                }
            }
        }
    }
    repositories {
        maven {
            url = uri(jreleaserDeployDirectory)
        }
    }
}

jreleaser {
    signing {
        pgp {
            active = ALWAYS
            armored = true
        }
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active = ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository(jreleaserDeployDirectory.path)
                }
            }
        }
    }
    release {
        github {
            uploadAssets = NEVER
        }
    }
}
