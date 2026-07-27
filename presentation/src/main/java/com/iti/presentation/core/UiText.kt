package com.iti.presentation.core

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.iti.domain.exceptions.AppException
import com.iti.domain.exceptions.AuthException
import com.iti.domain.exceptions.NetworkException
import com.iti.domain.exceptions.ServerException
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
    return when (this) {

        is AuthException.Unauthorized ->
            UiText.StringResource(R.string.error_unauthorized_session)
        is AuthException.InvalidCredentials ->
            UiText.StringResource(R.string.error_invalid_credentials)
        is AuthException.EmailAlreadyExists ->
            UiText.StringResource(R.string.error_email_already_exists)

        is NetworkException.NoInternet ->
            UiText.StringResource(R.string.error_no_internet)
        is NetworkException.Timeout ->
            UiText.StringResource(R.string.error_timeout)

        is ServerException.Unauthorized ->
            UiText.StringResource(R.string.error_server_unauthorized)
        is ServerException.Forbidden ->
            UiText.StringResource(R.string.error_server_forbidden)
        is ServerException.NotFound ->
            UiText.StringResource(R.string.error_server_not_found)
        is ServerException.InternalServerError ->
            UiText.StringResource(R.string.error_server_internal)
        is ServerException.ServiceUnavailable ->
            UiText.StringResource(R.string.error_server_unavailable)
        is ServerException.Generic ->
            UiText.StringResource(R.string.error_server_generic, message)

        is AppException.Unknown ->
            message?.takeIf { it.isNotBlank() }
                ?.let { UiText.DynamicString(it) }
                ?: UiText.StringResource(R.string.error_generic)

        else -> UiText.StringResource(R.string.error_generic)
    }
}