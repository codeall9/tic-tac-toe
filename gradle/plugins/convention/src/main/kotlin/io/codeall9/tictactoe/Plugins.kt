package io.codeall9.tictactoe

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.HasAndroidTest
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions


internal fun Project.java(block: JavaPluginExtension.() -> Unit) {
    (this as ExtensionAware).extensions.configure("java", block)
}

internal fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}

internal abstract class KotlinPlugin : Plugin<Project> {

    fun Project.plugins(block: PluginManager.() -> Unit) {
        pluginManager.apply(block)
    }

    fun PluginManager.id(pluginId: String) {
        apply(pluginId)
    }
}

internal abstract class AndroidApplicationPlugin : KotlinPlugin() {

    fun Project.android(block: ApplicationExtension.() -> Unit) {
        extensions.configure(block)
    }

    fun Project.androidComponents(block: ApplicationAndroidComponentsExtension.() -> Unit) {
        extensions.configure(block)
    }

    fun Project.registerPrintTestApkTask(variant: Variant) {
        if (variant !is HasAndroidTest) return

        val loader = variant.artifacts.getBuiltArtifactsLoader()
        val artifact = variant.androidTest?.artifacts?.get(SingleArtifact.APK)
        val javaSources = variant.androidTest?.sources?.java?.all
        val kotlinSources = variant.androidTest?.sources?.kotlin?.all

        val testSources = if (javaSources != null && kotlinSources != null) {
            javaSources.zip(kotlinSources) { javaDirs, kotlinDirs ->
                javaDirs + kotlinDirs
            }
        } else javaSources ?: kotlinSources

        if (artifact == null || testSources == null) return

        tasks.register("${variant.name}PrintTestApk", PrintApkPathTask::class.java) {
            apkFolder.set(artifact)
            builtArtifactsLoader.set(loader)
            variantName.set(variant.name)
            sources.set(testSources)
        }
    }

    fun Project.registerPrintBundleTask(variant: Variant) {
        tasks.register("${variant.name}PrintBundle", PrintBundlePathTask::class.java) {
            variantName.set(variant.name)
            bundleFile.set(variant.artifacts.get(SingleArtifact.BUNDLE))
        }
    }
}

internal abstract class AndroidLibraryPlugin : KotlinPlugin() {

    fun Project.android(block: LibraryExtension.() -> Unit) {
        extensions.configure(block)
    }

    fun Project.androidComponents(block: LibraryAndroidComponentsExtension.() -> Unit) {
        extensions.configure(block)
    }
}