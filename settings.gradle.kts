pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/") // google and mavenCentral
        maven("https://mirrors.tencent.com/nexus/repository/gradle-plugins/")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/") // google and mavenCentral
        google()
        mavenCentral()
    }
}

rootProject.name = "ModernAndroid"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")

// feature
include(":feature:me")
include(":feature:more")
include(":feature:uilearn")
include(":feature:album")
include(":feature:camerax")
include(":feature:bingwallpaper")

// core ui
include(":core-ui:resource")
include(":core-ui:common")

// base ui
include(":base-ui:common")

// feature data
include(":feature-data:album")
include(":feature-data:bingwallpaper")
include(":feature-data:setting")

// core data
include(":core-data:database")
include(":core-data:network")
include(":core-data:datastore")

// base android
include(":base-android:common")

// base jvm
include(":base-jvm:common")
