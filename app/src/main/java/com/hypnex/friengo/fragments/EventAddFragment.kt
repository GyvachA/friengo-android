package com.hypnex.friengo.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hypnex.friengo.MainApplication
import com.hypnex.friengo.R
import com.hypnex.friengo.data.models.Event
import com.hypnex.friengo.databinding.FragmentEventAddBinding
import com.hypnex.friengo.utils.BaseFragment
import com.hypnex.friengo.utils.toast
import com.hypnex.friengo.viewmodels.EventAddViewModel
import java.util.*

class EventAddFragment : BaseFragment() {

    private lateinit var binding: FragmentEventAddBinding

    private lateinit var viewModel: EventAddViewModel

    private val preferences = MainApplication.preferences

    private val calendar = Calendar.getInstance()

    private val args: EventAddFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventAddBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(EventAddViewModel::class.java)

        observeMainView(viewModel)

        initToolbar()

        initCalendarView()

        return binding.root
    }

    private fun initCalendarView() {
        binding.deadlineCalendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)

            calendarView.date = calendar.timeInMillis
        }
    }

    private fun initToolbar() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.saveToolbar -> {
                    toast(calendar.toString())
                    if (isFormCorrect()) {

                        val coordinates = args.coordinates
                        val user = preferences.getUser()

                        viewModel.createEvent(
                            Event(
                                coordinates.longitude,
                                coordinates.latitude,
                                binding.titleEditText.text.toString(),
                                binding.descriptionEditText.text.toString(),
                                user.photoUrl,
                                calendar.time
                            ),
                            user
                        )

                        findNavController().navigateUp()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun isFormCorrect(): Boolean {
        if (TextUtils.isEmpty(binding.titleEditText.text)
            || TextUtils.isEmpty(binding.descriptionEditText.text)) {
            toast("Пустое поле")
            return false
        }

        if (binding.titleEditText.text.toString().length > 49) {
            toast("Длина названия не более 50 символов")
            return false
        }

        return true
    }

}