package query

import TestingUtils
import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.dsl.collections.create
import github.otisgoodman.pocketKt.dsl.loginAdmin
import github.otisgoodman.pocketKt.dsl.query.SortFields
import github.otisgoodman.pocketKt.models.Collection
import github.otisgoodman.pocketKt.models.utils.BaseModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import PocketbaseClient as TestClient

class Sort : TestingUtils() {

        private val client = PocketbaseClient(TestClient.url)
        private val testCollection = "sort_testing"
        private var collectionId: String? = null

        private fun randomName(): String {
            val range = CharRange('A', 'Z')
            val result = StringBuilder()
            (1..5).forEach { _ ->
                result.append(range.random())
            }
            return result.toString()
        }

        @Serializable
        data class TestRecord(val field1: String, val field2: Boolean) : BaseModel()


        @BeforeTest
        fun before() = runBlocking {
            launch {
                client.loginAdmin {
                    val login =
                        client.admins.authWithPassword(TestClient.adminLogin.first, TestClient.adminLogin.second)
                    admin = login.record
                    token = login.token
                }

                val c = client.collections.create {
                    name = testCollection;type = Collection.CollectionType.BASE
                    schema { name = "field1";required = true; textSchema {} }
                    schema { name = "field2";booleanSchema() }
                }
                collectionId = c.id
                for (i in 1..5) {
                    client.records.create<TestRecord>(
                        testCollection,
                        Json.encodeToString(TestRecord(randomName(), Random.nextBoolean()))
                    )
                }
            }
            println()
        }

        @AfterTest
        fun after() = runBlocking {
            launch { client.collections.delete(collectionId!!) }
            println()
        }



    @Test
    fun sortByName() = runBlocking {
        assertDoesNotFail {
            launch {
                val sortedResponse = client.records.getList<TestRecord>(testCollection, 1, 5, SortFields("field1"))
                val expectedSort = sortedResponse.items.sortedBy { r -> r.field1 }
                assertEquals(expectedSort, sortedResponse.items, "Sorting does not match the expected sort!")
            }
            println()
        }
    }

    @Test
    fun sortByNameDescending() = runBlocking {
        assertDoesNotFail {
            launch {
                val sortedResponse = client.records.getList<TestRecord>(testCollection, 1, 5, -SortFields("field1"))
                val expectedSort = sortedResponse.items.sortedByDescending { r -> r.field1 }
                assertEquals(expectedSort, sortedResponse.items, "Sorting does not match the expected sort!")
            }
            println()
        }
    }

    @Test
    fun sortByPlus() = runBlocking {
        assertDoesNotFail {
            launch {
                val sortedResponse = client.records.getList<TestRecord>(testCollection, 1, 5, +SortFields("field1"))
                val expectedSort = sortedResponse.items.sortedBy { r -> r.field1 }
                assertEquals(expectedSort, sortedResponse.items, "Sorting does not match the expected sort!")
            }
            println()
        }
    }

    @Test
    fun sortByMultiple() = runBlocking {
        assertDoesNotFail {
            launch {
                val sortedResponse =
                    client.records.getList<TestRecord>(testCollection, 1, 5, +SortFields("field1", "field2"))
                val expectedSort = sortedResponse.items.sortedWith(compareBy({ r -> r.field1 }, { r -> r.field2 }))
                assertEquals(expectedSort, sortedResponse.items, "Sorting does not match the expected sort!")
            }
            println()
        }
    }

    @Test
    fun sortByMultipleDescending() = runBlocking {
        assertDoesNotFail {
            launch {
                val sortedResponse =
                    client.records.getList<TestRecord>(testCollection, 1, 5, -SortFields("field1", "field2"))
                val expectedSort =
                    sortedResponse.items.sortedWith(compareByDescending({ r -> r.field1 }, { r -> r.field2 }))
                assertEquals(expectedSort, sortedResponse.items, "Sorting does not match the expected sort!")
            }
            println()
        }
    }

}