// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    alias(this.libs.plugins.android.application) apply false
    alias(this.libs.plugins.android.library) apply false
    alias(this.libs.plugins.android.test) apply false
    alias(this.libs.plugins.kotlin.jvm) apply false
    alias(this.libs.plugins.kotlin.android) apply false
    alias(this.libs.plugins.ksp) apply false
    alias(this.libs.plugins.secrets) apply false
}

allprojects {
    repositories {
        /*
         * google should be first in the repository list because some of the play services
         * transitive dependencies was removed from jcenter, thus breaking gradle dependency resolution
         */
        google()
        mavenCentral()
        maven {
            name = "sonatype"
            url = uri("https://oss.sonatype.org/content/repositories/releases/")
        }
        maven {
            name = "jitpack"
            url = uri("https://jitpack.io")
        }
    }
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}