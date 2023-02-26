package github.otisgoodman.pocketKt.stores

import github.otisgoodman.pocketKt.models.Admin
import github.otisgoodman.pocketKt.models.User

public open class BaseAuthStore(admin: Admin?, user: User?, baseToken: String?) {

    //Watered down from JS SDK may improve later although Kotlin probably doesn't use cookies
    //    @TODO Document
    public var admin: Admin?
    public var user: User?
    public var token: String?

    init {
        this.admin = admin
        this.user = user

        this.token = baseToken
    }

    public fun save(admin: Admin?, user: User?, token: String?) {
        this.admin = admin
        this.user = user
        this.token = token
    }

    public open fun clear() {
        this.admin = null
        this.user = null
        this.token = null
    }
}