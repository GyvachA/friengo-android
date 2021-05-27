package com.hypnex.friengo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.hypnex.friengo.R
import com.hypnex.friengo.databinding.FragmentEventShowBinding
import java.text.SimpleDateFormat

class EventShowFragment : Fragment() {

    private lateinit var binding: FragmentEventShowBinding

    private val args: EventShowFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventShowBinding.inflate(inflater, container, false)

        initUi()
        initToolbar()

        return binding.root
    }

    private fun initUi() {
        val event = args.event

        binding.photoEvent.load(event.photoPath) {
            crossfade(true)
            placeholder(R.mipmap.ic_launcher)
        }

        binding.eventTitleTextView.text = event.title

        binding.eventDescriptionTextView.text = event.description

        val form = SimpleDateFormat("dd.MM.yyyy")

        binding.deadlineTextView.text = "Окончание ${form.format(event.deadlineDate)}"
    }

    private fun initToolbar() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}