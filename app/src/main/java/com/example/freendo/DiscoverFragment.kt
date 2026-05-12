package com.example.freendo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DiscoverFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvCategories)
        rv.layoutManager = GridLayoutManager(context, 2)
        rv.adapter = CategoryAdapter(CATEGORIES) { category ->
            val bundle = Bundle().apply {
                putString("categoryName", category.name)
            }
            findNavController().navigate(
                R.id.action_discoverFragment_to_categoryDetailFragment,
                bundle
            )
        }
    }
}