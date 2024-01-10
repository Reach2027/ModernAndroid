import com.android.build.api.dsl.ApplicationExtension
import com.reach.modernandroid.configureCompose
import com.reach.modernandroid.configureComposeLibraries
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")

            val extension = extensions.getByType<ApplicationExtension>()
            configureCompose(extension)
            configureComposeLibraries()
        }
    }
}