package model

data class Note(
    var id: Int,
    var title: String,
    var body: String,
    var priority: Int,
    var category: String,
    var isArchived: Boolean
){
    override fun toString(): String {
        val status = if (isArchived) "ARCHIVED" else "ACTIVE"
        return "$id: $title | $body | Priority: $priority | Category: $category [$status]"
    }

}
