package github.otisgoodman.pocketKt

import io.ktor.client.*

public expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient