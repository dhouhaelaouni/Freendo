package com.example.freendo

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendAdapter(private val names: List<String>)
    : RecyclerView.Adapter<FriendAdapter.VH>() {
    
    class VH(val tv: TextView) : RecyclerView.ViewHolder(tv)
    
    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(TextView(p.context).apply { 
            setPadding(32, 16, 32, 16)
            textSize = 18f
        })
        
    override fun onBindViewHolder(h: VH, i: Int) { 
        h.tv.text = names[i] 
    }
    
    override fun getItemCount() = names.size
}