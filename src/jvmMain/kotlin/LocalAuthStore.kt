import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.models.Admin
import github.otisgoodman.pocketKt.models.User
import github.otisgoodman.pocketKt.stores.BaseAuthStore
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path

public open class LocalAuthStore(
    client: PocketbaseClient,
    admin: Admin?, user: User?, baseToken: String?,
    fileName: String = "pocketbase_auth.data", filePath: Path? = null
) : BaseAuthStore(admin, user, baseToken) {
    private val file: File
    private var filePath: Path
    private val client = client

    init {
        if (filePath == null) this.filePath = Path(System.getProperty("user.dir"))
        else this.filePath = filePath

        this.file = File(this.filePath.toString(), fileName)
        if (file.exists()) {
            val pair = Json.decodeFromString<ModelTokenPair>(file.readText())
            this.token = pair.token
            this.admin = pair.admin
            this.user = pair.user
        } else {
            file.createNewFile()
            save(ModelTokenPair(admin, user, token))
        }
    }

    public fun save(value: ModelTokenPair) {
        file.writeText(Json.encodeToString(value))
        super.save(value.admin, value.user, value.token)
    }

    public fun save(admin: Admin?, token: String?) {
        file.writeText(Json.encodeToString(ModelTokenPair(admin, null, token)))
        super.save(admin, null, token)
    }

    public fun save(user: User?, token: String?) {
        file.writeText(Json.encodeToString(ModelTokenPair(null, user, token)))
        super.save(null, user, token)
    }

    override fun clear() {
        super.clear()
        if (file.exists()) file.delete()
    }

    @Serializable
    public data class ModelTokenPair(val admin: Admin?, val user: User?, val token: String?)

}