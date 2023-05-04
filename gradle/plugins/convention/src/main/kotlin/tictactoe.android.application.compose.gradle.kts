import io.codeall9.tictactoe.buildComposeMetricsParameters
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
private val composeCompilerVersion by lazy(LazyThreadSafetyMode.NONE) {
    libs.findVersion("androidxComposeCompiler")
        .get()
        .toString()
}

plugins {
    id("com.android.application")
}

android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeCompilerVersion
    }
}

dependencies {
    val bom = platform(":project-bom")
    "implementation"(bom)
    "androidTestImplementation"(bom)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + buildComposeMetricsParameters()
    }
}