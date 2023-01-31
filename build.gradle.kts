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
    kotlin("js")
    id("maven-publish")
}

group = "dk.rheasoft"
version = "1.2-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlinVersion: String by project
println("kotlinVersion $kotlinVersion")
dependencies {
    implementation(kotlin("stdlib-js", kotlinVersion))
    implementation(npm("bootstrap", "5.2.3"))
    implementation(npm("@popperjs/core", "2.11.6"))
    // implementation(npm("jquery", "^3.6.2"))
}

kotlin {
    js(IR) {
        compilations.all {
            kotlinOptions {
                moduleKind = "umd"
            }
        }
        browser {
            webpackTask {
                output.libraryTarget = KotlinWebpackOutput.Target.UMD2
            }
        }
        binaries.executable()
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
//        maven {
//            name = "CS_Aware_Next"
//            url = uri("https://maven.pkg.github.com/cs-aware-next/data-visualisation") // Github Package
//            credentials {
//                val githubUser: String by project
//                val githubToken: String by project
//                username = githubUser
//                password = githubToken
//
//            }
//        }
        maven {
            name = "Kafffe"
            url = uri("https://maven.pkg.github.com/palfred/kafffe") // Github Package
            credentials {
                val githubUser: String? by project
                val githubToken: String? by project
                println("User: " + System.getenv("githubUser"))
                println("Token: " + System.getenv("githubToken"))
                username = githubUser ?:  System.getenv("githubUser")
                password = githubToken ?:  System.getenv("githubToken")

            }
        }
    }

    publications {
        create<MavenPublication>("kotlin") {
            from(components["kotlin"])

            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            artifact(tasks.getByName<Zip>("kotlinSourcesJar"))

            //configurePom(project)
        }
    }
}