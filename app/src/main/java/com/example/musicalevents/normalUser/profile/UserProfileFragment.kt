package com.example.musicalevents.normalUser.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.databinding.FragmentUserProfileBinding
import com.example.musicalevents.utils.UtilsKt
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileFragment : Fragment() {

    private val args: UserProfileFragmentArgs by navArgs()
    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var editedUser: Userkt
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editedUser = args.userProf
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingFields()

        binding.apply {
            btAcceptLinks.setOnClickListener {
                editUser()
                Toast.makeText(context, R.string.updatedLinks, Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }

            btChangePassword.setOnClickListener {
                findNavController().navigate(R.id.action_userProfileFragment_to_changePasswordFragment2)
            }

        }
    }

    private fun bindingFields() {
        binding.apply {
            etEmail.text = editedUser.email
            etName.setText(editedUser.name)
        }
    }

    private fun editUser() {
        binding.apply {
            editedUser.email?.let { it1 ->
                db.collection(UtilsKt.personasTable).document(it1).set(
                    hashMapOf(
                        "email" to editedUser.email,
                        "admin" to editedUser.isAdmin,
                        "password" to editedUser.password,
                        "name" to etName.text.toString(),
                        "instagram" to "",
                        "twitter" to "",
                        "facebook" to "",
                        "website" to "",
                    )
                )
            }
        }
        binding.apply {
            editedUser.instagram = ""
            editedUser.twitter = ""
            editedUser.facebook = ""
            editedUser.website = ""
            editedUser.name = etName.text.toString()
        }
    }
}