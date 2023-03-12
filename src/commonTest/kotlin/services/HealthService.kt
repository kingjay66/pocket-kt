package services

import TestingUtils
import github.otisgoodman.pocketKt.PocketbaseClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import PocketbaseClient as TestClient

class HealthService : TestingUtils() {

    companion object {
        private val client = PocketbaseClient(TestClient.url)
    }

    @Test
    fun healthCheck() = runBlocking {
        assertDoesNotFail {
            launch {
                client.health.healthCheck()
            }
            println()
        }
    }

}