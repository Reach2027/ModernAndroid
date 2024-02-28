package com.reach.modernandroid

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs
                .findVersion("composeCompiler")
                .get()
                .toString()
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

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + buildComposeMetricsParameters()
        }
    }
}

private fun Project.buildComposeMetricsParameters(): List<String> {
    val metricParameters = mutableListOf<String>()
    val enableMetricsProvider = project.providers.gradleProperty("enableComposeCompilerMetrics")
    val relativePath = projectDir.relativeTo(rootDir)
    val buildDir = layout.buildDirectory.get().asFile
    val enableMetrics = (enableMetricsProvider.orNull == "true")
    if (enableMetrics) {
        val metricsFolder = buildDir.resolve("compose-metrics").resolve(relativePath)
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath,
        )
    }

    val enableReportsProvider = project.providers.gradleProperty("enableComposeCompilerReports")
    val enableReports = (enableReportsProvider.orNull == "true")
    if (enableReports) {
        val reportsFolder = buildDir.resolve("compose-reports").resolve(relativePath)
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath,
        )
    }
    return metricParameters.toList()
}

internal fun Project.configureComposeFeature() {
    dependencies {
        add("implementation", project(":base-ui:common"))
        add("implementation", project(":core-ui:resource"))
        add("implementation", project(":core-ui:common"))
    }
}