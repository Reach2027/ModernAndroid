package com.reach.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

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
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            apiVersion.set(KotlinVersion.KOTLIN_2_0)
            languageVersion.set(KotlinVersion.KOTLIN_2_0)
            jvmTarget.set(JvmTarget.JVM_17)
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
