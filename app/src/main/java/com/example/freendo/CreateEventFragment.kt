package com.example.freendo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class CreateEventFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var selectedDateTime = ""
    private val friendCheckboxMap = mutableMapOf<String, CheckBox>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvDateTime = view.findViewById<TextView>(R.id.tvSelectedDateTime)
        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etLocation = view.findViewById<EditText>(R.id.etLocation)
        val etNotes = view.findViewById<EditText>(R.id.etNotes)
        val llFriends = view.findViewById<LinearLayout>(R.id.llFriendsContainer)

        // Date & Time picker
        view.findViewById<Button>(R.id.btnPickDateTime).setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    TimePickerDialog(
                        requireContext(),
                        { _, hour, minute ->
                            selectedDateTime = String.format(
                                "%04d-%02d-%02d %02d:%02d",
                                year, month + 1, day, hour, minute
                            )
                            tvDateTime.text = selectedDateTime
                        },
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Load friends as checkboxes
        loadFriends(llFriends)

        // Save button
        view.findViewById<Button>(R.id.btnSaveEvent).setOnClickListener {
            val title = etTitle.text.toString().trim()
            val location = etLocation.text.toString().trim()
            val notes = etNotes.text.toString().trim()
            val uid = auth.currentUser?.uid ?: return@setOnClickListener

            if (title.isEmpty()) {
                etTitle.error = "Title required"
                return@setOnClickListener
            }
            if (selectedDateTime.isEmpty()) {
                Toast.makeText(context, "Please pick a date & time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val invitedIds = friendCheckboxMap
                .filter { it.value.isChecked }
                .keys.toList()

            val eventId = db.collection("events").document().id
            val event = hashMapOf(
                "id" to eventId,
                "title" to title,
                "dateTime" to selectedDateTime,
                "location" to location,
                "notes" to notes,
                "creatorId" to uid,
                "invitedFriendIds" to invitedIds
            )

            db.collection("events").document(eventId)
                .set(event)
                .addOnSuccessListener {
                    Toast.makeText(context, "Event saved!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun loadFriends(container: LinearLayout) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("friends")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { result ->
                container.removeAllViews()
                val friendIds = result.mapNotNull { it.getString("friendId") }.filter { it.isNotEmpty() }
                
                if (friendIds.isEmpty()) {
                    val noFriends = TextView(requireContext())
                    noFriends.text = "No friends added yet"
                    container.addView(noFriends)
                    return@addOnSuccessListener
                }

                db.collection("users")
                    .whereIn(FieldPath.documentId(), friendIds)
                    .get()
                    .addOnSuccessListener { users ->
                        for (userDoc in users) {
                            val fId = userDoc.id
                            val fName = userDoc.getString("name") ?: "Unknown"
                            val checkBox = CheckBox(requireContext()).apply {
                                text = fName
                                tag = fId
                            }
                            friendCheckboxMap[fId] = checkBox
                            container.addView(checkBox)
                        }
                    }
            }
    }
}