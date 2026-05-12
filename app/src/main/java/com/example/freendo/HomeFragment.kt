package com.example.freendo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var myEventsAdapter: EventAdapter
    private lateinit var invitedEventsAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup My Events RecyclerView
        myEventsAdapter = EventAdapter(emptyList())
        val rvMyEvents = view.findViewById<RecyclerView>(R.id.rvMyEvents)
        rvMyEvents.layoutManager = LinearLayoutManager(context)
        rvMyEvents.adapter = myEventsAdapter

        // Setup Invited Events RecyclerView
        invitedEventsAdapter = EventAdapter(emptyList())
        val rvInvitedEvents = view.findViewById<RecyclerView>(R.id.rvInvitedEvents)
        rvInvitedEvents.layoutManager = LinearLayoutManager(context)
        rvInvitedEvents.adapter = invitedEventsAdapter

        // Create Event button
        view.findViewById<Button>(R.id.btnCreateEvent).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createEventFragment)
        }

        loadMyEvents()
        loadInvitedEvents()
    }

    private fun loadMyEvents() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("events")
            .whereEqualTo("creatorId", uid)
            .get()
            .addOnSuccessListener { result ->
                val events = result.toObjects(Event::class.java)
                myEventsAdapter.updateList(events)
            }
    }

    private fun loadInvitedEvents() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("events")
            .whereArrayContains("invitedFriendIds", uid)
            .get()
            .addOnSuccessListener { result ->
                val events = result.toObjects(Event::class.java)
                invitedEventsAdapter.updateList(events)
            }
    }
}