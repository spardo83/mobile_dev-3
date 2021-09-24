package ar.edu.unlam.location.ui

import android.Manifest
import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import ar.edu.unlam.location.R
import ar.edu.unlam.location.databinding.ActivityMapsBinding
import ar.edu.unlam.location.ui.viewmodels.MapViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.model.EncodedPolyline
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.maps.CameraUpdateFactory

import android.R.attr.path
import android.graphics.Color
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.model.PolylineOptions


@SuppressLint("MissingPermission")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var currentLocation: LatLng
    private val vm: MapViewModel by viewModel()
    private val starbucks = LatLng(-34.644593, -58.565088)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.addMarker(MarkerOptions().position(starbucks).title("Starbucks"))
        mMap.isMyLocationEnabled = arePermissionsGranted()
        configureMap(mMap.uiSettings)
        retrieveAndMarkInitialLocation()
        setUpBindings()
        setUpObservers()
    }

    private fun setUpObservers() {


        vm.currentDirections.observe(this, { directions ->

            directions?.run {
                val path = mutableListOf<LatLng>()
                directions.routes.forEach { route ->
                    route.legs.forEach { leg ->
                        leg.steps.forEach { step ->
                            val encodedPoly = EncodedPolyline(step.polyline.points)
                            encodedPoly.decodePath().forEach { latLng ->
                                latLng?.run {
                                    path.add(LatLng(this.lat, this.lng))
                                }
                            }
                        }
                    }
                }

                if (path.size > 0) {
                    val opts = PolylineOptions().addAll(path)
                        .color(Color.BLUE)
                        .width(15f)
                    mMap.addPolyline(opts)
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 6f))
            }

        })

    }

    private fun setUpBindings() {
        binding.navigate.setOnClickListener { onNavigateClick() }
    }

    private fun configureMap(settings: UiSettings) {
        settings.isZoomControlsEnabled = true
        settings.isZoomGesturesEnabled = true
        settings.isCompassEnabled = true
    }

    private fun retrieveAndMarkInitialLocation() {
        if (arePermissionsGranted()) {

            val locationManager = LocationServices.getFusedLocationProviderClient(this)
            val locationRequest = LocationRequest.create().apply {
                interval = 10_000
                fastestInterval = 5_000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            }

            locationManager.requestLocationUpdates(
                locationRequest, object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        val location = result.lastLocation
                        currentLocation = LatLng(location.latitude, location.longitude)
                        mMap.addMarker(
                            MarkerOptions().position(currentLocation)
                                .title("Marker current location")
                        )

                        mMap.moveCamera(CameraUpdateFactory.zoomTo(18f))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
                    }

                }, Looper.getMainLooper()
            )
        }

    }

    fun onNavigateClick() {
        if (this::currentLocation.isInitialized) {
            vm.retrieveDirections(currentLocation, starbucks)
        }

    }

    private fun arePermissionsGranted() =

        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


}