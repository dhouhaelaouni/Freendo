package com.example.freendo

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

// ── Gemini Data classes ──────────────────────────────────────────────────────

data class GeminiPart(val text: String)

data class GeminiContent(val parts: List<GeminiPart>)

data class GeminiRequest(val contents: List<GeminiContent>)

data class GeminiCandidate(val content: GeminiContent)

data class GeminiResponse(val candidates: List<GeminiCandidate>)

// ── Retrofit interface ────────────────────────────────────────────────────────

interface OpenAiService {
    // We keep the name OpenAiService to avoid breaking other files immediately,
    // but update the endpoint and method for Gemini.
    @POST("models/gemini-1.5-flash:generateContent")
    fun sendMessage(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Call<GeminiResponse>
}