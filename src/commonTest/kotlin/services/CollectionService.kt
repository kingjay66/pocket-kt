package services

import CrudServiceTestSuite
import github.otisgoodman.pocketKt.*
import github.otisgoodman.pocketKt.dsl.collections.BaseSchemaBuilder
import github.otisgoodman.pocketKt.dsl.collections.create
import github.otisgoodman.pocketKt.dsl.collections.update
import github.otisgoodman.pocketKt.dsl.login
import github.otisgoodman.pocketKt.models.Collection
import github.otisgoodman.pocketKt.models.utils.SchemaField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toInstant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.*
import PocketbaseClient as TestClient

class CollectionService : CrudServiceTestSuite<Collection>(client.collections, "api/collections") {

    companion object {
        private val client = PocketbaseClient(TestClient.url)
    }

    var delete = true

    private val service = client.collections
    @BeforeTest
    fun before() = runBlocking {
        launch {
            delete = true
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
        "id": "q324nkpuc3ocsgq",
        "name": "test_collection_0",
        "type": "base",
        "system": false,
        "schema": [
            {
                "id": "d3ccyr2s",
                "name": "text",
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
        "options": {}
    }
    ]
                """.trimIndent()
            )
            client.collections.import(json)
        }
        println()
    }

    @AfterTest
    fun after() = runBlocking {
        launch {
            if (delete){
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
      "id": "y25869zt5494vb9",
      "created": "2023-01-20 02:41:00.141Z",
      "updated": "2023-01-20 02:41:00.141Z",
      "name": "test_collection_2",
      "type": "base",
      "system": false,
      "schema": [
        {
          "system": false,
          "id": "nug2ifoj",
          "name": "url",
          "type": "url",
          "required": true,
          "unique": true,
          "options": {
            "exceptDomains": [
              "google.com"
            ],
            "onlyDomains": null
          }
        }
      ],
      "listRule": null,
      "viewRule": null,
      "createRule": null,
      "updateRule": null,
      "deleteRule": null,
      "options": {}
    }
    ]
                """.trimIndent()
                )
                service.import(json, false)
                val collection = service.getOne<Collection>("y25869zt5494vb9")
                assertCollectionValid(collection)
                assertSchemaContains(collection.schema!![0]) {
                    name = "url";required = true;unique = true;urlSchema {
                    exceptDomains = listOf("google.com")
                }
                }
            }
            println()
        }
    }

    @Test
    fun create() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val usersId = client.collections.getOne<Collection>("users").id
                val collection = service.create {
                    id = "123456789123478"
                    name = "test_collection"
                    type = Collection.CollectionType.BASE
                    schema {
                        name = "string"
                        required = true
                        textSchema { min = 4 }
                    }
                    schema {
                        name = "int"
                        required = true
                        numberSchema {}
                    }
                    schema {
                        name = "bool"
                        booleanSchema()
                    }
                    schema {
                        name = "email"
                        unique = true
                        emailSchema {
                            onlyDomains = listOf("test.com")
                        }
                    }
                    schema {
                        name = "url"
                        unique = true
                        urlSchema {
                            onlyDomains = listOf("facebook.com")
                        }
                    }
                    schema {
                        name = "date"
                        required = true
                        dateSchema {
                            min = "2022-08-19T02:22:00.00Z".toInstant()
                            max = "2023-08-19T02:22:00.00Z".toInstant()
                        }
                    }
                    schema {
                        name = "select"
                        selectSchema {
                            maxSelect = 1
                            values = listOf("ADMIN", "USER", "SPECTATOR")
                        }
                    }
                    schema {
                        name = "json"
                        jsonSchema()
                    }
                    schema {
                        name = "file"
                        fileSchema {
                            maxSize = 5242880
                            maxSelect = 1
                        }
                    }
                    schema {
                        name = "relation"
                        relationSchema {
                            collectionId = usersId
                            cascadeDelete = false
                            maxSelect = 1
                        }
                    }
                    schema {
                        name = "editor"
                        editorSchema()
                    }
                    createRule = "@request.auth.verified = true".toJsonPrimitive()
                }
                val schema = collection.schema
                assertCollectionValid(collection)

                assertSchemaContains(schema!![0]) { name = "string";required = true;textSchema { min = 4 } }
                assertSchemaContains(schema[1]) { name = "int";required = true;numberSchema {} }
                assertSchemaContains(schema[2]) { name = "bool";booleanSchema() }
                assertSchemaContains(schema[3]) {
                    name = "email";unique = true;emailSchema {
                    onlyDomains = listOf("test.com")
                }
                }
                assertSchemaContains(schema[4]) {
                    name = "url";unique = true;urlSchema {
                    onlyDomains = listOf("facebook.com")
                }
                }
                assertSchemaContains(schema[5]) {
                    name = "date";required = true;dateSchema {
                    min = "2022-08-19T02:22:00.00Z".toInstant();max = "2023-08-19T02:22:00.00Z".toInstant()
                }
                }
                assertSchemaContains(schema[6]) {
                    name = "select";selectSchema {
                    maxSelect = 1;values = listOf("ADMIN", "USER", "SPECTATOR")
                }
                }
                assertSchemaContains(schema[7]) { name = "json";jsonSchema() }
                assertSchemaContains(schema[8]) { name = "file";fileSchema { maxSize = 5242880;maxSelect = 1 } }
                assertSchemaContains(schema[9]) {
                    name = "relation";relationSchema {
                    collectionId = usersId;cascadeDelete = false;maxSelect = 1
                }
                }
                assertSchemaContains(schema[10]) { name = "editor";editorSchema() }
                assertMatchesCreation<Collection>("name", "test_collection", collection.name)
                assertMatchesCreation<Collection>("id", "123456789123478", collection.id)
                assertMatchesCreation<Collection>("type", "BASE", collection.type?.name)
                assertMatchesCreation<Collection>("createRule", "@request.auth.verified = true", collection.createRule)

            }
            println()
        }
    }

    @Test
    fun update() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val collection = service.update("q324nkpuc3ocsgq") {
                    name = "test_collection_1"
                    schema {
                        name = "bool"
                        booleanSchema()
                        required = true
                    }
                    createRule = null
                }
                assertCollectionValid(collection)
                assertSchemaContains(collection.schema!![0]) { name = "bool";required = true;booleanSchema() }
                assertMatchesCreation<Collection>("name", "test_collection_1", collection.name)
                assertMatchesCreation<Collection>("createRule", null, collection.createRule)
            }
            println()
        }
    }

    @Test
    fun getOne() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val collection = service.getOne<Collection>("q324nkpuc3ocsgq")
                assertCollectionValid(collection)
                assertMatchesCreation<Collection>("name", "test_collection_0", collection.name)
                assertMatchesCreation<Collection>("createRule", null, collection.createRule)
                assertSchemaContains(collection.schema!![0]) { name = "text";textSchema {  } }
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
                    type = Collection.CollectionType.BASE
                    schema {
                        name = "int"
                        required = true
                        numberSchema { min = 0 }
                    }
                }
                service.create {
                    name = "test_collection_4"
                    type = Collection.CollectionType.BASE
                    schema {
                        name = "json"
                        jsonSchema()
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
                assertEquals(2, list.size)
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
}