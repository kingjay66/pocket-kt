package github.otisgoodman.pocketKt.models

import github.otisgoodman.pocketKt.models.utils.BaseModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
class ExternalAuth(val _data: Map<String, JsonElement>) : BaseModel(_data) {

    val userId: String
    val collectionId: String
    val provider: String
    val providerId: String

    init {
        this.userId = data.getOrDefault("userId", "") as String
        this.collectionId = data.getOrDefault("collectionId", "") as String
        this.provider = data.getOrDefault("provider", "") as String
        this.providerId = data.getOrDefault("providerId", "") as String
    }

}