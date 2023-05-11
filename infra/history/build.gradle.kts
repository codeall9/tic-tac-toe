plugins {
    id("tictactoe.android.library")
    id("tictactoe.android.room")
}

android {
    namespace = "io.codeall9.tictactoe.infra.history"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:engine"))
    api(project(":core:history"))

    testImplementation(project(":core:history-test"))
    androidTestImplementation(project(":core:history-test"))

    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestRuntimeOnly(libs.androidx.test.espresso.core)
}