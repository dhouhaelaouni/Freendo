package com.example.freendo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etBio: EditText
    private lateinit var etInterests: EditText
    private lateinit var btnSave: Button
    
    private lateinit var db: FirebaseFirestore
    private lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etName = view.findViewById(R.id.etName)
        etUsername = view.findViewById(R.id.etUsername)
        etBio = view.findViewById(R.id.etBio)
        etInterests = view.findViewById(R.id.etInterests)
        btnSave = view.findViewById(R.id.btnSave)

        db = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Step 9: Pre-fill fields when fragment opens
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    etName.setText(doc.getString("name"))
                    etUsername.setText(doc.getString("username"))
                    etBio.setText(doc.getString("bio"))
                    etInterests.setText(doc.getString("interests"))
                }
            }

        // Step 10: Save button writes back to Firestore
        btnSave.setOnClickListener {
            val updates = hashMapOf(
                "name"      to etName.text.toString(),
                "username"  to etUsername.text.toString(),
                "bio"       to etBio.text.toString(),
                "interests" to etInterests.text.toString()
            )
            db.collection("users").document(uid)
                .update(updates as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                .addOnFailureListener {
                    // Fallback to set if update fails (e.g. document doesn't exist)
                    db.collection("users").document(uid).set(updates)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                }
        }
    }
}