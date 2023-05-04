import io.codeall9.tictactoe.AndroidApplicationPlugin
import io.codeall9.tictactoe.kotlinOptions
import io.codeall9.tictactoe.setupGradleManagedDevices
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal class AndroidApplicationConventionPlugin : AndroidApplicationPlugin() {
    override fun apply(target: Project) = with(target) {
        plugins {
            id("com.android.application")
            id("org.jetbrains.kotlin.android")
        }
        android {
            compileSdk = 33
            defaultConfig {
                minSdk = 23
                targetSdk = 33
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
                isCoreLibraryDesugaringEnabled = true
            }
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
                jvmTarget = JavaVersion.VERSION_11.toString()
            }
            buildFeatures {
                aidl = false
                renderScript = false
                resValues = false
                shaders = false
            }
            testOptions {
                unitTests.all {
                    it.useJUnitPlatform()
                }
                setupGradleManagedDevices()
            }
        }
        androidComponents {
            onVariants { variant ->
                registerPrintTestApkTask(variant)
            }
        }
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
            add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())

            val bom = platform(":project-bom")

            "implementation"(bom)
            "testImplementation"(bom)
            "androidTestImplementation"(bom)

            "testImplementation"("org.junit.jupiter:junit-jupiter-api")
            "testImplementation"("org.junit.jupiter:junit-jupiter-params")
            "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine")

            "androidTestImplementation"("androidx.test:rules")
            "androidTestImplementation"("androidx.test.ext:junit")
            "androidTestRuntimeOnly"("androidx.test:runner")
        }
    }
}