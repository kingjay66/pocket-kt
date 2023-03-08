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

/**
 * A multiplatform idiomatic SDK for [Pocketbase](https://pocketbase.io)
 * @param [baseUrl] the URL of the Pocketbase server
 * Example: localhost:8090
 * @param [lang] the language of the Pocketbase server
 * @param [store] the authentication store used to store Pocketbase authentication data
 * [Example]()
 * @TODO add examples for auth store in web docs
 */
public class PocketbaseClient(baseUrl: URLBuilder.() -> Unit, lang: String = "en-US", store: BaseAuthStore? = null) {

    public val baseUrl: URLBuilder.() -> Unit
    public val lang: String

    public var authStore: BaseAuthStore

    /**
     * The API for Pocketbase [settings](https://pocketbase.io/docs/api-settings/)
     */
    public val settings: SettingsService

    /**
     * The API for Pocketbase [admins](https://pocketbase.io/docs/api-admins/)
     */
    public val admins: AdminAuthService

    /**
     * The API for Pocketbase [record auth](https://pocketbase.io/docs/api-records/#auth-record-actions) for the "users" collection
     *
     *  If you are looking for custom models or other auth collections go to [records]
     */
    public val users: UserAuthService

    /**
     * The API for Pocketbase [collections](https://pocketbase.io/docs/api-collections/)
     */
    public val collections: CollectionService

    /**
     * The API for Pocketbase [logs](https://pocketbase.io/docs/api-logs/)
     */
    public val logs: LogService

    /**
     * The API for Pocketbase [records](https://pocketbase.io/docs/api-records/)
     *
     * This includes both CRUD actions and collection auth methods
     */
    public val records: RecordsService

    /**
     * The API for Pocketbase [health](https://pocketbase.io/docs/api-health/)
     */
    public val health: HealthService

    /**
     * The API for Pocketbase [realtime](https://pocketbase.io/docs/api-realtime/)
     *
     * Adapted for [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
     */
    public val realtime: RealtimeService

    //  @TODO remove this init block
    init {
        this.baseUrl = baseUrl
        this.lang = lang
        this.authStore = store ?: BaseAuthStore(null)
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

    /**
     * The Ktor [HttpClient] used to connect to the [Pocketbase](https://pocketbase.io) API
     *
     * This automatically adds the current authorization token from the client's [authStore].
     */
    public val httpClient: HttpClient = httpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpTimeout)
        defaultRequest {
            url(baseUrl)
            header("Authorization", authStore.token)
        }
    }
}