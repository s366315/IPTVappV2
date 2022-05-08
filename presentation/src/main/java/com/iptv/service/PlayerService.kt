package com.iptv.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.iptv.MainActivity
import com.iptv.R
import com.iptv.data.preferences.Preferences
import com.iptv.domain.entities.Channel
import com.iptv.domain.entities.Result
import com.iptv.domain.interactor.channelUrl.ChannelUrlUseCase
import com.iptv.domain.interactor.signin.LoginUseCase
import dagger.android.AndroidInjection
import kotlinx.coroutines.*
import java.util.ArrayList
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PlayerService : Service(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var channelUrlUseCase: ChannelUrlUseCase

    @Inject
    lateinit var preferences: Preferences

    private var isPlaying = false

    private var currentChannel: Channel? = null
    private var channelsList: ArrayList<Channel> = arrayListOf()

    private val notificationManager: NotificationManager
        get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val prevAction: NotificationCompat.Action
        get() = NotificationCompat.Action.Builder(
            android.R.drawable.ic_media_previous, "Pause", prevIntent
        ).build()

    val playAction: NotificationCompat.Action
        get() = NotificationCompat.Action.Builder(
            if (!isPlaying) android.R.drawable.ic_media_play else android.R.drawable.ic_media_pause,
            "Pause",
            playIntent
        ).build()

    val nextAction: NotificationCompat.Action
        get() = NotificationCompat.Action.Builder(
            android.R.drawable.ic_media_next, "Pause", nextIntent
        ).build()

    val mediaSession: MediaSessionCompat
        get() = MediaSessionCompat(this, "PlayerService").apply {
            setMetadata(
                MediaMetadataCompat.Builder()
//                    .putString(MediaMetadata.METADATA_KEY_TITLE, currentChannel?.name)
//                    .putString(MediaMetadata.METADATA_KEY_ARTIST, currentChannel?.epgProgname)
                    .build()
            )
            setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setState(
                        PlaybackStateCompat.STATE_PLAYING,
                        5,
                        1f
                    )
                    .setState(
                        PlaybackStateCompat.STATE_STOPPED,
                        0,
                        1f
                    )
                    .build()
            )
        }

    private val notificationBuilder: NotificationCompat.Builder
        get() = NotificationCompat.Builder(this, CHANNEL_ID)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(
                    mediaSession.sessionToken
                )
            )
            .setContentIntent(contentIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .addAction(prevAction)
            .addAction(playAction)
            .addAction(nextAction)

    companion object {
        private const val NOTIFICATION_ID = 117
        private const val CHANNEL_ID = "500"
        private const val CHANNEL_NAME = "channel"
        private const val CHANNEL_DESCRIPTION = "channel"

        private const val ACTION_KEY = "action"
        const val CURRENT_CHANNEL_KEY = "current_track"
        const val CHANNELS_LIST_KEY = "track_list"
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()

        player.apply {
            addListener(playerListener)
        }

        startForeground(NOTIFICATION_ID, getNotification())
    }

    fun getNotification(): Notification {
        notificationManager.createNotificationChannel(createChannel)

        return notificationBuilder.build()
    }

    fun updateNotification(notificationText: String? = null) {
        notificationText?.let { notificationBuilder.setContentText(it) }
        notificationBuilder.setContentTitle(currentChannel?.name)
        notificationBuilder.setContentText(currentChannel?.epgProgname)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private val createChannel: NotificationChannel
        get() = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
            setSound(null, null)
        }

    private val contentIntent: PendingIntent
        get() {
            return PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

    private val prevIntent: PendingIntent
        get() {
            return PendingIntent.getService(
                this,
                0,
                Intent(this, PlayerService::class.java).apply {
                    putExtra(ACTION_KEY, PlayType.PREV)
                },
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

    private val playIntent: PendingIntent
        get() {
            return PendingIntent.getService(
                this,
                0,
                Intent(this, PlayerService::class.java).apply {
                    putExtra(ACTION_KEY, PlayType.PLAY)
                },
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

    private val nextIntent: PendingIntent
        get() {
            return PendingIntent.getService(
                this,
                0,
                Intent(this, PlayerService::class.java).apply {
                    putExtra(ACTION_KEY, PlayType.NEXT)
                },
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    enum class PlayType {
        PREV, PLAY, NEXT
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            isPlaying = player.isPlaying
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    println("onPlaybackStateChanged STATE_BUFFERING")
                    updateNotification("buffering")
                }
                else -> {
                    println("onPlaybackStateChanged")
                    updateNotification()
                }
            }
        }
    }

    private fun getChannelUrl() {
        val targetChannelIndex = currentChannel?.let {
            channelsList.indexOf(currentChannel)
        }?.plus(1) ?: 0
        val targetChannel = channelsList[if (targetChannelIndex < channelsList.size.minus(1)) targetChannelIndex else 0]

        targetChannel.let {
            currentChannel = it
            launch {
                val result =
                    channelUrlUseCase.createObservable(ChannelUrlUseCase.Params(channelId = it.id))

                when (result) {
                    is Result.Success -> {
                        player.apply {
                            stop()
                            setMediaItem(MediaItem.fromUri(result.data.url))
                            prepare()
                            play()
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println(intent?.getSerializableExtra(ACTION_KEY))
        (intent?.getSerializableExtra(CURRENT_CHANNEL_KEY) as Channel?)?.let {
            currentChannel = it
        }

        (intent?.getSerializableExtra(CHANNELS_LIST_KEY) as ArrayList<Channel>?)?.let {
            channelsList = it
        }

        when (intent?.getSerializableExtra("action")) {
            PlayType.PREV -> {
                getChannelUrl()
            }
            PlayType.PLAY -> {
                if (player.isPlaying) {
                    player.stop()
                } else {
                    player.play()
                }
            }
            PlayType.NEXT -> {
                getChannelUrl()
            }
        }
        return START_NOT_STICKY
    }
}