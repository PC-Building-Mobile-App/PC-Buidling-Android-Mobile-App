package com.iti.presentation.home.mapper

import com.iti.domain.hardwarenews.model.HardwareNewsArticle
import com.iti.presentation.home.model.HardwareNewsUiModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun HardwareNewsArticle.toUiModel(): HardwareNewsUiModel = HardwareNewsUiModel(
    id = id,
    title = title,
    description = description,
    sourceBadge = formatSourceBadge(sourceName, author),
    imageUrl = imageUrl ?: "",
    articleUrl = url,
    publishedDate = formatDate(publishedAt),
)

fun List<HardwareNewsArticle>.toUiModels(): List<HardwareNewsUiModel> = map { it.toUiModel() }

private fun formatSourceBadge(sourceName: String?, author: String): String {
    if (!sourceName.isNullOrBlank() && sourceName != "Hardware News") {
        return sourceName.take(15)
    }
    return if (author != "Unknown Author") author.take(15) else "Hardware"
}

private fun formatDate(rawDate: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val date = parser.parse(rawDate)
        if (date != null) formatter.format(date) else rawDate
    } catch (e: Exception) {
        rawDate.take(10)
    }
}