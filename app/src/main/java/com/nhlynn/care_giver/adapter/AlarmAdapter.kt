package com.nhlynn.care_giver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nhlynn.care_giver.databinding.AlarmItemBinding
import com.nhlynn.care_giver.delegate.AlarmDelegate
import com.nhlynn.care_giver.model.AlarmVO
import com.nhlynn.care_giver.utils.MyUtils

class AlarmAdapter(private val mDelegate: AlarmDelegate) :
    RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    private var mAlarmList = ArrayList<AlarmVO>()

    class ViewHolder(val viewBinder: AlarmItemBinding) : RecyclerView.ViewHolder(viewBinder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AlarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewHolder = holder.viewBinder
        val item = mAlarmList[position]

        viewHolder.tvTime.text = MyUtils.convertTime(item.alarmTime)

        viewHolder.switchAlarm.isChecked = true

        viewHolder.switchAlarm.setOnCheckedChangeListener { _, b ->
            if (!b) {
                mDelegate.onCloseAlarm(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return mAlarmList.size
    }

    fun setData(alarmList: ArrayList<AlarmVO>) {
        this.mAlarmList = alarmList
        notifyDataSetChanged()
    }
}