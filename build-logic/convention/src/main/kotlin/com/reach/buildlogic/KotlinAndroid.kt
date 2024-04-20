package com.reach.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        if (this is ApplicationExtension) {
            defaultConfig.targetSdk = 34
        } else {
            lint.targetSdk = 34
        }

        compileSdk = 34

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

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android.desugar.jdk").get())

        implementation(libs, "kotlinx-coroutines-android")

        implementation(libs, "koin-android")

        implementation(libs, "androidx-collection")
    }
}

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

        implementation(libs, "androidx-collection")
    }
}

/**
 * Configure base Kotlin options
 */
private fun Project.configureKotlin() {
    // Use withType to workaround https://youtrack.jetbrains.com/issue/KT-55947
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
            // Treat all Kotlin warnings as errors (disabled by default)
            // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
            val warningsAsErrors: String? by project
            allWarningsAsErrors = warningsAsErrors.toBoolean()
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlin.RequiresOptIn",
                // Enable experimental coroutines APIs, including Flow
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )
        }
    }
}

private fun Project.addTestImplementation() {
    dependencies {
        add("testImplementation", kotlin("test"))
        testImplementation(libs, "junit")
        testImplementation(libs, "kotlinx-coroutines-test")
    }
}
