// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

// Cache control configuration
tasks.withType<org.gradle.api.tasks.wrapper.Wrapper> {
    outputs.upToDateWhen { false }
}

// Force clean build
tasks.register("cleanAll") {
    dependsOn("clean")
    doLast {
        delete(project.buildDir)
        delete("${project.gradle.gradleUserHomeDir}/caches")
    }
}
