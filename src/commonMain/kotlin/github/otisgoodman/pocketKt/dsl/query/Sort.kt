package github.otisgoodman.pocketKt.dsl.query

import github.otisgoodman.pocketKt.toFieldList
import io.ktor.http.*


public data class SortFields(val fields: String) {
    public constructor(vararg fields: String) : this(fields.toFieldList())

    public fun addTo(params: ParametersBuilder) {
        params.append("sort", this.fields)
    }

    public operator fun unaryPlus(): SortFields = SortFields("+${this.removeModifiers().fields}")

    public operator fun unaryMinus(): SortFields = SortFields("-${this.removeModifiers().fields}")
}

private fun SortFields.removeModifiers() = SortFields(this.fields.removePrefix("+").removePrefix("-"))