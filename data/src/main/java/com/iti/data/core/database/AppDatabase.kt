package com.iti.data.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iti.data.builds.local.converter.Converters
import com.iti.data.builds.local.dao.BuildDao
import com.iti.data.builds.local.entity.BuildEntity
import com.iti.data.builds.local.entity.BuildIssueEntity
import com.iti.data.builds.local.entity.BuildItemEntity

@Database(
    entities = [BuildEntity::class, BuildItemEntity::class, BuildIssueEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun buildDao(): BuildDao
}