package services

import CrudServiceTestSuite
import github.otisgoodman.pocketKt.*
import github.otisgoodman.pocketKt.dsl.collections.BaseSchemaBuilder
import github.otisgoodman.pocketKt.dsl.collections.create
import github.otisgoodman.pocketKt.dsl.collections.update
import github.otisgoodman.pocketKt.dsl.login
import github.otisgoodman.pocketKt.models.Collection
import github.otisgoodman.pocketKt.models.Record
import github.otisgoodman.pocketKt.models.utils.SchemaField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.*
import PocketbaseClient as TestClient

class CollectionViewService : CrudServiceTestSuite<Collection>(client.collections, "api/collections") {
    @Serializable
    class TestRecord(
        val username: String,
        val password: String? = null,
        val passwordConfirm: String? = null,
        val email: String
    ) : Record()

    private fun random(): String {
        val range = CharRange('A', 'Z')
        val result = StringBuilder()
        (1..9).forEach { _ ->
            result.append(range.random())
        }
        return result.toString()
    }

    var recordId: String? = null

    companion object {
        private val client = PocketbaseClient(TestClient.url)
    }

    var delete = true

    private val service = client.collections

    @BeforeTest
    fun before() = runBlocking {
        launch {
            client.login {
                val login = client.admins.authWithPassword(
                    TestClient.adminLogin.first, TestClient.adminLogin.second
                )
                token = login.token
            }
            val json = Json.decodeFromString<List<Collection>>(
                """
                    [
                    {
        "id": "yc356em40o3y8u1",
        "name": "test_auth",
        "type": "auth",
        "system": false,
        "schema": [],
        "listRule": null,
        "viewRule": null,
        "createRule": null,
        "updateRule": null,
        "deleteRule": null,
        "options": {
            "allowEmailAuth": true,
            "allowOAuth2Auth": true,
            "allowUsernameAuth": true,
            "exceptEmailDomains": null,
            "manageRule": null,
            "minPasswordLength": 8,
            "onlyEmailDomains": null,
            "requireEmail": false
        }
    }
    ]
                """.trimIndent()
            )
            val json2 = Json.decodeFromString<List<Collection>>(
                """
                    [
{
        "id": "yzz7lwv6sqqyxbe",
        "name": "view_test",
        "type": "view",
        "system": false,
        "schema": [
            {
                "id": "o01krlxq",
                "name": "username",
                "type": "text",
                "system": false,
                "required": false,
                "unique": false,
                "options": {
                    "min": null,
                    "max": null,
                    "pattern": ""
                }
            }
        ],
        "listRule": null,
        "viewRule": null,
        "createRule": null,
        "updateRule": null,
        "deleteRule": null,
        "options": {
            "query": "SELECT id,username FROM test_auth"
        }
    }
    ]
                """.trimIndent()
            )
            client.collections.import(json)
            client.collections.import(json2)


            repeat(4) {
                val password = random()
                val record = client.records.create<TestRecord>(
                    "test_auth", Json.encodeToString(
                        TestRecord(
                            random(), password, password,
                            random() + "@random.com"
                        )
                    )
                )
                if (it == 3) recordId = record.id
            }
        }
        println()
    }

    @AfterTest
    fun after() = runBlocking {
        launch {
            if (delete) {
                val usersCollectionId = service.getOne<Collection>("users").id
                val collections = service.getFullList<Collection>(10)
                val ids = collections.map { it.id }
                ids.forEach { if (it != usersCollectionId) service.delete(it!!) }
            }
        }
        println()
    }


    private fun assertCollectionValid(collection: Collection) {
        assertNotNull(collection)
        assertNotNull(collection.id)
        assertNotNull(collection.created)
        assertNotNull(collection.updated)
        assertNotNull(collection.name)
        assertNotNull(collection.schema)
        assertNotNull(collection.type)
        assertNotNull(collection.system)
        println(collection)
    }

