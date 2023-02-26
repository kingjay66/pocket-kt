package github.otisgoodman.pocketKt

import github.otisgoodman.pocketKt.services.*
import github.otisgoodman.pocketKt.stores.BaseAuthStore
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

//@TODO Document

public class PocketbaseClient(baseUrl: URLBuilder.() -> Unit, lang: String = "en-US", store: BaseAuthStore? = null) {

    public val baseUrl: URLBuilder.() -> Unit
    public val lang: String
    public var authStore: BaseAuthStore
    public val settings: SettingsService
    public val admins: AdminAuthService
    public val users: UserAuthService
    public val collections: CollectionService
    public val logs: LogService
    public val records: RecordsService
    public val health: HealthService
    public val realtime: RealtimeService


    init {
        this.baseUrl = baseUrl
        this.lang = lang
        this.authStore = store ?: BaseAuthStore(null, null, null)
        this.settings = SettingsService(this)
        this.admins = AdminAuthService(this)
        this.users = UserAuthService(this)
        this.collections = CollectionService(this)
        this.logs = LogService(this)
        this.records = RecordsService(this)
        this.health = HealthService(this)

        this.realtime = RealtimeService(this)
    }

    private val json: Json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    public val httpClient: HttpClient = httpClient{
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpTimeout)
        defaultRequest {
            url(baseUrl)
            header("Authorization", authStore.token)
        }
    }

//    HttpClient(CIO) {
//        install(ContentNegotiation) {
//            json(json)
//        }
//        install(HttpTimeout)
//        defaultRequest {
//            url(baseUrl)
//            header("Authorization", authStore.token)
//        }
//    }
}