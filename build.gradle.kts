plugins {
	alias(libs.plugins.kotlin)
	alias(libs.plugins.kotlin.serialization)
	application
}

group = "org.testadirapa"
version = "1.0-SNAPSHOT"

dependencies {
	implementation(libs.bundles.ktor.client)
	implementation(libs.bundles.ktor.server)
	implementation(libs.kotlinx.coroutines.core.jvm)
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