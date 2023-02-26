package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.Untested
import github.otisgoodman.pocketKt.models.Admin
import github.otisgoodman.pocketKt.services.utils.CrudService
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive

//@TODO Document
public class AdminAuthService(client: PocketbaseClient) : CrudService<Admin>(client) {

    @Serializable
    public data class AdminAuthResponse(@SerialName("admin") val record: Admin, val token: String)

    override val baseCrudPath: String = "/api/admins"

    public suspend fun authWithPassword(
        email: String, password: String
    ): AdminAuthResponse {
        val params = mapOf(
            "identity" to JsonPrimitive(email),
            "password" to JsonPrimitive(password)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "auth-with-password")
                contentType(ContentType.Application.Json)
                header("Authorization", "")
            }
            setBody(Json.encodeToString(params))
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    public suspend fun authRefresh(): AdminAuthResponse {
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "auth-refresh")
            }
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    @Untested("Requires SMTP server")
    public suspend fun requestPasswordReset(
        email: String
    ): Boolean {
        val params = mapOf(
            "email" to JsonPrimitive(email)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "request-password-reset")
                contentType(ContentType.Application.Json)
            }
            setBody(Json.encodeToString(params))
        }
        PocketbaseException.handle(response)
        return true
    }

    @Untested("Requires SMTP server")
    public suspend fun confirmPasswordReset(
        passwordResetToken: String,
        password: String,
        passwordConfirm: String,
    ): AdminAuthResponse {
        val params = mapOf(
            "token" to JsonPrimitive(passwordResetToken),
            "password" to JsonPrimitive(password),
            "passwordConfirm" to JsonPrimitive(passwordConfirm)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "confirm-password-reset")
                contentType(ContentType.Application.Json)
            }
            setBody(Json.encodeToString(params))
        }
        PocketbaseException.handle(response)
        return response.body()
    }
}