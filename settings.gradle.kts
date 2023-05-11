rootProject.name = "TicTacToe"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
    includeBuild("gradle/plugins")
    includeBuild("gradle/platform")
}

dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
}

include(":app")
include(":core:engine")
include(":core:history")
include(":core:history-test")
include(":infra:history")
