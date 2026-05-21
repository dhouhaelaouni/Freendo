package com.example.freendo

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

// ── Gemini Data classes ──────────────────────────────────────────────────────

data class GeminiPart(val text: String)

data class GeminiContent(
    val parts: List<GeminiPart>,
    val role: String = "user"
)

data class GeminiRequest(val contents: List<GeminiContent>)

data class GeminiCandidate(val content: GeminiContent)

data class GeminiResponse(val candidates: List<GeminiCandidate>)

// ── Retrofit interface ────────────────────────────────────────────────────────

interface OpenAiService {
    /**
     * Using models/gemini-1.5-flash:generateContent on the stable v1 API.
     */
    @POST("models/gemini-1.5-flash:generateContent")
    fun sendMessage(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Call<GeminiResponse>
}