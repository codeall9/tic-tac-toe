import com.google.devtools.ksp.gradle.KspExtension

private val roomVersion: String by extra { "2.5.1" }

plugins {
    id("com.google.devtools.ksp")
}

extensions.configure<KspExtension> {
    // The schemas directory contains a schema file for each version of the Room database.
    // This is required to enable Room auto migrations.
    // see https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
//    arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
    arg("room.schemaLocation", "$projectDir/build/schemas")
}

dependencies {
    "implementation"("androidx.room:room-runtime:$roomVersion")
    "implementation"("androidx.room:room-ktx:$roomVersion")
    "ksp"("androidx.room:room-compiler:$roomVersion")

    "androidTestImplementation"("androidx.room:room-testing:$roomVersion")
}


/**
 * https://issuetracker.google.com/issues/132245929
 * [Export schemas](https://developer.android.com/training/data-storage/room/migrating-db-versions#export-schemas)
 */
class RoomSchemaArgProvider(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File,
) : CommandLineArgumentProvider {
    override fun asArguments() = listOf("room.schemaLocation=${schemaDir.path}")
}