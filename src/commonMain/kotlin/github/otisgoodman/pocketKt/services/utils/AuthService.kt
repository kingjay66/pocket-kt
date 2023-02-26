package github.otisgoodman.pocketKt.services.utils

import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.Untested
import github.otisgoodman.pocketKt.models.ExternalAuth
import github.otisgoodman.pocketKt.recordAuthFrom
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive

//@TODO Document
public interface AuthService {

    public val client: PocketbaseClient

    @Serializable
    public data class AuthProviderInfo(
        val name: String,
        val state: String,
        val codeVerifier: String,
        val codeChallenge: String,
        val codeChallengeMethod: String,
        val authUrl: String
    )

    @Serializable
    public data class AuthMethodsList(
        val emailPassword: Boolean,
        val usernamePassword: Boolean,
        val authProviders: List<AuthProviderInfo>
    )


    public suspend fun listAuthMethods(collection: String): AuthMethodsList {
        val response = client.httpClient.get {
            url {
                path(recordAuthFrom(collection), "auth-methods")
            }
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    @Untested("Requires oauth2")
    public suspend fun listExternalAuths(collection: String, userId: String): List<ExternalAuth> {
        val response = client.httpClient.get {
            url {
                path(recordAuthFrom(collection), userId, "external-auths")
            }
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    @Untested("Requires oauth2")
    public suspend fun unlinkExternalAuth(collection: String, userId: String, provider: String): Boolean {
        val response = client.httpClient.delete {
            url {
                path(recordAuthFrom(collection), userId, "external-auths", provider)
            }
        }
        PocketbaseException.handle(response)
        return true
    }


    @Untested("Requires SMTP server")
    public suspend fun requestVerification(
        collection: String,
        email: String
    ): Boolean {
        val params = mapOf(
            "email" to JsonPrimitive(email)
        )
        val response = client.httpClient.post {
            url {
                path(recordAuthFrom(collection), "request-verification")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(params))
            }
        }
        PocketbaseException.handle(response)
        return true
    }

    @Untested("Requires SMTP server")
    public suspend fun confirmVerification(
        collection: String,
        verificationToken: String
    ): Boolean {
        val params = mapOf(
            "token" to JsonPrimitive(verificationToken)
        )
        val response = client.httpClient.post {
            url {
                path(recordAuthFrom(collection), "confirm-verification")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(params))
            }
        }
        PocketbaseException.handle(response)
        return true
    }

    @Untested("Requires SMTP server")
    public suspend fun requestPasswordReset(
        collection: String,
        email: String
    ): Boolean {
        val params = mapOf(
            "email" to JsonPrimitive(email)
        )
        val response = client.httpClient.post {
            url {
                path(recordAuthFrom(collection), "request-password-reset")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(params))
            }
        }
        PocketbaseException.handle(response)
        return true
    }

    @Untested("Requires SMTP server")
    public suspend fun confirmPasswordReset(
        collection: String,
        passwordResetToken: String,
        password: String,
        passwordConfirm: String,
    ): Boolean {
        val params = mapOf(
            "token" to JsonPrimitive(passwordResetToken),
            "password" to JsonPrimitive(password),
            "passwordConfirm" to JsonPrimitive(passwordConfirm)
        )
        val response = client.httpClient.post {
            url {
                path(recordAuthFrom(collection), "confirm-password-reset")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(params))
            }
        }
        PocketbaseException.handle(response)
        return true
    }

    @Untested("Requires SMTP server")
    public suspend fun requestEmailChange(
        collection: String,
        newEmail: String
    ): Boolean {
        val params = mapOf(
            "newEmail" to JsonPrimitive(newEmail)
        )
        val response = client.httpClient.post {
            url {
                path(recordAuthFrom(collection), "request-email-change")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(params))
            }
        }
        PocketbaseException.handle(response)
        return true
    }

    @Untested("Requires SMTP server")
    public suspend fun confirmEmailChange(
        collection: String, emailChangeToken: String, password: String
    ): Boolean {
        val params = mapOf(
            "token" to JsonPrimitive(emailChangeToken),
            "password" to JsonPrimitive(password)
        )
        val response = client.httpClient.post {
            url {
                path(recordAuthFrom(collection), "confirm-email-change")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(params))
            }
        }
        PocketbaseException.handle(response)
        return true
    }
}