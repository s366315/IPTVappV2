package com.iptv.live.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.iptv.BuildConfig
import com.iptv.R
import com.iptv.databinding.ItemChannelBinding
import com.iptv.domain.entities.Channel
import com.iptv.utils.timestampToEpg
import javax.inject.Inject


class LiveChannelsAdapter @Inject constructor() :
    ListAdapter<Channel, LiveChannelsAdapter.Holder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Channel>() {
            override fun areItemsTheSame(
                oldItem: Channel, newItem: Channel
            ): Boolean {
                return true
            }

            override fun areContentsTheSame(
                oldItem: Channel, newItem: Channel
            ): Boolean {
                return oldItem.epgProgname == newItem.epgProgname
            }
        }
    }

    var clickListener: ((Channel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemChannelBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class Holder(private val binding: ItemChannelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(channel: Channel) = with(binding) {
            root.setOnClickListener { clickListener?.invoke(channel) }
            val start = channel.epgStart.timestampToEpg()
            val end = channel.epgEnd.timestampToEpg()

            labelChannelName.text = channel.name
            labelProgramName.text = channel.epgProgname
            labelChannelTime.isVisible = channel.isVideo
            labelChannelTime.text = "$start - $end"
            Glide.with(root)
                .load(BuildConfig.BASE_URL + channel.icon.substring(1, channel.icon.length))
                .placeholder(
                    R.drawable.ic_audiotrack_dark
                ).into(imageLogo)
            updateProgress(progress, channel)
        }

        private fun updateProgress(progressBar: ProgressBar, channel: Channel) {
            progressBar.postDelayed({
                progressBar.progress = if (channel.isVideo) {
                    ((System.currentTimeMillis() / 1000 - channel.epgStart) * 100 / (channel.epgEnd - channel.epgStart)).toInt()
                } else {
                    0
                }
                updateProgress(progressBar, channel)
            }, 1000)
        }
    }
}