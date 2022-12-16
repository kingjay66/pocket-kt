package github.otisgoodman.pocketKt.models.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
class SchemaField(private val data: Map<String, JsonElement>) {

    val id: String
    val name: String
    val type: String
    val system: Boolean
    val required: Boolean
    val unique: Boolean
    val options: Map<String, JsonElement>


    init {
        this.id = data.getOrDefault("id", "") as String
        this.name = data.getOrDefault("name", "") as String
        this.type = data.getOrDefault("type", "text") as String

        this.system = data.getOrDefault("system", false) as Boolean
        this.required = data.getOrDefault("required", false) as Boolean
        this.unique = data.getOrDefault("unique", false) as Boolean

        this.options = data.getOrDefault("options", emptyMap<String, JsonElement>()) as Map<String, JsonElement>
    }

}