package com.iti.domain.builds.model

import com.iti.domain.components.model.Component

data class Build(
    val id: String,
    val name: String,
    val totalPrice: Double,
    val compatible: Boolean,
    val items: List<Component>,
    val issues: List<String>,
    val createdAt: String,
    val updatedAt: String,
)