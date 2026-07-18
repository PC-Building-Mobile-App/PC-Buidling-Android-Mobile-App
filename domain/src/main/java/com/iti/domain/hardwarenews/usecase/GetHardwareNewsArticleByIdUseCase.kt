package com.iti.domain.hardwarenews.usecase

import com.iti.domain.hardwarenews.model.HardwareNewsArticle
import com.iti.domain.hardwarenews.repository.HardwareNewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHardwareNewsArticleByIdUseCase @Inject constructor(
    private val repository: HardwareNewsRepository
) {
    operator fun invoke(id: String): Flow<HardwareNewsArticle?> {
        return repository.getArticleById(id)
    }
}