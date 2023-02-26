package github.otisgoodman.pocketKt.dsl

import github.otisgoodman.pocketKt.PocketKtDSL
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.models.Admin
import github.otisgoodman.pocketKt.services.AdminAuthService
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

//@TODO Document

@PocketKtDSL
@Serializable
public open class BaseAdminBuilder {

    @PocketKtDSL
    public var avatar: Int? = null

    @PocketKtDSL
    public var password: String? = null

    @PocketKtDSL
    public var passwordConfirm: String? = null

    @PocketKtDSL
    public var email: String? = null

}


@PocketKtDSL
@Serializable
public class NewAdminBuilder : BaseAdminBuilder() {
    @PocketKtDSL
    public var id: String? = null
}


@PocketKtDSL
public suspend inline fun AdminAuthService.create(setup: NewAdminBuilder.() -> Unit): Admin {
    val builder = NewAdminBuilder()
    builder.setup()
    if (builder.password == null || builder.passwordConfirm == null || builder.email == null) throw PocketbaseException(
        "A Admin's email, password or password confirmation cannot be null"
    )
    if (builder.password != builder.passwordConfirm) throw PocketbaseException("The password and password confirmation do not match")
    val json = Json.encodeToString(builder)
    return this.create<Admin>(json)
}

@PocketKtDSL
public suspend inline fun AdminAuthService.update(id: String, setup: BaseAdminBuilder.() -> Unit): Admin {
    val builder = BaseAdminBuilder()
    builder.setup()
    if (builder.password != builder.passwordConfirm) throw PocketbaseException("The password and password confirmation do not match")
    val json = Json.encodeToString(builder)
    return this.update(id, json)
}