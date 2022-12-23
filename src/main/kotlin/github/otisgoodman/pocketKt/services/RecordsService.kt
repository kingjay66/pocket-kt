package github.otisgoodman.pocketKt.services

import github.otisgoodman.pocketKt.Client
import github.otisgoodman.pocketKt.models.Record
import github.otisgoodman.pocketKt.services.utils.SubCrudService
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonElement
import java.net.URLEncoder

class RecordsService(client: Client) : SubCrudService<Record>(client) {
    override fun baseCrudPath(collectionId: String) = "/api/collections/${URLEncoder.encode(collectionId)}/records"

    override fun decode(data: Map<String, JsonElement>): Record {
        return Record(data)
    }

    fun getFileURL(record: Record, filename: String): String {
        val url = URLBuilder()
        this.client.baseUrl(url)
        return "$url/api/files/${record.collectionId}/${record.id}/$filename"
    }

}