package github.otisgoodman.pocketKt.services.utils

import github.otisgoodman.pocketKt.FileUpload
import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.dsl.query.ExpandRelations
import github.otisgoodman.pocketKt.dsl.query.Filter
import github.otisgoodman.pocketKt.dsl.query.SortFields
import github.otisgoodman.pocketKt.models.utils.BaseModel
import github.otisgoodman.pocketKt.models.utils.ListResult
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

public abstract class BaseCrudService<T : BaseModel>(client: PocketbaseClient) : BaseService(client) {

    public suspend inline fun <reified T : BaseModel> _getFullList(
        path: String,
        batch: Int,
        sortBy: SortFields = SortFields(),
        filterBy: Filter = Filter(),
        expandRelations: ExpandRelations = ExpandRelations()
    ): List<T> {
        val result = mutableListOf<T>()
        var page = 1
        while (true) {
            val list = _getList<T>(path, page, batch, sortBy, filterBy, expandRelations)
            val items = list.items.toMutableList()
            result.addAll(items)
            if (items.isNotEmpty() && list.totalItems <= result.size) return result
            page += 1
        }
    }

    public suspend inline fun <reified T : BaseModel> _getList(
        path: String,
        page: Int,
        perPage: Int,
        sortBy: SortFields = SortFields(),
        filterBy: Filter = Filter(),
        expandRelations: ExpandRelations = ExpandRelations()
    ): ListResult<T> {
        val response = client.httpClient.get {
            url {
                path(path)
                sortBy.addTo(parameters)
                filterBy.addTo(parameters)
                expandRelations.addTo(parameters)
                parameters.append("page", page.toString())
                parameters.append("perPage", perPage.toString())
            }
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    public suspend inline fun <reified T : BaseModel> _getOne(
        path: String, id: String, expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        val response = client.httpClient.get {
            url {
                path(path, id)
                contentType(ContentType.Application.Json)
                expandRelations.addTo(parameters)
            }
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    public suspend inline fun <reified T : BaseModel> _create(
        path: String,
        body: String,
        expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        val response = client.httpClient.post {
            url {
                path(path)
                contentType(ContentType.Application.Json)
                expandRelations.addTo(parameters)
            }
            setBody(body)
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    public suspend inline fun <reified T : BaseModel> _update(
        path: String,
        id: String,
        body: String,
        expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        val response = client.httpClient.patch {
            url {
                path(path, id)
                contentType(ContentType.Application.Json)
                expandRelations.addTo(parameters)
            }
            setBody(body)
        }
        PocketbaseException.handle(response)
        return response.body()
    }

    public suspend inline fun _delete(path: String, id: String): Boolean {
        val response = client.httpClient.delete {
            url {
                path(path, id)
            }
        }
        PocketbaseException.handle(response)
        return true
    }
//@TODO Handle file uploads on native

    public suspend inline fun <reified T : BaseModel> _create(
        path: String,
        body: Map<String, Any>,
        files: List<FileUpload>,
        expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        val response = client.httpClient.post {
            url {
                path(path)
                expandRelations.addTo(parameters)
            }

            setBody(MultiPartFormDataContent(
                formData {
                    for (file in files) {
                        append(
                            file.field,
                            file.file ?: ByteArray(0),
                            headers = if (file.file != null) headersOf(
                                HttpHeaders.ContentDisposition,
                                "filename=\"${file.fileName}\""
                            ) else headersOf()
                        )
                    }
                    for (element in body) {
                        append(element.key, element.value.toString())
                    }
                }
            ))
        }
        PocketbaseException.handle(response)
        return response.body()
    }


    public suspend inline fun <reified T : BaseModel> _update(
        path: String,
        id: String,
        body: Map<String, Any>,
        files: List<FileUpload>,
        expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        val response = client.httpClient.patch {
            url {
                path(path, id)
                expandRelations.addTo(parameters)
            }

            setBody(MultiPartFormDataContent(
                formData {
                    for (file in files) {
                        append(
                            file.field,
                            file.file ?: ByteArray(0),
                            headers = if (file.file != null) headersOf(
                                HttpHeaders.ContentDisposition,
                                "filename=\"${file.fileName}\""
                            ) else headersOf()
                        )
                    }
                    for (element in body) {
                        append(element.key, element.value.toString())
                    }
                }
            ))
        }
        PocketbaseException.handle(response)
        return response.body()
    }
}