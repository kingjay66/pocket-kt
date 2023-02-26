package github.otisgoodman.pocketKt.dsl

import github.otisgoodman.pocketKt.*
import github.otisgoodman.pocketKt.dsl.query.ExpandRelations
import github.otisgoodman.pocketKt.models.User
import github.otisgoodman.pocketKt.services.UserAuthService
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

//@TODO Document

@PocketKtDSL
@Serializable
public open class BaseUserParams {
    @PocketKtDSL
    public var username: String? = null

    @PocketKtDSL
    public var emailVisibility: Boolean? = null

    @PocketKtDSL
    public var verified: Boolean? = null

    @PocketKtDSL
    public var password: String? = null

    @PocketKtDSL
    public var passwordConfirm: String? = null

    @PocketKtDSL
    public var email: String? = null
}


@PocketKtDSL
@Serializable
public class NewUserBuilder : BaseUserParams() {
    @PocketKtDSL
    public var id: String? = null
}

@PocketKtDSL
@Serializable
public class EditUserBuilder : BaseUserParams() {
    @PocketKtDSL
    public val oldPassword: String? = null
}


@PocketKtDSL
public suspend inline fun UserAuthService.create(
    expandRelations: ExpandRelations = ExpandRelations(),
    setup: NewUserBuilder.() -> Unit
): User {
    val builder = NewUserBuilder()
    builder.setup()
    if (builder.password == null || builder.passwordConfirm == null) throw PocketbaseException("A User's password or password confirmation cannot be null")
    if (builder.password != builder.passwordConfirm) throw PocketbaseException("The password and password confirmation do not match")
    val body = Json.encodeToString(builder)
    return this.create(body,expandRelations)
}

@PocketKtDSL
public suspend inline fun UserAuthService.update(
    id: String,
    expandRelations: ExpandRelations = ExpandRelations(),
    setup: EditUserBuilder.() -> Unit
): User {
    val builder = EditUserBuilder()
    builder.setup()
    if (builder.password != builder.passwordConfirm) throw PocketbaseException("The password and password confirmation do not match")
    val body = Json.encodeToString(builder)
    return this.update(id,body,expandRelations)
}