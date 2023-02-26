package github.otisgoodman.pocketKt.dsl.query

import github.otisgoodman.pocketKt.models.Record
import github.otisgoodman.pocketKt.toFieldList
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

//@TODO Document
public data class ExpandRelations(val relations: String) {
    public constructor(vararg relations: String) : this(relations.toFieldList())

    public fun addTo(params: ParametersBuilder) {
        params.append("expand", this.relations)
    }
}

@Serializable
public open class ExpandRecord<T : Record> : Record() {
    public val expand: Map<String, T>? = null

    override fun toString(): String {
        return "ExpandRecord(expand=$expand)"
    }
}

@Serializable
public open class ExpandJsonElement : Record() {
    public val expand: Map<String, JsonElement>? = null

    override fun toString(): String {
        return "ExpandJsonElement(expand=$expand)"
    }
}