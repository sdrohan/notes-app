import io.github.oshai.kotlinlogging.KotlinLogging
import model.Item
import model.Note
import persistence.JSONSerializer
import persistence.XMLSerializer
import service.ItemService
import service.NoteItemService
import service.NoteService
import utils.readNextBoolean
import utils.readNextInt
import utils.readNextLine
import utils.readValidCategory
import utils.readValidPriority
import java.io.File

val noteService = NoteService(XMLSerializer(File("notes.xml")))
//val noteService = NoteService(JSONSerializer(File("notes.json")))
val itemService = ItemService(XMLSerializer(File("items.xml")))
val noteItemService = NoteItemService(XMLSerializer(File("noteitems.xml")))

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
            6 -> searchNoteContents()
            7 -> addItemToNote()
            8 -> viewItemsForNote()
            10 -> addItem()
            11 -> listItems()
            12 -> searchItemContents()
            20 -> itemCountReport()
            90 -> save()
            91 -> load()
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
         > |   6) Search note contents      |
         > |   7) Add item to a note        |
         > |   8) View items for a note     |
         > ----------------------------------
         > | ITEM MENU                      |
         > |   10) Add an item              |
         > |   11) List all items           |
         > |   12) Search item contents     |
         > |   13) .....                    |
         > ----------------------------------
         > | REPORTS MENU                   |
         > |   20) Items Report             |
         > |   21) ....                     |
         > ----------------------------------
         > |   90) Save notes               |
         > |   91) Load notes               |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > """.trimMargin(">"))
    return readNextInt("==>> ")
}

fun addNote(){
    val title = readNextLine("Title: ")
    val body = readNextLine("Body: ")
    val priority = readValidPriority("Priority (1-5): ")
    val category = readValidCategory("Category: ")

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
    val priority = readValidPriority("Priority (1-5): ")
    val category = readValidCategory("Category: ")
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

fun searchNoteContents() {
    val content = readNextLine("Enter search contents: ")
    if (content != ""){
        noteService.searchNotesByContent(content).forEach {println(it)}
    }
}

fun addItemToNote() {
    val noteId = readNextInt("Enter Note ID: ")
    if (noteService.findNoteById(noteId) != null) {
        val itemId = readNextInt("Enter Item ID: ")
        if (itemService.findItemById(itemId) != null) {
            if (noteItemService.addItemToNote(noteId, itemId)){
                println("Added")
            }
            else{
                println("Item is already added to Note")
            }
        }
        else{
            println("Item not found")
        }
    } else {
        println("Note not found")
    }
}

fun viewItemsForNote() {
    val id = readNextInt("Enter Note ID: ")
    val itemsIds = noteItemService.getItemIdsForNote(id)
    if (itemsIds.isNotEmpty()) {
        itemsIds.forEach { itemId -> println(itemService.findItemById(itemId))}
    }
}


//-----------------------------------------------------------------------
//   Items Skeleton Code
//-----------------------------------------------------------------------
fun addItem() {
    val description = readNextLine("Description: ")
    val isCompleted = readNextBoolean("Completed (y/n): ")

    itemService.addItem(
        Item(0, description, isCompleted)
    )
    println("Item added")
}

fun listItems() {
    if (itemService.getItems().isEmpty()) {
        println("No items stored")
    } else {
        itemService.getItems().forEach { println(it) }
    }
}

fun searchItemContents() {
    val content = readNextLine("Enter search contents: ")
    if (content != "") {
        itemService.searchItemsByContent(content).forEach { println(it) }
    }
}

fun itemCountReport(){
    println("""
        | ----------------------
        | ITEM REPORT
        | ----------------------
        | Total Items:      ${itemService.numberOfItems()}
        | Completed Items:  ${itemService.numberOfCompletedItems()}
        | Incomplete Items: ${itemService.numberOfIncompleteItems()}
        | ----------------------        
    """.trimIndent())
}

//-----------------------------------------------------------------------
//   Persistence
//-----------------------------------------------------------------------

fun save() {
    try {
        noteService.store()
        itemService.store()
        noteItemService.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteItemService.load()
        itemService.load()
        noteService.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}