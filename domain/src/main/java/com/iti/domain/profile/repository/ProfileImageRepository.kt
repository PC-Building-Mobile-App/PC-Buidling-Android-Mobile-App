package com.iti.domain.profile.repository

import kotlinx.coroutines.flow.Flow

interface ProfileImageRepository {
    fun observeAvatarPath(): Flow<String?>

    suspend fun saveAvatarFromUri(sourceUriString: String): Result<String>
}