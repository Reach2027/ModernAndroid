import com.reach.modernandroid.configureComposeFeature
import org.gradle.api.Plugin
import org.gradle.api.Project

class UiFeatureModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("reach.android.library")
            }

            configureComposeFeature()
        }
    }
}