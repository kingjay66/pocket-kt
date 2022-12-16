package github.otisgoodman.pocketKt.models

import github.otisgoodman.pocketKt.models.utils.BaseModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
//@TODO use kotlin datetime
class User(private val data_: Map<String, JsonElement>) : BaseModel(data_) {
    val email: String
    val verified: Boolean
    val lastResetSentAt: String
    val lastVerificationSentAt: String
    val profile: Record

    init {
        this.email = data.getOrDefault("email", "") as String
        this.verified = data.getOrDefault("verified", false) as Boolean
        this.lastResetSentAt = data.getOrDefault("lastResetSentAt", "") as String
        this.lastVerificationSentAt = data.getOrDefault("lastVerificationSentAt", "") as String
        this.profile = data.getOrDefault("profile", false) as Record
//        Unlikely that this cast will work, but it's worth a shot
    }

}