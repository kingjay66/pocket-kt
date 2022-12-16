package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.Client
import github.otisgoodman.pocketKt.models.ExternalAuth
import github.otisgoodman.pocketKt.models.User
import github.otisgoodman.pocketKt.services.utils.CrudService
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

//@TODO Document
class UserAuthService(client: Client) : CrudService<User>(client) {

    data class UserAuthResponse(val token: String, val user: User, val data: Map<String, JsonElement>)


    @Serializable
    data class AuthProviderInfo(
        val name: String,
        val state: String,
        val codeVerifier: String,
        val codeChallenge: String,
        val codeChallengeMethod: String,
        val authUrl: String
    )

    data class AuthMethodsList(val emailPassword: Boolean, val authProviders: List<AuthProviderInfo>)


    override val baseCrudPath = "/api/users"

    override fun decode(data: Map<String, JsonElement>): User {
        return User(data)
    }


    //May not work
    suspend fun listAuthMethods(queryParams: Map<String, String>): AuthMethodsList {
        val response = client.httpClient.get {
            url {
                path(baseCrudPath, "auth-methods")
                queryParams.forEach { parameters.append(it.key, it.value) }
            }
        }.body<Map<String, JsonElement>>()
        val emailPassword = response.getOrDefault("emailPassword", false) as Boolean
        val authProviders = Json.decodeFromJsonElement<List<AuthProviderInfo>>(
            response["authProviders"] ?: JsonArray(
                emptyList()
            )
        )
        return AuthMethodsList(emailPassword, authProviders)
    }

    suspend fun authWithPassword(
        email: String, password: String, bodyParams: Map<String, JsonElement>, queryParams: Map<String, String>
    ): UserAuthResponse {
        val params = mapOf(
            *bodyParams.toList().toTypedArray(),
            "identity" to JsonPrimitive(email),
            "password" to JsonPrimitive(password)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "auth-with-password")
                contentType(ContentType.Application.Json)
                queryParams.forEach { parameters.append(it.key, it.value) }
                header("Authorization", "")
                setBody(Json.encodeToString(params))
            }
        }.body<UserAuthResponse>()
        return response
    }


    suspend fun authWithOauth2(
        provider: String,
        code: String,
        codeVerifier: String,
        redirectUrl: String,
        bodyParams: Map<String, JsonElement>,
        queryParams: Map<String, String>
    ): UserAuthResponse {
        val params = mapOf(
            *bodyParams.toList().toTypedArray(),
            "provider" to JsonPrimitive(provider),
            "code" to JsonPrimitive(code),
            "codeVerifier" to JsonPrimitive(codeVerifier),
            "redirectUrl" to JsonPrimitive(redirectUrl),
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "auth-with-oauth2")
                contentType(ContentType.Application.Json)
                queryParams.forEach { parameters.append(it.key, it.value) }
                header("Authorization", "")
                setBody(Json.encodeToString(params))
            }
        }.body<UserAuthResponse>()
        return response
    }


    suspend fun refresh(): UserAuthResponse {
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "refresh")
                contentType(ContentType.Application.Json)
            }
        }.body<UserAuthResponse>()
        return response
    }

    suspend fun requestPasswordReset(
        email: String, body: Map<String, JsonElement>, queryParams: Map<String, String>
    ): Boolean {
        val params = mapOf(
            *body.toList().toTypedArray(), "email" to JsonPrimitive(email)
        )
        return client.httpClient.post {
            url {
                path(baseCrudPath, "request-password-reset")
                contentType(ContentType.Application.Json)
                queryParams.forEach { parameters.append(it.key, it.value) }
                setBody(Json.encodeToString(params))
            }
        }.run { true }
    }

    suspend fun confirmPasswordReset(
        passwordResetToken: String,
        password: String,
        passwordConfirm: String,
        body: Map<String, JsonElement>,
        queryParams: Map<String, String>
    ): UserAuthResponse {
        val params = mapOf(
            *body.toList().toTypedArray(),
            "token" to JsonPrimitive(passwordResetToken),
            "password" to JsonPrimitive(password),
            "passwordConfirm" to JsonPrimitive(passwordConfirm)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "confirm-password-reset")
                contentType(ContentType.Application.Json)
                queryParams.forEach { parameters.append(it.key, it.value) }
                setBody(Json.encodeToString(params))
            }
        }.body<UserAuthResponse>()
        return response
    }

    suspend fun requestVerification(
        email: String, body: Map<String, JsonElement>, queryParams: Map<String, String>
    ): Boolean {
        val params = mapOf(
            *body.toList().toTypedArray(), "email" to JsonPrimitive(email)
        )
        return client.httpClient.post {
            url {
                path(baseCrudPath, "request-verification")
                contentType(ContentType.Application.Json)
                queryParams.forEach { parameters.append(it.key, it.value) }
                setBody(Json.encodeToString(params))
            }
        }.run { true }
    }


    suspend fun confirmVerification(
        verificationToken: String, body: Map<String, JsonElement>, queryParams: Map<String, String>
    ): UserAuthResponse {
        val params = mapOf(
            *body.toList().toTypedArray(), "token" to JsonPrimitive(verificationToken)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "confirm-verification")
                contentType(ContentType.Application.Json)
                queryParams.forEach { parameters.append(it.key, it.value) }
                setBody(Json.encodeToString(params))
            }
        }.body<UserAuthResponse>()
        return response
    }

    suspend fun requestEmailChange(
        newEmail: String, body: Map<String, JsonElement>, queryParams: Map<String, String>
    ): Boolean {
        val params = mapOf(
            *body.toList().toTypedArray(), "newEmail" to JsonPrimitive(newEmail)
        )
        return client.httpClient.post {
            url {
                path(baseCrudPath, "request-email-change")
                contentType(ContentType.Application.Json)
                queryParams.forEach { parameters.append(it.key, it.value) }
                setBody(Json.encodeToString(params))
            }
        }.run { true }
    }


    suspend fun confirmEmailChange(
        emailChangeToken: String, password: String, body: Map<String, JsonElement>, queryParams: Map<String, String>
    ): UserAuthResponse {
        val params = mapOf(
            *body.toList().toTypedArray(),
            "token" to JsonPrimitive(emailChangeToken),
            "password" to JsonPrimitive(password)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "confirm-email-change")
                contentType(ContentType.Application.Json)
                queryParams.forEach { parameters.append(it.key, it.value) }
                setBody(Json.encodeToString(params))
            }
        }.body<UserAuthResponse>()
        return response
    }

    suspend fun listExternalAuths(userId: String, queryParams: Map<String, String>): List<ExternalAuth> {
        val response = client.httpClient.get {
            url {
                path(baseCrudPath, userId, "external-auths")
                queryParams.forEach { parameters.append(it.key, it.value) }
            }
        }.body<List<ExternalAuth>>()
        return response
    }

    suspend fun unlinkExternalAuth(userId: String, provider: String, queryParams: Map<String, String>): Boolean {
        return client.httpClient.delete {
            url {
                path(baseCrudPath, userId, "external-auths", provider)
                queryParams.forEach { parameters.append(it.key, it.value) }
            }
        }.run { true }
    }
}