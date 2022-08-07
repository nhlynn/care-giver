package com.nhlynn.care_giver.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.nhlynn.care_giver.R
import com.nhlynn.care_giver.databinding.ActivityRegisterBinding
import com.nhlynn.care_giver.view_model.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val mRegisterViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.registration)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        waitResponse()

        binding.btnSave.setOnClickListener {
            if (isValidate()) {
                val userName = binding.edtUsername.text.toString()
                val contact = binding.edtContact.text.toString()
                val password = binding.edtPassword.text.toString()

                lifecycleScope.launch {
                    binding.progressbar.visibility = View.VISIBLE
                    mRegisterViewModel.register(userName, contact, password)
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun isValidate(): Boolean {
        return when {
            binding.edtUsername.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.enter_username), Toast.LENGTH_LONG).show()
                false
            }
            binding.edtContact.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.enter_contact), Toast.LENGTH_LONG).show()
                false
            }
            binding.edtPassword.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_LONG).show()
                false
            }
            binding.edtConfirmPassword.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.enter_confrim_password), Toast.LENGTH_LONG)
                    .show()
                false
            }
            binding.edtPassword.text.toString() != binding.edtConfirmPassword.text.toString() -> {
                Toast.makeText(this, getString(R.string.password_does_not_match), Toast.LENGTH_LONG)
                    .show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun waitResponse() {
        mRegisterViewModel.myResponse.observe(this) {
            binding.progressbar.visibility = View.GONE
            if (it.status == 1) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }
}