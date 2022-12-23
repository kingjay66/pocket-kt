package github.otisgoodman.pocketKt.stores

import github.otisgoodman.pocketKt.models.Admin
import github.otisgoodman.pocketKt.models.utils.BaseAuthModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path

open class LocalAuthStore(
    baseModel: BaseAuthModel?, baseToken: String?,
    fileName: String = "pocketbase_auth.data", filePath: Path? = null
) : BaseAuthStore(baseModel, baseToken) {
    private val file: File
    var filePath: Path

    init {
//      MAY NOT BE VALID CHECK LATER
        if (filePath == null) this.filePath = Path(System.getProperty("user.dir"))
        else this.filePath = filePath

        this.file = File(this.filePath.toString(), fileName)
        if (file.exists()){
            val pair = Json.decodeFromString<ModelTokenPair>(file.readText())
            this.token = pair.token
            this.model = pair.model
        }else{
            file.createNewFile()
            save(ModelTokenPair(model, token))
        }
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
    data class ModelTokenPair(val model: BaseAuthModel?, val token: String?)

}