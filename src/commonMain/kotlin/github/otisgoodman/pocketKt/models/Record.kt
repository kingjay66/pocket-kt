package github.otisgoodman.pocketKt.models

import github.otisgoodman.pocketKt.models.utils.BaseModel
import kotlinx.serialization.Serializable

@Serializable
public open class Record : BaseModel() {
    public val collectionId: String? = null
    public val collectionName: String? = null
    override fun toString(): String {
        return "Record(collectionId=$collectionId, collectionName=$collectionName)"
    }
}