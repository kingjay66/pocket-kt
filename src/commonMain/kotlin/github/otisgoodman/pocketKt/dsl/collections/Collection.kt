package github.otisgoodman.pocketKt.dsl.collections

import github.otisgoodman.pocketKt.PocketKtDSL
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.dsl.query.ExpandRelations
import github.otisgoodman.pocketKt.models.Collection
import github.otisgoodman.pocketKt.models.utils.SchemaField
import github.otisgoodman.pocketKt.services.CollectionService
import kotlinx.datetime.Instant
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

//@TODO Document

@OptIn(ExperimentalSerializationApi::class)
@PocketKtDSL
@Serializable
public open class BaseCollectionBuilder {
    @PocketKtDSL
    public var name: String? = null
    @PocketKtDSL
    public var type: Collection.CollectionType? = null
    @PocketKtDSL
    public var system: Boolean? = null

    @SerialName("schema")
    private val schemaFields = mutableListOf<BaseSchema>()
    @SerialName("options")
    private var collectionOptions = Collection.AuthOptions()

    @PocketKtDSL
    public var listRule: JsonPrimitive? = JsonNull
    @PocketKtDSL
    public var viewRule: JsonPrimitive? = JsonNull
    @PocketKtDSL
    public var createRule: JsonPrimitive? = JsonNull
    @PocketKtDSL
    public var updateRule: JsonPrimitive? = JsonNull
    @PocketKtDSL
    public var deleteRule: JsonPrimitive? = JsonNull

    @PocketKtDSL
    public fun schema(setup: BaseSchemaBuilder.() -> Unit) {
        val builder = BaseSchemaBuilder()
        builder.setup()
        schemaFields.add(builder.build())
    }

    @PocketKtDSL
    public fun options(setup: AuthOptionsBuilder.() -> Unit) {
        val builder = AuthOptionsBuilder()
        builder.setup()
        if (type == Collection.CollectionType.AUTH) {
            collectionOptions = builder.build()
        } else throw PocketbaseException("Collection options (currently only for auth collection types)s")
    }
}

@Serializable
public class CreateCollectionBuilder : BaseCollectionBuilder() {
    @PocketKtDSL
    public var id: String? = null
}


@Serializable
public data class BaseSchema(
    public val system: Boolean? = null,
    public val name: String? = null,
    public val type: SchemaField.SchemaFieldType? = null,
    public val required: Boolean? = null,
    public val unique: Boolean? = null,
    public val options: SchemaField.SchemaOptions = SchemaField.SchemaOptions()
)

@PocketKtDSL
public class AuthOptionsBuilder {
    @PocketKtDSL
    public var manageRule: String? = null
    @PocketKtDSL
    public var allowOAuth2Auth: Boolean? = null
    @PocketKtDSL
    public var allowUsernameAuth: Boolean? = null
    @PocketKtDSL
    public var allowEmailAuth: Boolean? = null
    @PocketKtDSL
    public var requireEmail: Boolean? = null
    @PocketKtDSL
    public var exceptEmailDomains: List<String>? = null
    @PocketKtDSL
    public var onlyEmailDomains: List<String>? = null
    @PocketKtDSL
    public var minPasswordLength: Int? = null
    internal fun build() = Collection.AuthOptions(
        manageRule,
        allowOAuth2Auth,
        allowUsernameAuth,
        allowEmailAuth,
        requireEmail,
        exceptEmailDomains,
        onlyEmailDomains,
        minPasswordLength
    )

}

@PocketKtDSL
public open class BaseSchemaBuilder {
    @PocketKtDSL
    public var system: Boolean? = null
    @PocketKtDSL
    public var name: String? = null
    internal var type: SchemaField.SchemaFieldType? = null
    @PocketKtDSL
    public var required: Boolean? = null
    @PocketKtDSL
    public var unique: Boolean? = null

    public var options: SchemaField.SchemaOptions? = null

    @PocketKtDSL
    public fun textSchema(min: Int? = null, max: Int? = null, pattern: String? = null, setup: TextSchemaBuilder.() -> Unit) {
        type = SchemaField.SchemaFieldType.TEXT
        val builder = TextSchemaBuilder(min, max, pattern)
        builder.setup()
        options = builder.build()
    }

