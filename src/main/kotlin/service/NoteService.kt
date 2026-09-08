package service

import model.Note
import persistence.Serializer

class NoteService(serializerType: Serializer){

    private var serializer: Serializer = serializerType

    private var notes = ArrayList<Note>()
    private var lastId = 0
    private fun getId(): Int = lastId++

    fun addNote(note: Note) {
        note.id = getId()
        note.isArchived = false
        notes.add(note)
    }

    fun getNotes(): List<Note> {
        return notes
    }

    fun deleteNote(id: Int): Boolean {
        return notes.removeIf { it.id == id }
    }

    fun updateNote(id: Int, updated: Note): Boolean {
        val index = notes.indexOfFirst { it.id == id }
        if (index != -1) {
            updated.id = id
            notes[index] = updated
            return true
        }
        return false
    }

    fun archiveNote(id: Int): Boolean {
        for (note in notes) {
            if (note.id == id && !note.isArchived) {
                note.isArchived = true
                return true
            }
        }
        return false
    }

    fun findNoteById(id: Int): Note? {
        return notes.find { it.id == id }
    }

    fun numberOfNotes(): Int {
        return notes.size
    }

    fun numberOfArchivedNotes()
         = notes.count { note: Note -> note.isArchived }

    fun numberOfActiveNotes()
         = notes.count { note: Note -> !note.isArchived }

    fun numberOfNotesByCategory(category: String)
         = notes.count { note: Note -> note.category == category }

    fun numberOfNotesByPriority(priority: Int)
         = notes.count { note: Note -> note.priority == priority }

    fun getActiveNotes() = notes.filter { note -> !note.isArchived }

    fun getArchivedNotes() = notes.filter { note -> note.isArchived }

    fun getNotesByCategory(category: String) = notes
            .filter { note -> note.category.equals(category, ignoreCase = true) }


    fun searchNotesByContent(content: String) = notes
        .filter { note -> ((note.title.contains(content, ignoreCase = true)
                       || (note.body.contains(content, ignoreCase = true))))}

    fun load() {
        val array = serializer.read(Array<Note>::class.java)
        notes = array.toCollection(ArrayList())
    }

    //@Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }
}