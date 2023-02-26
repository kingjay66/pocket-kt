package github.otisgoodman.pocketKt.models.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
public open class BaseModel {
    public val id: String? = null

    @SerialName("created")
    public val initialCreated: String? = null
    @SerialName("updated")
    public val initialUpdated: String? = null

    @Transient
    public val created: Instant? = initialCreated?.replace(" ", "T")?.toInstant()
    @Transient
    public val updated: Instant? = initialUpdated?.replace(" ", "T")?.toInstant()


    override fun toString(): String {
        return "BaseModel(id=$id, created=$initialCreated, updated=$initialUpdated)"
    }

}