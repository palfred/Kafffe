
plugins {
    kotlin("js")
}

group = "dk.rheasoft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlinVersion: String by project
dependencies {
    implementation(kotlin("stdlib-js", kotlinVersion))
}

kotlin {
    js {
        browser {
        }
        binaries.executable()
    }
}
