package com.mzdev.notesapp.feature_note.presentation.notes

import com.google.common.truth.Truth.assertThat
import com.mzdev.notesapp.feature_note.domain.model.Note
import com.mzdev.notesapp.feature_note.domain.use_cases.AddNoteUseCase
import com.mzdev.notesapp.feature_note.domain.use_cases.DeleteNoteUseCase
import com.mzdev.notesapp.feature_note.domain.use_cases.GetNoteUseCase
import com.mzdev.notesapp.feature_note.domain.use_cases.GetNotesUseCase
import com.mzdev.notesapp.feature_note.domain.use_cases.NoteUseCases
import com.mzdev.notesapp.feature_note.domain.util.NoteOrder
import com.mzdev.notesapp.feature_note.domain.util.OrderType
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private lateinit var viewModel: NotesViewModel
    private val getNotes: GetNotesUseCase = mockk()
    private val deleteNote: DeleteNoteUseCase = mockk(relaxed = true)
    private val addNote: AddNoteUseCase = mockk(relaxed = true)
    private val getNote: GetNoteUseCase = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { getNotes(any()) } returns flowOf(sampleNotes)

        viewModel = NotesViewModel(
            NoteUseCases(
                getNotes = getNotes,
                deleteNote = deleteNote,
                addNote = addNote,
                getNote = getNote
            )
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state contains sample notes`() = runTest {
        // Advance coroutine to allow state to be populated
        advanceUntilIdle()

        assertThat(viewModel.state.value.notes).isEqualTo(sampleNotes)
        assertThat(viewModel.state.value.noteOrder).isInstanceOf(NoteOrder.Date::class.java)
    }

    @Test
    fun `toggle order section should change visibility state`() = runTest {
        val initial = viewModel.state.value.isOrderSectionVisible

        viewModel.onEvent(NotesEvent.ToggleOrderSection)
        assertThat(viewModel.state.value.isOrderSectionVisible).isEqualTo(!initial)
    }

    @Test
    fun `delete and restore note should call use cases`() = runTest {
        val note = sampleNotes.first()

        viewModel.onEvent(NotesEvent.DeleteNote(note))
        advanceUntilIdle()

        coVerify  { deleteNote(note) }

        viewModel.onEvent(NotesEvent.RestoreNote)
        advanceUntilIdle()

        coVerify { addNote(note) }
    }

    @Test
    fun `ordering with same order type and class should not reload`() = runTest {
        // Let the initial call in init block complete
        advanceUntilIdle()

        // Clear any previous recorded invocations
        clearMocks(getNotes)

        // Trigger the event with the same order
        viewModel.onEvent(NotesEvent.Order(NoteOrder.Date(OrderType.Descending)))

        // Advance again to allow coroutine to run if any
        advanceUntilIdle()

        // Verify that getNotes is NOT called again
        verify(exactly = 0) { getNotes(any()) }
    }

    @Test
    fun `change ordering to Title Ascending should update state`() = runTest {
        viewModel.onEvent(NotesEvent.Order(NoteOrder.Title(OrderType.Ascending)))
        advanceUntilIdle()

        assertThat(viewModel.state.value.noteOrder).isInstanceOf(NoteOrder.Title::class.java)
        assertThat(viewModel.state.value.noteOrder.orderType).isEqualTo(OrderType.Ascending)
    }

    private val sampleNotes = listOf(
        Note(id = 1, title = "A", content = "Content A", timestamp = 1L, color = 0),
        Note(id = 2, title = "B", content = "Content B", timestamp = 2L, color = 0),
    )
}
