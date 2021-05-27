package com.hypnex.friengo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hypnex.friengo.MainActivity
import com.hypnex.friengo.databinding.FragmentSignBinding

class SignFragment : Fragment() {

    private lateinit var binding: FragmentSignBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignBinding.inflate(inflater, container, false)

        initButtons()

        return binding.root
    }

    private fun initButtons() {
        binding.googleSignInButton.setOnClickListener {
            binding.googleSignInButton.isEnabled = false
            (activity as MainActivity).startSignIn {
                binding.googleSignInButton.isEnabled = true
            }
        }
    }

}