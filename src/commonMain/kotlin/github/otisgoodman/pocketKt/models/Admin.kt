package github.otisgoodman.pocketKt.models


import github.otisgoodman.pocketKt.models.utils.BaseModel
import kotlinx.serialization.Serializable

@Serializable
public open class Admin(
    public val avatar: Int? = null,
    public val email: String? = null
) : BaseModel() {

    override fun toString(): String {
        return "Admin(avatar=$avatar, email=$email)"
    }
}