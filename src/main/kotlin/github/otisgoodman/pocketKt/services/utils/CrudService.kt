package github.otisgoodman.pocketKt.services.utils

import github.otisgoodman.pocketKt.Client
import github.otisgoodman.pocketKt.models.utils.BaseModel

//@TODO Document
abstract class CrudService<T : BaseModel>(client: Client) : BaseCrudService<T>(client) {
    abstract val baseCrudPath: String

    //Maybe I should type these...
    open suspend fun getFullList(batch: Int = 200, queryParams: Map<String, String>) =
        this._getFullList(this.baseCrudPath, batch, queryParams)

    open suspend fun getList(page: Int = 1, perPage: Int = 30, queryParams: Map<String, String>) =
        this._getList(this.baseCrudPath, page, perPage, queryParams)

    open suspend fun getOne(id: String, queryParams: Map<String, String>) = this._getOne(baseCrudPath, id, queryParams)

    open suspend fun create(id: String, body: String, queryParams: Map<String, String>) =
        this._create(baseCrudPath, id, body, queryParams)

    open suspend fun update(id: String, body: String, queryParams: Map<String, String>) =
        this._update(baseCrudPath, id, body, queryParams)

    open suspend fun delete(id: String, queryParams: Map<String, String>) = this._delete(baseCrudPath, id, queryParams)
}