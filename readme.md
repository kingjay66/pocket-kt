# Pocket KT

> Pocket-Kt is a multi-platform idiomatic Kotlin SDK for [Pocketbase](https://pocketbase.io).
---

> NOTE: While the library is stable, [our docs](https://otisgoodman.github.io/pocket-kt) are still in need of work. For
> now, please see our tests, and KDoc comments for help.
> Docs contributions are always welcome!

## Installation

Simply add the following to your buildscript

_GitHub packages requires users to authenticate before using a package. Be sure to replace `GITHUB_USERNAME` with your
username, and replace `GITHUB_PERSONAL_AUTH_TOKEN` with a GitHub personal auth token with the `read:packages scope`._

_I apologise for this hassle, at some point I'll publish Pocket-Kt to maven central..._

```kotlin
repositories {
    mavenCentral()
    maven {
        name = "Github Packages"
        url = uri("https://maven.pkg.github.com/OtisGoodman/pocket-kt")
        credentials {
            username = "GITHUB_USERNAME"
            password = "GITHUB_PERSONAL_AUTH_TOKEN"
        }
    }
}
```

```kotlin
dependencies {
    implementation("github.otisgoodman:pocket-kt:1.1")

// These are required to use Pocket-Kt (Versions may not be up to date)
    implementation("io.ktor:ktor-client-core:2.2.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
}
```