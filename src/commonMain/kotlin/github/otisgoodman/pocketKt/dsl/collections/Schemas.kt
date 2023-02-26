package github.otisgoodman.pocketKt.dsl.collections

import github.otisgoodman.pocketKt.PocketKtDSL
import github.otisgoodman.pocketKt.models.utils.SchemaField
import github.otisgoodman.pocketKt.toJsonPrimitive
import kotlinx.datetime.Instant

@PocketKtDSL
public class TextSchemaBuilder(initialMin: Int? = null, initialMax: Int? = null, initialPattern: String? = null) {
    @PocketKtDSL
    public var min: Int? = initialMin
    @PocketKtDSL
    public var max: Int? = initialMax
    @PocketKtDSL
    public var pattern: String? = initialPattern
    public fun build(): SchemaField.SchemaOptions =
        SchemaField.SchemaOptions(min = min?.toJsonPrimitive(), max = max?.toJsonPrimitive(), pattern = pattern)
}

@PocketKtDSL
public class NumberSchemaBuilder(initialMin: Int? = null, initialMax: Int? = null) {
    @PocketKtDSL
    public var min: Int? = initialMin
    @PocketKtDSL
    public var max: Int? = initialMax
    public fun build(): SchemaField.SchemaOptions = SchemaField.SchemaOptions(min = min?.toJsonPrimitive(), max = max?.toJsonPrimitive())
}

@PocketKtDSL
public class EmailSchemaBuilder(initialExceptDomains: List<String>? = null, initialOnlyDomains: List<String>? = null) {
    @PocketKtDSL
    public var exceptDomains: List<String>? = initialExceptDomains
    @PocketKtDSL
    public var onlyDomains: List<String>? = initialOnlyDomains
    public fun build(): SchemaField.SchemaOptions = SchemaField.SchemaOptions(exceptDomains = exceptDomains, onlyDomains = onlyDomains)
}

@PocketKtDSL
public class UrlSchemaBuilder(initialExceptDomains: List<String>? = null, initialOnlyDomains: List<String>? = null) {
    @PocketKtDSL
    public var exceptDomains: List<String>? = initialExceptDomains
    @PocketKtDSL
    public var onlyDomains: List<String>? = initialOnlyDomains
    public fun build(): SchemaField.SchemaOptions = SchemaField.SchemaOptions(exceptDomains = exceptDomains, onlyDomains = onlyDomains)
}

@PocketKtDSL
public class DateSchemaBuilder(initialMin: Instant? = null, initialMax: Instant? = null) {
    @PocketKtDSL
    public var min: Instant? = initialMin
    @PocketKtDSL
    public var max: Instant? = initialMax
    public fun build(): SchemaField.SchemaOptions = SchemaField.SchemaOptions(min = min.toJsonPrimitive(), max.toJsonPrimitive())
}

@PocketKtDSL
public class SelectSchemaBuilder(initialValues: List<String>? = null, initialMaxSelect: Int? = null) {
    @PocketKtDSL
    public var values: List<String>? = initialValues
    @PocketKtDSL
    public var maxSelect: Int? = initialMaxSelect
    public fun build(): SchemaField.SchemaOptions = SchemaField.SchemaOptions(values = values, maxSelect = maxSelect)
}

@PocketKtDSL
public class FileSchemaBuilder(
    initialMaxSelect: Int? = null,
    initialMaxSize: Int? = null,
    initialMimeTypes: List<String>? = null,
    initialThumbs: List<String>? = null
) {
    @PocketKtDSL
    public var maxSelect: Int? = initialMaxSelect
    @PocketKtDSL
    public var maxSize: Int? = initialMaxSize
    @PocketKtDSL
    public var mimeTypes: List<String>? = initialMimeTypes
    @PocketKtDSL
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
    public var maxSelect: Int? = initialMaxSelect
    @PocketKtDSL
    public var collectionId: String? = initialCollectionId
    @PocketKtDSL
    public var cascadeDelete: Boolean? = initialCascadeDelete
    public fun build(): SchemaField.SchemaOptions =
        SchemaField.SchemaOptions(maxSelect = maxSelect, collectionId = collectionId, cascadeDelete = cascadeDelete)
}