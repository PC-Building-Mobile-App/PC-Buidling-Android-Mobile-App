package com.iti.domain.hardwarenews.repository

import com.iti.domain.hardwarenews.model.HardwareNewsArticle
import kotlinx.coroutines.flow.Flow

interface HardwareNewsRepository {
    fun getLatestHardwareNews(): Flow<List<HardwareNewsArticle>>
    fun getArticleById(id: String): Flow<HardwareNewsArticle?>
}