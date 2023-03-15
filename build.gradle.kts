import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetPreset
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    val kotlinVersion = "1.8.10"
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("maven-publish")
}

group = "github.otisgoodman"
version = "1.0"
archivesName.set("Pocket-Kt")


val ktorVersion = "2.2.3"
val kotlinSerializationVersion = "1.4.1"
val kotlinCoroutinesVersion = "1.6.4"
val kotlinTimeVersion = "0.4.0"

val nativeCIOMainSets: MutableList<KotlinSourceSet> = mutableListOf()
val nativeCIOTestSets: MutableList<KotlinSourceSet> = mutableListOf()
val nativeWinHTTPMainSets: MutableList<KotlinSourceSet> = mutableListOf()
val nativeWinHTTPTestSets: MutableList<KotlinSourceSet> = mutableListOf()
val host: Host = getHostType()

repositories {
    mavenCentral()
}


kotlin {
    fun addNativeTarget(preset: KotlinTargetPreset<*>, desiredHost: Host) {
        val target = targetFromPreset(preset)
        if (desiredHost == Host.WINDOWS) {
            nativeWinHTTPMainSets.add(target.compilations.getByName("main").kotlinSourceSets.first())
            nativeWinHTTPTestSets.add(target.compilations.getByName("test").kotlinSourceSets.first())
        } else {
            nativeCIOMainSets.add(target.compilations.getByName("main").kotlinSourceSets.first())
            nativeCIOTestSets.add(target.compilations.getByName("test").kotlinSourceSets.first())
        }
        if (host != desiredHost) {
            target.compilations.configureEach {
                compileKotlinTask.enabled = false
            }
        }
    }
    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()

    }


    // Linux
    addNativeTarget(presets["linuxX64"], Host.LINUX)

    // MacOS
    addNativeTarget(presets["macosX64"], Host.MAC_OS)
    addNativeTarget(presets["macosArm64"], Host.MAC_OS)

    // iOS
    addNativeTarget(presets["iosArm64"], Host.MAC_OS)
    addNativeTarget(presets["iosX64"], Host.MAC_OS)
    addNativeTarget(presets["iosSimulatorArm64"], Host.MAC_OS)

    // Windows
    addNativeTarget(presets["mingwX64"], Host.WINDOWS)

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinTimeVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinTimeVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion")
            }
        }

        val nativeCIOMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }

        val nativeCIOTest by creating {
            dependsOn(commonTest)
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }

        val nativeWinHTTPMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-winhttp:$ktorVersion")
            }
        }

        val nativeWinHTTPTest by creating {
            dependsOn(commonTest)
            dependencies {
                implementation("io.ktor:ktor-client-winhttp:$ktorVersion")
            }
        }

        val jvmMain by getting { dependsOn(nativeCIOMain) }
        val jvmTest by getting { dependsOn(nativeCIOTest) }

        configure(nativeCIOMainSets) { dependencies { dependsOn(nativeCIOMain) } }
        configure(nativeCIOTestSets) { dependencies { dependsOn(nativeCIOTest) } }

        configure(nativeWinHTTPMainSets) { dependencies { dependsOn(nativeWinHTTPMain) } }
        configure(nativeWinHTTPTestSets) { dependencies { dependsOn(nativeWinHTTPTest) } }
    }
}
val isMainMachine: Boolean = project.findProperty("isMainMachine").toString().toBoolean()

publishing {
    val githubUser: String = project.findProperty("githubUser").toString()
    val githubToken: String = project.findProperty("githubToken").toString()

    if (githubUser != "null" || githubToken != "null") {
        repositories {
            maven {
                setUrl("https://maven.pkg.github.com/OtisGoodman/pocket-kt")
                credentials {
                    username = githubUser
                    password = githubToken
                }
            }
        }
    } else {
        println("Skipped publish because Github credentials are not set!")
    }
    publications {
        withType<MavenPublication>() {


            pom {
                name.set("Pocket-Kt")
                description.set("A tiny Kotlin multiplatform library that assists in saving and restoring objects to and from disk using kotlinx.coroutines, kotlinx.serialisation and okio")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/OtisGoodman/pocket-kt/blob/master/LICENSE")
                    }
                }
                url.set("https://otisgoodman.github.io/Pocket-Kt/")
                scm {
                    connection.set("https://github.com/OtisGoodman/Pocket-Kt.git")
                    url.set("https://github.com/OtisGoodman/Pocket-Kt")
                }
                developers {
                    developer {
                        name.set("Otis Goodman")
                    }
                }
            }
        }
        tasks.withType(AbstractPublishToMaven::class).configureEach {
            onlyIf { isPublicationAllowed(publication.name) }
        }

        tasks.withType(GenerateModuleMetadata::class).configureEach {
            onlyIf { isPublicationAllowed(publication.get().name) }
        }
    }
}

fun getHostType(): Host {
    val hostOs = System.getProperty("os.name")
    return when {
        hostOs.startsWith("Windows") -> Host.WINDOWS
        hostOs.startsWith("Mac") -> Host.MAC_OS
        hostOs == "Linux" -> Host.LINUX
        else -> throw Error("Invalid host.")
    }
}

fun isPublicationAllowed(name: String): Boolean {
    println(name)
    return when {
        name.startsWith("mingw") -> host == Host.WINDOWS
        name.startsWith("macos") ||
                name.startsWith("ios") ||
                name.startsWith("watchos") ||
                name.startsWith("tvos") -> host == Host.MAC_OS

        name.contains("jvm") -> isMainMachine
        else -> host == Host.LINUX
    }
}
enum class Host { WINDOWS, MAC_OS, LINUX }