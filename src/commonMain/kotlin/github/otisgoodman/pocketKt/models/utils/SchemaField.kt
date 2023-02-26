package github.otisgoodman.pocketKt.models.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

@Serializable
public class SchemaField {
    public val system: Boolean? = null
    public val id: String? = null
    public val name: String? = null
    public val type: SchemaFieldType? = null
    public val required: Boolean? = null
    public val unique: Boolean? = null

    public val options: SchemaOptions? = null


    @Serializable
    public data class SchemaOptions(
        val min: JsonPrimitive? = null,
        val max: JsonPrimitive? = null,
        val pattern: String? = null,
        val exceptDomains: List<String>? = null,
        val onlyDomains: List<String>? = null,
        val values: List<String>? = null,
        val maxSelect: Int? = null,
        val collectionId: String? = null,
        val cascadeDelete: Boolean? = null,
        val maxSize: Int? = null,
        val mimeTypes: List<String>? = null,
        val thumbs: List<String>? = null
    )

    @Serializable
    public enum class SchemaFieldType {
        @SerialName("text")
        TEXT,
        @SerialName("number")
        NUMBER,
        @SerialName("bool")
        BOOL,
        @SerialName("email")
        EMAIL,
        @SerialName("url")
        URL,
        @SerialName("date")
        DATE,
        @SerialName("select")
        SELECT,
        @SerialName("json")
        JSON,
        @SerialName("file")
        FILE,
        @SerialName("relation")
        RELATION,
        @SerialName("editor")
        EDITOR
    }

    override fun toString(): String {
        return "SchemaField(system=$system, id=$id, name=$name, type=$type, required=$required, unique=$unique, options=$options)"
    }

}