    @PocketKtDSL
    public fun numberSchema(min: Int? = null, max: Int? = null, setup: NumberSchemaBuilder.() -> Unit) {
        type = SchemaField.SchemaFieldType.NUMBER
        val builder = NumberSchemaBuilder(min, max)
        builder.setup()
        options = builder.build()
    }

    @PocketKtDSL
    public fun booleanSchema() {
        type = SchemaField.SchemaFieldType.BOOL
    }

    @PocketKtDSL
    public fun emailSchema(
        exceptDomains: List<String>? = null,
        onlyDomains: List<String>? = null,
        setup: EmailSchemaBuilder.() -> Unit
    ) {
        type = SchemaField.SchemaFieldType.EMAIL
        val builder = EmailSchemaBuilder(exceptDomains, onlyDomains)
        builder.setup()
        options = builder.build()
    }

    @PocketKtDSL
    public fun urlSchema(
        exceptDomains: List<String>? = null,
        onlyDomains: List<String>? = null,
        setup: UrlSchemaBuilder.() -> Unit
    ) {
        type = SchemaField.SchemaFieldType.URL
        val builder = UrlSchemaBuilder(exceptDomains, onlyDomains)
        builder.setup()
        options = builder.build()
    }

    @PocketKtDSL
    public fun dateSchema(min: Instant? = null, max: Instant? = null, setup: DateSchemaBuilder.() -> Unit) {
        type = SchemaField.SchemaFieldType.DATE
        val builder = DateSchemaBuilder(min, max)
        builder.setup()
        options = builder.build()
    }

    @PocketKtDSL
    public fun selectSchema(values: List<String>? = null, maxSelect: Int? = null, setup: SelectSchemaBuilder.() -> Unit) {
        type = SchemaField.SchemaFieldType.SELECT
        val builder = SelectSchemaBuilder(values, maxSelect)
        builder.setup()
        options = builder.build()
    }

    @PocketKtDSL
    public fun jsonSchema() {
        type = SchemaField.SchemaFieldType.JSON
    }

    @PocketKtDSL
    public fun fileSchema(
        maxSelect: Int? = null,
        maxSize: Int? = null,
        mimeTypes: List<String>? = null,
        thumbs: List<String>? = null,
        setup: FileSchemaBuilder.() -> Unit
    ) {
        type = SchemaField.SchemaFieldType.FILE
        val builder = FileSchemaBuilder(maxSelect, maxSize, mimeTypes, thumbs)
        builder.setup()
        options = builder.build()
    }

    @PocketKtDSL
    public fun relationSchema(
        maxSelect: Int? = null,
        collectionId: String? = null,
        cascadeDelete: Boolean? = null,
        setup: RelationSchemaBuilder.() -> Unit
    ) {
        type = SchemaField.SchemaFieldType.RELATION
        val builder = RelationSchemaBuilder(maxSelect, collectionId, cascadeDelete)
        builder.setup()
        options = builder.build()
    }

    @PocketKtDSL
    public fun editorSchema() {
        type = SchemaField.SchemaFieldType.EDITOR
    }

    internal fun build(): BaseSchema {
        return BaseSchema(system, name, type, required, unique, options ?: SchemaField.SchemaOptions())
    }
}


public suspend inline fun CollectionService.update(
    id: String,
    expandRelations: ExpandRelations = ExpandRelations(),
    setup: BaseCollectionBuilder.() -> Unit
): Collection {
    val builder = BaseCollectionBuilder()
    builder.setup()
    val json = Json.encodeToString(builder)
    return this.update(id, json, expandRelations)
}

public suspend inline fun CollectionService.create(
    expandRelations: ExpandRelations = ExpandRelations(),
    setup: CreateCollectionBuilder.() -> Unit
): Collection {
    val builder = CreateCollectionBuilder()
    builder.setup()
    val json = Json.encodeToString(builder)
    return this.create<Collection>(json, expandRelations)
}