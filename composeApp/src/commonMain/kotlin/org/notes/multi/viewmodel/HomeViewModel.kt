package org.notes.multi.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.notes.multi.action.HomeAction
import org.notes.multi.repository.TestRepository
import org.notes.multi.state.HomeState

class HomeViewModel(
    private val testRepository: TestRepository
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        injectTest()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.Test -> {
                _state.update { it.copy(test = action.test) }
            }
        }
    }

    private fun injectTest() {
        _state.update { it.copy(injectTest = testRepository.test) }
    }
}