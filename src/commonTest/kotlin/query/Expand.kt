package query

import TestingUtils
import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.dsl.collections.create
import github.otisgoodman.pocketKt.dsl.loginAdmin
import github.otisgoodman.pocketKt.dsl.query.ExpandRecord
import github.otisgoodman.pocketKt.dsl.query.ExpandRelations
import github.otisgoodman.pocketKt.models.Collection
import github.otisgoodman.pocketKt.models.Record
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import PocketbaseClient as TestClient

class Expand : TestingUtils() {

        private val client = PocketbaseClient(TestClient.url)

        private  val dataCollection = "expand_testing_data"
        private  val testCollection = "expand_testing"
        private var testingCollectionId: String? = null
        private var dataCollectionId: String? = null
        private val recordRelations = mutableMapOf<String, String>()

        private fun randomName(): String {
            val range = CharRange('A', 'Z')
            val result = StringBuilder()
            (1..3).forEach { _ ->
                result.append(range.random())
            }
            return result.toString()
        }

        @Serializable
        data class DataRecord(val field1: String) : Record()


        @Serializable
        data class TestRecord(val rel: String) : ExpandRecord<DataRecord>()

        @BeforeTest
        fun before() = runBlocking {
            launch {
                client.loginAdmin {
                    val login =
                        client.admins.authWithPassword(TestClient.adminLogin.first, TestClient.adminLogin.second)
                    admin = login.record
                    token = login.token
                }

                val data = client.collections.create {
                    name = dataCollection;type = Collection.CollectionType.BASE
                    schema { name = "field1";required = true; textSchema {} }
                }
                dataCollectionId = data.id

                val testing = client.collections.create {
                    name = testCollection;type = Collection.CollectionType.BASE
                    schema {
                        name = "rel";required = true; relationSchema {
                        collectionId = dataCollectionId!!;maxSelect = 1;cascadeDelete = false
                    }
                    }
                }
                testingCollectionId = testing.id

                for (i in 1..5) {
                    val d = client.records.create<DataRecord>(
                        dataCollection,
                        Json.encodeToString(DataRecord(randomName()))
                    )
                    val r = client.records.create<TestRecord>(
                        testCollection,
                        Json.encodeToString(TestRecord(d.id!!))
                    )
                    recordRelations[r.id!!] = d.id!!
                }
            }
            println()
        }

        @AfterTest
        fun after() = runBlocking {
            launch {
                client.collections.delete(testingCollectionId!!)
                client.collections.delete(dataCollectionId!!)
            }
            println()
        }

    @Test
    fun getOneRelation() = runBlocking {
        assertDoesNotFail {
            launch {
                val relatedResponse = client.records.getList<TestRecord>(
                    testCollection,
                    1,
                    1,
                    expandRelations = ExpandRelations("rel")
                ).items[0]
                val relation = relatedResponse.expand!!["rel"]!!.id
                assertEquals(recordRelations[relatedResponse.id], relation)
            }
            println()
        }
    }

    @Test
    fun getAllRelations() = runBlocking {
        assertDoesNotFail {
            launch {
                val relatedResponse =
                    client.records.getList<TestRecord>(testCollection, 1, 5, expandRelations = ExpandRelations("rel"))
                relatedResponse.items.forEach { testRecord ->
                    val relation = testRecord.expand!!["rel"]!!.id
                    assertEquals(recordRelations[testRecord.id], relation)
                }
            }
            println()
        }
    }
}