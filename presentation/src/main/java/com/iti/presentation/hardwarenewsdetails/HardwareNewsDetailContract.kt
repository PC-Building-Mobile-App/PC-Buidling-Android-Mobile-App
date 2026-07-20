package com.iti.presentation.hardwarenewsdetails

import com.iti.presentation.core.UiText
import com.iti.presentation.home.model.HardwareNewsUiModel

object HardwareNewsDetailContract {

    data class State(
        val isLoading: Boolean = true,
        val article: HardwareNewsUiModel? = null,
        val errorMessage: UiText? = null
    )

    sealed interface Event {
        data class LoadArticle(val articleId: String) : Event
        data object OpenInBrowser : Event
    }

    sealed interface Effect {
        data class OpenUrl(val url: String) : Effect
    }
}