# Overview

Pocket-Kt is a [multi-platform]() idiomatic Kotlin SDK for [Pocketbase](https://pocketbase.io)

*This assumes you have knowledge of the [Pocketbase API](https://pocketbase.io/docs/api-records)*
!!! Note
    **Pocket-Kt is compatible with Pocketbase 0.12**

### Installation

> TODO

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

    _Docs are still heavily work in progress, for examples check out our_ [tests](https://github.com/OtisGoodman/pocket-kt/tree/master/src/commonTest/kotlin)
    
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