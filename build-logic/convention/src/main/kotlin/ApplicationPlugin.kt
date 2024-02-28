import com.android.build.api.dsl.ApplicationExtension
import com.reach.buildlogic.configureAndroid
import com.reach.buildlogic.configureCompose
import com.reach.buildlogic.configureComposeFeature
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureAndroid(this)
                configureCompose(this)
            }
            configureComposeFeature()
        }
    }
}