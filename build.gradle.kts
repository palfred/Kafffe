plugins {
    kotlin("js")
}

group = "dk.rheasoft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-js"))
}

kotlin.target {
    browser {}

    compilations.all {
        kotlinOptions {
            metaInfo = true
            sourceMap = true
            sourceMapEmbedSources = "always"
        }
    }
}

// do not know how to get browserRun adn browser Webpack to work properly 2019.11.12
// kotlin.target.browser { }

    val assembleWeb by tasks.creating(Copy::class) {
        dependsOn("build")
        configurations["compileClasspath"].forEach {
            println(it.absolutePath)
            from(zipTree(it.absolutePath)) {
                includeEmptyDirs = false
                include("**/*")
                exclude("**/*.kotlin_metadata")
                // exclude("**/*.kjsm")
                exclude("/META-INF/**")
                include("/META-INF/resources/**")
            }
        }
        from(zipTree("build/libs/Kafffe-${version}.jar")) {
            include("**/*")
        }
        into("${buildDir}/web")
    }
