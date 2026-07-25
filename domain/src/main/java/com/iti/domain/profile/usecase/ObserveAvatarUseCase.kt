package com.iti.domain.profile.usecase

import com.iti.domain.profile.repository.ProfileImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAvatarUseCase @Inject constructor(
    private val repository: ProfileImageRepository,
) {
    operator fun invoke(): Flow<String?> = repository.observeAvatarPath()
}