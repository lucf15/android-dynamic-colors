plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.legacy.kapt) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.dokka) apply false
}

// AGP includes Kotlin. Pin its compiler to the version used by the Compose plugin.
buildscript {
    dependencies { classpath(libs.kotlin.gradle.plugin) }
}

val ktfmt =
    configurations.create("ktfmt") {
        attributes { attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.SHADOWED)) }
    }

dependencies {
    add(ktfmt.name, "org.jetbrains.kotlinx:ktfmt:0.65")
}

val kotlinSources =
    fileTree(rootDir) {
        include("**/*.kt", "**/*.kts")
        exclude("**/build/**", ".gradle/**")
    }

for ((taskName, checkOnly) in mapOf("formatKotlin" to false, "checkFormatting" to true)) {
    tasks.register<JavaExec>(taskName) {
        group = "verification"
        description = if (checkOnly) "Check Kotlin formatting" else "Format Kotlin source files"
        classpath = ktfmt
        mainClass.set("org.jetbrains.kotlinx.ktfmt.cli.Main")
        args("--kotlinlang-style")
        if (checkOnly) args("--dry-run", "--set-exit-if-changed")
        args(kotlinSources.files.sorted().map { it.absolutePath })
        inputs.files(kotlinSources)
    }
}

tasks.register("verify") {
    group = "verification"
    description = "Check formatting, library tests and lint, release libraries, and the sample"
    dependsOn("checkFormatting", ":sample:assembleDebug")
    for (module in
        listOf(
            "dynamic-colors",
            "dynamic-colors-compose",
            "dynamic-colors-views",
            "dynamic-colors-databinding",
        )) {
        dependsOn(":$module:testDebugUnitTest", ":$module:lintRelease", ":$module:assembleRelease")
    }
}
