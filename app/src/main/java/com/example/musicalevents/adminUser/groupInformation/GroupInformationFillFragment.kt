package com.example.musicalevents.adminUser.groupInformation

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
import com.example.musicalevents.databinding.FragmentGroupFillInformationBinding
import com.example.musicalevents.utils.UtilsKt
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Fragment que realiza la acción de edición del perfil del usuario
 * @author Rafa
 */

class GroupInformationFillFragment : Fragment() {

    private val args: GroupInformationFillFragmentArgs by navArgs()
    private lateinit var binding: FragmentGroupFillInformationBinding
    private lateinit var editedUser: Userkt
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editedUser = args.usuarioAct
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupFillInformationBinding.inflate(inflater, container, false)
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
                findNavController().navigate(R.id.action_groupInformationFillFragment_to_changePasswordFragment)
            }

        }
    }

    private fun bindingFields() {
        binding.apply {
            etEmail.text = editedUser.email
            etInsta.setText(editedUser.instagram)
            etTwitter.setText(editedUser.twitter)
            etFacebook.setText(editedUser.facebook)
            etWebsite.setText(editedUser.website)
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
                        "instagram" to etInsta.text.toString(),
                        "twitter" to etTwitter.text.toString(),
                        "facebook" to etFacebook.text.toString(),
                        "website" to etWebsite.text.toString(),
                    )
                )
            }
        }
        binding.apply {
            editedUser.instagram = etInsta.text.toString()
            editedUser.twitter = etTwitter.text.toString()
            editedUser.facebook = etFacebook.text.toString()
            editedUser.website = etWebsite.text.toString()
            editedUser.name = etName.text.toString()
        }
    }
}