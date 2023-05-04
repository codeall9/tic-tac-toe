plugins {
    id("java-platform")
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform(("org.jetbrains.kotlin:kotlin-bom:1.8.20")))
    api(platform(("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.7.0-RC")))
    api(platform(("androidx.compose:compose-bom:2023.04.01")))
    api(platform(("com.google.firebase:firebase-bom:32.0.0")))
    api(platform(("com.fasterxml.jackson:jackson-bom:2.15.0")))
    api(platform(("org.junit:junit-bom:5.9.3")))
    api(platform(("org.mockito:mockito-bom:5.3.1")))
    constraints {

        api("androidx.test:rules:1.5.0")
        api("androidx.test:runner:1.5.1")
        api("androidx.test.ext:junit:1.1.4")
    }
}
