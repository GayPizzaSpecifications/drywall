plugins {
	`kotlin-dsl`
	kotlin("plugin.serialization") version "1.5.31"
	`maven-publish`
}

group = "gay.pizza.foundation"
version = "0.1.0-SNAPSHOT"

repositories {
	gradlePluginPortal()
}

dependencies {
	implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.3.3")
	testImplementation(kotlin("test"))
}

tasks.test {
	useJUnitPlatform()
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.compileKotlin {
	kotlinOptions.jvmTarget = "1.8"
}

gradlePlugin {
	plugins {
		create(name) {
			id = "${group}.${name}"
			implementationClass = "${id}.ResourcesGeneratorPlugin"
		}
	}
}

publishing {
	repositories {
		mavenLocal()

		val githubPackagesToken = System.getenv("GITHUB_TOKEN")
			?: project.findProperty("github.token") as String?
		val gitlabPackagesToken = System.getenv("GITLAB_TOKEN")
			?: project.findProperty("gitlab.com.accessToken") as String?

		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/gaypizzaspecifications/drywall")
			credentials {
				username = project.findProperty("github.username") as String? ?: "gaypizzaspecifications"
				password = githubPackagesToken
			}
		}

		maven {
			name = "GitLab"
			url = uri("https://gitlab.com/api/v4/projects/47347654/packages/maven")
			credentials(HttpHeaderCredentials::class.java) {
				name = "Private-Token"
				value = gitlabPackagesToken
			}
			authentication {
				create<HttpHeaderAuthentication>("header")
			}
		}
	}
}
