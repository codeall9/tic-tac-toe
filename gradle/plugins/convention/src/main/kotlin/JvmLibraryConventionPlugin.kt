import io.codeall9.tictactoe.KotlinPlugin
import io.codeall9.tictactoe.java
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal class JvmLibraryConventionPlugin : KotlinPlugin() {
    override fun apply(target: Project): Unit = with(target) {
        plugins {
            id("java-library")
            id("org.jetbrains.kotlin.jvm")
        }
        java {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        dependencies {
            val projectBom = platform(":project-bom")

            "implementation"(projectBom)
            "testImplementation"(projectBom)
        }
        tasks.named("compileKotlin", KotlinCompile::class.java) {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_11.toString()
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-Xexplicit-api=warning",
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                )
            }
        }
        tasks.named("compileTestKotlin", KotlinCompile::class.java) {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_11.toString()
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                )
            }
        }
    }
}