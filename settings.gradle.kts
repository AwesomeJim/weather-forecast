pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
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
include(":core:designsysytem")