    private fun assertSchemaContains(schema: SchemaField, setup: BaseSchemaBuilder.() -> Unit) {
        val b = BaseSchemaBuilder()
        b.setup()
        val builder = b.build()
        val builderOptions = builder.options
        val schemaOptions = schema.options


        assertEqualNullOrFalse(builder.name, schema.name, "name")
        assertEqualNullOrFalse(builder.required, schema.required, "required")
        assertEqualNullOrFalse(builder.system, schema.system, "system")
        assertEqualNullOrFalse(builder.unique, schema.unique, "unique")
        assertEqualNullOrFalse(builder.type, schema.type, "type")
        if (builderOptions.min?.isInstant() == true) {
            assertEqualNullOrFalse(builderOptions.min?.toInstant(), schemaOptions?.min?.toInstant(), "min")
            assertEqualNullOrFalse(builderOptions.max?.toInstant(), schemaOptions?.max?.toInstant(), "max")
        } else if (builderOptions.min?.isNumber() == true) {
            assertEqualNullOrFalse(builderOptions.min?.toNumber(), schemaOptions?.min?.toNumber(), "min")
            assertEqualNullOrFalse(builderOptions.max?.toNumber(), schemaOptions?.max?.toNumber(), "max")
        }
        assertEqualNullOrFalse(builderOptions.pattern, schemaOptions?.pattern, "pattern")
        assertEqualNullOrFalse(builderOptions.exceptDomains, schemaOptions?.exceptDomains, "exceptDomains")
        assertEqualNullOrFalse(builderOptions.onlyDomains, schemaOptions?.onlyDomains, "onlyDomains")
        assertEqualNullOrFalse(builderOptions.values, schemaOptions?.values, "values")
        assertEqualNullOrFalse(builderOptions.maxSelect, schemaOptions?.maxSelect, "maxSelect")
        assertEqualNullOrFalse(builderOptions.collectionId, schemaOptions?.collectionId, "collectionId")
        assertEqualNullOrFalse(builderOptions.cascadeDelete, schemaOptions?.cascadeDelete, "cascadeDelete")
        assertEqualNullOrFalse(builderOptions.maxSize, schemaOptions?.maxSize, "maxSize")
        assertEqualNullOrFalse(builderOptions.mimeTypes, schemaOptions?.mimeTypes, "mimeTypes")
        assertEqualNullOrFalse(builderOptions.thumbs, schemaOptions?.thumbs, "thumbs")
    }

    @Test
    override fun assertCrudPathValid() {
        super.assertCrudPathValid()
    }


