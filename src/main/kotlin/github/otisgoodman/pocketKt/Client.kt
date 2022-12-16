package github.otisgoodman.pocketKt

import github.otisgoodman.pocketKt.services.*
import github.otisgoodman.pocketKt.stores.BaseAuthStore
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*

//@TODO Document

@Suppress("MemberVisibilityCanBePrivate")
class Client(baseUrl: String = "/", lang: String = "en-US", authStore: BaseAuthStore? = null) {

    val baseUrl: String
    val lang: String
    val authStore: BaseAuthStore

    val settings: SettingsService
    val admins: AdminAuthService
    val collections: CollectionService
    val logs: LogService
    val records: RecordsService

//    @TODO Realtime and Health services

    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            url(baseUrl)
            headers {
                if (authStore != null) appendIfNameAbsent("Authorization", authStore.token ?: "")

            }
        }
    }


    init {
        this.baseUrl = baseUrl
        this.lang = lang
        this.authStore = authStore ?: BaseAuthStore(null, null)

        this.settings = SettingsService(this)
        this.admins = AdminAuthService(this)
        this.collections = CollectionService(this)
        this.logs = LogService(this)
        this.records = RecordsService(this)

    }


}