package com.example.freendo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvBio: TextView
    private lateinit var tvInterests: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvBio = view.findViewById(R.id.tvBio)
        tvInterests = view.findViewById(R.id.tvInterests)
        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)
        val btnLogout = view.findViewById<ImageButton>(R.id.btnLogout)

        // Step 6: Read user doc from Firestore
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    tvName.text = doc.getString("name")
                    tvEmail.text = doc.getString("email")
                    tvBio.text = doc.getString("bio")
                    tvInterests.text = doc.getString("interests")
                }
            }

        // Step 7: Wire Edit Profile button
        btnEditProfile.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_editProfileFragment
            )
        }

        // Logout logic
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(
                R.id.action_profileFragment_to_loginFragment
            )
        }
    }
}