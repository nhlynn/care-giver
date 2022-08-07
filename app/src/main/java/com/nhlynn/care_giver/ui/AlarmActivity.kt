package com.nhlynn.care_giver.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nhlynn.care_giver.R
import com.nhlynn.care_giver.adapter.AlarmAdapter
import com.nhlynn.care_giver.databinding.ActivityAlarmBinding
import com.nhlynn.care_giver.delegate.AlarmDelegate
import com.nhlynn.care_giver.model.AlarmVO
import com.nhlynn.care_giver.view_model.AlarmViewModel
import com.nhlynn.care_giver.view_model.CloseAlarmViewModel
import com.nhlynn.care_giver.view_model.CreateAlarmViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity(), AlarmDelegate {
    private lateinit var binding: ActivityAlarmBinding

    private val mAlarmViewModel: AlarmViewModel by viewModels()
    private val mCreateAlarmViewModel: CreateAlarmViewModel by viewModels()
    private val mCloseAlarmViewModel: CloseAlarmViewModel by viewModels()

    private lateinit var mAlarmAdapter: AlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.alarm)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAlarmAdapter = AlarmAdapter(this)
        binding.rvAlarm.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAlarm.adapter = mAlarmAdapter

        waitResponse()

        getAlarm()

        binding.btnCreateAlarm.setOnClickListener {
            val alarmTime = "${binding.timePicker.hour}:${binding.timePicker.minute}"
            lifecycleScope.launch {
                binding.progressbar.visibility = View.VISIBLE
                mCreateAlarmViewModel.createAlarm(alarmTime)
            }
        }
    }

    private fun getAlarm() {
        lifecycleScope.launch {
            binding.progressbar.visibility = View.VISIBLE
            mAlarmViewModel.getAlarm()
        }
    }

    private fun waitResponse() {
        mAlarmViewModel.myResponse.observe(this) {
            binding.progressbar.visibility = View.GONE
            if (it.status == 1) {
                if (it.alarm.isNullOrEmpty()) {
                    binding.lblEmpty.visibility = View.VISIBLE
                } else {
                    binding.lblEmpty.visibility = View.GONE
                    mAlarmAdapter.setData(it.alarm!!)
                }
            } else {
                Toast.makeText(
                    this,
                    it.message ?: getString(R.string.check_your_internet_connection),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        mCreateAlarmViewModel.myResponse.observe(this) {
            binding.progressbar.visibility = View.GONE
            if (it.status == 1) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                getAlarm()
            } else {
                Toast.makeText(
                    this,
                    it.message ?: getString(R.string.check_your_internet_connection),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        mCloseAlarmViewModel.myResponse.observe(this) {
            binding.progressbar.visibility = View.GONE
            if (it.status == 1) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                getAlarm()
            } else {
                Toast.makeText(
                    this,
                    it.message ?: getString(R.string.check_your_internet_connection),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCloseAlarm(alarm: AlarmVO) {
        if (alarm.alId != null) {
            lifecycleScope.launch {
                binding.progressbar.visibility = View.VISIBLE
                mCloseAlarmViewModel.closeAlarm(alarm.alId!!, 0)
            }
        } else {
            Toast.makeText(this, "Your alarm id is incorrect.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AlarmActivity::class.java)
        }
    }
}