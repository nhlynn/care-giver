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
import com.nhlynn.care_giver.databinding.ActivityCreateNewFeedBinding
import com.nhlynn.care_giver.view_model.CreateNewFeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateNewFeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewFeedBinding

    private val mCreateNewFeedViewModel: CreateNewFeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.create_new_feed)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        waitResponse()

        binding.btnSave.setOnClickListener {
            if (isValidate()) {
                val name = binding.edtName.text.toString()
                val instruction = binding.edtInstruction.text.toString()
                val caution = binding.edtCaution.text.toString()
                val photoPath = binding.edtPhotoPath.text.toString()

                lifecycleScope.launch {
                    binding.progressbar.visibility = View.VISIBLE
                    mCreateNewFeedViewModel.createNewFeed(name, instruction, caution, photoPath)
                }
            }
        }
    }

    private fun waitResponse() {
        mCreateNewFeedViewModel.myResponse.observe(this) {
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

    private fun isValidate(): Boolean {
        return when {
            binding.edtName.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.enter_name), Toast.LENGTH_LONG).show()
                false
            }
            binding.edtInstruction.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.enter_instruction), Toast.LENGTH_LONG)
                    .show()
                false
            }
            binding.edtCaution.text.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.enter_caution), Toast.LENGTH_LONG).show()
                false
            }
            else -> {
                true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CreateNewFeedActivity::class.java)
        }
    }
}