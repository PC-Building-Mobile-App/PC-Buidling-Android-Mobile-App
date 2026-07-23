package com.iti.domain.ai.usecase

import com.iti.domain.ai.repository.AiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAiOverviewUseCase @Inject constructor(
    private val repository: AiRepository
) {
    operator fun invoke(query: String): Flow<Result<String>> {
        return repository.getAiOverview(query)
    }
}
