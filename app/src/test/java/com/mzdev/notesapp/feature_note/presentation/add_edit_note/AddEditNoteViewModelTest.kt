import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mzdev.notesapp.feature_note.domain.use_cases.AddNoteUseCase
import com.mzdev.notesapp.feature_note.domain.use_cases.DeleteNoteUseCase
import com.mzdev.notesapp.feature_note.domain.use_cases.GetNoteUseCase
import com.mzdev.notesapp.feature_note.domain.use_cases.GetNotesUseCase
import com.mzdev.notesapp.feature_note.domain.use_cases.NoteUseCases
import com.mzdev.notesapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.mzdev.notesapp.feature_note.presentation.add_edit_note.AddEditNoteViewModel
import com.mzdev.notesapp.feature_note.presentation.add_edit_note.UiEvent
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditNoteViewModelTest {

    private lateinit var viewModel: AddEditNoteViewModel
    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var getNoteUseCase: GetNoteUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var getNotesUseCase: GetNotesUseCase

    @Before
    fun setup() {
        addNoteUseCase = mockk(relaxed = true)
        getNoteUseCase = mockk(relaxed = true)
        getNotesUseCase = mockk(relaxed = true)
        deleteNoteUseCase = mockk(relaxed = true)

        viewModel = AddEditNoteViewModel(
            noteUseCases = NoteUseCases(
                getNote = getNoteUseCase,
                addNote = addNoteUseCase,
                deleteNote = deleteNoteUseCase,
                getNotes = getNotesUseCase
            ),
            savedStateHandle = SavedStateHandle()
        )
    }

    @Test
    fun `save note with valid data emits SaveNote event`() = runTest {
        viewModel.onEvent(AddEditNoteEvent.EnteredTitle("Test Title"))
        viewModel.onEvent(AddEditNoteEvent.EnteredContent("Test Content"))

        viewModel.eventFlow.test {
            viewModel.onEvent(AddEditNoteEvent.SaveNote)

            val event = awaitItem()
            assertThat(event).isInstanceOf(UiEvent.SaveNote::class.java)

            cancelAndConsumeRemainingEvents()
        }

        coVerify {
            addNoteUseCase.invoke(
                match {
                    it.title == "Test Title" && it.content == "Test Content"
                }
            )
        }
    }
}
