package com.example.freendo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        view.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val name = view.findViewById<EditText>(R.id.etName).text.toString().trim()
            val email = view.findViewById<EditText>(R.id.etEmail).text.toString().trim()
            val password = view.findViewById<EditText>(R.id.etPassword).text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val userId = result.user!!.uid
                    val username = email.substringBefore("@")

                    val userDoc = hashMapOf(
                        "userId" to userId,
                        "name" to name,
                        "email" to email,
                        "username" to username
                    )

                    db.collection("users").document(userId).set(userDoc)
                        .addOnSuccessListener {
                            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "DB Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Auth Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        view.findViewById<TextView>(R.id.tvToLogin).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
