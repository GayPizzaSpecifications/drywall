plugins {
	`kotlin-dsl`
	kotlin("plugin.serialization") version "1.5.31"
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
