package com.iti.data.aichat.datasource

import com.iti.data.aichat.model.AiChatRequestDto
import com.iti.data.aichat.model.AiChatResponseDto
import com.iti.data.aichat.remote.AiChatApiService
import com.iti.data.util.safeCall
import javax.inject.Inject

class AiChatRemoteDataSourceImpl @Inject constructor(
    private val apiService: AiChatApiService,
) : AiChatDataSource {

    override suspend fun sendMessage(sessionId: Long?, message: String): Result<AiChatResponseDto> =
        safeCall {
            val request = AiChatRequestDto(
                sessionId = sessionId,
                message = message,
            )
            val response = apiService.sendMessage(request)
            if (response.status && response.data != null) {
                response.data
            } else {
                throw Exception(response.message)
            }
        }
}
