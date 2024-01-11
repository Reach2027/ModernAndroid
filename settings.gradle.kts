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

// UI common
include(":ui-common:album")
include(":ui-common:camerax")

// UI core
include(":ui-core:resource")
include(":ui-core:common")

// Data feature
include(":data-feature:album")

// Data common
include(":data-common")

// Data core
include(":data-core")

// Android core
include(":android-core:common")

// Jvm core
include(":jvm-core:common")
