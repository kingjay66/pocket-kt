package github.otisgoodman.pocketKt.models.utils

import kotlinx.serialization.Serializable

@Serializable
public data class ListResult<T>(
    val page: Int = 1,
    val perPage: Int = 0,
    val totalItems: Int = 0,
    val totalPages: Int = 0,
    val items: List<T>
) {
    override fun toString(): String {
        return "ListResult(page=$page, perPage=$perPage, totalItems=$totalItems, totalPages=$totalPages, items=$items)"
    }
}