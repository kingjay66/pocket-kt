package github.otisgoodman.pocketKt.models.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement

@Serializable
open class BaseModel(@Transient val data: Map<String, JsonElement> = mapOf()) {
    val id: String = data.getOrDefault("id", "") as String
    val created: String = data.getOrDefault("created", "") as String
    val updated: String = data.getOrDefault("updated", "") as String


    fun isNew(): Boolean {
        return this.id != ""
    }

}