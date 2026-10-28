import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("io.papermc.paperweight.patcher") version "2.0.0-beta.24"
}

paperweight {
    upstreams.paper {
        ref = providers.gradleProperty("paperRef")

        patchFile {
            path = "paper-server/build.gradle.kts"
            outputFile = file("mccreativelab-server/build.gradle.kts")
            patchFile = file("mccreativelab-server/build.gradle.kts.patch")
        }
        patchFile {
            path = "paper-api/build.gradle.kts"
            outputFile = file("mccreativelab-api/build.gradle.kts")
            patchFile = file("mccreativelab-api/build.gradle.kts.patch")
        }
        patchDir("paperCheckstyle") {
            upstreamPath = "paper-checkstyle"
            excludes = setOf("build", ".gradle")
            patchesDir = file("build-data/paper-checkstyle-patches")
            outputDir = file("paper-checkstyle")
        }
        patchDir("paperCheckstyleConfig") {
            upstreamPath = ".checkstyle"
            patchesDir = file("build-data/checkstyle-config-patches")
            outputDir = file(".checkstyle")
        }
        patchDir("paperApi") {
            upstreamPath = "paper-api"
            excludes = setOf("build.gradle.kts")
            patchesDir = file("mccreativelab-api/paper-patches")
            outputDir = file("paper-api")
        }
    }
}

// Rebuild tasks stage patch directories in this repository's shared Git index.
// Serialize only these writers; compilation and tests can still run in parallel.
val patchIndexWriters = listOf(
    ":rebuildPaperCheckstyleConfigFilePatches", ":rebuildPaperCheckstyleConfigFeaturePatches",
    ":rebuildPaperCheckstyleFilePatches", ":rebuildPaperCheckstyleFeaturePatches",
    ":mccreativelab-server:rebuildPaperServerFilePatches", ":mccreativelab-server:rebuildPaperServerFeaturePatches",
    ":rebuildPaperApiFilePatches", ":rebuildPaperApiFeaturePatches",
    ":mccreativelab-server:rebuildMinecraftResourcePatches", ":mccreativelab-server:rebuildMinecraftSourcePatches",
    ":mccreativelab-server:rebuildMinecraftFeaturePatches"
)
allprojects {
    tasks.configureEach {
        val writerIndex = patchIndexWriters.indexOf(path)
        if (writerIndex > 0) mustRunAfter(patchIndexWriters.take(writerIndex))
    }
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

subprojects {
    // A combined apply/rebuild invocation must finish the shared upstream checkout
    // before any subproject task reads it (paperweight-core does not model this).
    tasks.configureEach {
        mustRunAfter(":checkoutPaperRepo", ":applyPaperCheckstyleFilePatches", ":applyPaperCheckstyleFeaturePatches",
            ":applyPaperCheckstyleConfigFilePatches", ":applyPaperCheckstyleConfigFeaturePatches")
    }
    // These paperweight inputs come from the checkout managed by the root patcher.
    tasks.matching {
        it.name in setOf("collectPaperATsFromPatches", "filterPaperServerFromPaper", "importPaperLibraryFiles")
    }.configureEach {
        dependsOn(":checkoutPaperRepo")
    }
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
        maven("https://repo.verdox.de/snapshots")
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 25
        options.isFork = true
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test> {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }

    extensions.configure<PublishingExtension> {
        repositories {
            maven {
                name = "verdox"
                url = uri("https://repo.verdox.de/snapshots")
                credentials {
                    username = (findProperty("reposilite.verdox.user") ?: System.getenv("REPO_USER")).toString()
                    password = (findProperty("reposilite.verdox.key") ?: System.getenv("REPO_PASSWORD")).toString()
                }
            }
        }
    }
}
