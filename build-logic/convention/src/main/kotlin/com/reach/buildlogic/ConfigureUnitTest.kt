package com.reach.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

internal fun Project.addTestImplementation() {
    dependencies {
        add("testImplementation", kotlin("test"))
        testImplementation(libs, "junit")
        testImplementation(libs, "kotlinx-coroutines-test")
    }
}