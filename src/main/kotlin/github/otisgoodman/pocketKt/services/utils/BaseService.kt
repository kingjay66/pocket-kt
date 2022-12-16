package github.otisgoodman.pocketKt.services.utils

import github.otisgoodman.pocketKt.Client

//@TODO Document
open class BaseService(client: Client) {
    val client: Client

    init {
        this.client = client
    }
}