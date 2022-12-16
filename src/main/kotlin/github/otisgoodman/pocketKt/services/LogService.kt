package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.Client
import github.otisgoodman.pocketKt.models.LogRequest
import github.otisgoodman.pocketKt.models.utils.ListResult
import github.otisgoodman.pocketKt.services.utils.BaseService
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

//@TODO Document
class LogService(client: Client) : BaseService(client) {
    //  @TODO Sterilize to Java date object
    @Serializable
    data class HourlyStats(val total: Int, val date: String)


    suspend fun getRequestsList(
        page: Int = 1,
        perPage: Int = 30,
        queryParams: Map<String, String>
    ): ListResult<LogRequest> {
        val params = mapOf<String, String>(
            "page" to page.toString(),
            "perPage" to perPage.toString(),
            *queryParams.toList().toTypedArray()
        )
        val response = client.httpClient.get {
            url {
                path("api", "logs", "requests")
                queryParams.forEach { parameters.append(it.key, it.value) }
            }
        }.body<ListResult<LogRequest>>()
        return response
    }


    suspend fun getRequest(id: String, queryParams: Map<String, String>): LogRequest {
        val response = client.httpClient.get {
            url {
                path("api", "logs", "requests", id)
                queryParams.forEach { parameters.append(it.key, it.value) }
            }
        }.body<LogRequest>()
        return response
    }


    suspend fun getRequestsStats(queryParams: Map<String, String>): List<HourlyStats> {
        val response = client.httpClient.get {
            url {
                path("api", "logs", "requests", "stats")
                queryParams.forEach { parameters.append(it.key, it.value) }
            }
        }.body<List<HourlyStats>>()
        return response
    }

}