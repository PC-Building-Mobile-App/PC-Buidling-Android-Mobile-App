package com.iti.data.ai.datasource

import com.iti.data.BuildConfig
import com.iti.data.util.safeCall
import com.google.ai.client.generativeai.GenerativeModel
import javax.inject.Inject

class AiRemoteDataSourceImpl @Inject constructor() : AiDataSource {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-3.5-flash-lite",
        apiKey = BuildConfig.GEMINI_KEY
    )

    override suspend fun generateOverview(query: String): Result<String> = safeCall {
        if (BuildConfig.GEMINI_KEY.isBlank()) {
            throw Exception("API key missing")
        }

        val prompt = """
            You are a PC hardware expert. 
            Provide a helpful 3-sentence summary for: "$query".
            If it is a specific component, mention its pros, cons, and performance.
            If it is a category (GPU, CPU, etc.), mention current market trends or buying advice.
            Be objective and professional.
        """.trimIndent()

        val response = generativeModel.generateContent(prompt)
        response.text ?: throw Exception("Empty response")
    }
}
