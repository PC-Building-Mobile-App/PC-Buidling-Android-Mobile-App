package com.iti.domain.hardwarenews.usecase

import com.iti.domain.hardwarenews.model.HardwareNewsArticle
import com.iti.domain.hardwarenews.repository.HardwareNewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLatestHardwareNewsUseCase @Inject constructor(
    private val repository: HardwareNewsRepository
) {
    operator fun invoke(limit: Int? = null): Flow<List<HardwareNewsArticle>> {
        return repository.getLatestHardwareNews().map { articles ->
            if (limit != null) {
                articles.take(limit)
            } else {
                articles
            }
        }
    }
}