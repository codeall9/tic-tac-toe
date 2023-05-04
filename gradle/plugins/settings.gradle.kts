dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("catalog") {
            from(files("../libs.versions.toml"))
        }
    }
    includeBuild("../platform")
}

include(":convention")
include(":publication")