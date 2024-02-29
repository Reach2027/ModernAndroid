/*
 * Copyright 2024 Reach Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    alias(libs.plugins.reach.application)
}

android {
    namespace = "com.reach.modernandroid"

    defaultConfig {
        applicationId = "com.reach.modernandroid"
        versionCode = 1
        versionName = "0.0.1-alpha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.feature.more)
    implementation(projects.feature.me)

    implementation(projects.feature.uilearn)
    implementation(projects.feature.bingwallpaper)
    implementation(projects.feature.album)
    implementation(projects.feature.camerax)
    implementation(projects.feature.settings)

    implementation(projects.featureData.settings)

    implementation(libs.androidx.startup)

    implementation(libs.androidx.core)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.material3.adaptive)
    implementation(libs.compose.material3.adaptive.navigation)
}