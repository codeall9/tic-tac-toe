plugins {
    id("tictactoe.android.application")
    id("tictactoe.android.application.compose")
}

android {
    namespace = "io.codeall9.tictactoe"

    defaultConfig {
        applicationId = "io.codeall9.tictactoe"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//            signingConfig = signingConfigs.getByName("debug")
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:engine"))
    implementation(project(":infra:history"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.android.material)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material2)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.kt.compose)
    implementation(libs.koin.android)

    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.kotlinx.coroutines.test)
    androidTestRuntimeOnly(libs.androidx.test.espresso.core)
}