/**
 * Configures the [sourceSets][org.gradle.api.tasks.SourceSetContainer] extension.
 */
fun org.gradle.api.Project.`sourceSets`(configure: Action<org.gradle.api.tasks.SourceSetContainer>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("sourceSets", configure)

fun Project.setupTestLoggerSystem() {
    dependencies {
        add("testApi", "io.github.karlatemp.mxlib:mxlib-logger-slf4j")
        add("testApi", "io.github.karlatemp.mxlib:mxlib-logger")
        add("testApi", "io.github.karlatemp.mxlib:mxlib-common")
        add("testApi", "io.github.karlatemp.mxlib:mxlib-api")
    }
}

fun Project.configureFlattenSourceSets() {
    sourceSets {
        findByName("main")?.apply {
            resources.setSrcDirs(listOf(projectDir.resolve("resources")))
            java.setSrcDirs(listOf(projectDir.resolve("src")))
        }
        findByName("test")?.apply {
            resources.setSrcDirs(listOf(projectDir.resolve("resources")))
            java.setSrcDirs(listOf(projectDir.resolve("test")))
        }
    }
}

allprojects {
    afterEvaluate {
        configureFlattenSourceSets()
        setupTestLoggerSystem()
    }
}
