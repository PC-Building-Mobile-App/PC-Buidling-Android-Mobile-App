package com.iti.domain.profile.usecase

import com.iti.domain.profile.repository.ProfileImageRepository
import javax.inject.Inject

class SaveAvatarUseCase @Inject constructor(
    private val repository: ProfileImageRepository,
) {
    suspend operator fun invoke(sourceUriString: String): Result<String> =
        repository.saveAvatarFromUri(sourceUriString)
}