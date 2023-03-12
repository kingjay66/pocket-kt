package github.otisgoodman.pocketKt.dsl.collections

import github.otisgoodman.pocketKt.PocketKtDSL
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.dsl.query.ExpandRelations
import github.otisgoodman.pocketKt.models.Collection
import github.otisgoodman.pocketKt.models.Collection.CollectionType
import github.otisgoodman.pocketKt.models.Collection.CollectionType.*
import github.otisgoodman.pocketKt.models.Record
import github.otisgoodman.pocketKt.models.utils.SchemaField
import github.otisgoodman.pocketKt.services.CollectionService
import kotlinx.datetime.Instant
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive


@PocketKtDSL
@Serializable
public open class BaseCollectionBuilder {
    @PocketKtDSL
    /**
     * The [Collection]'s name
     */
    public var name: String? = null

    @PocketKtDSL
    /**
     * The [Collection]'s type.
     * Collections of the [AUTH] collection type require custom [options].
     */
    public var type: Collection.CollectionType? = null

    @PocketKtDSL
    /**
     * Weather or not the collection is a system generate collection.
     */
    public var system: Boolean? = null

    @SerialName("schema")
    private val schemaFields = mutableListOf<BaseSchema>()

    @SerialName("options")
    private var collectionOptions = Collection.AuthOptions()

    @PocketKtDSL
    /**
     * The API delete rule which determines who can list records [Record] from this [Collection].
     * Check the [Pocketbase docs](https://pocketbase.io/docs/manage-collections/#rules-filters-syntax) for more info
     */
    public var listRule: JsonPrimitive? = JsonNull

    @PocketKtDSL
    /**
     * The API delete rule which determines who can view a [Record] from this [Collection].
     * Check the [Pocketbase docs](https://pocketbase.io/docs/manage-collections/#rules-filters-syntax) for more info
     */
    public var viewRule: JsonPrimitive? = JsonNull

    @PocketKtDSL
    /**
     * The API delete rule which determines who can create a [Record] from this [Collection].
     * Check the [Pocketbase docs](https://pocketbase.io/docs/manage-collections/#rules-filters-syntax) for more info
     */
    public var createRule: JsonPrimitive? = JsonNull

    @PocketKtDSL
    /**
     * The API delete rule which determines who can update a [Record] from this [Collection].
     * Check the [Pocketbase docs](https://pocketbase.io/docs/manage-collections/#rules-filters-syntax) for more info
     */
    public var updateRule: JsonPrimitive? = JsonNull

    @PocketKtDSL
    /**
     * The API delete rule which determines who can delete a [Record] from this [Collection].
     * Check the [Pocketbase docs](https://pocketbase.io/docs/manage-collections/#rules-filters-syntax) for more info
     */
    public var deleteRule: JsonPrimitive? = JsonNull

    @PocketKtDSL
    /**
     * Adds a new [SchemaField] to the collection's schema.
     */
    public fun schema(setup: BaseSchemaBuilder.() -> Unit) {
        val builder = BaseSchemaBuilder()
        builder.setup()
        schemaFields.add(builder.build())
    }

    @PocketKtDSL
    /**
     * Sets the [Collection]'s options. Only applicable to collections with the [CollectionType] of [AUTH].
     */
    public fun options(setup: AuthOptionsBuilder.() -> Unit) {
        val builder = AuthOptionsBuilder()
        builder.setup()
        if (type == AUTH) {
            collectionOptions = builder.build()
        } else throw PocketbaseException("Collection options is currently only for auth collection types")
    }
}

@Serializable
public class CreateCollectionBuilder : BaseCollectionBuilder() {
    @PocketKtDSL
    /**
     * The ID of the new collection. (optional)
     */
    public var id: String? = null
}


@Serializable
public data class BaseSchema(
    val system: Boolean? = null,
    val name: String? = null,
    val type: SchemaField.SchemaFieldType? = null,
    val required: Boolean? = null,
    val unique: Boolean? = null,
    val options: SchemaField.SchemaOptions = SchemaField.SchemaOptions()
)

@PocketKtDSL
public class AuthOptionsBuilder {
    @PocketKtDSL
    /**
     * The API manage rule which determines who can manage records [Record] from this [Collection].
     * Check the [Pocketbase docs](https://pocketbase.io/docs/manage-collections/#rules-filters-syntax) for more info
     */
    public var manageRule: String? = null

    @PocketKtDSL
    /**
     * Weather or not to allow users to authenticate with oauth2.
     */
    public var allowOAuth2Auth: Boolean? = null

    @PocketKtDSL
    /**
     * Weather or not to allow users to authenticate with username and password.
     */
    public var allowUsernameAuth: Boolean? = null

    @PocketKtDSL
    /**
     * Weather or not to allow users to authenticate with email and password.
     */
    public var allowEmailAuth: Boolean? = null

    @PocketKtDSL
    /**
     * Whether to always require email address when creating or updating auth records.
     */
    public var requireEmail: Boolean? = null

    @PocketKtDSL
    /**
     * Whether to allow sign-ups only with the email domains not listed in the specified list.
     */
    public var exceptEmailDomains: List<String>? = null

    @PocketKtDSL
    /**
     * Whether to allow sign-ups only with the email domains listed in the specified list.
     */
    public var onlyEmailDomains: List<String>? = null

