import com.android.build.api.dsl.ApplicationExtension
import com.reach.buildlogic.configureAndroid
import com.reach.buildlogic.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class ApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<ApplicationExtension> {
                configureAndroid(this)
            }

            extensions.configure<ComposeCompilerGradlePluginExtension> {
                configureCompose(this)
            }

            dependencies {
                add("implementation", project(":ui-base:common"))
                add("implementation", project(":ui-core:design"))
                add("implementation", project(":ui-core:common"))
            }
        }
    }
}