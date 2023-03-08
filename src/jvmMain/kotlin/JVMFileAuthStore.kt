import github.otisgoodman.pocketKt.stores.BaseAuthStore
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path

//@TODO Cleanup and mark as opt in
/**
 * A Pocketbase auth store that stores your tokens in the file system.
 * This only is usable in JVM versions
 */
public class JVMFileAuthStore(
    baseToken: String?,
    fileName: String = "pocketbase_auth.data", filePath: Path? = null
) : BaseAuthStore(baseToken) {
    private val file: File
    private var filePath: Path

    init {
        if (filePath == null) this.filePath = Path(System.getProperty("user.dir"))
        else this.filePath = filePath

        this.file = File(this.filePath.toString(), fileName)
        if (file.exists()) {
            this.token = Json.decodeFromString<String>(file.readText())
        } else {
            file.createNewFile()
            save(token)
        }
    }


    public override fun save(token: String?) {
        file.writeText(Json.encodeToString(token))
        super.save(token)
    }

    override fun clear() {
        super.clear()
        if (file.exists()) file.delete()
    }

}