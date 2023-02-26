package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.models.Collection
import github.otisgoodman.pocketKt.services.utils.CrudService
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

//@TODO Document
public class CollectionService(client: PocketbaseClient) : CrudService<Collection>(client) {

    @Serializable
    private data class ImportRequestBody(val collections: List<Collection>, val deleteMissing: Boolean)

    override val baseCrudPath: String = "/api/collections"

    public suspend fun import(
        collections: List<Collection>,
        deleteMissing: Boolean = false,
    ): Boolean {
        val response = client.httpClient.put {
            url {
                path(baseCrudPath, "import")
                contentType(ContentType.Application.Json)
            }
            setBody(ImportRequestBody(collections, deleteMissing))
        }
        PocketbaseException.handle(response)
        return true
    }
}