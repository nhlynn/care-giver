package com.nhlynn.care_giver.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.nhlynn.care_giver.R
import com.nhlynn.care_giver.databinding.ActivityLoginBinding
import com.nhlynn.care_giver.utils.MyConstants
import com.nhlynn.care_giver.view_model.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val mLoginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.login)

        waitResponse()

        binding.btnGuest.setOnClickListener {
            startActivity(MainActivity.newIntent(this))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            if (isValidate()) {
                val userName = binding.edtUsername.text.toString()
                val password = binding.edtPassword.text.toString()

                lifecycleScope.launch {
                    binding.progressbar.visibility = View.VISIBLE
                    mLoginViewModel.login(userName, password)
                }
            }
        }
    }

    private fun waitResponse() {
        mLoginViewModel.myResponse.observe(this) {
            binding.progressbar.visibility = View.GONE
            if (it.status == 1) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                startActivity(MainActivity.newIntent(this).putExtra(MyConstants.ADMIN, 1))
                finish()
            } else {
                Toast.makeText(
                    this,
                    it.message ?: getString(R.string.check_your_internet_connection),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun isValidate(): Boolean {
        return when {
            binding.edtUsername.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.enter_username), Toast.LENGTH_LONG).show()
                false
            }
            binding.edtPassword.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_LONG).show()
                false
            }
            else -> {
                true
            }
        }
    }
}