package com.nhlynn.care_giver.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nhlynn.care_giver.R
import com.nhlynn.care_giver.adapter.NewFeedAdapter
import com.nhlynn.care_giver.databinding.ActivityMainBinding
import com.nhlynn.care_giver.utils.MyConstants
import com.nhlynn.care_giver.view_model.NewFeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var mNewFeedAdapter: NewFeedAdapter
    private val mNewFeedViewModel: NewFeedViewModel by viewModels()

    private var isAdmin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isAdmin = intent.getIntExtra(MyConstants.ADMIN, 0)

        closeFABMenu()

        mNewFeedAdapter = NewFeedAdapter()
        binding.rvNewFeed.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNewFeed.adapter = mNewFeedAdapter

        waitResponse()

        getNewFeed()

        binding.fabClose.setOnClickListener {
            closeFABMenu()
        }

        binding.fabMenu.setOnClickListener {
            showFABMenu()
        }

        binding.fabCreateNewFeed.setOnClickListener {
            closeFABMenu()
            startActivity(CreateNewFeedActivity.newIntent(this))
        }

        binding.fabSearchHospital.setOnClickListener {
            closeFABMenu()
            startActivity(HospitalActivity.newIntent(this))
        }

        binding.fabRegistration.setOnClickListener {
            closeFABMenu()
            startActivity(RegisterActivity.newIntent(this))
        }

        binding.fabAlarm.setOnClickListener {
            closeFABMenu()
            startActivity(AlarmActivity.newIntent(this))
        }

        binding.srRefresh.setOnRefreshListener {
            getNewFeed()
            closeFABMenu()
            binding.srRefresh.isRefreshing=false
        }
    }

    private fun getNewFeed() {
        lifecycleScope.launch {
            binding.progressbar.visibility = View.VISIBLE
            mNewFeedViewModel.getNewFeed()
        }
    }

    private fun waitResponse() {
        mNewFeedViewModel.myResponse.observe(this) {
            binding.progressbar.visibility = View.GONE
            if (it.status == 1) {
                if (it.newFeed.isNullOrEmpty()) {
                    binding.lblEmpty.visibility = View.VISIBLE
                } else {
                    binding.lblEmpty.visibility = View.GONE
                    mNewFeedAdapter.setData(it.newFeed!!)
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

    private fun showFABMenu() {
        binding.fabMenu.hide()
        binding.fabClose.show()
        binding.fabClose.animate().rotation(135f).translationY(0F)
        binding.fabRegistration.animate()
            .translationY(-resources.getDimension(R.dimen.standard_185))
        binding.fabCreateNewFeed.animate()
            .translationY(-resources.getDimension(R.dimen.standard_145))
        binding.fabAlarm.animate().translationY(-resources.getDimension(R.dimen.standard_105))
        binding.fabSearchHospital.animate()
            .translationY(-resources.getDimension(R.dimen.standard_65))

        binding.fabSearchHospital.visibility = View.VISIBLE
        binding.fabAlarm.visibility = View.VISIBLE

        if (isAdmin == 1) {
            binding.fabCreateNewFeed.visibility = View.VISIBLE
            binding.fabRegistration.visibility = View.VISIBLE
        }
    }

    private fun closeFABMenu() {
        binding.fabClose.hide()
        binding.fabMenu.show()
        binding.fabRegistration.animate().translationY(0F)
        binding.fabCreateNewFeed.animate().translationY(0F)
        binding.fabAlarm.animate().translationY(0F)
        binding.fabSearchHospital.animate().translationY(0F)

        binding.fabSearchHospital.visibility = View.GONE
        binding.fabAlarm.visibility = View.GONE
        binding.fabCreateNewFeed.visibility = View.GONE
        binding.fabRegistration.visibility = View.GONE
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}