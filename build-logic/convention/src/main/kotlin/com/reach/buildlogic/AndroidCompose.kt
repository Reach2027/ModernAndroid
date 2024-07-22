package com.reach.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

internal fun Project.configureCompose(
    composeCompilerGradlePluginExtension: ComposeCompilerGradlePluginExtension,
) {
    with(composeCompilerGradlePluginExtension) {
        enableStrongSkippingMode.set(true)
        enableNonSkippingGroupOptimization.set(true)
        targetKotlinPlatforms.set(setOf(KotlinPlatformType.androidJvm))
    }

    dependencies {
        val bom = libs.findLibrary("compose-bom").get()
        add("implementation", platform(bom))
        implementation(libs, "compose.animation")
        implementation(libs, "compose.material3")
        implementation(libs, "compose.material3.window.size")
        implementation(libs, "compose.material.icon")
        implementation(libs, "compose.material.icon.extended")
        implementation(libs, "compose.foundation")
        implementation(libs, "compose.foundation.layout")
        implementation(libs, "compose.ui")
        implementation(libs, "compose.ui.util")
        implementation(libs, "compose.ui.graphics")
        implementation(libs, "compose.ui.tooling.preview")
        debugImplementation(libs, "compose.ui.tooling")
        debugImplementation(libs, "compose.ui.test.manifest")
        implementation(libs, "compose.runtime")

        implementation(libs, "androidx.lifecycle.runtime.compose")
        implementation(libs, "androidx.lifecycle.viewmodel.compose")
        implementation(libs, "androidx.lifecycle.viewmodel.savedstate")
        implementation(libs, "androidx.lifecycle.common")

        implementation(libs, "androidx.navigation")

        implementation(libs, "koin.androidx.compose")
        implementation(libs, "koin.androidx.compose.navigation")

        implementation(libs, "coil")
    }
}