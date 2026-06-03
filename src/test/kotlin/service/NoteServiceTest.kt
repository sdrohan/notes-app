package service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import model.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested

class NoteServiceTest {

    private lateinit var emptyNoteService: NoteService
    private lateinit var populatedNoteService: NoteService
    private lateinit var note1: Note
    private lateinit var note2: Note
    private lateinit var note3: Note
    private lateinit var note4: Note
    private lateinit var note5: Note

    @BeforeEach
    fun setUp() {
        note1 = Note(0, "Test1", "Body1", 1, "Work", false)
        note2 = Note(0, "Test2", "Body2", 2, "Home", true)
        note3 = Note(0, "Test3", "Body3", 3, "College", false)
        note4 = Note(0, "Test4", "Body4", 4, "Social", true)
        // For use in tests - not added to populated list in SetUp
        note5 = Note(0, "Test5", "Body5", 5, "Hobbies", true)

        emptyNoteService = NoteService()
        populatedNoteService = NoteService()

        populatedNoteService.addNote(note1)
        populatedNoteService.addNote(note2)
        populatedNoteService.addNote(note3)
        populatedNoteService.addNote(note4)
    }

    @AfterEach
    fun tearDown() {
        println("Test completed")
    }

    @Nested
    inner class AddNotes {

        @Test
        fun `add note to an empty service increases size correctly`() {
            assertEquals(0, emptyNoteService.getNotes().size)
            emptyNoteService.addNote(note1)
            assertEquals(1, emptyNoteService.getNotes().size)
        }

        @Test
        fun `add note to an populated service increases size correctly`() {
            assertEquals(4, populatedNoteService.getNotes().size)
            populatedNoteService.addNote(note5)
            assertEquals(5, populatedNoteService.getNotes().size)
        }

        @Test
        fun `add note stores correct values`() {
            emptyNoteService.addNote(note1)

            val retrievedNote = emptyNoteService.getNotes().first()
            // Note: other functions test isArchived and id
            assertEquals(note1.title, retrievedNote.title)
            assertEquals(note1.body, retrievedNote.body)
            assertEquals(note1.priority, retrievedNote.priority)
            assertEquals(note1.category, retrievedNote.category)
        }

        @Test
        fun `add note sets isArchived to false`() {
            emptyNoteService.addNote(note1)
            emptyNoteService.addNote(note2)

            val retrievedNote1 = emptyNoteService.findNoteById(0)
            val retrievedNote2 = emptyNoteService.findNoteById(0)

            assertEquals(false, retrievedNote1?.isArchived)
            assertEquals(false, retrievedNote2?.isArchived)
        }

        @Test
        fun `ensure ids remain unique after multiple inserts`() {
            assertEquals(0, populatedNoteService.findNoteById(0)?.id)
            assertEquals(1, populatedNoteService.findNoteById(1)?.id)
            assertEquals(2, populatedNoteService.findNoteById(2)?.id)
            assertEquals(3, populatedNoteService.findNoteById(3)?.id)
        }
    }

    @Nested
    inner class GetNotes{

        @Test
        fun `getNotes returns an empty list when service is empty`() {
            assertTrue(emptyNoteService.getNotes().isEmpty())
        }

        @Test
        fun `getNotes returns all notes when service is populated`() {
            val notes = populatedNoteService.getNotes()
            assertEquals(4, notes.size)
        }
    }

    @Nested
    inner class DeleteNotes{

        @Test
        fun `deleting an existing note returns true`() {
            assertTrue(populatedNoteService.deleteNote(1))
        }

        @Test
        fun `deleting a non existant note returns false`() {
            assertFalse(populatedNoteService.deleteNote(5))
        }

        @Test
        fun `deleting from an empty service returns false`() {
            assertFalse(emptyNoteService.deleteNote(1))
        }

        @Test
        fun `deleting an existing note reduces the number of notes by one`() {
            val sizeBefore = populatedNoteService.getNotes().size
            populatedNoteService.deleteNote(3)
            assertEquals(sizeBefore - 1, populatedNoteService.getNotes().size)
        }

        @Test
        fun `deleting a note removes the correct note`() {
            populatedNoteService.deleteNote(1)
            assertFalse(populatedNoteService.getNotes().any { it.id == 1 })
        }

        @Test
        fun `deleting one note does not remove other notes`() {
            populatedNoteService.deleteNote(1)
            assertTrue(populatedNoteService.getNotes().any { it.id == 0 })
            assertTrue(populatedNoteService.getNotes().any { it.id == 2 })
            assertTrue(populatedNoteService.getNotes().any { it.id == 3 })
        }
    }

