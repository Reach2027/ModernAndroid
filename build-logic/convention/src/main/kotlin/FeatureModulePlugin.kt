import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.reach.buildlogic.configureAndroid
import com.reach.buildlogic.configureCompose
import com.reach.buildlogic.disableUnnecessaryAndroidTests
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class FeatureModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<LibraryExtension> {
                configureAndroid(this)
            }

            extensions.configure<ComposeCompilerGradlePluginExtension> {
                configureCompose(this)
            }

            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }

            dependencies {
                add("implementation", project(":ui-base:common"))
                add("implementation", project(":ui-core:design"))
                add("implementation", project(":ui-core:common"))

                add("implementation", project(":data-base:common"))
            }
        }
    }
}