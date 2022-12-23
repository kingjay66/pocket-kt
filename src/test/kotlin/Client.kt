import github.otisgoodman.pocketKt.Client
import github.otisgoodman.pocketKt.dsl.login
import github.otisgoodman.pocketKt.models.User
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.io.path.Path
import kotlin.test.assertNotNull

class Client {


    @Disabled("authWithPassword must be working before this test can be finished")
    @Test
    fun loginAsUser() = runBlocking {
        val client = Client(url)
        client.login {
            launch {
                val login = client.users.authWithPassword(userLogin.first, userLogin.second)
                model = login.record.toUser()
                token = login.token
            }
            assertNotNull(token,"Token should not be null")
            assertNotNull(model,"Model should not be null")

            assertNotNull(client.authStore.token,"Auth store token should not be null")
            assertNotNull(client.authStore.model,"Auth store model should not be null")

            assert(client.authStore.model is User) { "Auth store model should be instance of User" }
        }
    }

    companion object {
        val url: URLBuilder.() -> Unit = {
            protocol = URLProtocol.HTTP
            host = "localhost"
            port = 8090
        }
        val userLogin = "user@test.com" to "12345"
        val adminLogin = "admin@test.com" to "test12345!"
    }

}