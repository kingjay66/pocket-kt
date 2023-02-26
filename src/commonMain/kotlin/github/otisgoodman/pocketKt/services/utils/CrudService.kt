package github.otisgoodman.pocketKt.services.utils

import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.FileUpload
import github.otisgoodman.pocketKt.dsl.query.ExpandRelations
import github.otisgoodman.pocketKt.dsl.query.Filter
import github.otisgoodman.pocketKt.dsl.query.SortFields
import github.otisgoodman.pocketKt.models.utils.BaseModel
import github.otisgoodman.pocketKt.models.utils.ListResult
import kotlinx.serialization.json.JsonElement

public abstract class CrudService<T : BaseModel>(client: PocketbaseClient) : BaseCrudService<T>(client) {

    public abstract val baseCrudPath: String

    public suspend inline fun <reified T : BaseModel> getFullList(
        batch: Int,
        sortBy: SortFields = SortFields(),
        filterBy: Filter = Filter(),
        expandRelations: ExpandRelations = ExpandRelations()
    ): List<T> {
        return _getFullList<T>(baseCrudPath, batch, sortBy, filterBy, expandRelations)
    }

    public suspend inline fun <reified T : BaseModel> getList(
        page: Int,
        perPage: Int,
        sortBy: SortFields = SortFields(),
        filterBy: Filter = Filter(),
        expandRelations: ExpandRelations = ExpandRelations()
    ): ListResult<T> {
        return _getList(baseCrudPath, page, perPage, sortBy, filterBy, expandRelations)
    }

    public suspend inline fun <reified T : BaseModel> getOne(
        id: String, expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        return _getOne(baseCrudPath, id, expandRelations)
    }

    public suspend inline fun <reified T : BaseModel> create(
        body: String, expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        return _create(baseCrudPath, body, expandRelations)
    }

    public suspend inline fun <reified T : BaseModel> update(
        id: String, body: String, expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        return _update(baseCrudPath, id, body, expandRelations)
    }

    public suspend inline fun <reified T : BaseModel> create(
        body: Map<String, Any>, files: List<FileUpload>, expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        return _create(baseCrudPath, body, files, expandRelations)
    }

    public suspend inline fun <reified T : BaseModel> update(
        id: String,
        body: Map<String, Any>,
        files: List<FileUpload>,
        expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        return _update(baseCrudPath, id, body, files, expandRelations)
    }

    public suspend inline fun delete(id: String): Boolean {
        return _delete(baseCrudPath, id)
    }
}