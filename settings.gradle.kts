pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "mccreativelab"

include("mccreativelab-api")
include("mccreativelab-server")

include("paper-checkstyle")
file("paper-checkstyle").mkdirs()
