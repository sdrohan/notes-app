import io.github.oshai.kotlinlogging.KotlinLogging
import model.Note
import service.NoteService
import utils.readNextBoolean
import utils.readNextInt
import utils.readNextLine

val noteService = NoteService()
private val logger = KotlinLogging.logger {}

fun main(){
    logger.info { "Notes App Starting" }
    runMenu()
}

fun runMenu (){
    var input : Int
    do {
        input = mainMenu()
        when(input) {
            1 -> addNote()
            2 -> listNotes()
            3 -> updateNote()
            4 -> deleteNote()
            0 -> logger.info { "Notes App Exiting" }
            else -> println("Invalid Option")
        }
    } while (input != 0)
}

fun mainMenu(): Int {
    print(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List all notes            |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > """.trimMargin(">"))
    return readNextInt("==>> ")
}

fun addNote(){
    val title = readNextLine("Title: ")
    val body = readNextLine("Body: ")
    val priority = readNextInt("Priority (1-5): ")
    val category = readNextLine("Category: ")

    noteService.addNote(
        Note(0, title, body, priority, category, false)
    )

    println("Note added")
}

fun listNotes() {
    noteService.getNotes().forEach { println(it) }
}

fun updateNote() {
    val id = readNextInt("Enter ID: ")
    val title = readNextLine("Title: ")
    val body = readNextLine("Body: ")
    val priority = readNextInt("Priority (1-5): ")
    val category = readNextLine("Category: ")
    val isArchived = readNextBoolean("Archive the note (y/n): ")

    val updated = Note(id, title, body, priority, category, isArchived)

    if (noteService.updateNote(id, updated)) {
        println("Updated")
    } else {
        println("Not found")
    }
}

fun deleteNote() {
    val id = readNextInt("Enter ID: ")

    if (noteService.deleteNote(id)) {
        println("Deleted")
    } else {
        println("Not found")
    }
}


