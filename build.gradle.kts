
plugins {
    kotlin("js")
    id("maven-publish")
}

group = "dk.rheasoft"
version = "1.10-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlinVersion: String by project
println("kotlinVersion $kotlinVersion")
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

publishing {
    repositories {
        maven {
            name = "CS_Aware_Next"
            url = uri("https://maven.pkg.github.com/cs-aware-next/data-visualisation") // Github Package
            credentials {
                val githubUser: String by project
                val githubToken: String by project
                username = githubUser
                password = githubToken

            }
        }
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

            artifact(tasks.getByName<Zip>("kotlinSourcesJar"))

            //configurePom(project)
        }
    }
}