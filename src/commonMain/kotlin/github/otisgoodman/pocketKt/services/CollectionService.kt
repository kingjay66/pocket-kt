package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.models.Collection
import github.otisgoodman.pocketKt.services.utils.CrudService
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

public class CollectionService(client: PocketbaseClient) : CrudService<Collection>(client) {

    @Serializable
    private data class ImportRequestBody(val collections: List<Collection>, val deleteMissing: Boolean)

    override val baseCrudPath: String = "/api/collections"

    /**
     * Bulk imports the provided Collections configuration.
     * @param [collections] List of collections to import (replace and create).
     * @param [deleteMissing] If true all existing collections and schema fields that are not present in the imported configuration will be deleted, including their related records data.
     */
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