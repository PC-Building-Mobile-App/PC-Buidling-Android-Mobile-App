package com.iti.data.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException


suspend inline fun <T> safeCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline call: suspend () -> T
): Result<T> = withContext(dispatcher) {
    try {
        Result.success(call())
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.failure(e.toAppException())
    }
}
