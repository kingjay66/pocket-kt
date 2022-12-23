package services

import CrudServiceTestSuite
import github.otisgoodman.pocketKt.AuthResponse
import github.otisgoodman.pocketKt.Client
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.models.User
import github.otisgoodman.pocketKt.services.UserAuthService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.*
import Client as TestClient

class UserAuthService: CrudServiceTestSuite<User>(UserAuthService(client),"/api/collections/users") {

    companion object{
        private val client =  Client(TestClient.url)
    }

    val service = client.users



    @Test
    fun auth(){

    }

    @Test
    fun authWithPassword() = runBlocking {
        assertDoesNotThrow("No exceptions should be thrown") {
            launch {
                val response = service.authWithPassword(TestClient.userLogin.first, TestClient.userLogin.second)
                    val token = response.token
                    println(response.record.id)
                    println(response.record.data)

                    assertNotNull(token)
            }
            println("ran")
        }
    }

    override suspend fun createBaseData() {
        TODO()
    }

    override suspend fun deleteBaseData() {
        TODO("Not yet implemented")
    }

    @Disabled("Not yet implemented")
    @Test
    override fun getFullList(block: () -> Unit): Job {
        return super.getFullList(block)
    }

    @Disabled("Not yet implemented")
    @Test
    override fun getList(block: () -> Unit): Job {
        return super.getList(block)
    }

    @Disabled("Not yet implemented")
    @Test
    override fun getOne(block: () -> Unit): Job {
        return super.getOne(block)
    }

    @Disabled("Not yet implemented")
    @Test
    override fun create(block: () -> Unit): Job {
        return super.create(block)
    }


    @Disabled("Not yet implemented")
    @Test
    override fun update(block: () -> Unit): Job {
        return super.update(block)
    }

    @Disabled("Not yet implemented")
    @Test
    override fun delete(block: () -> Unit): Job {
        return super.delete(block)
    }


}