import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class GitVersionTask: DefaultTask() {

    @get:OutputFile
    abstract val gitVersionOutputFile: RegularFileProperty

    @TaskAction
    fun taskAction() {
         val firstProcess = ProcessBuilder("git","rev-parse --short HEAD").start()
         val error = firstProcess.errorStream.readBytes().decodeToString()
         if (error.isNotBlank()) {
              System.err.println("GitVersionTask error : $error")
         }
         val gitVersion = firstProcess.inputStream.readBytes().decodeToString()

        gitVersionOutputFile.get().asFile.writeText(gitVersion)
    }
}

/*
val gitVersionProvider = tasks.register<GitVersionTask>("gitVersionProvider") {
    File(project.buildDir, "intermediates/gitVersionProvider/output").also {
        it.parentFile.mkdirs()
        gitVersionOutputFile.set(it)
    }
    outputs.upToDateWhen { false }
}*/
