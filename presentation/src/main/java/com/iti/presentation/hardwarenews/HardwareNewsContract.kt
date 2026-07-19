package com.iti.presentation.hardwarenews

import com.iti.presentation.core.UiText
import com.iti.presentation.home.model.HardwareNewsUiModel

object HardwareNewsContract {

    data class State(
        val isLoading: Boolean = true,
        val articles: List<HardwareNewsUiModel> = emptyList(),
        val errorMessage: UiText? = null
    )

    sealed interface Event {
        data class ArticleClicked(val articleId: String) : Event
    }

    sealed interface Effect {
        data class NavigateToDetail(val articleId: String) : Effect
    }
}
