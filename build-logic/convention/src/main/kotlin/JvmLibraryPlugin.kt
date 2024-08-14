import com.reach.buildlogic.configureJvm
import com.reach.buildlogic.getPluginId
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(getPluginId("kotlinJvm"))
        }

        configureJvm()
    }
}
