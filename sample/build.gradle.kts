plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.legacy.kapt)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "io.github.lucf15.dynamiccolors.sample"
    compileSdk = 37
    defaultConfig {
        applicationId = "io.github.lucf15.dynamiccolors.sample"
        minSdk = 23
        targetSdk = 37
        versionCode = 1
        versionName = "0.1.0"
    }
    buildFeatures {
        compose = true
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin { jvmToolchain(17) }

composeCompiler {
    if (providers.gradleProperty("composeReports").isPresent) {
        reportsDestination = layout.buildDirectory.dir("compose-reports")
        metricsDestination = layout.buildDirectory.dir("compose-reports")
    }
}

dependencies {
    implementation(project(":dynamic-colors-compose"))
    implementation(project(":dynamic-colors-views"))
    implementation(project(":dynamic-colors-databinding"))
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.animation)
    implementation(libs.dynamic.animation)
    implementation(libs.compose.ui)
    implementation(libs.activity.compose)
    implementation(libs.compose.tooling.preview)
    debugImplementation(libs.compose.tooling)
}
