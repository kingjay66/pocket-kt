package github.otisgoodman.pocketKt.models.utils

import github.otisgoodman.pocketKt.models.Admin
import github.otisgoodman.pocketKt.models.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement
@Serializable
open class BaseAuthModel(private val record: Map<String, JsonElement>, @Transient val type: AuthModelType = AuthModelType.User) : BaseModel(record) {
    val email: String = data.getOrDefault("email", "") as String


    fun toAdmin(): Admin = Admin(data)

    fun toUser(): User = User(data)


}

enum class AuthModelType{
    User,Admin;
}