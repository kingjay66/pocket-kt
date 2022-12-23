package github.otisgoodman.pocketKt.services.utils

import github.otisgoodman.pocketKt.Client
import github.otisgoodman.pocketKt.PocketbaseError
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.models.utils.BaseModel
import github.otisgoodman.pocketKt.models.utils.ListResult
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonElement

//@TODO Document
abstract class BaseCrudService<T : BaseModel>(client: Client) : BaseService(client) {

    abstract fun decode(data: Map<String, JsonElement>): T


    protected suspend fun _getFullList(
        basePath: String,
        batchSize: Int = 100,
        queryParams: Map<String, String>
    ): List<T> {
        val result = mutableListOf<T>()

        suspend fun request(page: Int): List<T> {
            val list = _getList(basePath, page, batchSize, queryParams)
            val items = list.items
            result.addAll(listOf(items as T))
            return if (items.isNotEmpty() && list.totalItems > result.size) request(page + 1)
            else result
        }
        return request(1)
    }

    suspend fun _getList(
        basePath: String,
        page: Int = 1,
        perPage: Int = 30,
        queryParams: Map<String, String>
    ): ListResult<T> {
        val items = mutableListOf<BaseModel>()
        val response = client.httpClient.get {
            url {
                path(basePath)
                queryParams.forEach { parameters.append(it.key, it.value) }
                parameters.append("page", page.toString())
                parameters.append("perPage", perPage.toString())
            }
        }
        PocketbaseException.handle(response)
        return response.body<ListResult<T>>()
    }

    protected suspend fun _getOne(basePath: String, id: String, queryParams: Map<String, String>): T {
        val response = client.httpClient.get {
            url {
                path(basePath, id)
                queryParams.forEach { parameters.append(it.key, it.value) }
            }
        }
        PocketbaseException.handle(response)
        return response.body<BaseModel>() as T
    }


    protected suspend fun _create(basePath: String, id: String, body: String, queryParams: Map<String, String>): T {
        val response = client.httpClient.post {
            url {
                path(basePath, id)
                contentType(ContentType.Application.Json)
                queryParams.forEach { parameters.append(it.key, it.value) }
                setBody(body)
            }
        }
        PocketbaseException.handle(response)
        return response.body<BaseModel>() as T
    }


    protected suspend fun _update(basePath: String, id: String, body: String, queryParams: Map<String, String>): T {
        val response = client.httpClient.patch {
            url {
                path(basePath, id)
                contentType(ContentType.Application.Json)
                queryParams.forEach { parameters.append(it.key, it.value) }
                setBody(body)
            }
        }

        PocketbaseException.handle(response)
        return response.body<BaseModel>() as T
    }

    protected suspend fun _delete(basePath: String, id: String, queryParams: Map<String, String>): Boolean {
        val response = client.httpClient.delete {
            url {
                path(basePath, id)
                queryParams.forEach { parameters.append(it.key, it.value) }
            }
        }
        PocketbaseException.handle(response)
        return true
    }

}