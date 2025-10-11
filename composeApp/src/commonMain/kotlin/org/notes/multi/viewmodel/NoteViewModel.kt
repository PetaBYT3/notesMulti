package org.notes.multi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.notes.multi.action.NoteAction
import org.notes.multi.state.NoteState

class NoteViewModel(

) : ViewModel() {

    private val _state = MutableStateFlow(NoteState())
    val state = _state.asStateFlow()

    fun onAction(action: NoteAction) {
        when (action) {
            NoteAction.IsPageClosed -> {
                isPageClosed()
            }
            is NoteAction.IsTitleEditEnabled -> {
                isTitleEditEnabled(action.isEnabled)
            }
            is NoteAction.TitleDraft -> {
                titleDraft(action.titleDraft)
            }
            is NoteAction.TextDraft -> {
                textDraft(action.textDraft)
            }
        }
    }

    private fun isPageClosed() {
        _state.update { it.copy(
            titleDraft = null,
            textDraft = null,
            isTitleEditingEnabled = false
        ) }
    }

    private fun isTitleEditEnabled(isEnabled: Boolean) {
        _state.update { it.copy(isTitleEditingEnabled = isEnabled) }
    }

    private fun titleDraft(title: String) {
        _state.update { it.copy(titleDraft = title) }
    }
    private fun textDraft(text: String) {
        _state.update { it.copy(textDraft = text) }
    }
}