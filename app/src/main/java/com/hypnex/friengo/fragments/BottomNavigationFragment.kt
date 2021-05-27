package com.hypnex.friengo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hypnex.friengo.R
import com.hypnex.friengo.databinding.FragmentBottomNavigationBinding

class BottomNavigationFragment : Fragment() {

    private lateinit var binding: FragmentBottomNavigationBinding

    private var bottomNavIsHide = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomNavigationBinding.inflate(inflater, container, false)

        val host =
            childFragmentManager.findFragmentById(R.id.nav_bottom_fragment_container) as NavHostFragment

        binding.mainBottomNavigation.setupWithNavController(host.navController)

        return binding.root
    }


}