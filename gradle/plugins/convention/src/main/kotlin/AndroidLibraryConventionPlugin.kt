import io.codeall9.tictactoe.AndroidLibraryPlugin
import io.codeall9.tictactoe.kotlinOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

internal class AndroidLibraryConventionPlugin: AndroidLibraryPlugin() {
    override fun apply(target: Project) = with(target) {
        plugins {
            id("com.android.library")
            id("org.jetbrains.kotlin.android")
        }
        android {
            compileSdk = 33
            defaultConfig {
                minSdk = 23
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
                isCoreLibraryDesugaringEnabled = true
            }
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-Xexplicit-api=warning",
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                )
                jvmTarget = JavaVersion.VERSION_11.toString()
            }
            buildFeatures {
                buildConfig = false
                aidl = false
                renderScript = false
                resValues = false
                shaders = false
            }
            testOptions {
                unitTests {
                    all { it.useJUnitPlatform() }
                    isIncludeAndroidResources = false
                    isReturnDefaultValues = true
                }
            }
        }
        androidComponents {
            beforeVariants {
                it.enableAndroidTest = it.enableAndroidTest
                        && project.projectDir.resolve("src/androidTest").exists()
            }
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
            add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())

            val bom = platform(":project-bom")

            "implementation"(bom)
            "testImplementation"(bom)
            "androidTestImplementation"(bom)

            "testImplementation"(kotlin("test"))
            "testImplementation"(kotlin("test-junit5"))
            "androidTestImplementation"(kotlin("test"))

            "testImplementation"("org.junit.jupiter:junit-jupiter-api")
            "testImplementation"("org.junit.jupiter:junit-jupiter-params")
            "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine")

            "androidTestImplementation"("androidx.test:rules")
            "androidTestImplementation"("androidx.test.ext:junit")
            "androidTestRuntimeOnly"("androidx.test:runner")
        }
    }
}