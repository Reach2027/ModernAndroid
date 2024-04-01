import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.protobuf) apply false

    alias(libs.plugins.detekt)
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        detektPlugins("com.twitter.compose.rules:detekt:0.0.26")
    }

    val jvmVersion = JavaVersion.VERSION_17.toString()

    detekt {
        buildUponDefaultConfig = true
        allRules = true
        config.setFrom(files("$rootDir/detekt/detekt.yml"))
    }

    tasks.withType<Detekt>().configureEach {
        jvmTarget = jvmVersion
        reports {
            html.required = true
            xml.required = false
            txt.required = false
            sarif.required = false
            md.required = false
        }
        basePath = rootDir.absolutePath
    }

    tasks.withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = jvmVersion
    }
}
