package service

import model.Note
import model.NoteItem
import persistence.Serializer

class NoteItemService (serializerType: Serializer){

    private var serializer: Serializer = serializerType
    private var noteItems = ArrayList<NoteItem>()

    fun addItemToNote(noteId: Int, itemId: Int): Boolean {

        if (noteItems.any {
                it.noteId == noteId && it.itemId == itemId
            }) {
            return false
        }

        noteItems.add(
            NoteItem(noteId, itemId)
        )

        return true
    }

    fun getItemIdsForNote(noteId: Int): List<Int> {
        return noteItems
            .filter { it.noteId == noteId }
            .map { it.itemId }
    }

    fun load() {
        val array = serializer.read(Array<NoteItem>::class.java)
        noteItems = array.toCollection(ArrayList())
    }

    //@Throws(Exception::class)
    fun store() {
        serializer.write(noteItems)
    }
}
