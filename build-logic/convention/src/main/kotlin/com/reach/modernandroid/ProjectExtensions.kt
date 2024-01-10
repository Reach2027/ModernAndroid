package com.reach.modernandroid

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandlerScope.implementation(libs: VersionCatalog, alias: String) {
    add("implementation", libs.findLibrary(alias).get())
}

fun DependencyHandlerScope.debugImplementation(libs: VersionCatalog, alias: String) {
    add("debugImplementation", libs.findLibrary(alias).get())
}

fun DependencyHandlerScope.testImplementation(libs: VersionCatalog, alias: String) {
    add("testImplementation", libs.findLibrary(alias).get())
}
