plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.dokka)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "io.github.lucf15.dynamiccolors.compose"
    compileSdk = 37
    defaultConfig {
        minSdk = 23
        aarMetadata { minCompileSdk = 37 }
    }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions.unitTests.isIncludeAndroidResources = true
    lint {
        warningsAsErrors = true
        disable += listOf("GradleDependency", "AndroidGradlePluginVersion", "NewerVersionAvailable")
    }
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

dependencies {
    api(project(":dynamic-colors"))
    api(platform(libs.compose.bom))
    api(libs.compose.runtime)
    api(libs.compose.graphics)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    testImplementation(libs.material3)
    testImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.test.manifest)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.test.core)
}
