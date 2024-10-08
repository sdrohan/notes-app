package controllers

import models.Note
import persistence.Serializer
import utils.isValidListIndex

/**
 * This class manages a list of notes and provides functionality for adding, updating, deleting,
 * and filtering notes. It uses a [Serializer] to load and store the notes persistently.
 *
 * @property serializer A serializer instance for reading and writing the notes.
 * @constructor Initializes the NoteAPI with the specified [serializerType].
 */
class NoteAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType
    private var notes = ArrayList<Note>()

    /**
     * Adds a new [Note] to the list.
     *
     * @param note The [Note] to be added.
     * @return `true` if the note was successfully added, `false` otherwise.
     */
    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun listAllNotes(): String =
        if (notes.isEmpty()) {
            "No notes stored"
        } else {
            formatListString(notes)
        }

    fun listActiveNotes(): String =
        if (numberOfActiveNotes() == 0) {
            "No active notes stored"
        } else {
            formatListString(notes.filter { note -> !note.isNoteArchived })
        }

    fun listArchivedNotes(): String =
        if (numberOfArchivedNotes() == 0) {
            "No archived notes stored"
        } else {
            formatListString(notes.filter { note -> note.isNoteArchived })
        }

    fun listNotesBySelectedPriority(priority: Int): String =
        if (notes.isEmpty()) {
            "No notes stored"
        } else {
            val listOfNotes = formatListString(notes.filter { note -> note.notePriority == priority })
            if (listOfNotes == "") {
                "No notes with priority: $priority"
            } else {
                "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
            }
        }

    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else {
            null
        }
    }

    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        // find the note object by the index number
        val foundNote = findNote(indexToUpdate)

        // if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            return true
        }

        // if the note was not found, return false, indicating that the update was not successful
        return false
    }

    fun searchByTitle(searchString: String) =
        formatListString(
            notes.filter { note -> note.noteTitle.contains(searchString, ignoreCase = true) }
        )

    fun archiveNote(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val noteToArchive = notes[indexToArchive]
            if (!noteToArchive.isNoteArchived) {
                noteToArchive.isNoteArchived = true
                return true
            }
        }
        return false
    }

    fun numberOfNotes() = notes.size
    fun numberOfArchivedNotes() = notes.count { note: Note -> note.isNoteArchived }
    fun numberOfActiveNotes() = notes.count { note: Note -> !note.isNoteArchived }
    fun numberOfNotesByPriority(priority: Int) = notes.count { note: Note -> note.notePriority == priority }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else {
            null
        }
    }

    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, notes)
    }

    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }

    private fun formatListString(notesToFormat: List<Note>): String =
        notesToFormat
            .joinToString(separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString()
            }
}
