package com.iti.data.builds.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BuildWithItemsAndIssues(
    @Embedded val build: BuildEntity,
    @Relation(parentColumn = "id", entityColumn = "buildId") val items: List<BuildItemEntity>,
    @Relation(parentColumn = "id", entityColumn = "buildId") val issues: List<BuildIssueEntity>,
)
