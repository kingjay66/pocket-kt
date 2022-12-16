package github.otisgoodman.pocketKt.services.utils

import github.otisgoodman.pocketKt.Client
import github.otisgoodman.pocketKt.models.utils.BaseModel

//@TODO Document
abstract class SubCrudService<T : BaseModel>(client: Client) : BaseCrudService<T>(client) {

    abstract fun baseCrudPath(collectionId: String): String

    open suspend fun getFullList(sub: String, batch: Int = 200, queryParams: Map<String, String>) =
        this._getFullList(this.baseCrudPath(sub), batch, queryParams)

    open suspend fun getList(sub: String, page: Int = 1, perPage: Int = 30, queryParams: Map<String, String>) =
        this._getList(this.baseCrudPath(sub), page, perPage, queryParams)

    open suspend fun getOne(sub: String, id: String, queryParams: Map<String, String>) =
        this._getOne(baseCrudPath(sub), id, queryParams)

    open suspend fun create(sub: String, id: String, body: String, queryParams: Map<String, String>) =
        this._create(baseCrudPath(sub), id, body, queryParams)

    open suspend fun update(sub: String, id: String, body: String, queryParams: Map<String, String>) =
        this._update(baseCrudPath(sub), id, body, queryParams)

    open suspend fun delete(sub: String, id: String, queryParams: Map<String, String>) =
        this._delete(baseCrudPath(sub), id, queryParams)
}