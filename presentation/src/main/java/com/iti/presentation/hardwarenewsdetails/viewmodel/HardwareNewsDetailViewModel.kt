package com.iti.presentation.hardwarenewsdetails.viewmodel

import androidx.lifecycle.viewModelScope
import com.iti.domain.hardwarenews.usecase.GetHardwareNewsArticleByIdUseCase
import com.iti.presentation.R
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.UiText
import com.iti.presentation.core.toUiText
import com.iti.presentation.home.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.iti.presentation.hardwarenewsdetails.HardwareNewsDetailContract.Effect
import com.iti.presentation.hardwarenewsdetails.HardwareNewsDetailContract.Event
import com.iti.presentation.hardwarenewsdetails.HardwareNewsDetailContract.State

@HiltViewModel
class HardwareNewsDetailViewModel @Inject constructor(
    private val getArticleByIdUseCase: GetHardwareNewsArticleByIdUseCase
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State()

    override fun onEvent(event: Event) {
        when (event) {
            is Event.LoadArticle -> loadArticle(event.articleId)
            is Event.OpenInBrowser -> {
                state.value.article?.articleUrl?.let { url ->
                    sendEffect(Effect.OpenUrl(url))
                }
            }
        }
    }

    private fun loadArticle(articleId: String) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            getArticleByIdUseCase(articleId)
                .catch { throwable ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.toUiText()
                        )
                    }
                }
                .collect { article ->
                    updateState {
                        if (article != null) {
                            it.copy(
                                isLoading = false,
                                article = article.toUiModel()
                            )
                        } else {
                            it.copy(
                                isLoading = false,
                                errorMessage = UiText.StringResource(R.string.article_not_found)
                            )
                        }
                    }
                }
        }
    }
}