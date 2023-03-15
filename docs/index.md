# Overview

Pocket-Kt is a [multi-platform](https://kotlinlang.org/docs/multiplatform-library.html) idiomatic Kotlin SDK
for [Pocketbase](https://pocketbase.io)

*This assumes you have knowledge of the [Pocketbase API](https://pocketbase.io/docs/api-records)*
!!! Note
**Pocket-Kt has been tested and been verified to work with Pocketbase V0.12**

### Installation

Simply add the following to your buildscript

```
dependencies {
    implementation("github.otisgoodman:pocket-kt:1.0")
    
// These are required to use Pocket-Kt (Versions may not be up to date)
    implementation("io.ktor:ktor-client-core:2.2.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
}
```

!!! Note
GitHub packages requires users to authenticate before using a package.
Be sure to replace `GITHUB_USERNAME` with your username, and replace `GITHUB_PERSONAL_AUTH_TOKEN` with a GitHub personal
auth token with the `read:packages` scope.

    At some point in the future I'll get around to publishing Pocket-Kt on maven central which will get rid of this hassle...   

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

## Usage

### Creating a Pocketbase Client

All of Pocket-Kt's methods are accessed through a PocketbaseClient.

```kotlin
//the default lang is en-US so this code is optional 
val client = PocketbaseClient(lang = "en-US", baseUrl = {
    //The url pointing to the Pocketbase server
    protocol = URLProtocol.HTTP
    host = "localhost"
    port = 8090
})
```

### Logging in

Accessing most methods in the Pocketbase API requires some sort of authentication.

=== "Admin Auth"

    ```kotlin
    val client = PocketbaseClient(...)
    //Logs in as a Pocketbase admin
    client.login(client.admins.authWithPassword("admin email", "admin password").token)
    ```

=== "User Auth"

    ```kotlin
    val client = PocketbaseClient(...)
    //Logs in as a Pocketbase record from the "users" collection
    client.login(client.users.authWithPassword("user email", "password").token)
    
    //Logs in as a Pocketbase record from the "users" collection 
    client.login(client.users.authWithUsername("user username", "password").token)
    ```

=== "Record Auth"
```kotlin
val client = PocketbaseClient(...)
//Logs in to Pocketbase as a record from an auth collection
client.login(client.records.authWithPassword("collection name", "record email", "password").token)

    //Logs in to Pocketbase as a record from an auth collection
    client.login(client.records.authWithUsername("collection name", "record username", "password").token)
    ```

### Logging out

```kotlin
val client = PocketbaseClient(...)
//Logs the client out
client.logout()
```

## Interacting with the Pocketbase API

!!! note

    ### To interact with the Pocketbase API simply use the client's services.
    !!! warning
        _Docs are still heavily work in progress, for examples check out our_ [tests](https://github.com/OtisGoodman/pocket-kt/tree/master/src/commonTest/kotlin) _and other documentation pages._
        
    
    | Pocketbase API                                            | Pocket-Kt            |
    |-----------------------------------------------------------|----------------------|
    | [Records](https://pocketbase.io/docs/api-records)         | `client.record`      |
    | [Realtime](https://pocketbase.io/docs/api-realtime)       | `client.realtime`    |
    | [Files](https://pocketbase.io/docs/api-files)             | `client.files`       |
    | [Admins](https://pocketbase.io/docs/api-admins)           | `client.admins`      |
    | [Collections](https://pocketbase.io/docs/api-collections) | `client.collections` |
    | [Settings](https://pocketbase.io/docs/api-settings)       | `client.settings`    |
    | [Logs](https://pocketbase.io/docs/api-logs)               | `client.logs`        |
    | [Health](https://pocketbase.io/docs/api-health)           | `client.health`      |