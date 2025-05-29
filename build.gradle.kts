plugins {
	alias(libs.plugins.kotlin)
	alias(libs.plugins.kotlin.serialization)
	application
}

group = "org.testadirapa"
version = "1.0-SNAPSHOT"

dependencies {
	implementation(libs.bundles.ktor.client)
	implementation(libs.kotlinx.coroutines.core.jvm)
	testImplementation(kotlin("test"))
}

tasks.test {
	useJUnitPlatform()
}

kotlin {
	jvmToolchain(21)
}