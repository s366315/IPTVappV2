package com.iptv.live

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.iptv.base.BaseFragment
import com.iptv.R
import com.iptv.databinding.FragmentLiveBinding
import com.iptv.databinding.LayoutPlayerControllerBinding
import com.iptv.domain.entities.Channel
import com.iptv.live.adapters.LiveChannelsAdapter
import com.iptv.service.PlayerService
import com.iptv.utils.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class LiveFragment : BaseFragment<FragmentLiveBinding, LiveFragmentViewModel>() {

    @Inject
    lateinit var liveAdapter: LiveChannelsAdapter

    @Inject
    lateinit var player: ExoPlayer

    private var currentChannel: Channel? = null

    private val bottomSheetBehavior: BottomSheetBehavior<RecyclerView> by lazy {
        BottomSheetBehavior.from(binding.recyclerView).apply {
            peekHeight = 450
            isHideable = true
        }
    }

    override val viewModelClass: Class<LiveFragmentViewModel>
        get() = LiveFragmentViewModel::class.java

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLiveBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.channelListState bind channelsDataObserver
        viewModel.channelUrlState bind channelUrlDataObserver
        viewModel.onRootClickState bind hideBottomSheetObserver
        viewModel.onBtnShowChannelsClickState bind channelsBtnObserver
        viewModel.onBtnSettingsClickState bind settingsBtnObserver
        viewModel.errorData bind errorConsumer
        viewModel.bottomSheetState bind hideBottomSheetStateObserver
        viewModel.channelState bind channelPlayedObserver

        binding.btnSettings.clicks() bind viewModel.onBtnSettingsClick

        initSheetCallbacks()

        initSwipeListener()

        initPlayer()

        binding.recyclerView.adapter = liveAdapter.apply {
            clickListener = viewModel.onChannelClick
        }

        val b = binding.playerView.rootView.findViewById<ImageButton>(R.id.exo_play_pause)
        println()
    }

    private fun initPlayer() {
        player.apply {
            binding.playerView.player = this
//            addListener(playerListener)
            playWhenReady = true
            currentMediaItem?.let {
                play()
            }
        }
    }

    private fun initSheetCallbacks() {
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                viewModel.onSheetState(newState)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                materialShapeDrawable.interpolation = 1 - slideOffset
            }
        })

        ViewCompat.setBackground(binding.recyclerView, materialShapeDrawable)
    }

    private fun initSwipeListener() {
        binding.playerView.setOnTouchListener(swipeListener)
        binding.root.setOnTouchListener(swipeListener)
    }

    private val swipeListener: OnSwipeTouchListener by lazy {
        object : OnSwipeTouchListener(requireContext()) {
            override fun onSwipeBottom() {
                hideBottomSheetObserver(Unit)
            }

            override fun onSwipeTop() {
                channelsBtnObserver(Unit)
            }

            override fun onTap() {
                binding.playerView.performClick()
            }
        }
    }

    private val shapeAppearanceModel = ShapeAppearanceModel()
        .toBuilder()
        .setTopLeftCornerSize(10f.dp)
        .setTopRightCornerSize(10f.dp)
        .build()
    val materialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
        fillColor = ColorStateList.valueOf(Color.WHITE)
    }

    private val hideBottomSheetStateObserver: (Int) -> Unit = {
        if (bottomSheetBehavior.state != it) {
            bottomSheetBehavior.state = it
        }

        when (it) {
            BottomSheetBehavior.STATE_HIDDEN -> {
                binding.btnSettings.isVisible = false
                requireActivity().window.hideSystemUI(binding.root)
            }
            else -> {
                binding.btnSettings.isVisible = true
                requireActivity().window.showSystemUI(binding.root)
            }
        }
    }

    private val hideBottomSheetObserver: (Unit) -> Unit = {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private val channelsBtnObserver: (Unit) -> Unit = {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private val settingsBtnObserver: (Unit) -> Unit = {
        navigateTo(R.id.action_fragmentLife_to_fragmentSettings)
    }

    private val channelUrlDataObserver: (String) -> Unit = {
        player.apply {
            binding.playerView.player = this
            setMediaItem(MediaItem.fromUri(it))
            prepare()
            play()
            hideBottomSheetObserver.invoke(Unit)
        }
    }

    private val channelsDataObserver: (List<Channel>) -> Unit = { channels ->
        liveAdapter.submitList(channels)

        currentChannel?.let { channel ->
            currentChannel = channels.first { channel.id == it.id }
        }
    }

    private val channelPlayedObserver: (Channel) -> Unit = { channel ->
        currentChannel = channel

        val end = channel.epgEnd.timestampToEpg()
        binding.playerView.findViewById<TextView>(R.id.player_duration).text = end

        val start = channel.epgStart.timestampToEpg()
        binding.playerView.findViewById<TextView>(R.id.player_position).text = start

        binding.playerView.findViewById<LinearProgressIndicator>(R.id.player_progress).apply {

            updateProgress(this, channel)
        }
    }

    private fun updateProgress(progressBar: LinearProgressIndicator, channel: Channel) {
        progressBar.postDelayed({



            progressBar.progress = if (channel.isVideo) {
                ((System.currentTimeMillis() / 1000 - channel.epgStart) * 100 / (channel.epgEnd - channel.epgStart)).toInt()
            } else {
                0
            }
            updateProgress(progressBar, channel)
        }, 1000)
    }

    private val errorConsumer: (String) -> Unit = {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    println("onPlaybackStateChanged STATE_BUFFERING")
                    binding.progress.isVisible = true
                }
                else -> {
                    println("onPlaybackStateChanged")
                    binding.progress.isVisible = false
                }
            }
        }
    }

    override fun onStop() {

        player.removeListener(playerListener)
        if (currentChannel?.isVideo == false && player.isPlaying) {
            val channels: ArrayList<Channel> = arrayListOf()
//            viewModel.channelListState.value.filter { !it.isVideo }.map { channels.add(it) }
            ContextCompat.startForegroundService(
                requireContext(),
                Intent(requireContext(), PlayerService::class.java).apply {
                    putExtra(PlayerService.CURRENT_CHANNEL_KEY, currentChannel)
                    putExtra(PlayerService.CHANNELS_LIST_KEY, channels)
                })
        } else {
            player.pause()
        }
        super.onStop()
    }
}