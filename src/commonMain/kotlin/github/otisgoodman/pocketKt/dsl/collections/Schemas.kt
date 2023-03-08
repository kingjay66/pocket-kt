package github.otisgoodman.pocketKt.dsl.collections

import github.otisgoodman.pocketKt.PocketKtDSL
import github.otisgoodman.pocketKt.models.utils.SchemaField
import github.otisgoodman.pocketKt.toJsonPrimitive
import kotlinx.datetime.Instant

@PocketKtDSL
public class TextSchemaBuilder(initialMin: Int? = null, initialMax: Int? = null, initialPattern: String? = null) {
    @PocketKtDSL
    /**
     * The minimum number of characters for the field's text.
     */
    public var min: Int? = initialMin

    @PocketKtDSL
    /**
     * The maximum number of characters for the field's text.
     */
    public var max: Int? = initialMax

    @PocketKtDSL
    /**
     * The GO regex pattern that this field's text must follow.
     */
    public var pattern: String? = initialPattern
    public fun build(): SchemaField.SchemaOptions =
        SchemaField.SchemaOptions(min = min?.toJsonPrimitive(), max = max?.toJsonPrimitive(), pattern = pattern)
}

@PocketKtDSL
public class NumberSchemaBuilder(initialMin: Int? = null, initialMax: Int? = null) {
    @PocketKtDSL
    /**
     * The minimum number for the field.
     */
    public var min: Int? = initialMin

    @PocketKtDSL
    /**
     * The maximum number for the field.
     */
    public var max: Int? = initialMax
    public fun build(): SchemaField.SchemaOptions = SchemaField.SchemaOptions(min = min?.toJsonPrimitive(), max = max?.toJsonPrimitive())
}

@PocketKtDSL
public class EmailSchemaBuilder(initialExceptDomains: List<String>? = null, initialOnlyDomains: List<String>? = null) {
    @PocketKtDSL
    /**
     *  Allows emails from all domains except the ones in the list.
     */
    public var exceptDomains: List<String>? = initialExceptDomains

    @PocketKtDSL
    /**
     * Allows emails from only domains in the list.
     */
    public var onlyDomains: List<String>? = initialOnlyDomains
    public fun build(): SchemaField.SchemaOptions = SchemaField.SchemaOptions(exceptDomains = exceptDomains, onlyDomains = onlyDomains)
}

@PocketKtDSL
public class UrlSchemaBuilder(initialExceptDomains: List<String>? = null, initialOnlyDomains: List<String>? = null) {
    @PocketKtDSL
    /**
     *  Allows emails from all domains except the ones in the list.
     */
    public var exceptDomains: List<String>? = initialExceptDomains

    @PocketKtDSL
    /**
     * Allows emails from only domains in the list.
     */
    public var onlyDomains: List<String>? = initialOnlyDomains
    public fun build(): SchemaField.SchemaOptions = SchemaField.SchemaOptions(exceptDomains = exceptDomains, onlyDomains = onlyDomains)
}

@PocketKtDSL
public class DateSchemaBuilder(initialMin: Instant? = null, initialMax: Instant? = null) {
    @PocketKtDSL
    /**
     * The minimum date that can be in this field (only dates after this will work).
     */
    public var min: Instant? = initialMin

    @PocketKtDSL
    /**
     *
     */
    public var max: Instant? = initialMax
    public fun build(): SchemaField.SchemaOptions = SchemaField.SchemaOptions(min = min.toJsonPrimitive(), max.toJsonPrimitive())
}

@PocketKtDSL
public class SelectSchemaBuilder(initialValues: List<String>? = null, initialMaxSelect: Int? = null) {
    @PocketKtDSL
    /**
     * All the values that can be in this field.
     */
    public var values: List<String>? = initialValues

    @PocketKtDSL
    /**
     * The maximum amount of values that can be selected.
     */
    public var maxSelect: Int? = initialMaxSelect
    public fun build(): SchemaField.SchemaOptions = SchemaField.SchemaOptions(values = values, maxSelect = maxSelect)
}

//@TODO Add min select
@PocketKtDSL
public class FileSchemaBuilder(
    initialMaxSelect: Int? = null,
    initialMaxSize: Int? = null,
    initialMimeTypes: List<String>? = null,
    initialThumbs: List<String>? = null
) {
    @PocketKtDSL
    /**
     * The maximum amount of files that can be in this field.
     */
    public var maxSelect: Int? = initialMaxSelect

    @PocketKtDSL
    /**
     * The maximum size of each file in this field (in bytes).
     */
    public var maxSize: Int? = initialMaxSize

    /**
     * The list of allowed mime types for this field.
     */
    @PocketKtDSL
    public var mimeTypes: List<String>? = initialMimeTypes

    @PocketKtDSL
    /**
     * The list of additional thumb sizes for each image file.
     */
    public var thumbs: List<String>? = initialThumbs
    public fun build(): SchemaField.SchemaOptions =
        SchemaField.SchemaOptions(maxSelect = maxSelect, maxSize = maxSize, mimeTypes = mimeTypes, thumbs = thumbs)
}

@PocketKtDSL
public class RelationSchemaBuilder(
    initialMaxSelect: Int? = null,
    initialCollectionId: String? = null,
    initialCascadeDelete: Boolean? = null
) {
    @PocketKtDSL
    /**
     * The maximum amount of relations that can be selected.
     */
    public var maxSelect: Int? = initialMaxSelect

    @PocketKtDSL
    /**
     * The ID of the collection where the relations are found.
     */
    public var collectionId: String? = initialCollectionId

    @PocketKtDSL
    /**
     * Weather or not to delete the related record when a record in this collection is deleted.
     */
    public var cascadeDelete: Boolean? = initialCascadeDelete
    public fun build(): SchemaField.SchemaOptions =
        SchemaField.SchemaOptions(maxSelect = maxSelect, collectionId = collectionId, cascadeDelete = cascadeDelete)
}