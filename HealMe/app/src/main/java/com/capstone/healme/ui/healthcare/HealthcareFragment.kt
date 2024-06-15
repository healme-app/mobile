package com.capstone.healme.ui.healthcare

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
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
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient

class HealthcareFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHealthcareBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
//    private lateinit var placesClient: PlacesClient

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLat: Double = 0.0
    private var userLon: Double = 0.0

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
//                getNearbyPlaces()
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled = true
//        getNearbyPlaces()
        getLocation()

        mMap.uiSettings.isCompassEnabled = true

//        mMap.uiSettings.isZoomControlsEnabled = true
//        mMap.uiSettings.isIndoorLevelPickerEnabled = true
//        mMap.uiSettings.isCompassEnabled = true
//        mMap.uiSettings.isMapToolbarEnabled = true
    }

//    private fun getNearbyPlaces() {
//        Log.d("Nearby places", "initialized")
//        Places.initialize(requireContext(), key)
//        placesClient = Places.createClient(requireContext())
//        val placeFields = listOf(
//            Place.Field.ID,
//            Place.Field.NAME,
//            Place.Field.ADDRESS,
//            Place.Field.LAT_LNG,
//            Place.Field.TYPES
//        )
//        val placeType = Place.Type.HEALTH
//
//        val request = FindCurrentPlaceRequest.builder(placeFields)
//            .build()
//
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//            return
//        }
//        placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
//            Log.d("Success fetch nearby", "INIT")
//            for (placeLikelihood in response.placeLikelihoods) {
//                val place = placeLikelihood.place
//                Log.d("success fetch nearby","Healthcare Place: ${place.name}, ${place.address}, ${place.latLng}, ${place.types}")
//            }
//        }.addOnFailureListener { response ->
//            Log.d("error fetch nearby", response.message.toString())
//        }
//    }

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
}