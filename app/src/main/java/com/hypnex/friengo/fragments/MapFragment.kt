package com.hypnex.friengo.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hypnex.friengo.MainApplication
import com.hypnex.friengo.data.models.Event
import com.hypnex.friengo.databinding.FragmentMapBinding
import com.hypnex.friengo.utils.BaseFragment
import com.hypnex.friengo.utils.PermissionUtils
import com.hypnex.friengo.viewmodels.MapViewModel

class MapFragment : BaseFragment(), OnMapReadyCallback, OnRequestPermissionsResultCallback,
    OnMapClickListener,
    OnMarkerClickListener {

    private lateinit var binding: FragmentMapBinding

    private lateinit var map: GoogleMap

    private lateinit var viewModel: MapViewModel

    private val user = MainApplication.preferences.getUser()

    private var chooseLocationMarker: Marker? = null

    private val markers = mutableListOf<Pair<Marker, Event>>()

    private var permissionDenied = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        binding = FragmentMapBinding.inflate(inflater, container, false)

        binding.googleMap.onCreate(savedInstanceState)
        binding.googleMap.getMapAsync(this)

        observeMainView(viewModel)

        viewModel.friendEmails.observe(viewLifecycleOwner, {
            val myFriendList = it as MutableList<String>
            myFriendList.add(user.email!!)

            for (email in myFriendList) {
                viewModel.getUserEvent(email) { events ->
                    for (event in events) {
                        markers.add(
                            Pair(
                                addMarkerEvent(
                                    LatLng(event.latitude!!, event.longitude!!)
                                )!!, event
                            )
                        )
                    }
                }
            }
        })

        initButtons()

        return binding.root
    }

    private fun initButtons() {
        binding.addEventFloatingButton.hide()
        binding.addEventFloatingButton.setOnClickListener {
            if (chooseLocationMarker != null) {
                navigateToEventAdd(chooseLocationMarker!!.position)
            }
        }
    }

    private fun addMarkerEvent(coordinates: LatLng): Marker? {
        return map.addMarker(
            MarkerOptions()
                .position(coordinates)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        )
    }


    override fun onResume() {
        binding.googleMap.onResume()
        if (permissionDenied) {
            showMissingPermissionError()
            permissionDenied = false
        }
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.googleMap.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.googleMap.onStop()
    }

    override fun onPause() {
        binding.googleMap.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.googleMap.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.googleMap.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap ?: return
        enableMyLocation()
        map.setOnMapClickListener(this)
        map.setOnMarkerClickListener(this)

        viewModel.getDefaultGroupEmails(user.email!!)
    }

    private fun setChooseLocationMarker(coordinates: LatLng) {
        chooseLocationMarker = map.addMarker(
            MarkerOptions()
                .position(coordinates)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
        )
    }

    private fun enableMyLocation() {
        if (!::map.isInitialized) return

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            PermissionUtils.requestPermission(
                requireActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) return

        if (PermissionUtils.isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            enableMyLocation()
        } else {
            permissionDenied = true
        }
    }

    private fun showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true)
            .show(childFragmentManager, "dialog")
    }

    private fun navigateToEventAdd(coordinates: LatLng) {
        val destination = MapFragmentDirections.actionMapFragmentToEventAddFragment(coordinates)

        findNavController().navigate(destination)
    }

    private fun navigateToShow(event: Event) {
        val destination = MapFragmentDirections.actionMapFragmentToEventShowFragment(event)

        findNavController().navigate(destination)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onMapClick(coordinates: LatLng) {
        binding.addEventFloatingButton.show()
        if (chooseLocationMarker == null) {
            setChooseLocationMarker(coordinates)
            return
        }
        chooseLocationMarker?.position = coordinates
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        for (m in markers) {
            if (marker == m.first) {
                navigateToShow(m.second)
                return true
            }
        }
        return false
    }

}