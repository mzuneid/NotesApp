package com.mzdev.notesapp.feature_note.domain.use_cases

import com.mzdev.notesapp.feature_note.domain.model.Note
import com.mzdev.notesapp.feature_note.domain.repository.NoteRepository

class GetNoteUseCase(
    private val repository: NoteRepository,
) {
    suspend operator fun invoke(id: Int): Note? = repository.getNoteById(id)
}
