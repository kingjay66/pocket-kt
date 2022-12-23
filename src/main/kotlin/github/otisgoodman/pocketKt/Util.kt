package github.otisgoodman.pocketKt

import github.otisgoodman.pocketKt.models.utils.BaseAuthModel
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement


fun Set<Pair<String, Any>>.containsKey(s: String): Boolean {
    for (pair in this) {
        if (pair.first == s) return true
    }
    return false
}

data class AuthResponse(val token: String, val record: BaseAuthModel)

@Serializable
data class PocketbaseError(val code: Int, val message:String, val data: Map<String,JsonElement>)

@DslMarker
annotation class PocketKtDSL

class PocketbaseException(reason: String): Exception(reason){
    constructor(error:PocketbaseError): this("${error.message}: ${error.code}\n ${Json.encodeToString(error.data)}")

    companion object{
        suspend fun handle(response: HttpResponse){
            if (!response.status.isSuccess()) throw PocketbaseException(response.body<PocketbaseError>())
        }
    }
}