package github.otisgoodman.pocketKt.models.utils

import kotlinx.serialization.Serializable

@Serializable
data class ListResult<T>(
    val page: Int = 1,
    val perPage: Int = 0,
    val totalItems: Int = 0,
    val totalPages: Int = 0,
    val items: List<T>
)