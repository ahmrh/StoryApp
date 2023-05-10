package com.ahmrh.storyapp.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ahmrh.storyapp.R
import com.ahmrh.storyapp.databinding.ActivityMapsBinding
import com.ahmrh.storyapp.ui.main.MainViewModel
import com.ahmrh.storyapp.util.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUtil()
        setupData()

    }

    private fun setupUtil() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mainViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(
            MainViewModel::class.java
        )
    }

    private fun setupData() {
        mainViewModel.fetchStories()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        displayMarker()
    }

    private fun displayMarker() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.mapStateFlow.collect { MapState ->
                    MapState.listStory.forEach { story ->
                        val position = LatLng(story.lat, story.lon)
                        mMap.addMarker(MarkerOptions().position(position).title(story.name))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 8f))
                    }
                }
            }
        }
    }
}