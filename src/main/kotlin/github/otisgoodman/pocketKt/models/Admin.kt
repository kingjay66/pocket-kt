package github.otisgoodman.pocketKt.models

import github.otisgoodman.pocketKt.models.utils.AuthModelType
import github.otisgoodman.pocketKt.models.utils.BaseAuthModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
class Admin(private val data_: Map<String, JsonElement>) : BaseAuthModel(data_,AuthModelType.Admin) {
    val avatar: Int

    init {
        this.avatar = data.getOrDefault("avatar", 0) as Int
    }

}