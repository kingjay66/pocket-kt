# Realtime

Pocket-Kt uses [coroutine flows]() in order to handle Pocketbase realtime events.

## Connecting to the realtime service and listening to events

```kotlin
runBlocking {
    val client = PocketbaseClient(...)
    val realtime = client.realtime
//  Creates a new coroutine which connects to the Pocketbase SSE endpoint
    launch { realtime.connect() }
//  Creates a new coroutine which subscribes to a certain event and creates a new event handler
    launch {
//      Subscribes to realtime events from a certain collection or record
        realtime.subscribe("COLLECTION NAME OR ID FOR RECORDS COLLECTION NAME OR ID /RECORD ID")
//        You can subscribe to multiple events by caling subscribe again

//      Listens for realtime events from subscribed collections or records
//      The code inside this block will be run whenever an event is received
        realtime.listen {
//            The action of the event (connection to sse, create, update, delete)
            val action = action
//            The record send by the event in string form (NOTE: this record will be null when the action is CONNECT)
            val record = record
            //This code will only be run if the event is not a connection event
            if (action.isBodyEvent()) {
//              This parses the record string to an object
                val record = parseRecord<Record>()
                println(record.id)
            }
        }
    }
}
```

## Disconnecting from the realtime service

```kotlin
//  Disconnects from the realtime service and stops all realtime corutines 
realtime.disconnect()
// You can now open another realtime connection whenever you are ready
```