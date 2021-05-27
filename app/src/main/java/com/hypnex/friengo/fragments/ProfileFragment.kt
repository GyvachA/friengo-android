package com.hypnex.friengo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.hypnex.friengo.MainApplication
import com.hypnex.friengo.R
import com.hypnex.friengo.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val preferences = MainApplication.preferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        initInformation()

        return binding.root
    }

    private fun initInformation() {
        val user = preferences.getUser()

        binding.profileImage
            .load(user.photoUrl) {
                crossfade(true)
                fallback(R.drawable.logo_round)
                placeholder(R.drawable.logo_round)
                transformations(CircleCropTransformation())
            }
        binding.nameSecondNameTextView.text = user.displayName ?: "Имя Фамилия"
        binding.emailTextView.text = user.email ?: "mail@mail.com"
        binding.usernameTextInputEditText.setText(user.username ?: "username")
    }

}