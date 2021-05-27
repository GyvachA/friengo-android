package com.hypnex.friengo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.hypnex.friengo.MainApplication
import com.hypnex.friengo.R
import com.hypnex.friengo.adapters.recyclerview.FriendsAdapter
import com.hypnex.friengo.data.models.FriendsGroup
import com.hypnex.friengo.databinding.FragmentFriendsBinding
import com.hypnex.friengo.utils.BaseFragment
import com.hypnex.friengo.utils.DialogUtils
import com.hypnex.friengo.viewmodels.FriendsViewModel

class FriendsFragment : BaseFragment() {

    private lateinit var binding: FragmentFriendsBinding

    private lateinit var viewModel: FriendsViewModel

    private val preferences = MainApplication.preferences

    private val rvAdapter = FriendsAdapter()

    private var friendGroups = listOf<FriendsGroup>()

    private lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(FriendsViewModel::class.java)

        initSpinner()

        initToolbar()

        binding.friendsRecyclerView.adapter = rvAdapter

        observeMainView(viewModel)

        viewModel.groupsInformation.observe(viewLifecycleOwner, { list ->
            friendGroups = list
            spinnerAdapter.clear()
            spinnerAdapter.addAll(list.map { it.title } )
        })

        binding.friendsGroupSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                for (group in friendGroups) {
                    if (parent?.getItemAtPosition(position).toString() == group.title) {
                        viewModel.loadUsersList(group.userIds ?: listOf())
                        break
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        viewModel.usersList.observe(viewLifecycleOwner, {
            rvAdapter.updateFriends(it)
        })

        viewModel.loadUserFriendGroups(preferences.getUser().email!!)

        return binding.root
    }

    private fun initToolbar() {
        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.addFriends -> {
                    DialogUtils.DialogWithEditText {
                        viewModel.addFriend(preferences.getUser().email!!, it.text.toString())
                    }.show(childFragmentManager, "Dialog add")
                    true
                }
                else -> false
            }
        }
    }

    private fun initSpinner() {
        spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_friends_item)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.friendsGroupSpinner.adapter = spinnerAdapter
    }

    override fun onResume() {
        super.onResume()
    }
}