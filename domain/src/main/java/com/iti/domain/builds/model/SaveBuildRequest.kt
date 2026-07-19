package com.iti.domain.builds.model

data class SaveBuildRequest(
    val name: String,
    val componentIds: List<Long>,
    val categoryId: String,
)