package com.iti.domain.components.model

data class PageResult<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int
)
