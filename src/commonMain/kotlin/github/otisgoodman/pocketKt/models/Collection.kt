package github.otisgoodman.pocketKt.models

import github.otisgoodman.pocketKt.models.utils.BaseModel
import github.otisgoodman.pocketKt.models.utils.SchemaField
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//    @TODO Document
@Serializable
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

    public val options: AuthOptions? = null


    @Serializable
    public data class AuthOptions(
        val manageRule: String? = null,
        val allowOAuth2Auth: Boolean? = null,
        val allowUsernameAuth: Boolean? = null,
        val allowEmailAuth: Boolean? = null,
        val requireEmail: Boolean? = null,
        val exceptEmailDomains: List<String>? = null,
        val onlyEmailDomains: List<String>? = null,
        val minPasswordLength: Int? = null
    )

    public enum class CollectionType {
        @SerialName("base")
        BASE,
        @SerialName("auth")
        AUTH;
    }

    override fun toString(): String {
        return "Collection(name=$name, type=$type, system=$system, schema=$schema, listRule=$listRule, viewRule=$viewRule, createRule=$createRule, updateRule=$updateRule, deleteRule=$deleteRule, options=$options)"
    }

}