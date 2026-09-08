import io.github.oshai.kotlinlogging.KotlinLogging
import model.Note
import persistence.JSONSerializer
import persistence.XMLSerializer
import service.NoteService
import utils.readNextBoolean
import utils.readNextInt
import utils.readNextLine
import java.io.File

//val noteService = NoteService(XMLSerializer(File("notes.xml")))
val noteService = NoteService(JSONSerializer(File("notes.json")))

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
            5 -> archiveNote()
            20 -> save()
            21 -> load()
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
         > |   5) Archive a note            |
         > ----------------------------------
         > |   20) Save notes               |
         > |   21) Load notes               |
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
    if (noteService.numberOfNotes() > 0){
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL notes          |
                  > |   2) View ACTIVE notes       |
                  > |   3) View ARCHIVED notes     |
                  > --------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> noteService.getNotes().forEach {println(it)}
            2 -> noteService.getActiveNotes().forEach {println(it)}
            3 -> noteService.getArchivedNotes().forEach {println(it)}
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No notes stored")
    }

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

fun archiveNote(){
    noteService.getActiveNotes().forEach { println(it) }
    val id = readNextInt("Enter ID of Note to be Archived: ")
    if (noteService.archiveNote(id))
        println("Archived")
    else
        println("Not found")
}

fun save() {
    try {
        noteService.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteService.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}