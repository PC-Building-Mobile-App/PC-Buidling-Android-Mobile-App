package com.iti.presentation.core

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.iti.presentation.R

sealed interface UiText {

    data class DynamicString(val value: String) : UiText

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(id = resId, formatArgs = args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}

fun Throwable.toUiText(): UiText {
    return this.message?.takeIf { it.isNotBlank() }?.let {
        UiText.DynamicString(it)
    } ?: UiText.StringResource(R.string.generic_error_title)
}