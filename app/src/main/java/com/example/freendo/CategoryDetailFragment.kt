package com.example.freendo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryName = arguments?.getString("categoryName") ?: "Activities"
        val activities = ACTIVITIES_MAP[categoryName] ?: emptyList()

        view.findViewById<TextView>(R.id.tvCategoryName).text = categoryName
        
        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            findNavController().navigateUp()
        }

        val rv = view.findViewById<RecyclerView>(R.id.rvActivities)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                object : RecyclerView.ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_activity, parent, false)
                ) {}

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder.itemView.findViewById<TextView>(R.id.tvActivityTitle).text =
                    activities[position]
            }

            override fun getItemCount() = activities.size
        }
    }
}