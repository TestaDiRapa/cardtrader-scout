import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
}

kotlin {
    jvmToolchain(21)
    jvm {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
            freeCompilerArgs.add("-Xcontext-receivers")
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "card-trader-scout"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "card-trader-scout.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.bundles.ktor.client)
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.bundles.ktor.server)
                implementation(libs.telegram.bot)
                implementation(libs.caffeine)
                implementation(libs.krontab)
                implementation(libs.logback)
            }
        }

//        jsMain {
//            dependencies {
//                implementation(compose.html.core)
//                implementation(libs.telegram.bot.webapps)
//            }
//        }

        wasmJsMain {
            dependencies {
                implementation(libs.bundles.coil)
                implementation(libs.compose.icons)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtimeCompose)
            }
        }
    }
}

tasks.register<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("fatJar") {
    group = "build"
    archiveClassifier.set("all")
    manifest {
        attributes["Main-Class"] = "org.testadirapa.MainKtKt"
    }
    from(kotlin.jvm().compilations["main"].output)
    configurations = listOf(project.configurations.getByName("jvmRuntimeClasspath"))
}

tasks.named("build") {
    dependsOn("fatJar")
}


