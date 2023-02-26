package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.AuthResponse
import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.Untested
import github.otisgoodman.pocketKt.dsl.query.ExpandRelations
import github.otisgoodman.pocketKt.models.User
import github.otisgoodman.pocketKt.services.utils.AuthService
import github.otisgoodman.pocketKt.services.utils.CrudService
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

//@TODO Document
public class UserAuthService(client: PocketbaseClient) : CrudService<User>(client), AuthService {

    private val basePath = "/api/collections/users"

    override val baseCrudPath: String = "$basePath/records"

    public suspend fun authWithPassword(
        email: String, password: String, expandRelations: ExpandRelations = ExpandRelations()
    ): AuthResponse<User> {
        val params = mapOf(
            "identity" to JsonPrimitive(email),
            "password" to JsonPrimitive(password)
        )
        val response = client.httpClient.post {
            url {
                path(basePath, "auth-with-password")
                expandRelations.addTo(parameters)
            }
            header("Authorization", "")
            contentType(ContentType.Application.Json)
            setBody(params)
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    public suspend fun authWithUsername(
        username: String,
        password: String,
        expandRelations: ExpandRelations = ExpandRelations()
    ): AuthResponse<User> {
        return authWithPassword(username, password, expandRelations)
    }

    @Untested("Requires oauth2")
//  @TODO handle createData body param
    public suspend fun authWithOauth2(
        provider: String,
        code: String,
        codeVerifier: String,
        redirectUrl: String,
        expandRelations: ExpandRelations = ExpandRelations()
    ): AuthResponse<User> {
        val params = mapOf(
            "provider" to JsonPrimitive(provider),
            "code" to JsonPrimitive(code),
            "codeVerifier" to JsonPrimitive(codeVerifier),
            "redirectUrl" to JsonPrimitive(redirectUrl),
        )
        val response = client.httpClient.post {
            url {
                path(basePath, "auth-with-oauth2")
                contentType(ContentType.Application.Json)
                header("Authorization", "")
                expandRelations.addTo(parameters)
            }
            setBody(Json.encodeToString(params))
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    public suspend fun refresh(expandRelations: ExpandRelations = ExpandRelations()): AuthResponse<User> {
        val response = client.httpClient.post {
            url {
                path(basePath, "auth-refresh")
                contentType(ContentType.Application.Json)
                expandRelations.addTo(parameters)
            }
        }
        PocketbaseException.handle(response)
        return response.body()
    }
}