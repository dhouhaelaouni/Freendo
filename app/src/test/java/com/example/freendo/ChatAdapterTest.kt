package com.example.freendo

import org.junit.Assert.assertEquals
import org.junit.Test

class ChatAdapterTest {

    @Test
    fun testAdapterItemCount() {
        val messages = mutableListOf(
            ChatBubble("Hello", true),
            ChatBubble("Hi there!", false)
        )
        val adapter = ChatAdapter(messages)
        assertEquals(2, adapter.itemCount)
    }

    @Test
    fun testItemViewType() {
        val messages = mutableListOf(
            ChatBubble("User message", true),
            ChatBubble("AI response", false)
        )
        val adapter = ChatAdapter(messages)
        
        // TYPE_USER = 1, TYPE_AI = 2
        assertEquals(1, adapter.getItemViewType(0))
        assertEquals(2, adapter.getItemViewType(1))
    }

    @Test
    fun testAddMessage() {
        val messages = mutableListOf<ChatBubble>()
        val adapter = ChatAdapter(messages)
        
        adapter.addMessage(ChatBubble("New message", true))
        
        assertEquals(1, adapter.itemCount)
        assertEquals("New message", messages[0].content)
    }
}
