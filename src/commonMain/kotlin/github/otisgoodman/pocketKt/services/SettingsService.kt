package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.Untested
import github.otisgoodman.pocketKt.services.utils.BaseService
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

//@TODO Document
public class SettingsService(client: PocketbaseClient) : BaseService(client) {

    public suspend inline fun <reified T> getAll(): T {
        val response = client.httpClient.get {
            url {
                path("/api/settings")
            }
        }
        PocketbaseException.handle(response)
        return response.body()
    }


    public suspend inline fun <reified T> update(body: String): T {
        val response = client.httpClient.patch {
            url {
                path("/api/settings")
                contentType(ContentType.Application.Json)
            }
            setBody(body)
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    @Untested("Requires an S3 Server. Will not be tested because it's just an http request without a body.")
    public suspend fun testS3(): Boolean {
        val response = client.httpClient.post {
            url {
                path("/api/settings/test/s3")
            }
        }
        PocketbaseException.handle(response)
        return true
    }

    @Untested("Requires SMTP server")
    public suspend fun testEmail(toEmail: String, emailTemplate: String): Boolean {
        val body = mapOf(
            "email" to toEmail,
            "template" to emailTemplate
        )
        val response = client.httpClient.post {
            url {
                path("/api/settings/test/email")
                setBody(Json.encodeToString(body))
            }
        }
        PocketbaseException.handle(response)
        return true
    }
}