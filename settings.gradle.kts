pluginManagement {
	repositories {
		google()
		mavenCentral()
		gradlePluginPortal()
	}
}

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage")
	repositories {
		google()
		mavenCentral()
		maven { url = uri("https://jitpack.io") }
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "cardtrader-scout"