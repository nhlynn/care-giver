package com.nhlynn.care_giver.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.nhlynn.care_giver.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.nhlynn.care_giver.adapter.HospitalAdapter
import com.nhlynn.care_giver.databinding.ActivityHospitalBinding
import com.nhlynn.care_giver.utils.MyConstants
import com.nhlynn.care_giver.view_model.NearestHospitalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.ClassCastException

@AndroidEntryPoint
class HospitalActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityHospitalBinding

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isRequesting = false
    private var latitude: Double? = null
    private var longitude: Double? = null

    private val mHospitalViewModel: NearestHospitalViewModel by viewModels()

    private lateinit var mHospitalAdapter: HospitalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.hospital)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)

        waitResponse()

        mHospitalAdapter = HospitalAdapter()
        binding.rvHospital.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHospital.adapter = mHospitalAdapter
    }

    private fun waitResponse() {
        mHospitalViewModel.myResponse.observe(this) {
            binding.progressbar.visibility = View.GONE
            if (it.status == 1) {
                if (it.nearestHospitals.isNullOrEmpty()) {
                    binding.lblEmpty.visibility = View.VISIBLE
                } else {
                    binding.lblEmpty.visibility = View.GONE
                    mHospitalAdapter.setData(it.nearestHospitals!!)
                }
            } else {
                Toast.makeText(
                    this,
                    it.message ?: getString(R.string.check_your_internet_connection),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun locationCheck() {
        val manager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            turnGPSOn()
        } else {
            if (hasPermissions(this, MyConstants.LOCATION_PERMISSIONS)) {
                getCurrentLocation()
            } else {
                locationReqLauncher.launch(
                    MyConstants.LOCATION_PERMISSIONS
                )
            }
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    private val locationReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Location Permission is denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun turnGPSOn() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                Log.d("LogData", response.toString())
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                this,
                                Priority.PRIORITY_HIGH_ACCURACY
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (hasPermissions(this, MyConstants.LOCATION_PERMISSIONS)) {
            startLocationUpdates()
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location == null) {
                        startLocationUpdates()
                    } else {
                        if (location.accuracy < 30) {
                            isRequesting = true
                            stopLocationUpdates()

                            showLocation(
                                location.latitude.toString(),
                                location.longitude.toString()
                            )
                        } else {
                            startLocationUpdates()
                        }
                    }
                }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        @SuppressLint("SetTextI18n")
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            if (location != null) {
                if (location.accuracy < 30) {
                    isRequesting = true
                    stopLocationUpdates()
                    showLocation(location.latitude.toString(), location.longitude.toString())
                }
            }
        }
    }

    private fun stopLocationUpdates() {
        if (!isRequesting) {
            return
        }
        fusedLocationClient.removeLocationUpdates(locationCallback).addOnCompleteListener {
            isRequesting = false
        }
    }

    private fun showLocation(lat: String, lng: String) {
        this.latitude = lat.toDouble()
        this.longitude = lng.toDouble()
        showMap()

        lifecycleScope.launch {
            if (latitude == null && longitude == null) {
                Toast.makeText(
                    this@HospitalActivity,
                    "Your location is not available.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                binding.progressbar.visibility = View.VISIBLE
                mHospitalViewModel.getNearestHospital(latitude!!, longitude!!)
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        showMap()

        locationCheck()
    }

    private fun showMap() {
        if (latitude != null && longitude != null) {
            mMap.clear()
            val myLocation = LatLng(latitude!!, longitude!!)

            mMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18f))
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, HospitalActivity::class.java)
        }
    }
}