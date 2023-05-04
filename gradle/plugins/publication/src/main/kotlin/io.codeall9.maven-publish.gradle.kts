import java.net.URI

@Suppress("JavaPluginLanguageLevel")
plugins {
    id("java-library")
    id("maven-publish")
    signing
}

(this as ExtensionAware).extensions.configure<JavaPluginExtension>("java") {
    withJavadocJar()
    withSourcesJar()
}

fun RepositoryHandler.githubPackage(
    owner: String,
    repository: String,
    githubUser: String? = properties["gpr.user"]?.toString() ?: System.getenv("GITHUB_ACTOR"),
    githubToken: String? = properties["gpr.key"]?.toString() ?: System.getenv("GITHUB_TOKEN"),
    init: MavenArtifactRepository.() -> Unit = {}
) = maven() {
    url = URI("https://maven.pkg.github.com/$owner/$repository")
    name = "$owner-$repository"
    credentials {
        username = requireNotNull(githubUser)
        password = requireNotNull(githubToken)
    }
    this.init()
}
