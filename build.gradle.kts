import org.gradle.api.JavaVersion.VERSION_25
import org.jreleaser.model.Active.ALWAYS
import org.jreleaser.model.Active.NEVER

plugins {
    `java-library`
    `maven-publish`
    id("com.github.ben-manes.versions") version "0.54.0"
    id("org.jreleaser") version "1.23.0"
}

group = "net.jacobpeterson"
version = "1.2.2"

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
    implementation("com.google.errorprone:error_prone_core:2.49.0")
}

tasks.withType(JavaCompile::class) {
    options.compilerArgs.addAll(setOf(
            "--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
            "--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
            "--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
            "--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED"))
}

tasks.withType(Javadoc::class) {
    isFailOnError = false
}

val jreleaserMavenRepositoryDirectory = layout.buildDirectory.dir("jreleaser-staging")

publishing {
    publications.create("jreleaser", MavenPublication::class) {
        from(components["java"])
        pom {
            name = artifactId
            description = "An Error Prone plugin check for `final` keyword usage on effectively final variables."
            url = "https://github.com/Petersoj/final-coat"
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
                connection = pom.url.map { "scm:git:$it.git" }
                developerConnection = connection
                url = pom.url
            }
        }
    }
    repositories.maven(uri(jreleaserMavenRepositoryDirectory))
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
                    stagingRepository(jreleaserMavenRepositoryDirectory.get())
                    skipPublicationCheck = true
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

tasks.jreleaserFullRelease {
    mustRunAfter(tasks.build)
}
