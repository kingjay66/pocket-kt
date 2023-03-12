import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetPreset

plugins {
    val kotlinVersion = "1.8.10"
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
}

group = "github.otisgoodman"
version = "1.0"


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
    addNativeTarget(presets["iosArm32"], Host.MAC_OS)
    addNativeTarget(presets["iosX64"], Host.MAC_OS)
    addNativeTarget(presets["iosSimulatorArm64"], Host.MAC_OS)

    // watchOS
    addNativeTarget(presets["watchosX86"], Host.MAC_OS)
    addNativeTarget(presets["watchosX64"], Host.MAC_OS)
    addNativeTarget(presets["watchosArm32"], Host.MAC_OS)
    addNativeTarget(presets["watchosArm64"], Host.MAC_OS)
    addNativeTarget(presets["watchosSimulatorArm64"], Host.MAC_OS)

    // tvOS
    addNativeTarget(presets["tvosArm64"], Host.MAC_OS)
    addNativeTarget(presets["tvosX64"], Host.MAC_OS)
    addNativeTarget(presets["tvosSimulatorArm64"], Host.MAC_OS)


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

fun getHostType(): Host {
    val hostOs = System.getProperty("os.name")
    return when {
        hostOs.startsWith("Windows") -> Host.WINDOWS
        hostOs.startsWith("Mac") -> Host.MAC_OS
        hostOs == "Linux" -> Host.LINUX
        else -> throw Error("Invalid host.")
    }
}

enum class Host { WINDOWS, MAC_OS, LINUX }