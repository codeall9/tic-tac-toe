package io.codeall9.tictactoe

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

abstract class PrintBundlePathTask: DefaultTask() {
    @get:InputFile
    abstract val bundleFile: RegularFileProperty

    @get:Input
    abstract val variantName: Property<String>

    @TaskAction
    fun taskAction() {
        println("Android bundle file path: ${bundleFile.get().asFile.absolutePath}")
    }
}