package github.otisgoodman.pocketKt.models

import github.otisgoodman.pocketKt.models.utils.BaseModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
class Admin(private val data_: Map<String, JsonElement>) : BaseModel(data_) {
    val avatar: Int
    val email: String

    init {
        this.avatar = data.getOrDefault("avatar", 0) as Int
        this.email = data.getOrDefault("email", "") as String
    }

}