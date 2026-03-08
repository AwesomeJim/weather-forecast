pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WeatherForecast"
include(":app")
include(":core:network")
include(":core:data")
include(":core:data-test")
include(":core:database")
include(":core:designsystem")
include(":core:model")
include(":feature:photos")
include(":feature:settings")
include(":feature:bookmarks")
include(":feature:forecast")
