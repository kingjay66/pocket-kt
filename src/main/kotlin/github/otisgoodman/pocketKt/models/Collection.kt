package github.otisgoodman.pocketKt.models

import github.otisgoodman.pocketKt.models.utils.BaseModel
import github.otisgoodman.pocketKt.models.utils.SchemaField
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
class Collection(private val data_: Map<String, JsonElement>) : BaseModel(data_) {

    val name: String
    val type: String
    val schema: MutableList<SchemaField>
    val system: Boolean
    val listRule: String?
    val viewRule: String?
    val createRule: String?
    val updateRule: String?
    val deleteRule: String?
    val options: Map<String, JsonElement>

    init {
        this.system = data.getOrDefault("system", false) as Boolean
        this.name = data.getOrDefault("name", "") as String
        this.type = data.getOrDefault("type", "base") as String
        this.options = data.getOrDefault("options", emptyMap<String, JsonElement>()) as Map<String, JsonElement>

//      Rules
        this.listRule = data.get("listRule") as String
        this.viewRule = data.get("viewRule") as String
        this.createRule = data.get("createRule") as String
        this.updateRule = data.get("updateRule") as String
        this.deleteRule = data.get("deleteRule") as String

//      Schema
//      Rough translation possible bugs
        this.schema = emptyList<SchemaField>().toMutableList()
        val dataSchema = data.getOrDefault("schema", emptyArray<SchemaField>()) as Array<SchemaField>
        dataSchema.forEach { this.schema.add(it) }
    }


//    @TODO Document

    fun isBase() = this.type == "base"

    fun isAuth() = this.type == "auth"

    fun isSingle() = this.type == "single"


}