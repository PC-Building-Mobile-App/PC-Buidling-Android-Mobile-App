package com.iti.data.builds.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "build_entity")
data class BuildEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String,
    val typeDisplayName: String,
    val totalPrice: Double,
    val compatible: Boolean,
    val createdAt: String,
    val updatedAt: String,
)
