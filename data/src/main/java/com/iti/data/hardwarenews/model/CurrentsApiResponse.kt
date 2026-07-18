package com.iti.data.hardwarenews.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentsApiResponse(
    @SerialName("status") val status: String,
    @SerialName("news") val news: List<ArticleDataModel> = emptyList()
)

@Serializable
data class ArticleDataModel(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String = "",
    @SerialName("url") val url: String,
    @SerialName("author") val author: String = "",
    @SerialName("image") val image: String? = null,
    @SerialName("published") val published: String = "",
    @SerialName("urlToImage") val urlToImage: String? = null,
    @SerialName("source") val source: SourceDataModel? = null
)

@Serializable
data class SourceDataModel(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null
)