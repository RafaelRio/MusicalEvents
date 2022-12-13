package com.example.musicalevents.adminUser.groupInformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musicalevents.R
import com.example.musicalevents.data.repository.LoginRepository
import com.example.musicalevents.databinding.FragmentChangePasswordBinding
import com.example.musicalevents.utils.UtilsKt
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.internal.Util

class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding
    private var currentUser = LoginRepository.currentUser
    private val db = FirebaseFirestore.getInstance()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnChange.setOnClickListener {

                //Comprobacion de que la contraseña introducida coincide con la del usuario
                if (Util.shaBase64(binding.tieYourPassword.text.toString()) == currentUser.password) {

                    //Comprobacion de que las contraseñas nuevas coincidan
                    if (tieNewPassword.text.toString() == tieConfirmNewPassword.text.toString()) {

                        //Comprobacion de que cumple el formato
                        if (UtilsKt.isPasswordValid(tieNewPassword.text.toString())) {
                            currentUser.password = Util.shaBase64(tieNewPassword.text.toString())
                            currentUser.email?.let { it1 ->
                                db.collection(UtilsKt.personasTable).document(it1).set(
                                    hashMapOf(
                                        "email" to currentUser.email,
                                        "admin" to currentUser.isAdmin,
                                        "password" to currentUser.password,
                                        "name" to currentUser.name,
                                        "instagram" to currentUser.instagram,
                                        "twitter" to currentUser.twitter,
                                        "facebook" to currentUser.facebook,
                                        "website" to currentUser.website,
                                    )
                                )
                            }

                            Toast.makeText(context, R.string.updated_password, Toast.LENGTH_LONG).show()
                            findNavController().navigateUp()
                        } else {
                            Toast.makeText(
                                context,
                                R.string.error_passwordFormat,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            R.string.error_password_dont_match,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(context, R.string.not_your_password, Toast.LENGTH_LONG).show()
                }
            }
        }


    }


}