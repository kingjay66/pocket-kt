package github.otisgoodman.pocketKt.models

import github.otisgoodman.pocketKt.models.Collection.CollectionType
import github.otisgoodman.pocketKt.models.Collection.CollectionType.*
import github.otisgoodman.pocketKt.models.utils.BaseModel
import github.otisgoodman.pocketKt.models.utils.SchemaField
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/**
 * The object returned from the Pocketbase Collections API
 *
 * @property [name] the name of the collection
 * @property [type] the type of the collection
 * @property [system] weather or not the collection was created internally by Pocketbase
 * @property [schema] the collection's [SchemaField]s which are used to determine what values are acceptable
 * @property [listRule] the pocketbase API rule which determines who can view each [Record] in the collection
 * @property [viewRule] the pocketbase API rule which determines who can view an individual [Record] in the collection
 * @property [createRule] the pocketbase API rules which determines who can create a [Record] in the collection
 * @property [updateRule] the pocketbase API rules which determines who can update a [Record] in the collection
 * @property [deleteRule] the pocketbase API rules which determines who can delete a [Record] in the collection
 * @property [options] this collection's options (This only applicable for collections with the type of [CollectionType.AUTH])
 * */
public open class Collection : BaseModel() {

    public val name: String? = null
    public val type: CollectionType? = null
    public val system: Boolean? = null
    public val schema: MutableList<SchemaField>? = null

    public val listRule: String? = null
    public val viewRule: String? = null
    public val createRule: String? = null
    public val updateRule: String? = null
    public val deleteRule: String? = null

    public val options: SchemaOptions? = null


    @Serializable
    /**
     * a collection's options (This only applicable for collections with the type of [CollectionType.AUTH] and [CollectionType.VIEW])
     */
    public data class SchemaOptions(
        val manageRule: String? = null,
        val allowOAuth2Auth: Boolean? = null,
        val allowUsernameAuth: Boolean? = null,
        val allowEmailAuth: Boolean? = null,
        val requireEmail: Boolean? = null,
        val exceptEmailDomains: List<String>? = null,
        val onlyEmailDomains: List<String>? = null,
        val minPasswordLength: Int? = null,
        val query: String? = null
    )

    /**
     * All the supported collection types
     * @property [BASE] the base collection type, no additional options
     * @property [AUTH] an authentication collection with [SchemaOptions] (everything besides query)
     * @property [VIEW] a view collection with [SchemaOptions] (only query)
     */
    public enum class CollectionType {
        @SerialName("base")
        BASE,

        @SerialName("auth")
        AUTH,

        @SerialName("view")
        VIEW;
    }

    override fun toString(): String {
        return "Collection(name=$name, type=$type, system=$system, schema=$schema, listRule=$listRule, viewRule=$viewRule, createRule=$createRule, updateRule=$updateRule, deleteRule=$deleteRule, options=$options)"
    }

}