package github.otisgoodman.pocketKt

import io.ktor.client.*
import io.ktor.client.engine.cio.*

public actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(CIO) { config(this) }
}