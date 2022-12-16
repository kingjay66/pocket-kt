package github.otisgoodman.pocketKt.models

import github.otisgoodman.pocketKt.models.utils.BaseModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
class Record(private val _data: Map<String, JsonElement>) : BaseModel(_data) {

    val collectionId: String
    val collectionName: String
    val expand: Map<String, Array<Record>>


    init {
        this.collectionId = data.getOrDefault("collectionId", "") as String
        this.collectionName = data.getOrDefault("collectionName", "") as String

        this.expand = data.getOrDefault("expand", emptyMap<String, Array<Record>>())
                as Map<String, Array<Record>>
    }

}