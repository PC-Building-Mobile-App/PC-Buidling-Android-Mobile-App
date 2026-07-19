package com.iti.presentation.hardwarenews.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.hardwarenews.usecase.GetLatestHardwareNewsUseCase
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.toUiText
import com.iti.presentation.hardwarenews.HardwareNewsContract.Effect
import com.iti.presentation.hardwarenews.HardwareNewsContract.Event
import com.iti.presentation.hardwarenews.HardwareNewsContract.State
import com.iti.presentation.home.mapper.toUiModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HardwareNewsViewModel @Inject constructor(
    private val getLatestHardwareNewsUseCase: GetLatestHardwareNewsUseCase
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State()

    init {
        loadArticles()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.ArticleClicked -> {
                sendEffect(Effect.NavigateToDetail(event.articleId))
            }
        }
    }

    private fun loadArticles() {
        viewModelScope.launch {
            getLatestHardwareNewsUseCase()
                .catch { throwable ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.toUiText()
                        )
                    }
                }
                .collect { articles ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            articles = articles.toUiModels()
                        )
                    }
                }
        }
    }
}
