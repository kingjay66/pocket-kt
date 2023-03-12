package github.otisgoodman.pocketKt.dsl

import github.otisgoodman.pocketKt.PocketKtDSL
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.models.Admin
import github.otisgoodman.pocketKt.services.AdminAuthService
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@PocketKtDSL
@Serializable
public open class BaseAdminBuilder {

    @PocketKtDSL
    /**
     * The admin's avatar number
     */
    public var avatar: Int? = null

    @PocketKtDSL
    /**
     * The admin's login password. (not visible ever)
     */
    public var password: String? = null

    @PocketKtDSL
    /**
     * A confirmation of the admin's password, the request will fail if this is not the same as [password].
     */
    public var passwordConfirm: String? = null

    @PocketKtDSL
    /**
     * The admin's email address, this is always visible to all admins.
     */
    public var email: String? = null

}


@PocketKtDSL
@Serializable
public class NewAdminBuilder : BaseAdminBuilder() {
    @PocketKtDSL
    /**
     * The ID of the new admin. (optional)
     */
    public var id: String? = null
}


@PocketKtDSL
/**
 * Creates a new [Admin] with the [AdminAuthService].
 */
public suspend inline fun AdminAuthService.create(setup: NewAdminBuilder.() -> Unit): Admin {
    val builder = NewAdminBuilder()
    builder.setup()
    if (builder.password == null || builder.passwordConfirm == null || builder.email == null) throw PocketbaseException(
        "A Admin's email, password or password confirmation cannot be null"
    )
    if (builder.password != builder.passwordConfirm) throw PocketbaseException("The password and password confirmation do not match")
    val json = Json.encodeToString(builder)
    return this.create(json)
}

@PocketKtDSL
/**
 * Updates an existing [Admin] with the given [id] with the [AdminAuthService].
 * @param [id] The admin's id that you wish to update.
 */
public suspend inline fun AdminAuthService.update(id: String, setup: BaseAdminBuilder.() -> Unit): Admin {
    val builder = BaseAdminBuilder()
    builder.setup()
    if (builder.password != builder.passwordConfirm) throw PocketbaseException("The password and password confirmation do not match")
    val json = Json.encodeToString(builder)
    return this.update(id, json)
}