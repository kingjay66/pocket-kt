package github.otisgoodman.pocketKt.models

import kotlinx.serialization.Serializable


//@TODO Document
@Serializable
public class User(
    public val verified: Boolean? = null,
    public open val username: String? = null,
    public val email: String? = null,
    public val emailVisibility: Boolean? = null,
) : Record() {
    override fun toString(): String {
        return "User(verified=$verified, username=$username, email=$email, emailVisibility=$emailVisibility)"
    }
}