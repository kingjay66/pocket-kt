package github.otisgoodman.pocketKt.models

import github.otisgoodman.pocketKt.models.utils.BaseModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
class LogRequest(val _data: Map<String, JsonElement>) : BaseModel(_data) {

    val url: String
    val method: String
    val status: Int
    val auth: String
    val remoteIp: String
    val userIp: String
    val referer: String
    val userAgent: String
    val meta: Map<String, JsonElement>

    init {
//        Did not add backward compatibility for the ip field!


        this.url = data.getOrDefault("url", "") as String
        this.method = data.getOrDefault("method", "GET") as String
        this.status = data.getOrDefault("status", 200) as Int
        this.auth = data.getOrDefault("auth", "guest") as String
        this.remoteIp = data.getOrDefault("remoteIp", "") as String
        this.userIp = data.getOrDefault("userIp", "") as String
        this.referer = data.getOrDefault("referer", "") as String
        this.userAgent = data.getOrDefault("userAgent", "") as String

        this.meta = data.getOrDefault("meta", emptyMap<String, JsonElement>()) as Map<String, JsonElement>
    }

}