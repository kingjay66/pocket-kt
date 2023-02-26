package services

import CrudServiceTestSuite
import PocketbaseClient.Companion.adminId
import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.dsl.create
import github.otisgoodman.pocketKt.dsl.loginAdmin
import github.otisgoodman.pocketKt.dsl.update
import github.otisgoodman.pocketKt.models.Admin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.*
import PocketbaseClient as TestClient

class AdminAuthService : CrudServiceTestSuite<Admin>(client.admins, "api/admins") {

    companion object {
        private val client = PocketbaseClient(TestClient.url)
    }

    private val service = client.admins
    var testAdminId: String? = null

    @BeforeTest
    fun before() = runBlocking {
        launch {
            client.loginAdmin {
                val login = client.admins.authWithPassword(
                    TestClient.adminLogin.first, TestClient.adminLogin.second
                )
                admin = login.record
                token = login.token
            }

            val testAdmin = client.admins.create {
                email = "0.genadmin@test.com"
                avatar = 0
                password = "1234567891"
                passwordConfirm = "1234567891"
            }
            testAdminId = testAdmin.id
        }
        println()
    }


    @AfterTest
    fun after() = runBlocking {
        launch {
            if (testAdminId != null) client.admins.delete(testAdminId!!)
        }
        println()
    }


    private fun assertAdminValid(admin: Admin) {
        assertNotNull(admin)
        assertNotNull(admin.id)
        assertNotNull(admin.created)
        assertNotNull(admin.updated)
        assertNotNull(admin.email)
        assertNotNull(admin.avatar)
        println(admin)
    }

    @Test
    override fun assertCrudPathValid() {
        super.assertCrudPathValid()
    }

    @Test
    fun authWithPassword() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val response = service.authWithPassword(TestClient.adminLogin.first, TestClient.adminLogin.second)
                assertNotNull(response.token)
                assertAdminValid(response.record)
            }
            println()
        }
    }

    @Test
    fun refresh() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val response = service.authRefresh()
                assertNotNull(response.token)
                assertAdminValid(response.record)
            }
            println()
        }
    }

    @Test
    fun create() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val admin = service.create {
                    id = "123456789123478"
                    email = "genadmin@test.com"
                    avatar = 0
                    password = "1234567891"
                    passwordConfirm = "1234567891"
                }
                assertAdminValid(admin)
                assertMatchesCreation<Admin>("email", "genadmin@test.com", admin.email)
                assertMatchesCreation<Admin>("avatar", 0, admin.avatar)
                assertMatchesCreation<Admin>("id", "123456789123478", admin.id)
            }
            println()
        }
    }

    @Test
    fun update() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val admin = service.update(testAdminId!!) {
                    email = "1.genadmin@test.com"
                    avatar = 9
                }
                assertAdminValid(admin)
                assertMatchesCreation<Admin>("email", "1.genadmin@test.com", admin.email)
                assertMatchesCreation<Admin>("avatar", 9, admin.avatar)
                assertMatchesCreation<Admin>("id", testAdminId!!, admin.id)
            }
            println()
        }
    }

    @Test
    fun getOne() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val admin = service.getOne<Admin>(testAdminId!!)
                assertAdminValid(admin)
                assertMatchesCreation<Admin>("email", "0.genadmin@test.com", admin.email)
                assertMatchesCreation<Admin>("avatar", 0, admin.avatar)
                assertMatchesCreation<Admin>("id", testAdminId, admin.id)
            }
            println()
        }
    }

    @Test
    fun getList() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                service.create {
                    password = "123456789123478"
                    passwordConfirm = "123456789123478"
                    email = "2.genadmin@test.com"
                    avatar = 1
                }
                service.create {
                    password = "123456789123478"
                    passwordConfirm = "123456789123478"
                    email = "3.genadmin@test.com"
                    avatar = 2
                }
                service.create {
                    password = "123456789123478"
                    passwordConfirm = "123456789123478"
                    email = "4.genadmin@test.com"
                    avatar = 3
                }
                val list = service.getList<Admin>(1, 2)
                assertMatchesCreation<Admin>("page", 1, list.page)
                assertMatchesCreation<Admin>("perPage", 2, list.perPage)
                assertMatchesCreation<Admin>("totalItems", 5, list.totalItems)
                assertMatchesCreation<Admin>("totalPages", 3, list.totalPages)

                assertEquals(list.items.size, 2)
                list.items.forEach { admin -> assertAdminValid(admin) }
            }
            println()
        }
    }

    @Test
    fun getFullList() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val list = service.getFullList<Admin>(10)
                assertEquals(list.size, 2)
                list.forEach { admin -> assertAdminValid(admin) }
            }
            println()
        }
    }

    @Test
    fun delete() = runBlocking {
        assertDoesNotFail("No exceptions should be thrown") {
            launch {
                val admins = service.getFullList<Admin>(10)
                val ids = admins.map { it.id }
                ids.forEach { if (it != adminId) service.delete(it!!) }
                val isClean = service.getFullList<Admin>(10).size == 1
                assertTrue(isClean, "Admins should only contain the test admin!")
                testAdminId = null
            }
            println()
        }
    }
}