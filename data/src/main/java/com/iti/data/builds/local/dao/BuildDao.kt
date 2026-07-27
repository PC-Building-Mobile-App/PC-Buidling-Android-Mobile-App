package com.iti.data.builds.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.iti.data.builds.local.entity.BuildEntity
import com.iti.data.builds.local.entity.BuildIssueEntity
import com.iti.data.builds.local.entity.BuildItemEntity
import com.iti.data.builds.local.entity.BuildWithItemsAndIssues
import com.iti.data.builds.model.BuildCategoryDto
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildDao {

    @Transaction
    @Query("SELECT * FROM build_entity WHERE type = :categoryId")
    suspend fun getBuildsByCategory(categoryId: String): List<BuildWithItemsAndIssues>

    @Query("DELETE FROM build_entity WHERE type = :categoryId")
    suspend fun clearCategory(categoryId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuilds(builds: List<BuildEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<BuildItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIssues(issues: List<BuildIssueEntity>)

    @Query("SELECT type, COUNT(*) as buildsCount FROM build_entity GROUP BY type")
    fun getCategoriesCount(): Flow<List<BuildCategoryDto>>
}