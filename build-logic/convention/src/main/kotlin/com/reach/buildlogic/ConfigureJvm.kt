package com.reach.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    configureKotlin()

    addTestImplementation()

    dependencies {
        implementation(libs, "kotlinx-coroutines-core")

        implementation(libs, "koin-core")
        implementation(libs, "koin-core-coroutines")

        implementation(libs, "androidx-collection")
    }
}

