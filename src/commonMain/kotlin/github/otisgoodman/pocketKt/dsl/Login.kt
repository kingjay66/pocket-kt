package github.otisgoodman.pocketKt.dsl

import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.PocketKtDSL
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.models.Admin
import github.otisgoodman.pocketKt.models.User

//@TODO Document

@PocketKtDSL
public open class TokenLoginBuilder(initialToken: String?) {

    @PocketKtDSL
    public var token: String? = initialToken

}

@PocketKtDSL
public class AdminLoginBuilder(initialModel: Admin?, initialToken: String?) : TokenLoginBuilder(initialToken) {

    @PocketKtDSL
    public var admin: Admin? = initialModel
}

@PocketKtDSL
public class UserLoginBuilder(initialModel: User?, initialToken: String?) : TokenLoginBuilder(initialToken) {

    @PocketKtDSL
    public var user: User? = initialModel
}


@PocketKtDSL
public inline fun PocketbaseClient.loginAdmin(setup: AdminLoginBuilder.() -> Unit = {}) {
    val store = this.authStore
    val loginBuilder = AdminLoginBuilder(store.admin, store.token)
    loginBuilder.setup()
    this.authStore.save(admin=loginBuilder.admin,null, token=loginBuilder.token)
    if (store.admin == null || store.token == null) throw PocketbaseException("Authorization cannot be null!")
}

@PocketKtDSL
public inline fun PocketbaseClient.loginUser(setup: UserLoginBuilder.() -> Unit = {}) {
    val store = this.authStore
    val loginBuilder = UserLoginBuilder(store.user, store.token)
    loginBuilder.setup()
    this.authStore.save(user=loginBuilder.user,admin=null, token=loginBuilder.token)
    if (store.user == null || store.token == null) throw PocketbaseException("Authorization cannot be null!")
}

@PocketKtDSL
public inline fun PocketbaseClient.loginToken(setup: TokenLoginBuilder.() -> Unit = {}) {
    val store = this.authStore
    val loginBuilder = TokenLoginBuilder(store.token)
    loginBuilder.setup()
    this.authStore.save(admin = null, user = null, loginBuilder.token)
    if (store.token == null) throw PocketbaseException("Authorization cannot be null!")
}

public fun PocketbaseClient.logout() {
    this.authStore.clear()
}