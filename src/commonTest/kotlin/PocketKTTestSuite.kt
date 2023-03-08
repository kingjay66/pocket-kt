import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.httpClient
import github.otisgoodman.pocketKt.models.utils.BaseModel
import github.otisgoodman.pocketKt.services.utils.CrudService
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


open class CrudServiceTestSuite<T : BaseModel>(service: CrudService<T>, expectedBasePath: String) : TestingUtils() {

    val crudService: CrudService<T>
    val expectedBasePath: String

    init {
        this.crudService = service
        this.expectedBasePath = expectedBasePath
    }

    open fun assertCrudPathValid() {
        assertEquals(
            "/$expectedBasePath",
            crudService.baseCrudPath,
            "Should correctly return the service's base crud path."
        )
    }
}

open class TestingUtils {
    inline fun <reified T> className() = T::class.simpleName

    private class SuccessException : Exception()

    internal fun assertDoesNotFail(block:() -> Unit){
        assertFailsWith<SuccessException> {
            block()
            throw SuccessException()
        }
    }

    internal fun assertDoesNotFail(message: String,block:() -> Unit){
        assertFailsWith<SuccessException>(message) {
            block()
            throw SuccessException()
        }
    }

    protected suspend fun getTestImage(number: Int): ByteArray {
        val http = httpClient()
        return when (number) {
            1 -> http.get("https://media.npr.org/assets/img/2017/09/12/macaca_nigra_self-portrait-3e0070aa19a7fe36e802253048411a38f14a79f8-s1100-c50.jpg")
                .body()

            2 -> http.get("https://upload.wikimedia.org/wikipedia/commons/thumb/7/75/Man_of_the_woods.JPG/1200px-Man_of_the_woods.JPG")
                .body()

            else -> {
                throw PocketbaseException("Invalid number")
            }
        }
    }

    inline fun <reified T> assertMatchesCreation(key: String, expected: String?, actual: String?) =
        assertEquals(
            expected,
            actual,
            "${key.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }} should match the ${key.uppercase()} used to create the ${className<T>()}"
        )

    inline fun <reified T> assertMatchesCreation(key: String, expected: Boolean, actual: Boolean?) =
        assertEquals(
            expected,
            actual,
            "${key.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }} should match the ${key.uppercase()} used to create the ${className<T>()}"
        )

    inline fun <reified T> assertMatchesCreation(key: String, expected: Int?, actual: Int?) =
        assertEquals(
            expected,
            actual,
            "${key.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }} should match the ${key.uppercase()} used to create the ${className<T>()}"
        )

    fun assertEqualNullOrFalse(actual: Any?, expected: Any?, msg: String? = null) {
        if (expected == false && actual == null) return
        if (expected == "" && actual == null) return
        assertEquals(expected, actual, msg)
    }

    inline fun <reified T> printJson(obj: T?) = println(Json.encodeToString(obj))

    private fun <T> compareValuesByImpl(a: T, b: T, selectors: Array<out (T) -> Comparable<*>?>): Int {
        for (fn in selectors) {
            val v1 = fn(a)
            val v2 = fn(b)
            val diff = compareValues(v1, v2)
            if (diff != 0) return diff
        }
        return 0
    }

    internal inline fun <T> compareByDescending(vararg selectors: (T) -> Comparable<*>?): Comparator<T> {
        require(selectors.isNotEmpty())
        return Comparator { a, b -> compareValuesByImpl(b, a, selectors) }
    }
}