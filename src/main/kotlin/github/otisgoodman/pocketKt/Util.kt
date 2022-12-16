package github.otisgoodman.pocketKt


fun Set<Pair<String, Any>>.containsKey(s: String): Boolean {
    for (pair in this) {
        if (pair.first == s) return true
    }
    return false
}