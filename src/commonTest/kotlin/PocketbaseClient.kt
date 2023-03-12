import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.dsl.login
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertNotNull

class PocketbaseClient {

    @Test
    fun login() = runBlocking {
        val client = PocketbaseClient(url)
        launch {
            client.login {
                val login = client.users.authWithPassword(userLogin.first.first, userLogin.second)
                token = login.token
            }
            assertNotNull(client.authStore.token, "Auth store token should not be null")
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