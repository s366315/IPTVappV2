package com.iptv.livePlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.exoplayer2.*
import com.iptv.R
import com.iptv.base.BaseFragment
import com.iptv.databinding.FragmentLivePlayerBinding
import javax.inject.Inject

class LivePlayerFragment : BaseFragment<FragmentLivePlayerBinding, LivePlayerViewModel>() {

    companion object {
        const val CID_ARG = "cid"
    }

    private val cid: String by lazy { requireArguments().getString(CID_ARG) ?: "" }

    @Inject
    lateinit var player: ExoPlayer

    override val viewModelClass: Class<LivePlayerViewModel>
        get() = LivePlayerViewModel::class.java

    override var toolbarId: Int? = R.id.toolbar

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLivePlayerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        player.apply {
            binding.playerView.player = this
            setMediaItem(MediaItem.fromUri(cid))
            prepare()
            play()

            addListener(playerListener)
        }
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
        player.release()
        super.onStop()
    }
}