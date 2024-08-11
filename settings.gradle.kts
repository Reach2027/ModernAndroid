pluginManagement {
    includeBuild("build-logic")
    repositories {
        // gradle https\://mirrors.tencent.com/gradle/gradle-8.9-all.zip
        // google and mavenCentral
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
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
include(":feature:settings")

// ui core
include(":ui-core:design")
include(":ui-core:common")

// ui base
include(":ui-base:common")

// feature data
include(":feature-data:album")
include(":feature-data:bingwallpaper")
include(":feature-data:settings")

// data core
include(":data-core:database")
include(":data-core:network")
include(":data-core:datastore")

// data base
include(":data-base:common")
