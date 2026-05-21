package com.example.freendo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatFragment : Fragment() {

    private val messages = mutableListOf<ChatBubble>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvChat = view.findViewById<RecyclerView>(R.id.rvChat)
        val etMessage = view.findViewById<EditText>(R.id.etMessage)
        val btnSend = view.findViewById<Button>(R.id.btnSend)

        chatAdapter = ChatAdapter(messages)
        rvChat.layoutManager = LinearLayoutManager(context).also {
            it.stackFromEnd = true
        }
        rvChat.adapter = chatAdapter

        btnSend.setOnClickListener {
            val text = etMessage.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener
            etMessage.setText("")
            sendMessage(text, rvChat)
        }
    }

    private fun sendMessage(userText: String, rvChat: RecyclerView) {
        chatAdapter.addMessage(ChatBubble(userText, isUser = true))
        rvChat.scrollToPosition(messages.size - 1)

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(
                        GeminiPart(text = "You are a helpful social assistant. Suggest a fun activity based on this message: $userText")
                    )
                )
            )
        )

        // Trim the API key to avoid any potential whitespace issues causing 404/403
        val apiKey = BuildConfig.GEMINI_API_KEY.trim()

        if (apiKey.isEmpty()) {
            chatAdapter.addMessage(ChatBubble("⚠️ API Key is missing in local.properties", isUser = false))
            return
        }

        RetrofitClient.openAiService.sendMessage(apiKey, request)
            .enqueue(object : Callback<GeminiResponse> {
                override fun onResponse(
                    call: Call<GeminiResponse>,
                    response: Response<GeminiResponse>
                ) {
                    val aiText = if (response.isSuccessful) {
                        response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                            ?: "No response received"
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("ChatFragment", "API Error: $errorBody")
                        "Error ${response.code()}: ${response.message()}"
                    }
                    
                    activity?.runOnUiThread {
                        chatAdapter.addMessage(ChatBubble(aiText, isUser = false))
                        rvChat.scrollToPosition(messages.size - 1)
                    }
                }

                override fun onFailure(call: Call<GeminiResponse>, t: Throwable) {
                    Log.e("ChatFragment", "Network Failure", t)
                    activity?.runOnUiThread {
                        chatAdapter.addMessage(
                            ChatBubble("⚠️ Network error: ${t.message}", isUser = false)
                        )
                        rvChat.scrollToPosition(messages.size - 1)
                    }
                }
            })
    }
}