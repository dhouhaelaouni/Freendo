package com.example.freendo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ChatBubble(val content: String, val isUser: Boolean)

class ChatAdapter(private val messages: MutableList<ChatBubble>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_USER = 1
        private const val TYPE_AI   = 2
    }

    override fun getItemViewType(position: Int) =
        if (messages[position].isUser) TYPE_USER else TYPE_AI

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = if (viewType == TYPE_USER)
            R.layout.item_chat_user else R.layout.item_chat_ai
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tvMessage).text = messages[position].content
    }

    override fun getItemCount() = messages.size

    fun addMessage(bubble: ChatBubble) {
        messages.add(bubble)
        notifyItemInserted(messages.size - 1)
    }
}