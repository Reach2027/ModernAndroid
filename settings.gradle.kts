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
include(":ui-feature:lottie")
include(":ui-feature:skeletonloader")

// UI common
include(":ui-common:album")
include(":ui-common:camerax")

// UI base
include(":ui-base:resource")
include(":ui-base:common")

// Data feature
include(":data-feature:album")

// Data common
include(":data-common")

// Core android
include(":core-android:common")

// Core jvm
include(":core-jvm:common")
include(":core-android:database")
