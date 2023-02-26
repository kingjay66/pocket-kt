package github.otisgoodman.pocketKt

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.engine.winhttp.*
import io.ktor.http.*


public actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient{
    return HttpClient(WinHttp) { config(this) }
}