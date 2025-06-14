plugins {
	alias(libs.plugins.kotlin) apply true
	alias(libs.plugins.kotlin.serialization) apply true
	application
}

group = "org.testadirapa"
version = "1.0-SNAPSHOT"

dependencies {
	implementation(libs.bundles.ktor.client)
	implementation(libs.ktor.client.okhttp)
	implementation(libs.bundles.ktor.server)
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.telegram.bot)
	implementation(libs.caffeine)
	implementation(libs.krontab)
	testImplementation(kotlin("test"))
}

tasks.test {
	useJUnitPlatform()
}

kotlin {
	jvmToolchain(21)
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
		freeCompilerArgs.add("-Xcontext-receivers")
	}
}