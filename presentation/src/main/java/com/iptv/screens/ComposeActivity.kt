@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)

package com.iptv.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SwipeableDefaults.AnimationSpec
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.DefaultAnalyticsCollector
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.util.Clock
import javax.inject.Inject
import com.iptv.R
import com.iptv.data.preferences.Preferences
import kotlinx.coroutines.launch


class ComposeActivity : ComponentActivity() {
    @Inject
    lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(darkColorScheme()) {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "tv") {
        composable("tv") { TvScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TvScreen(navController: NavHostController) {
    val context = LocalContext.current
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri("https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"))
            this.prepare()
        }
    }
    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    val squareSize = 150.dp
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val swipeableAnchors = mapOf(0f to 0, sizePx to 1)
    val swipeableState = rememberSwipeableState(initialValue = 0)

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val showState = remember { mutableStateOf(false) }
    val sheetPeekHeight by animateDpAsState(
        targetValue = if (showState.value) 150.dp else 0.dp
    )

    val cornerRadius = remember { mutableStateOf(20f) }

    val scope = rememberCoroutineScope()

    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) {
        showDialog(openDialog)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("settings") {
                                popUpTo("tv")
                            }
//                            openDialog.value = true
                        },
                        content = {
                            Icon(
                                painterResource(id = R.drawable.icon_settings_thin),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    )
                }
            )
        },
        content = {
            Box {
                Column(
                    modifier = Modifier.padding(16.dp),
                    content = {
                        StyledPlayerView(LocalContext.current).apply {
                            this.useController = true
                            this.player = exoPlayer
                            exoPlayer.playWhenReady = true
                            exoPlayer.setMediaItem(MediaItem.fromUri("https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"))
                            exoPlayer.prepare()
                            exoPlayer.play()
                        }
                    }
                )
                /*DisposableEffect(
                    AndroidView(
                        modifier =
                        Modifier.testTag("VideoPlayer")
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        factory = {

                            // exo player view for our video player
                            PlayerView(context).apply {
                                player = exoPlayer
                                layoutParams =
                                    FrameLayout.LayoutParams(
                                        ViewGroup.LayoutParams
                                            .MATCH_PARENT,
                                        ViewGroup.LayoutParams
                                            .MATCH_PARENT
                                    )
                            }
                        }
                    )
                ) {
                    onDispose {
                        // relase player when no longer needed
                        exoPlayer.release()
                    }
                }*/
                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetShape = RoundedCornerShape(
                        topStart = cornerRadius.value.dp,
                        topEnd = cornerRadius.value.dp
                    ),

                    sheetPeekHeight = sheetPeekHeight,
                    modifier = Modifier
                        .background(Color.Blue)
                    /*.swipeable(
                        state = swipeableState,
                        anchors = swipeableAnchors,
                        orientation = Orientation.Vertical
                    )*/,
                    sheetContent = {
                        Column(
                            modifier = Modifier.swipeable(
                                state = swipeableState,
                                anchors = swipeableAnchors,
                                orientation = Orientation.Vertical
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(color = Color.Gray)
                                    .height(10.dp)
                            ) {

                            }
                            LazyColumn {
                                item {

                                }
                                items(50) {
                                    ListItem(
                                        text = { Text("Item $it") },
                                        icon = {
                                            Icon(
                                                Icons.Default.Favorite,
                                                contentDescription = "Localized description"
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black)
                            .padding(top = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val fraction = scaffoldState.bottomSheetState.progress.fraction
                        val from = scaffoldState.bottomSheetState.progress.from
                        val to = scaffoldState.bottomSheetState.progress.to
                        cornerRadius.value = cornerRadius(scaffoldState.bottomSheetState)
                        Text("f = $fraction; from = $from; to = $to")
                        Text("offset: ${swipeableState.offset.value}")
                        Spacer(Modifier.height(20.dp))
                        Button(onClick = {
                            scope.launch {
//                                scaffoldState.bottomSheetState.expand()
                                showState.value = showState.value.not()
                            }
                        }) {
                            Text("Click to show sheet")
                        }
                    }
                }
            }


        })
}

@Preview(showBackground = true)
@Composable
fun showDialog(openDialog: MutableState<Boolean> = mutableStateOf(false)) {
    val state = remember { mutableStateOf(true) }

    val alpha by animateFloatAsState(
        targetValue = if (state.value) 1f else 0f,
        animationSpec = tween(1000)
    )

    val height by animateIntAsState(
        targetValue = if (state.value) 350 else 150,
        animationSpec = tween(1000)
    )

    Dialog(
        onDismissRequest = { openDialog.value = false },
        properties = DialogProperties(usePlatformDefaultWidth = true),
    ) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(15.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .height(height.dp)
            ) {
                Button(onClick = {
                    state.value = !state.value
                }) {
                    Text(text = "Click")
                }
                Text(text = "Turned on by default", modifier = Modifier.alpha(alpha))
                Text(text = "Turned on by default")
                Text(text = "Turned on by default")
                Text(text = "Turned on by default")
                Text(text = "Turned on by default")
                Text(text = "Turned on by default")
                Text(text = "Turned on by default")
                Text(text = "Turned on by default")
                Text(text = "Turned on by default")
            }
        }
    }
}

fun providePlayer(context: Context, settings: Preferences): ExoPlayer {
    val defaultBuffer = 2000
    val loadControl = DefaultLoadControl.Builder().setBufferDurationsMs(
        defaultBuffer, defaultBuffer, defaultBuffer, defaultBuffer
    ).build()

    return ExoPlayer.Builder(
        context,
        DefaultRenderersFactory(context),
        DefaultMediaSourceFactory(context),
        DefaultTrackSelector(context),
        loadControl,
        DefaultBandwidthMeter.Builder(context).build(),
        DefaultAnalyticsCollector(Clock.DEFAULT)
    ).build()
}

@Composable
fun SettingsScreen(navController: NavHostController) {
    Card(
        elevation = 8.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Button(onClick = {
                navController.navigateUp()
            }) {
                Text(text = "Click")
            }
            Text(text = "Turned on by default")
            Text(text = "Turned on by default")
            Text(text = "Turned on by default")
            Text(text = "Turned on by default")
            Text(text = "Turned on by default")
            Text(text = "Turned on by default")
            Text(text = "Turned on by default")
            Text(text = "Turned on by default")
            Text(text = "Turned on by default")
        }
    }
}

fun cornerRadius(state: BottomSheetState): Float = state.currentFraction * 20f

val BottomSheetState.currentFraction: Float
    get() {
        val fraction = progress.fraction
        val targetValue = targetValue
        val currentValue = currentValue

        return when {
            currentValue == BottomSheetValue.Collapsed && targetValue == BottomSheetValue.Collapsed -> 1f
            currentValue == BottomSheetValue.Expanded && targetValue == BottomSheetValue.Expanded -> 0f
            currentValue == BottomSheetValue.Collapsed && targetValue == BottomSheetValue.Expanded -> 1f - fraction
            else -> fraction
        }
    }