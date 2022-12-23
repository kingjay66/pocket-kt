package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.AuthResponse
import github.otisgoodman.pocketKt.Client
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.models.ExternalAuth
import github.otisgoodman.pocketKt.models.User
import github.otisgoodman.pocketKt.models.utils.BaseAuthModel
import github.otisgoodman.pocketKt.services.utils.CrudService
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

//@TODO Document
//API CONSISTENT
//UNTESTED
class UserAuthService(client: Client) : CrudService<User>(client) {



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


    override val baseCrudPath = "api/collections/users"

    override fun decode(data: Map<String, JsonElement>): User {
        return User(data)
    }


    //May not work
    suspend fun listAuthMethods(): AuthMethodsList {
        val response = client.httpClient.get {
            url {
                path(baseCrudPath, "auth-methods")
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
        email: String, password: String
    ): AuthResponse {
        val params = mapOf(
            "identity" to JsonPrimitive(email),
            "password" to JsonPrimitive(password)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "auth-with-password")
            }
            header("Authorization", "")
            contentType(ContentType.Application.Json)
            setBody(params)
        }
        println(response.body<String>())
        PocketbaseException.handle(response)
        val json = response.body<String>()
        println(json)
        @Serializable
        class Token(val token: String)

        return AuthResponse(this.client.json.decodeFromString<Token>(json).token,this.client.json.decodeFromString<BaseAuthModel>(json))
    }

    suspend fun authWithUsername(username: String, password: String): AuthResponse{
        return authWithPassword(username,password)
    }


    suspend fun authWithOauth2(
        provider: String,
        code: String,
        codeVerifier: String,
        redirectUrl: String,
        queryParams: Map<String, String>
    ): AuthResponse {
        val params = mapOf(
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
        }
        PocketbaseException.handle(response)
        return response.body<AuthResponse>()
    }


    suspend fun refresh(): AuthResponse {
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "refresh")
                contentType(ContentType.Application.Json)
            }
        }
        PocketbaseException.handle(response)
        return response.body<AuthResponse>()
    }

    suspend fun requestVerification(
        email: String
    ): Boolean {
        val params = mapOf(
           "email" to JsonPrimitive(email)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "request-verification")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(params))
            }
        }
        PocketbaseException.handle(response)
        return true
    }

    suspend fun confirmVerification(
        verificationToken: String
    ): Boolean {
        val params = mapOf(
          "token" to JsonPrimitive(verificationToken)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "confirm-verification")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(params))
            }
        }
        PocketbaseException.handle(response)
        return true
    }

    suspend fun requestPasswordReset(
        email: String
    ): Boolean {
        val params = mapOf(
            "email" to JsonPrimitive(email)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "request-password-reset")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(params))
            }
        }
        PocketbaseException.handle(response)
        return true
    }

    suspend fun confirmPasswordReset(
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
                path(baseCrudPath, "confirm-password-reset")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(params))
            }
        }
        PocketbaseException.handle(response)
        return true
    }

    suspend fun requestEmailChange(
        newEmail: String
    ): Boolean {
        val params = mapOf(
           "newEmail" to JsonPrimitive(newEmail)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "request-email-change")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(params))
            }
        }
        PocketbaseException.handle(response)
        return true
    }


    suspend fun confirmEmailChange(
        emailChangeToken: String, password: String
    ): Boolean {
        val params = mapOf(
            "token" to JsonPrimitive(emailChangeToken),
            "password" to JsonPrimitive(password)
        )
        val response = client.httpClient.post {
            url {
                path(baseCrudPath, "confirm-email-change")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(params))
            }
        }
        PocketbaseException.handle(response)
        return true
    }

    suspend fun listExternalAuths(userId: String): List<ExternalAuth> {
        val response = client.httpClient.get {
            url {
                path(baseCrudPath, userId, "external-auths")
            }
        }
        PocketbaseException.handle(response)
        return response.body<List<ExternalAuth>>()
    }

    suspend fun unlinkExternalAuth(userId: String, provider: String): Boolean {
        val response = client.httpClient.delete {
            url {
                path(baseCrudPath, userId, "external-auths", provider)
            }
        }
        PocketbaseException.handle(response)
        return true
    }
}