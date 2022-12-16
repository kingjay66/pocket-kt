package github.otisgoodman.pocketKt.stores

import github.otisgoodman.pocketKt.models.Admin
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path

open class LocalAuthStore(
    baseModel: Admin?, baseToken: String?,
    fileName: String = "pocketbase_auth.data", filePath: Path
) : BaseAuthStore(baseModel, baseToken) {
    val file: File

    init {
//      MAY NOT BE VALID CHECK LATER
        this.file = File(filePath.toString(), fileName)
    }

    fun get(): ModelTokenPair {
        return Json.decodeFromString<ModelTokenPair>(file.readText())
    }

    fun save(value: ModelTokenPair) {
        file.writeText(Json.encodeToString(value))
        super.save(value.model, value.token)
    }

    override fun clear() {
        super.clear()
        if (file.exists()) file.delete()
    }

    @Serializable
    data class ModelTokenPair(val model: Admin?, val token: String?)

}