package github.otisgoodman.pocketKt.stores

import github.otisgoodman.pocketKt.models.Admin
import github.otisgoodman.pocketKt.models.utils.BaseAuthModel

open class BaseAuthStore(baseModel: BaseAuthModel?, baseToken: String?) {

    //Watered down from JS SDK may improve later although Kotlin probably doesn't use cookies
    //    @TODO Document
    var model: BaseAuthModel?
    var token: String?

    init {
        this.model = baseModel
        this.token = baseToken
    }

    fun save(model: BaseAuthModel?, token: String?) {
        this.model = model
        this.token = token
    }

    open fun clear() {
        this.model = null
        this.token = null
    }
}