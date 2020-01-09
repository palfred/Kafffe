rootProject.name = "Kafffe"

pluginManagement {
    val kotlinVersion: String by settings
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.js" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.multiplatform" -> useVersion(kotlinVersion)
            }
        }
    }
}