package com.volleyball.pickup.game

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.google.android.material.snackbar.Snackbar
import com.volleyball.pickup.game.databinding.ActivityMainBinding
import com.volleyball.pickup.game.models.FirestoreResult
import com.volleyball.pickup.game.models.Operation
import com.volleyball.pickup.game.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var accessTokenTracker: AccessTokenTracker
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}