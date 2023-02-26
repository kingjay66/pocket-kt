import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.dsl.loginAdmin
import github.otisgoodman.pocketKt.dsl.loginUser
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertNotNull

class PocketbaseClient {

    @Test
    fun loginAsUser() = runBlocking {
        val client = PocketbaseClient(url)
        launch {
            client.loginUser {
                val login = client.users.authWithPassword(userLogin.first.first, userLogin.second)
                user = login.record
                token = login.token
            }
            assertNotNull(client.authStore.token, "Auth store token should not be null")
            assertNotNull(client.authStore.user, "Auth store model should not be null")
        }
        println()
    }

    @Test
    fun loginAsAdmin() = runBlocking {
        val client = PocketbaseClient(url)
        launch {
            client.loginAdmin {
                val login = client.admins.authWithPassword(adminLogin.first, adminLogin.second)
                admin = login.record
                token = login.token
            }
            assertNotNull(client.authStore.token, "Auth store token should not be null")
            assertNotNull(client.authStore.admin, "Auth store model should not be null")
        }
        println()
    }

    companion object {
        val url: URLBuilder.() -> Unit = {
            protocol = URLProtocol.HTTP
            host = "localhost"
            port = 8090
        }

        //                        EMAIL                USERNAME      PASSWORD
        val userLogin = ("user@test.com" to "test_user") to "test12345!"
        val testUserID = "cu3zuef4w99swn8"

        //                         EMAIL               PASSWORD
        val adminLogin = "admin@test.com" to "test12345!"
        val adminId = "7ccs23ms6g9f3md"
    }

}