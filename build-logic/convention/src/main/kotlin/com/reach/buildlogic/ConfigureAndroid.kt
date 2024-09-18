package com.reach.buildlogic;

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        if (this is ApplicationExtension) {
            defaultConfig.targetSdk = 35
        } else {
            lint.targetSdk = 35
        }

        compileSdk = 35

        defaultConfig {
            minSdk = 21
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            isCoreLibraryDesugaringEnabled = true
        }
    }

    configureKotlin()

    addTestImplementation()

    if (this is LibraryExtension) {
        extensions.configure<LibraryAndroidComponentsExtension> {
            disableUnnecessaryAndroidTests(this@configureAndroid)
        }
    }

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android-desugar-jdk").get())

        implementation(libs, "kotlinx-coroutines-android")

        implementation(libs, "koin-core")
        implementation(libs, "koin-core-coroutines")
        implementation(libs, "koin-android")

        implementation(libs, "androidx-collection")
    }
}
