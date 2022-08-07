package com.nhlynn.care_giver.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nhlynn.care_giver.R
import com.nhlynn.care_giver.databinding.NewFeedItemBinding
import com.nhlynn.care_giver.model.NewFeedVO

class NewFeedAdapter : RecyclerView.Adapter<NewFeedAdapter.ViewHolder>() {
    private var mNewFeedList = ArrayList<NewFeedVO>()

    class ViewHolder(val viewBinder: NewFeedItemBinding) : RecyclerView.ViewHolder(viewBinder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NewFeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewHolder = holder.viewBinder
        val item = mNewFeedList[position]

        Glide.with(viewHolder.ivPhoto)
            .load(item.photo)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(viewHolder.ivPhoto)

        viewHolder.tvInstruction.text = item.instruction
        viewHolder.tvCaution.text = item.caution
    }

    override fun getItemCount(): Int {
        return mNewFeedList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newFeedList: ArrayList<NewFeedVO>) {
        this.mNewFeedList = newFeedList
        notifyDataSetChanged()
    }
}