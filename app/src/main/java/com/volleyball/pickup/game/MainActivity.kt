package com.volleyball.pickup.game

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.volleyball.pickup.game.databinding.ActivityMainBinding
import com.volleyball.pickup.game.models.FirestoreResult
import com.volleyball.pickup.game.models.Location
import com.volleyball.pickup.game.models.Operation
import com.volleyball.pickup.game.ui.login.LoginActivity
import com.volleyball.pickup.game.utils.LOCATION_REQUEST_INTERVAL
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var accessTokenTracker: AccessTokenTracker
    private lateinit var locationCallback: LocationCallback

    private val viewModel: MainViewModel by viewModels()
    private val geoCoder by lazy { Geocoder(this, Locale.TAIWAN) }
    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // region location permission
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val location = geoCoder.getFromLocation(it.latitude, it.longitude, 1)
                    viewModel.updateLocation(Location(location[0].adminArea, location[0].locality))
                }
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }

        if (hasLocationPermission()) {
            requestLocationUpdate()
            // test
        } else {
            val locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        requestLocationUpdate()
                    }
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        requestLocationUpdate()
                    }
                }
            }
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        // endregion

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragment_container
        ) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)

        accessTokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(
                oldAccessToken: AccessToken?,
                currentAccessToken: AccessToken?
            ) {
                Timber.d("oldAccessToken: ${oldAccessToken?.token}")
                Timber.d("currentAccessToken: ${currentAccessToken?.token}")
                if (currentAccessToken == null) {
                    viewModel.signOut()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
        accessTokenTracker.startTracking()

        viewModel.bottomNavVisible.observe(this) {
            binding.bottomNavView.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.firestoreResult.observe(this) {
            if (it is FirestoreResult.Success) {
                when (it.operation) {
                    Operation.ADD -> {
                        showHint(getString(R.string.add_success))
                        navController.popBackStack()
                    }
                    Operation.UPDATE -> {
                        showHint(getString(R.string.edit_success))
                        navController.popBackStack()
                    }
                    Operation.DELETE -> showHint(getString(R.string.delete_success))
                    Operation.REGISTER -> showHint(getString(R.string.register_success))
                    Operation.UNREGISTER -> showHint(getString(R.string.unregister_success))
                }
            } else if (it is FirestoreResult.Failure) {
                showHint(it.error, isError = true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        accessTokenTracker.stopTracking()
    }

    private fun showHint(message: String, isError: Boolean = false) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(getColor(if (isError) android.R.color.holo_red_light else R.color.yellow))
            .show()
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationUpdate() {
        val locationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_HIGH_ACCURACY
            interval = LOCATION_REQUEST_INTERVAL
            fastestInterval = LOCATION_REQUEST_INTERVAL
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}