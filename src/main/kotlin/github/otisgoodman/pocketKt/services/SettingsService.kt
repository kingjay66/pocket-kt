package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.Client
import github.otisgoodman.pocketKt.services.utils.BaseService
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

//@TODO Document
class SettingsService(client: Client) : BaseService(client) {

    suspend fun getAll(queryParams: Map<String, String>): Map<String, JsonElement> {
        val response = client.httpClient.get {
            url {
                path("/api/settings")
                queryParams.forEach { parameters.append(it.key, it.value) }
            }
        }.body<Map<String, JsonElement>>()
        return response
    }


    suspend fun update(body: String, queryParams: Map<String, String>): Map<String, JsonElement> {
        val response = client.httpClient.patch {
            url {
                path("/api/settings")
                queryParams.forEach { parameters.append(it.key, it.value) }
                setBody(body)
            }
        }.body<Map<String, JsonElement>>()
        return response
    }


    suspend fun testS3(queryParams: Map<String, String>): Boolean {
        return client.httpClient.post {
            url {
                path("/api/settings/test/s3")
                queryParams.forEach { parameters.append(it.key, it.value) }
            }
        }.run { true }
    }


    suspend fun testEmail(toEmail: String, emailTemplate: String, queryParams: Map<String, String>): Boolean {
        val body = mapOf<String, String>(
            "email" to toEmail,
            "template" to emailTemplate
        )
        return client.httpClient.post {
            url {
                path("/api/settings/test/email")
                queryParams.forEach { parameters.append(it.key, it.value) }
                setBody(Json.encodeToString(body))
            }
        }.run { true }
    }
}