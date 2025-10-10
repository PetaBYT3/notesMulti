package org.notes.multi.action

sealed interface HomeAction {

    data class Test(val test: String): HomeAction

}