    @Nested
    inner class UpdateNotes{

        @Test
        fun `updating an existing note returns true`() {
            val updated = Note(1, "Updated Title", "Updated Body",
                               5, "Work", true)
            assertTrue(populatedNoteService.updateNote(1, updated))
        }

        @Test
        fun `updating a non existing note returns false`() {
            val updated = Note(999, "Updated Title", "Updated Body",
                               5, "Work", false)
            assertFalse(populatedNoteService.updateNote(999, updated))
        }

        @Test
        fun `updating in an empty service returns false`() {
            val updated = Note(1, "Updated Title", "Updated Body",
                5, "Work", false)
            assertFalse(emptyNoteService.updateNote(1, updated))
        }

        @Test
        fun `updating a note changes its stored values`() {
            val updated = Note(1, "Updated Title", "Updated Body",
                5, "Personal", true)

            populatedNoteService.updateNote(1, updated)

            val retrieved = populatedNoteService.getNotes()[1]

            assertEquals("Updated Title", retrieved.title)
            assertEquals("Updated Body", retrieved.body)
            assertEquals(5, retrieved.priority)
            assertEquals("Personal", retrieved.category)
            assertTrue(retrieved.isArchived)
        }

        @Test
        fun `updating a note does not change the number of notes`() {
            val sizeBefore = populatedNoteService.getNotes().size

            val updated = Note(1, "Updated Title", "Updated Body",
                               5, "Personal", true)
            populatedNoteService.updateNote(1, updated)

            assertEquals(sizeBefore, populatedNoteService.getNotes().size)
        }

        @Test
        fun `updating an existing note preserves its id`() {
            val updated = Note(99, "Updated Title", "Updated Body",
                               5, "Work", false)

            populatedNoteService.updateNote(1, updated)
            assertEquals(1, populatedNoteService.findNoteById(1)?.id)
        }

    }

    @Nested
    inner class CountingNotes{

        @Test
        fun `numberOfNotes returns correct total count for populated service`() {
            assertEquals(4, populatedNoteService.numberOfNotes())
        }

        @Test
        fun `numberOfNotes returns zero for empty service`() {
            assertEquals(0, emptyNoteService.numberOfNotes())
        }

        @Test
        fun `numberOfArchivedNotes returns correct count after archiving notes`() {
            assertTrue(populatedNoteService.archiveNote(2))
            assertTrue(populatedNoteService.archiveNote(3))
            assertEquals(2, populatedNoteService.numberOfArchivedNotes())
        }

        @Test
        fun `numberOfArchivedNotes returns zero when no notes are archived`() {
            // new notes are always set to active
            emptyNoteService.addNote(note1)
            emptyNoteService.addNote(note2)
            emptyNoteService.addNote(note3)

            assertEquals(0, emptyNoteService.numberOfArchivedNotes())
        }

        @Test
        fun `numberOfActiveNotes returns correct count when all notes are still active`() {
            // new notes are always added as active, regardless.
            assertEquals(4, populatedNoteService.numberOfActiveNotes())
        }

        @Test
        fun `numberOfActiveNotes returns zero when all notes are archived`() {
            // new notes are always set to active
            emptyNoteService.addNote(note1)
            emptyNoteService.addNote(note2)
            emptyNoteService.addNote(note3)

            // archive all notes
            emptyNoteService.archiveNote(note1.id)
            emptyNoteService.archiveNote(note2.id)
            emptyNoteService.archiveNote(note3.id)

            assertEquals(0, emptyNoteService.numberOfActiveNotes())
        }

        @Test
        fun `numberOfNotesByCategory returns correct count for Work`() {
            assertEquals(1, populatedNoteService.numberOfNotesByCategory("Work"))
        }

        @Test
        fun `numberOfNotesByCategory returns zero when category does not exist`() {
            assertEquals(0, populatedNoteService.numberOfNotesByCategory("Travel"))
        }

        @Test
        fun `numberOfNotesByPriority returns correct count`() {
            // Priority = 2 (note2 only)
            assertEquals(1, populatedNoteService.numberOfNotesByPriority(2))
        }

        @Test
        fun `numberOfNotesByPriority returns zero when priority does not exist`() {
            assertEquals(0, populatedNoteService.numberOfNotesByPriority(99))
        }
    }
}

