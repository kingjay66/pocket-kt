package github.otisgoodman.pocketKt.models

import github.otisgoodman.pocketKt.models.utils.AuthModelType
import github.otisgoodman.pocketKt.models.utils.BaseAuthModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


//@TODO use kotlin datetime
//@TODO Document
//@TODO Add More Fields
class User(private val data_: Map<String, JsonElement>) : BaseAuthModel(data_,AuthModelType.User) {
    val verified: Boolean
    val lastResetSentAt: String
    val lastVerificationSentAt: String

    init {
        this.verified = data.getOrDefault("verified", false) as Boolean
        this.lastResetSentAt = data.getOrDefault("lastResetSentAt", "") as String
        this.lastVerificationSentAt = data.getOrDefault("lastVerificationSentAt", "") as String
    }

}