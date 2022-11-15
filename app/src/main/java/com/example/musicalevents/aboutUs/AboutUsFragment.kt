package com.example.musicalevents.aboutUs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.musicalevents.databinding.FragmentAboutUsBinding

class AboutUsFragment : Fragment() {

    private lateinit var binding: FragmentAboutUsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            instagramButton.setOnClickListener {
                openLinks("https://www.instagram.com/rafalito_rio/")
            }

            twitterButton.setOnClickListener {
                openLinks("https://twitter.com/RafaRClt")
            }

            facebookButton.setOnClickListener {
                openLinks("https://www.facebook.com/rafa.rioperez.5/")
            }

            githubButton.setOnClickListener {
                openLinks("https://github.com/RafaelRio/MusicalEvents")
            }
        }
    }

    private fun openLinks(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}