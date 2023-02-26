package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.services.utils.BaseService
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

//@TODO Document
public class HealthService(client: PocketbaseClient) : BaseService(client) {

    @Serializable
    public data class HealthResponses(val code: Int, val message: String)

    public suspend fun healthCheck(): HealthResponses {
        val response = client.httpClient.get {
            url {
                path("api", "health")
            }
        }
        PocketbaseException.handle(response)
        return response.body()
    }

}