package com.nhlynn.care_giver.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nhlynn.care_giver.databinding.HospitalItemBinding
import com.nhlynn.care_giver.model.HospitalVO

class HospitalAdapter : RecyclerView.Adapter<HospitalAdapter.MyViewHolder>() {
    private var hospitalList = ArrayList<HospitalVO>()

    class MyViewHolder(val viewBinder: HospitalItemBinding) :
        RecyclerView.ViewHolder(viewBinder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            HospitalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val viewHolder = holder.viewBinder
        val item = hospitalList[position]

        viewHolder.tvName.text = item.hospitalName
        viewHolder.tvPhone.text = item.phone
        viewHolder.tvAddress.text = item.address
    }

    override fun getItemCount(): Int {
        return hospitalList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(hospitalList: ArrayList<HospitalVO>) {
        this.hospitalList = hospitalList
        notifyDataSetChanged()
    }
}