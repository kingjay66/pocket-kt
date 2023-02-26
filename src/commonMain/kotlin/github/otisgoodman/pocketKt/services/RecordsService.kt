package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.AuthResponse
import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.Untested
import github.otisgoodman.pocketKt.dsl.query.ExpandRelations
import github.otisgoodman.pocketKt.models.Record
import github.otisgoodman.pocketKt.models.utils.BaseModel
import github.otisgoodman.pocketKt.services.utils.AuthService
import github.otisgoodman.pocketKt.services.utils.SubCrudService
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive

//@TODO Document
public class RecordsService(client: PocketbaseClient) : SubCrudService<Record>(client), AuthService {
    public fun basePath(collectionId: String): String = "api/collections/$collectionId"
    override fun baseCrudPath(collectionId: String): String = "${basePath(collectionId)}/records"

    public enum class ThumbFormat {
        WxH,
        WxHt,
        WxHb,
        WxHf,
        `0xH`,
        Wx0;
    }

    public fun getFileURL(record: Record, filename: String, thumbFormat: ThumbFormat? = null): String {
        val url = URLBuilder()
        this.client.baseUrl(url)
        return if (thumbFormat != null){
            "$url/api/files/${record.collectionId}/${record.id}/$filename?thumb=$thumbFormat"
        }else{
            "$url/api/files/${record.collectionId}/${record.id}/$filename"
        }
    }


    public suspend inline fun <reified T : BaseModel> authWithPassword(
        collection: String, email: String, password: String, expandRelations: ExpandRelations = ExpandRelations()
    ): AuthResponse<T> {
        val params = mapOf(
            "identity" to JsonPrimitive(email),
            "password" to JsonPrimitive(password)
        )
        val response = client.httpClient.post {
            url {
                path(basePath(collection), "auth-with-password")
                expandRelations.addTo(parameters)
            }
            header("Authorization", "")
            contentType(ContentType.Application.Json)
            setBody(params)
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    public suspend inline fun <reified T : BaseModel> authWithUsername(
        collection: String,
        username: String,
        password: String,
        expandRelations: ExpandRelations = ExpandRelations()
    ): AuthResponse<T> {
        return authWithPassword(collection, username, password, expandRelations)
    }

    @Untested("Requires oauth2")
//  @TODO handle createData body param
    public suspend inline fun <reified T : BaseModel> authWithOauth2(
        collection: String,
        provider: String,
        code: String,
        codeVerifier: String,
        redirectUrl: String,
        expandRelations: ExpandRelations = ExpandRelations()
    ): AuthResponse<T> {
        val params = mapOf(
            "provider" to JsonPrimitive(provider),
            "code" to JsonPrimitive(code),
            "codeVerifier" to JsonPrimitive(codeVerifier),
            "redirectUrl" to JsonPrimitive(redirectUrl),
        )
        val response = client.httpClient.post {
            url {
                path(basePath(collection), "auth-with-oauth2")
                contentType(ContentType.Application.Json)
                header("Authorization", "")
                expandRelations.addTo(parameters)
            }
            setBody(Json.encodeToString(params))
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    public suspend inline fun <reified T : BaseModel> refresh(
        collection: String,
        expandRelations: ExpandRelations = ExpandRelations()
    ): AuthResponse<T> {
        val response = client.httpClient.post {
            url {
                path(basePath(collection), "auth-refresh")
                expandRelations.addTo(parameters)
            }
        }
        PocketbaseException.handle(response)
        return response.body()
    }
}