package com.capstone.healme.ui.healthcare

import android.Manifest
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.healme.R
import com.capstone.healme.ViewModelFactory
import com.capstone.healme.data.remote.requestbody.HealthcareBody
import com.capstone.healme.databinding.FragmentHealthcareBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class HealthcareFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHealthcareBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private val markersMap = mutableMapOf<LatLng, Marker?>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLat: Double = 0.0
    private var userLon: Double = 0.0

    private lateinit var cardView: CardView
    private var currentState = STATE_EXPANDED

    private lateinit var healthcareViewModel: HealthcareViewModel
    private lateinit var adapter: HealthcareAdapter

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

        setupViewModel()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        cardView = binding.cardViewListHealthcare

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.adjustableCardView.setOnClickListener {
            handlePerformClick()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.apply {
            rvHealthcare.layoutManager = LinearLayoutManager(context)
            adapter = HealthcareAdapter {
                updateCameraBound(it.location!!.latitude!!, it.location.longitude!!)
            }
            rvHealthcare.adapter = adapter
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HealthcareViewModel by viewModels {
            factory
        }
        healthcareViewModel = viewModel

        healthcareViewModel.latLong.observe(viewLifecycleOwner) {
            val healthcareBody = HealthcareBody(it[0], it[1], RADIUS)
            healthcareViewModel.getNearbyHealthcare(healthcareBody)
        }

        healthcareViewModel.healthcareResponse.observe(viewLifecycleOwner) { healthcareResponse ->
            if (healthcareResponse.error == true) {
                healthcareViewModel.latLong.value.let {
                    val healthcareBody = HealthcareBody(it!![0], it[1], RADIUS)
                    healthcareViewModel.getNearbyHealthcare(healthcareBody)
                }
            } else {
                healthcareResponse.data?.let { dataItems ->
                    adapter.submitList(dataItems)
                    val boundsBuilder = LatLngBounds.Builder()

                    dataItems.forEach { data ->
                        data?.location?.let {
                            val latLng = LatLng(it.latitude!!, it.longitude!!)
                            val marker = mMap.addMarker(
                                MarkerOptions().position(latLng).title(data.displayName)
                                    .snippet(data.shortFormattedAddress)
                            )
                            markersMap[latLng] = marker
                            boundsBuilder.include(latLng)
                        }
                    }

                    val bounds = boundsBuilder.build()
                    val padding = 50
                    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                    mMap.animateCamera(cameraUpdate)
                }
            }
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

    private fun updateCameraBound(lat: Double, lon: Double) {
        val boundsBuilder = LatLngBounds.Builder()

        val userLatLng = LatLng(userLat, userLon)
        val targetLatLng = LatLng(lat, lon)

        boundsBuilder.include(userLatLng)
        boundsBuilder.include(targetLatLng)

        val bounds = boundsBuilder.build()
        val padding = 300
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.animateCamera(cameraUpdate)

        markersMap[targetLatLng]?.showInfoWindow()
    }

    private fun updateCamera(lat: Double, lon: Double) {
        val zoomLevel = 15f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), zoomLevel)
        mMap.animateCamera(cameraUpdate)
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
                        updateCamera(userLat, userLon)
                        healthcareViewModel.setLatLong(userLat, userLon)
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
        const val RADIUS = 5000
    }
}