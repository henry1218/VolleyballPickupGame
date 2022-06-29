package com.volleyball.pickup.game.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.snackbar.Snackbar
import com.volleyball.pickup.game.MainActivity
import com.volleyball.pickup.game.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.apply {
            toolTipMode = LoginButton.ToolTipMode.NEVER_DISPLAY
            setLogoutText("登入中...")
            setPermissions(listOf("public_profile"))
            registerCallback(
                CallbackManager.Factory.create(),
                object : FacebookCallback<LoginResult> {
                    override fun onCancel() {
                        val snack = Snackbar.make(this@apply, "登入取消", Snackbar.LENGTH_LONG)
                        snack.show()
                    }

                    override fun onError(error: FacebookException) {
                        val snack = Snackbar.make(
                            this@apply,
                            error.message.toString()
                                .substringAfter("[message]: ")
                                .substringBefore("[extra]"),
                            Snackbar.LENGTH_LONG
                        )
                        Timber.e("facebook login error(${error.message})")
                        snack.show()
                    }

                    override fun onSuccess(result: LoginResult) {
                        binding.btnLogin.isEnabled = false
                        viewModel.signInWithFacebook(result.accessToken)
                    }
                })
        }

        viewModel.isUserAuthenticated.observe(this) { isAuthenticated ->
            if (isAuthenticated) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                val snack = Snackbar.make(binding.root, "登入失敗，請再試一次", Snackbar.LENGTH_LONG)
                snack.show()
            }
        }
    }
}