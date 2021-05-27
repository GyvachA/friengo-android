package com.hypnex.friengo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hypnex.friengo.adapters.recyclerview.FriendsAdapter
import com.hypnex.friengo.databinding.FragmentChatBinding
import com.hypnex.friengo.utils.UserListTestData

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        val adapter = FriendsAdapter()

        binding.chatsRecyclerView.adapter = adapter

        adapter.updateFriends(UserListTestData.getChat())

        return binding.root
    }

}