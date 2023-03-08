@file:OptIn(InitialProperty::class)
package github.otisgoodman.pocketKt.models.utils

import github.otisgoodman.pocketKt.InitialProperty
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
/**
 * The base class used for all [Pocketbase](https://pocketbase.io) models used
 *
 * @property [id] The unique ID of the model
 * @property [initialCreated] the raw created field from the model formatted in the GO timestamp format.
 * Use [created] to access the type of [Instant]
 * @property [initialUpdated] the raw updated field from the model formatted in the GO timestamp format.
 * Use [updated] to access the type of [Instant]
 *
 */
public open class BaseModel {
    public val id: String? = null

    @SerialName("created")
    @InitialProperty
    public val initialCreated: String? = null

    @SerialName("updated")
    @InitialProperty
    public val initialUpdated: String? = null

    @Transient
    public val created: Instant? = initialCreated?.replace(" ", "T")?.toInstant()

    @Transient
    public val updated: Instant? = initialUpdated?.replace(" ", "T")?.toInstant()


    override fun toString(): String {
        return "BaseModel(id=$id, created=$initialCreated, updated=$initialUpdated)"
    }

}