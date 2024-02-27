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

// UI feature
include(":ui-feature:me")
include(":ui-feature:more")
include(":ui-feature:uilearn")
include(":ui-feature:album")
include(":ui-feature:camerax")
include(":ui-feature:bingwallpaper")

// UI base
include(":ui-base:resource")
include(":ui-base:common")

// Core ui
include(":core-ui:common")

// Data feature
include(":data-feature:album")
include(":data-feature:bingwallpaper")

// Data base
include(":data-base:database")
include(":data-base:network")
include(":data-base:datastore")

// Core android
include(":core-android:common")

// Core jvm
include(":core-jvm:common")
