package query

import TestingUtils
import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.dsl.collections.create
import github.otisgoodman.pocketKt.dsl.login
import github.otisgoodman.pocketKt.dsl.query.Filter
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
import kotlin.test.assertTrue
import PocketbaseClient as TestClient

class Filter : TestingUtils() {

    private val client = PocketbaseClient(TestClient.url)
    private val testCollection = "filter_testing"

    private var collectionId: String? = null

    private fun randomNum() = (1..10).random()

    @Serializable
    data class TestRecord(val field1: Int, val field2: Boolean) : BaseModel()

    @BeforeTest
    fun before() = runBlocking {
        launch {
            client.login {
                val login =
                    client.admins.authWithPassword(TestClient.adminLogin.first, TestClient.adminLogin.second)
                token = login.token
            }

            val c = client.collections.create {
                name = testCollection;type = Collection.CollectionType.BASE
                schema { name = "field1";required = true; numberSchema {} }
                schema { name = "field2";booleanSchema() }
            }
            collectionId = c.id
            for (i in 1..8) {
                client.records.create<TestRecord>(
                    testCollection,
                    Json.encodeToString(TestRecord(randomNum(), Random.nextBoolean()))
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
    fun filterByField1() = runBlocking {
        assertDoesNotFail {
            launch {
                val sortedResponse =
                    client.records.getList<TestRecord>(testCollection, 1, 10, filterBy = Filter("3 > field1"))
                sortedResponse.items.forEach { item -> println(item.field1);assertTrue("Item's retried from the request should have a field1 less than than three!") { item.field1 < 3 } }
            }
            println()
        }
    }

    @Test
    fun filterByField2() = runBlocking {
        assertDoesNotFail {
            launch {
                val sortedResponse =
                    client.records.getList<TestRecord>(testCollection, 1, 10, filterBy = Filter("field2 = true"))
                sortedResponse.items.forEach { item ->
                    println(item.field1);assertTrue(
                    item.field2,
                    "Item's retried from the request should have field2 = true"
                )
                }
            }
            println()
        }
    }

    @Test
    fun filterByBoth() = runBlocking {
        assertDoesNotFail {
            launch {
                val sortedResponse = client.records.getList<TestRecord>(
                    testCollection,
                    1,
                    10,
                    filterBy = Filter("3 < field1 && field2 = true")
                )
                sortedResponse.items.forEach { item ->
                    println(item.field1);assertTrue(
                    item.field1 > 3 && item.field2,
                    "Item's retried from the request should have field2 = true and field1 greater than 3"
                )
                }
            }
            println()
        }
    }


}