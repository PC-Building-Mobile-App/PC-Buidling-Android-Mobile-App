package com.iti.data.profile

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iti.domain.profile.repository.ProfileImageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProfileImageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: DataStore<Preferences>,
) : ProfileImageRepository {

    private object Keys {
        val AVATAR_PATH = stringPreferencesKey("avatar_path")
    }

    override fun observeAvatarPath(): Flow<String?> =
        dataStore.data.map { it[Keys.AVATAR_PATH] }

    override suspend fun saveAvatarFromUri(sourceUriString: String): Result<String> = runCatching {
        val sourceUri = Uri.parse(sourceUriString)
        val destFile = File(context.filesDir, "avatar_${System.currentTimeMillis()}.jpg")

        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            destFile.outputStream().use { output -> input.copyTo(output) }
        } ?: error("Could not open the selected image")

        val previousPath = observeAvatarPath().first()

        dataStore.edit { it[Keys.AVATAR_PATH] = destFile.absolutePath }

        if (previousPath != null && previousPath != destFile.absolutePath) {
            runCatching { File(previousPath).delete() }
        }

        destFile.absolutePath
    }
}