package com.iti.data.builds.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "build_issue_entity",
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
data class BuildIssueEntity(
    @PrimaryKey(autoGenerate = true) val autoId: Long = 0,
    val buildId: Int,
    val category: String,
    val reason: String,
)
