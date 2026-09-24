package service

import model.Item
import model.Note
import persistence.Serializer

class ItemService (serializerType: Serializer) {

    private var serializer: Serializer = serializerType

    private var items = ArrayList<Item>()
    private var lastId = 0

    private fun getId(): Int = lastId++

    fun addItem(item: Item) {
        item.id = getId()
        items.add(item)
    }

    fun getItems(): List<Item> {
        return items
    }

    fun findItemById(id: Int): Item? {
        return items.find { it.id == id }
    }

    fun searchItemsByContent(content: String) = items
        .filter { item -> item.description.contains(
            content, ignoreCase = true) }

    fun numberOfItems() = items.size
    fun numberOfCompletedItems() = items.count { it.isCompleted}
    fun numberOfIncompleteItems() = items.count { !it.isCompleted}


    fun load() {
        val array = serializer.read(Array<Item>::class.java)
        items = array.toCollection(ArrayList())
    }

    //@Throws(Exception::class)
    fun store() {
        serializer.write(items)
    }
}