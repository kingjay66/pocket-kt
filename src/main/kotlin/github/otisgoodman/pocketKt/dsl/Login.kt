package github.otisgoodman.pocketKt.dsl

import github.otisgoodman.pocketKt.Client
import github.otisgoodman.pocketKt.PocketKtDSL
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.models.utils.AuthModelType
import github.otisgoodman.pocketKt.models.utils.BaseAuthModel
import github.otisgoodman.pocketKt.stores.LocalAuthStore
import kotlin.io.path.Path



@PocketKtDSL
class LoginBuilder(initialModel: BaseAuthModel?, initialToken: String?){

    @PocketKtDSL
    var model = initialModel

    @PocketKtDSL
    var token = initialToken

}

@PocketKtDSL
fun Client.login(setup: LoginBuilder.() -> Unit = {}){
    val store = this.authStore

    val loginBuilder: LoginBuilder = LoginBuilder(store.model,store.token)
    loginBuilder.setup()
    this.authStore.save(LocalAuthStore.ModelTokenPair(loginBuilder.model,loginBuilder.token))

    if (store.model == null || store.token == null) throw PocketbaseException("Authorization cannot be null!")

}