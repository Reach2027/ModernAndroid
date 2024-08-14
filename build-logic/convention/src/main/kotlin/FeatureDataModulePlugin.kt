import com.android.build.gradle.LibraryExtension
import com.reach.buildlogic.configureAndroid
import com.reach.buildlogic.getPluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class FeatureDataModulePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(getPluginId("androidLibrary"))
            apply(getPluginId("kotlinAndroid"))
        }

        extensions.configure<LibraryExtension> {
            configureAndroid(this)
        }

        dependencies {
            add("implementation", project(":data-base:common"))
        }
    }
}