    @Test
    fun import() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val json = Json.decodeFromString<List<Collection>>(
                    """
                    [
{
        "id": "yzz7lwv6sqqyxbt",
        "name": "view_test2",
        "type": "view",
        "system": false,
        "schema": [
            {
                "id": "o01krlxe",
                "name": "username",
                "type": "text",
                "system": false,
                "required": false,
                "unique": false,
                "options": {
                    "min": null,
                    "max": null,
                    "pattern": ""
                }
            }
        ],
        "listRule": null,
        "viewRule": null,
        "createRule": null,
        "updateRule": null,
        "deleteRule": null,
        "options": {
            "query": "SELECT id,username FROM test_auth"
        }
    }
    ]
                """.trimIndent()
                )
                service.import(json, false)
                val collection = service.getOne<Collection>("yzz7lwv6sqqyxbt")
                assertCollectionValid(collection)
                assertSchemaContains(collection.schema!![0]) {
                    name = "username";textSchema {}
                }
            }
            println()
        }
    }

    @Test
    fun create() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val collection = service.create {
                    id = "123456789123478"
                    name = "test_collection"
                    type = Collection.CollectionType.VIEW
                    viewCollectionOptions {
                        query = "SELECT id,created,email from test_auth"
                    }
                }
                val schema = collection.schema
                assertCollectionValid(collection)
                assertSchemaContains(schema!![0]) { name = "email";emailSchema { } }
                assertMatchesCreation<Collection>("name", "test_collection", collection.name)
                assertMatchesCreation<Collection>("id", "123456789123478", collection.id)
                assertMatchesCreation<Collection>("type", "VIEW", collection.type?.name)
                assertMatchesCreation<Collection>(
                    "query",
                    "SELECT id,created,email from test_auth",
                    collection.options?.query
                )

            }
            println()
        }
    }

    @Test
    fun update() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val collection = service.update("view_test") {
                    name = "query_test_2"
                    viewCollectionOptions {
                        query = "SELECT id,username,email FROM test_auth"
                    }
                }
                val schema = collection.schema
                assertCollectionValid(collection)
                assertSchemaContains(schema!![0]) { name = "username";textSchema { } }
                assertMatchesCreation<Collection>("name", "query_test_2", collection.name)
                assertMatchesCreation<Collection>("id", "yzz7lwv6sqqyxbe", collection.id)
                assertMatchesCreation<Collection>("type", "VIEW", collection.type?.name)
                assertMatchesCreation<Collection>(
                    "query",
                    "SELECT id,username,email FROM test_auth",
                    collection.options?.query
                )
            }
            println()
        }
    }

    @Test
    fun getOne() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val collection = service.getOne<Collection>("view_test")
                assertCollectionValid(collection)
                assertMatchesCreation<Collection>("name", "view_test", collection.name)
                assertMatchesCreation<Collection>("type", "VIEW", collection.type?.name)
                assertMatchesCreation<Collection>(
                    "query",
                    "SELECT id,username FROM test_auth",
                    collection.options?.query
                )
                assertSchemaContains(collection.schema!![0]) { name = "username";textSchema { } }
            }
            println()
        }
    }

    @Test
    fun getList() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                service.create {
                    name = "test_collection_3"
                    type = Collection.CollectionType.VIEW
                    viewCollectionOptions {
                        query = "select id,created FROM test_auth"
                    }
                }
                service.create {
                    name = "test_collection_2"
                    type = Collection.CollectionType.VIEW
                    viewCollectionOptions {
                        query = "select id,updated FROM test_auth"
                    }
                }
                val list = service.getList<Collection>(1, 2)
                assertMatchesCreation<Collection>("page", 1, list.page)
                assertMatchesCreation<Collection>("perPage", 2, list.perPage)
                assertEquals(list.items.size, 2)
                list.items.forEach { collection -> assertCollectionValid(collection) }
            }
            println()
        }
    }

    @Test
    fun getFullList() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val list = service.getFullList<Collection>(10)
                assertEquals(3, list.size)
                list.forEach { collection -> assertCollectionValid(collection) }
            }
            println()
        }
    }

    @Test
    fun delete() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                delete = false
                val usersCollectionId = service.getOne<Collection>("users").id
                val collections = service.getFullList<Collection>(10)
                val ids = collections.map { it.id }
                ids.forEach { if (it != usersCollectionId) service.delete(it!!) }
                val isClean = service.getFullList<Collection>(10).size == 1
                assertTrue(isClean, "Collections should only contain the user's collection!")
            }
            println()
        }
    }

    @Test
    fun recordGetOne() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val record = client.records.getOne<TestRecord>("test_auth", recordId!!)
                assertNotNull(record.id)
                assertNotNull(record.username)
            }
            println()
        }
    }

    @Test
    fun recordGetList() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val list = client.records.getList<TestRecord>("test_auth", 1, 2)
                assertMatchesCreation<Collection>("page", 1, list.page)
                assertMatchesCreation<Collection>("perPage", 2, list.perPage)
                assertMatchesCreation<Collection>("totalItems", 4, list.totalItems)
                assertMatchesCreation<Collection>("totalPages", 2, list.totalPages)

                assertEquals(list.items.size, 2)
                list.items.forEach { record ->
                    assertNotNull(record.id)
                    assertNotNull(record.username)
                }
            }
            println()
        }
    }

    @Test
    fun recordGetFullList() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val list = client.records.getFullList<TestRecord>("test_auth", 10)
                assertEquals(list.size, 4)
                list.forEach { record ->
                    assertNotNull(record.id)
                    assertNotNull(record.username)
                }
            }
            println()
        }
    }

}