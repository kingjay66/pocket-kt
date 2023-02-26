package github.otisgoodman.pocketKt.services.utils

import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.FileUpload
import github.otisgoodman.pocketKt.dsl.query.ExpandRelations
import github.otisgoodman.pocketKt.dsl.query.Filter
import github.otisgoodman.pocketKt.dsl.query.SortFields
import github.otisgoodman.pocketKt.models.utils.BaseModel
import github.otisgoodman.pocketKt.models.utils.ListResult
import kotlinx.serialization.json.JsonElement

//@TODO Document
public abstract class SubCrudService<T : BaseModel>(client: PocketbaseClient) : BaseCrudService<T>(client) {

    public abstract fun baseCrudPath(collectionId: String): String

    public suspend inline fun <reified T : BaseModel> getFullList(
        sub: String,
        batch: Int,
        sortBy: SortFields = SortFields(),
        filterBy: Filter = Filter(),
        expandRelations: ExpandRelations = ExpandRelations()
    ): List<T> {
        return _getFullList(baseCrudPath(sub), batch, sortBy, filterBy, expandRelations)
    }

    public suspend inline fun <reified T : BaseModel> getList(
        sub: String,
        page: Int,
        perPage: Int,
        sortBy: SortFields = SortFields(),
        filterBy: Filter = Filter(),
        expandRelations: ExpandRelations = ExpandRelations()
    ): ListResult<T> {
        return _getList(baseCrudPath(sub), page, perPage, sortBy, filterBy, expandRelations)
    }

    public suspend inline fun <reified T : BaseModel> getOne(
        sub: String, id: String, expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        return _getOne(baseCrudPath(sub), id, expandRelations)
    }

    public suspend inline fun <reified T : BaseModel> create(
        sub: String,
        body: String,
        expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        return _create(baseCrudPath(sub), body, expandRelations)
    }

    public suspend inline fun <reified T : BaseModel> update(
        sub: String,
        id: String,
        body: String,
        expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        return _update(baseCrudPath(sub), id, body, expandRelations)
    }

    public suspend inline fun <reified T : BaseModel> create(
        sub: String,
        body: Map<String, JsonElement>,
        files: List<FileUpload>,
        expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        return _create(baseCrudPath(sub), body, files, expandRelations)
    }

    public suspend inline fun <reified T : BaseModel> update(
        sub: String,
        id: String,
        body: Map<String, JsonElement>,
        files: List<FileUpload>,
        expandRelations: ExpandRelations = ExpandRelations()
    ): T {
        return _update(baseCrudPath(sub), id, body, files, expandRelations)
    }

    public suspend inline fun delete(sub: String, id: String): Boolean {
        return _delete(baseCrudPath(sub), id)
    }
}