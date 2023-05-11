plugins {
    id("tictactoe.jvm.library")
    id("tictactoe.jvm.library.junit5")
}

group = "io.codeall9.tictactoe.core"
version = "1.0.0-RC"

dependencies {
    implementation(project(":core:engine"))
    implementation(project(":core:history"))
}