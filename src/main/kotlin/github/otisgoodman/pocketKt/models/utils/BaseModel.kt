package github.otisgoodman.pocketKt.models.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
open class BaseModel(val data: Map<String, JsonElement>) {
    val id: String
    val created: String
    val updated: String


    init {
        this.id = data.getOrDefault("id", "") as String
        this.created = data.getOrDefault("created", "") as String
        this.updated = data.getOrDefault("updated", "") as String
    }


    fun isNew(): Boolean {
        return this.id != ""
    }

}