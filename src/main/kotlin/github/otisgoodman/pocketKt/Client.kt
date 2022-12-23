package github.otisgoodman.pocketKt

import github.otisgoodman.pocketKt.dsl.LoginBuilder
import github.otisgoodman.pocketKt.models.utils.BaseAuthModel
import github.otisgoodman.pocketKt.services.*
import github.otisgoodman.pocketKt.stores.BaseAuthStore
import github.otisgoodman.pocketKt.stores.LocalAuthStore
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import kotlin.io.path.Path

//@TODO Document

@Suppress("MemberVisibilityCanBePrivate")
class Client(baseUrl: URLBuilder.() -> Unit, lang: String = "en-US", authStore: LocalAuthStore? = null) {

    val baseUrl: URLBuilder.() -> Unit
    val lang: String
    var authStore: LocalAuthStore

    val settings: SettingsService
    val admins: AdminAuthService
    val users: UserAuthService
    val collections: CollectionService
    val logs: LogService
    val records: RecordsService

//    @TODO Realtime and Health services

    val json = Json{
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
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
        this.authStore = authStore ?: LocalAuthStore(null, null)

        this.settings = SettingsService(this)
        this.admins = AdminAuthService(this)
        this.users = UserAuthService(this)
        this.collections = CollectionService(this)
        this.logs = LogService(this)
        this.records = RecordsService(this)

    }


}