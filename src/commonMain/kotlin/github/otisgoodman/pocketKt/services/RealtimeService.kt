package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.PocketbaseClient
import github.otisgoodman.pocketKt.PocketbaseException
import github.otisgoodman.pocketKt.cancelAndJoin
import github.otisgoodman.pocketKt.services.utils.BaseService
import github.otisgoodman.pocketKt.services.utils.SseEvent
import github.otisgoodman.pocketKt.services.utils.readSse
import github.otisgoodman.pocketKt.toJsonPrimitive
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

//@TODO Document
public class RealtimeService(client: PocketbaseClient) : BaseService(client) {

    private val unknownKeysJson = Json {
        ignoreUnknownKeys = true
    }

    @Serializable
    public data class MessageData(val action: RealtimeActionType, val record: String?) {
        public inline fun <reified T> parseRecord(): T {
            if (action == RealtimeActionType.CONNECT) throw PocketbaseException("Connect event cannot be parsed!")
            val cleanedAction = record!!
                .replaceFirst("{\"action\":\"${action.name.lowercase()}\",\"record\":", "")
                .replaceFirst("}", "")
            return Json.decodeFromString(cleanedAction)
        }
    }

    @Serializable
    public enum class RealtimeActionType {
        CONNECT,

        @SerialName("create")
        CREATE,

        @SerialName("update")
        UPDATE,

        @SerialName("delete")
        DELETE;

        public fun isBodyEvent(event: RealtimeActionType): Boolean {
            return when (event) {
                CONNECT -> false
                else -> {
                    true
                }
            }
        }

        @Serializable
        internal data class Action(val action: RealtimeActionType)

    }

    private var clientId: String? = null
    private var connected: Boolean = false
    private var connection = MutableSharedFlow<MessageData>()
    private val subscriptions: MutableSet<String> = mutableSetOf()
    private val sseCoroutines: MutableSet<Job> = mutableSetOf()

    public suspend fun connect() {
        coroutineScope {
            val job = launch {
                if (connected) throw PocketbaseException("You are already connected to the realtime service!")
                connected = true
                while (isActive) {
                    val eventFlow: Flow<SseEvent> = client.httpClient.readSse("/api/realtime")
                    eventFlow.collectLatest { event ->
                        if (clientId == null || event.id != clientId) {
                            clientId = event.id
                            sendSubscribeRequest()
                        }
                        try {
                            val a = unknownKeysJson.decodeFromString<RealtimeActionType.Action>(event.data).action
                            connection.emit(MessageData(a, event.data))
                        } catch (e: Exception) {
                            connection.emit(MessageData(RealtimeActionType.CONNECT, null))
                        }
                    }
                }
            }
            sseCoroutines.add(job)
        }
    }


    public suspend fun listen(callback: MessageData.() -> Unit) {
        coroutineScope {
            val job = launch {
                if (!connected) PocketbaseException("You must connect to the SSE client first!")
                connection.collectLatest { event ->
                    callback(event)
                    coroutineContext.ensureActive()
                }
            }
            sseCoroutines.add(job)
        }
    }

    public suspend fun subscribe(subscription: String, delay: Long = 2000) {
        if (!connected || clientId == null) delay(delay)
        if (!connected) PocketbaseException("You must connect to the SSE client first!")
        subscriptions.add(subscription)
        sendSubscribeRequest()
    }

    public suspend fun subscribe(delay: Long = 2000, vararg subscriptionList: String) {
        if (!connected || clientId == null) delay(delay)
        if (!connected) PocketbaseException("You must connect to the SSE client first!")
        subscriptionList.forEach { subscription -> subscriptions.add(subscription) }
        sendSubscribeRequest()
    }

    public suspend fun unsubscribe(subscription: String, delay: Long = 2000) {
        if (!connected || clientId == null) delay(delay)
        if (!connected) PocketbaseException("You must connect to the SSE client first!")
        subscriptions.remove(subscription)
        sendSubscribeRequest()
    }

    public suspend fun unsubscribeAll(delay: Long = 2000) {
        if (!connected || clientId == null) delay(delay)
        if (!connected) PocketbaseException("You must connect to the SSE client first!")
        subscriptions.clear()
        sendSubscribeRequest()
    }

    public suspend fun unsubscribe(delay: Long = 2000, vararg subscriptionList: String) {
        if (!connected || clientId == null) delay(delay)
        if (!connected) PocketbaseException("You must connect to the SSE client first!")
        subscriptionList.forEach { subscription -> subscriptions.remove(subscription) }
        sendSubscribeRequest()
    }

    public suspend fun disconnect() {
        subscriptions.clear()
        if (clientId != null) sendSubscribeRequest()
        clientId = null
        connected = false
        sseCoroutines.forEach { job ->
            job.cancelChildren()
            job.cancelAndJoin()
        }
        sseCoroutines.clear()
    }

    public suspend fun sendSubscribeRequest(): Boolean {
        val body = mapOf<String, JsonElement>(
            "clientId" to clientId!!.toJsonPrimitive(),
            "subscriptions" to JsonArray(subscriptions.map { it.toJsonPrimitive() })
        )
        val response = client.httpClient.post {
            url {
                path("/api/realtime")
                contentType(ContentType.Application.Json)
            }
            setBody(body)
        }
        PocketbaseException.handle(response)
        return true
    }

}