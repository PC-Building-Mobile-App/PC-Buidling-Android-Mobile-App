package com.iti.data.builds.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "build_item_entity",
    foreignKeys = [
        ForeignKey(
            entity = BuildEntity::class,
            parentColumns = ["id"],
            childColumns = ["buildId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("buildId")],
)
data class BuildItemEntity(
    @PrimaryKey(autoGenerate = true) val autoId: Long = 0,
    val buildId: Int,
    val productId: Long,
    val productName: String,
    val category: String,
    val price: Double,
    val quantity: Int,
    val subtotal: Double,
    val imageUrl: String?,
    val images: List<String>,
)
