import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl`
}

group = "com.reach.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        apiVersion.set(KotlinVersion.KOTLIN_2_0)
        languageVersion.set(KotlinVersion.KOTLIN_2_0)
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.compose.compiler.plugin)
}

gradlePlugin {
    plugins {
        register("jvmLibrary") {
            id = "reach.jvm.library"
            implementationClass = "JvmLibraryPlugin"
        }

        register("androidLibrary") {
            id = "reach.android.library"
            implementationClass = "AndroidLibraryPlugin"
        }
        register("composeLibrary") {
            id = "reach.compose.library"
            implementationClass = "ComposeLibraryPlugin"
        }

        register("featureModule") {
            id = "reach.feature.module"
            implementationClass = "FeatureModulePlugin"
        }

        register("application") {
            id = "reach.application"
            implementationClass = "ApplicationPlugin"
        }

    }
}
