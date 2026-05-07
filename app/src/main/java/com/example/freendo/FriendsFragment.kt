package com.example.freendo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class FriendsFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var uid: String

    private lateinit var etSearchUser: EditText
    private lateinit var btnSearch: Button
    private lateinit var tvSearchResult: TextView
    private lateinit var btnAddFriend: Button
    private lateinit var rvFriends: RecyclerView

    private var foundFriendId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        uid = auth.currentUser?.uid ?: return

        etSearchUser = view.findViewById(R.id.etSearchUser)
        btnSearch = view.findViewById(R.id.btnSearch)
        tvSearchResult = view.findViewById(R.id.tvSearchResult)
        btnAddFriend = view.findViewById(R.id.btnAddFriend)
        rvFriends = view.findViewById(R.id.rvFriends)

        // Step 14: Set up RecyclerView
        rvFriends.layoutManager = LinearLayoutManager(context)

        // Step 16: Wire Search button
        btnSearch.setOnClickListener {
            val query = etSearchUser.text.toString().trim()
            if (query.isEmpty()) return@setOnClickListener

            db.collection("users")
                .whereEqualTo("username", query)
                .get()
                .addOnSuccessListener { docs ->
                    if (docs.isEmpty) {
                        tvSearchResult.text = "No user found"
                        btnAddFriend.visibility = View.GONE
                    } else {
                        val doc = docs.first()
                        foundFriendId = doc.id
                        tvSearchResult.text = "Found: ${doc.getString("name")}"
                        btnAddFriend.visibility = View.VISIBLE
                    }
                }
        }

        // Step 17: Wire Add Friend button
        btnAddFriend.setOnClickListener {
            val fid = foundFriendId ?: return@setOnClickListener
            val data = hashMapOf("userId" to uid, "friendId" to fid)
            db.collection("friends").add(data)
                .addOnSuccessListener {
                    Toast.makeText(context, "Friend added!", Toast.LENGTH_SHORT).show()
                    btnAddFriend.visibility = View.GONE
                    loadFriends()   // refresh the list
                }
        }

        loadFriends()
    }

    // Step 18: loadFriends() helper function
    private fun loadFriends() {
        db.collection("friends")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { docs ->
                val friendIds = docs.map { it.getString("friendId") ?: "" }.filter { it.isNotEmpty() }
                if (friendIds.isEmpty()) {
                    rvFriends.adapter = FriendAdapter(emptyList())
                    return@addOnSuccessListener
                }

                db.collection("users")
                    .whereIn(FieldPath.documentId(), friendIds)
                    .get()
                    .addOnSuccessListener { users ->
                        val names = users.map { it.getString("name") ?: "Unknown" }
                        rvFriends.adapter = FriendAdapter(names)
                    }
            }
    }
}