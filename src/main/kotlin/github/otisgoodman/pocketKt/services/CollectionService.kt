package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.Client
import github.otisgoodman.pocketKt.models.Collection
import github.otisgoodman.pocketKt.services.utils.CrudService
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

//@TODO Document
class CollectionService(client: Client) : CrudService<Collection>(client) {

    override val baseCrudPath = "/api/collections"

    override fun decode(data: Map<String, JsonElement>): Collection {
        return Collection(data)
    }

    @Serializable
    private data class ImportRequestBody(val collections: List<Collection>, val deleteMissing: Boolean)

    suspend fun import(
        collections: List<Collection>,
        deleteMissing: Boolean = false,
        queryParams: Map<String, String>
    ): Boolean {
        return client.httpClient.put {
            url {
                path(baseCrudPath, "import")
                queryParams.forEach { parameters.append(it.key, it.value) }
                setBody(ImportRequestBody(collections, deleteMissing))
            }
        }.run { true }
    }
}