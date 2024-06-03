import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

// Disable KOtlin JS requirement of "gradlew kotlinUpgradeYarnLock" when dependencies has changed:
rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin::class.java) {
    rootProject.the<YarnRootExtension>().yarnLockMismatchReport = YarnLockMismatchReport.NONE
    rootProject.the<YarnRootExtension>().reportNewYarnLock = false // true
    rootProject.the<YarnRootExtension>().yarnLockAutoReplace = true // true
}

plugins {
    kotlin("multiplatform")
    id("maven-publish")
    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "dk.rheasoft"
val workingOnVersion = "1.7"

// Setup SNAPSHOT version if not on master branch.
val githubBranch: String? by project
println("Gradle working on branch : $githubBranch")
version = workingOnVersion + if ("master".equals(githubBranch)) "" else "-SNAPSHOT"
println("Gradle working on version: ${project.version}")

repositories {
    mavenCentral()
}

val kotlinVersion: String by project
println("kotlinVersion: $kotlinVersion")

kotlin {
    js(IR) {
        browser {
            webpackTask {
                output.libraryTarget = KotlinWebpackOutput.Target.UMD2
            }
            testTask {
                enabled = true // do not know how to load "mxclient.js" so mxshape and others are defined.
                useKarma {
                    useChromeHeadless()

                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js", kotlinVersion))
                implementation(npm("bootstrap", "5.2.3"))
                implementation(npm("@popperjs/core", "2.11.6"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-js")
            }
        }
    }
}

tasks.register("checkForSnapshots") {
    group = "verification"
    description = "Check whether there are any SNAPSHOT dependencies or this project is a SNAPSHOT."
    doLast {
        val allViolations =
            project.configurations
                .filter { it.isCanBeResolved }
                .flatMap { configuration ->
                    configuration.resolvedConfiguration.resolvedArtifacts
                        .map { it.moduleVersion.id }
                        .filter { it.version.endsWith("-SNAPSHOT") }
                }
                .map { "$it" }
                .toSet()
        if (allViolations.isNotEmpty()) {
            val violationsString = allViolations.joinToString("\n")
            error("Snapshot dependencies found for this project version ${project.version}:\n$violationsString")
        }
        if (project.version.toString().endsWith("-SNAPSHOT")) {
            error("The project is self a SNAPSHOT:\n${project.version}")
        }
    }
}

publishing {
    repositories {
        maven {
            name = "Kafffe"
            url = uri("https://maven.pkg.github.com/palfred/kafffe") // Github Package
            credentials {
                val githubUser: String by project
                val githubToken: String by project
                username = githubUser
                password = githubToken

            }
        }
    }

    publications {
        create<MavenPublication>("kotlin") {
            from(components["kotlin"])

            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

        }
    }
}
