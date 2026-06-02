package service

import model.Note

class NoteService {

    private val notes = ArrayList<Note>()
    private var lastId = 0
    private fun getId(): Int = lastId++

    fun addNote(note: Note) {
        note.id = getId()
        notes.add(note)
    }

    fun getNotes(): List<Note> {
        return notes
    }

}