    @PocketKtDSL
    /**
     * The minimum required password length for new auth records.
     */
    public var minPasswordLength: Int? = null
    public fun build(): Collection.AuthOptions = Collection.AuthOptions(
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
    /**
     * Weather or not the schema field is generated.
     */
    public var system: Boolean? = null

    @PocketKtDSL
    /**
     * The name of the schema field.
     */
    public var name: String? = null
    internal var type: SchemaField.SchemaFieldType? = null

    @PocketKtDSL
    /**
     * Weather or not a value is required to be in the schema field. (if the schema field is boolean type this means the field can only be true)
     */
    public var required: Boolean? = null

    @PocketKtDSL
    /**
     * Weather or not the value of the schema field should be unique.
     */
    public var unique: Boolean? = null

    private var options: SchemaField.SchemaOptions? = null

    @PocketKtDSL
    /**
     * Designates this schema field as a text schema field.
     * @param [min] The minimum number of characters for the field's text.
     * @param [max] The maximum number of characters for the field's text.
     * @param [pattern] The GO regex pattern that this field's text must follow.
     */
    public fun textSchema(
        min: Int? = null,
        max: Int? = null,
        pattern: String? = null,
        setup: TextSchemaBuilder.() -> Unit
    ) {
        type = SchemaField.SchemaFieldType.TEXT
        val builder = TextSchemaBuilder(min, max, pattern)
        builder.setup()
        options = builder.build()
    }

    @PocketKtDSL
    /**
     * Designates this schema field as a number schema field.
     * @param [min] The minimum number for the field.
     * @param [max] The maximum number for the field.
     */
    public fun numberSchema(min: Int? = null, max: Int? = null, setup: NumberSchemaBuilder.() -> Unit) {
        type = SchemaField.SchemaFieldType.NUMBER
        val builder = NumberSchemaBuilder(min, max)
        builder.setup()
        options = builder.build()
    }

    @PocketKtDSL
    /**
     * Designates this schema field as a boolean schema field.
     */
    public fun booleanSchema() {
        type = SchemaField.SchemaFieldType.BOOL
    }

    @PocketKtDSL
    /**
     * Designates this schema field as an email schema field.
     * @param [exceptDomains] Allows emails from all domains except the ones in the list.
     * @param [onlyDomains] Allows emails from only domains in the list.
     */
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
    /**
     * Designates this schema field as a URL schema field.
     * @param [exceptDomains] Allows urls from all domains except the ones in the list.
     * @param [onlyDomains] Allows urls from only domains in the list.
     */
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
    /**
     * Designates this schema field as a date schema field.
     * @param [min] The minimum date that can be in this field (only dates after this will work).
     * @param [max] The maximum date that can be in this field (only dates before this will work).
     */
    public fun dateSchema(min: Instant? = null, max: Instant? = null, setup: DateSchemaBuilder.() -> Unit) {
        type = SchemaField.SchemaFieldType.DATE
        val builder = DateSchemaBuilder(min, max)
        builder.setup()
        options = builder.build()
    }

    @PocketKtDSL
    /**
     * Designates this schema field as a select schema field.
     * @param [values] All the values that can be in this field.
     * @param [maxSelect] The maximum amount of values that can be selected.
     */
    public fun selectSchema(
        values: List<String>? = null,
        maxSelect: Int? = null,
        setup: SelectSchemaBuilder.() -> Unit
    ) {
        type = SchemaField.SchemaFieldType.SELECT
        val builder = SelectSchemaBuilder(values, maxSelect)
        builder.setup()
        options = builder.build()
    }

    @PocketKtDSL
    /**
     *  Designates this schema field as a json schema field.
     */
    public fun jsonSchema() {
        type = SchemaField.SchemaFieldType.JSON
    }

    @PocketKtDSL
    /**
     * Designates this schema field as a file schema field.
     * @param [maxSelect] The maximum amount of files that can be in this field.
     * @param [maxSize] The maximum size of each file in this field (in bytes).
     * @param [mimeTypes] The list of allowed mime types for this field.
     * @param [thumbs] The list of additional thumb sizes for each image file.
     */
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
    /**
     * Designates this schema field as a relation schema field.
     * @param [maxSelect] The maximum amount of relations that can be selected.
     * @param [collectionId] The ID of the collection where the relations are found.
     * @param [cascadeDelete] Weather or not to delete the related record when a record in this collection is deleted.
     * @TODO Check and add additional fields if needed because there might be more added in 0.13
     */
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
    /**
     * Designates this schema field as an editor schema field.
     */
    public fun editorSchema() {
        type = SchemaField.SchemaFieldType.EDITOR
    }

    public fun build(): BaseSchema {
//        @TODO Throw if view collection
        return BaseSchema(system, name, type, required, unique, options ?: SchemaField.SchemaOptions())
    }
}

/**
 * Updates an existing [Collection] with the given [id].
 * @param [id] The collection's id that you wish to update.
 */
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

/**
 * Creates a [Collection].
 */
public suspend inline fun CollectionService.create(
    expandRelations: ExpandRelations = ExpandRelations(),
    setup: CreateCollectionBuilder.() -> Unit
): Collection {
    val builder = CreateCollectionBuilder()
    builder.setup()
    val json = Json.encodeToString(builder)
    return this.create(json, expandRelations)
}