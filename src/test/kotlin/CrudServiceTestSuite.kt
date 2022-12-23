import github.otisgoodman.pocketKt.models.utils.BaseModel
import github.otisgoodman.pocketKt.services.utils.CrudService
import org.junit.jupiter.api.DisplayName
import kotlin.test.*
import kotlinx.coroutines.*
abstract class CrudServiceTestSuite<T: BaseModel>(service: CrudService<T>, expectedBasePath: String) {

    val crudService: CrudService<T>
    val expectedBasePath: String

    init {
        this.crudService=service
        this.expectedBasePath=expectedBasePath
    }

    abstract suspend fun createBaseData(): Unit
    abstract suspend fun deleteBaseData(): Unit

    fun baseCrudPath(){
        assertEquals(expectedBasePath,crudService.baseCrudPath,"Should correctly return the service's base crud path.")
    }
    open fun getFullList(block: () -> Unit) = runBlocking{
        launch { createBaseData() }
        launch {
           block()
        }
        launch { deleteBaseData() }
    }

    open fun getList(block: () -> Unit) = runBlocking{
        launch { createBaseData() }
        launch {
            block()
        }
        launch { deleteBaseData() }
    }

    open fun getOne(block: () -> Unit) = runBlocking{
        launch { createBaseData() }
        launch {
            block()
        }
        launch { deleteBaseData() }
    }

    open fun create(block: () -> Unit) = runBlocking{
        launch { createBaseData() }
        launch {
            block()
        }
        launch { deleteBaseData() }
    }

    open fun update(block: () -> Unit) = runBlocking{
        launch { createBaseData() }
        launch {
            block()
        }
        launch { deleteBaseData() }
    }

    open fun delete(block: () -> Unit) = runBlocking{
        launch { createBaseData() }
        launch {
            block()
        }
        launch { deleteBaseData() }
    }

}