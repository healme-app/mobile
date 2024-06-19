package com.capstone.healme.ui.healthcare

import android.Manifest
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.capstone.healme.R
import com.capstone.healme.databinding.FragmentHealthcareBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class HealthcareFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHealthcareBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLat: Double = 0.0
    private var userLon: Double = 0.0

    private lateinit var cardView: CardView
    private var currentState = STATE_EXPANDED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthcareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        cardView = binding.cardViewListHealthcare

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.adjustableCardView.setOnClickListener {
            handlePerformClick()
        }
    }

    private fun handlePerformClick() {
        when (currentState) {
            STATE_COLLAPSED -> expandCardView()
            STATE_EXPANDED -> collapseCardView()
        }
    }

    private fun expandCardView() {
        animateCardViewHeight(cardView.height, 300.dpToPx())
        currentState = STATE_EXPANDED
    }

    private fun collapseCardView() {
        animateCardViewHeight(cardView.height, 52.dpToPx())
        currentState = STATE_COLLAPSED
    }


    private fun animateCardViewHeight(startHeight: Int, endHeight: Int) {
        val animator = ValueAnimator.ofInt(startHeight, endHeight)
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val layoutParams = cardView.layoutParams
            layoutParams.height = value
            cardView.layoutParams = layoutParams
        }
        animator.duration = 300
        animator.start()
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled = true
        getLocation()

        mMap.uiSettings.isCompassEnabled = true
    }

    private fun updateCamera() {
        val zoomLevel = 15f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(userLat, userLon), zoomLevel)
        mMap.moveCamera(cameraUpdate)
    }

    private fun getLocation() {
        val context = requireContext()
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        userLat = location.latitude
                        userLon = location.longitude
                        updateCamera()
                    }
                }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val STATE_COLLAPSED = 0
        const val STATE_EXPANDED = 1
    }
}