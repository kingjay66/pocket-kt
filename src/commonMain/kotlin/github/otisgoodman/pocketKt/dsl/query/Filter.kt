package github.otisgoodman.pocketKt.dsl.query

import io.ktor.http.*

//@TODO Document
public data class Filter(val expression: String? = null) {
    public fun addTo(params: ParametersBuilder) {
        if (this.expression != null) params.append("filter", this.expression)
    }
}