package com.example.freendo

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// ── Gemini Data classes ──────────────────────────────────────────────────────

data class GeminiPart(val text: String)

data class GeminiContent(
    val parts: List<GeminiPart>,
    val role: String = "user"
)

/**
 * FIXED: contents must be a List of GeminiContent.
 * This resolves the "Argument type mismatch" compiler error.
 */
data class GeminiRequest(val contents: List<GeminiContent>)

data class GeminiCandidate(val content: GeminiContent)

data class GeminiResponse(val candidates: List<GeminiCandidate>)

// ── Retrofit interface ────────────────────────────────────────────────────────

interface OpenAiService {
    /**
     * Using v1beta and gemini-1.5-flash.
     * Passing the API key via Header (x-goog-api-key) is the recommended method
     * for Gemini to avoid 404 errors related to URL structure.
     */
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    fun sendMessage(
        @Header("x-goog-api-key") apiKey: String,
        @Body request: GeminiRequest
    ): Call<GeminiResponse>
}