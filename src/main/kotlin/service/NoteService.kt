package service

import model.Note
import persistence.Serializer
import kotlin.streams.toList
import kotlin.text.category

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

    fun numberOfArchivedNotes(): Int {
        return notes.stream()
            .filter{note: Note -> note.isArchived}
            .count()
            .toInt()
    }

    fun numberOfActiveNotes(): Int {
        return notes.stream()
            .filter{note: Note -> !note.isArchived}
            .count()
            .toInt()
    }

    fun numberOfNotesByCategory(category: String): Int {
        return notes.stream()
            .filter{note: Note -> note.category == category}
            .count()
            .toInt()
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        return notes.stream()
            .filter{note: Note -> note.priority == priority}
            .count()
            .toInt()
    }

    fun getActiveNotes(): List<Note>
        = notes.stream()
            .filter { note -> !note.isArchived }
            .toList()

    fun getArchivedNotes(): List<Note> = notes
            .stream()
            .filter { note -> note.isArchived }
            .toList()

    fun getNotesByCategory(category: String): List<Note>
        = notes.stream()
            .filter { note -> note.category.equals(category, ignoreCase = true) }
            .toList()

    fun load() {
        val array = serializer.read(Array<Note>::class.java)
        notes = array.toCollection(ArrayList())
    }

    //@Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }
}