package com.adiluhung.mobilejournaling.ui.screen.authed.detailSession

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.adiluhung.mobilejournaling.R
import com.adiluhung.mobilejournaling.data.network.responses.SessionDetail
import com.adiluhung.mobilejournaling.ui.ViewModelFactory
import com.adiluhung.mobilejournaling.ui.common.UiState
import com.adiluhung.mobilejournaling.ui.theme.JournalingTheme
import com.adiluhung.mobilejournaling.ui.theme.Sky900
import kotlinx.coroutines.delay

data class Music(
   val name: String,
   val artist: String,
   val music: Int,
   val cover: Int,
)

@Composable
fun TrackSlider(
   value: Float,
   onValueChange: (newValue: Float) -> Unit,
   onValueChangeFinished: () -> Unit,
   songDuration: Float
) {
   Slider(
      value = value,
      onValueChange = {
         onValueChange(it)
      },
      onValueChangeFinished = {

         onValueChangeFinished()

      },
      valueRange = 0f..songDuration,
      colors = SliderDefaults.colors(
         thumbColor = Color.Black,
         activeTrackColor = Color.DarkGray,
         inactiveTrackColor = Color.Gray,
      )
   )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSessionScreen(
   navController: NavController,
   sessionId: String,
   viewModel: DetailSessionViewModel = viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
   val context = LocalContext.current

   var session by remember { mutableStateOf<SessionDetail?>(null) }
   var isLoadingInit by remember { mutableStateOf(true) }
   val durationSet = remember { mutableStateOf(false) }


   LaunchedEffect(Unit) {
      viewModel.getDetailSession(sessionId)
   }

   LaunchedEffect(Unit) {
      viewModel
         .toastMessage
         .collect { message ->
            Toast.makeText(
               context,
               message,
               Toast.LENGTH_SHORT,
            ).show()
         }
   }

   viewModel.uiState.observeAsState().value.let { uiState ->
      when (uiState) {
         is UiState.Loading -> {
            isLoadingInit = true
         }

         is UiState.Success -> {
            isLoadingInit = false
            session = uiState.data?.data
         }

         is UiState.Error -> {
            isLoadingInit = false
         }

         else -> {
            isLoadingInit = false
         }
      }
   }


   Box(
      modifier = with(Modifier) {
         fillMaxSize()
            .paint(
               painterResource(id = R.drawable.screen_background),
               contentScale = ContentScale.FillBounds
            )

      })
   {
      if (session != null) {
         var autoPlay by remember { mutableStateOf(true) }

         val isPlaying = remember { mutableStateOf(false) }
         var currentPosition = remember { mutableLongStateOf(0) }
         val sliderPosition = remember { mutableLongStateOf(0) }
         val totalDuration = remember { mutableLongStateOf(0) }

         val player = remember {
            val myPlayer = ExoPlayer.Builder(context).build()

            val path = session?.mediaFile
            val mediaItem = path?.let { MediaItem.fromUri(it) }
            if (mediaItem != null) {
               myPlayer.setMediaItem(mediaItem)
            }

            myPlayer.prepare()

            myPlayer.playWhenReady = true
            myPlayer.seekTo(currentPosition.longValue)

            val listener = object : Player.Listener {
               override fun onPlaybackStateChanged(playbackState: Int) {
                  super.onPlaybackStateChanged(playbackState)
                  if (playbackState == Player.STATE_READY && !durationSet.value) {
                     totalDuration.longValue = myPlayer.duration
                     durationSet.value = true
                  }
                  if (playbackState == Player.STATE_ENDED) {
                     isPlaying.value = false
                     viewModel.completeSession(sessionId)
                     navController.navigate("sessionComplete/$sessionId")
                  }
               }

               override fun onIsPlayingChanged(isPlaying_: Boolean) {
                  super.onIsPlayingChanged(isPlaying_)
                  isPlaying.value = isPlaying_
               }

               override fun onPlayerError(error: PlaybackException) {
                  super.onPlayerError(error)
                  Log.e("DetailSessionScreen", "Error: ${error.message}")
               }
            }

            myPlayer.addListener(listener)

            myPlayer
         }

         fun updateState() {
            autoPlay = player.playWhenReady
            currentPosition.longValue = 0L.coerceAtLeast(player.contentPosition)
         }

         if (isPlaying.value) {
            LaunchedEffect(Unit) {
               while (true) {
                  delay(1000)
                  currentPosition.longValue = player.currentPosition
               }
            }
         }

         LaunchedEffect(currentPosition.longValue) {
            sliderPosition.longValue = currentPosition.longValue
         }

         fun onPlaySession() {
            if (isPlaying.value) {
               player.pause()
               updateState()
               isPlaying.value = false
            } else {
               player.play()
               updateState()
               isPlaying.value = true
            }
         }

         DisposableEffect(Unit) {
            onDispose {
               updateState()
               player.release()
            }
         }


         Column(
            modifier = Modifier
               .fillMaxWidth()
               .padding(
                  top = 12.dp,
                  start = 12.dp,
                  end = 12.dp,
                  bottom = 12.dp
               )
         ) {
            IconButton(
               modifier = Modifier
                  .padding(top = 24.dp, start = 8.dp),
               onClick = {
                  navController.navigateUp()
               }) {
               Icon(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = "Back",
                  modifier = Modifier
                     .height(28.dp)
                     .width(28.dp),
                  tint = Sky900,
               )
            }

            Row(
               modifier = Modifier
                  .fillMaxWidth()
                  .weight(1f),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.Center
            ) {
               Column(
                  modifier = Modifier.clickable {
                     player.seekTo(currentPosition.longValue - 10000)
                  }, horizontalAlignment = Alignment.CenterHorizontally
               ) {
                  Icon(
                     imageVector = Icons.Default.ArrowBack,
                     contentDescription = "Back",
                     modifier = Modifier
                        .size(36.dp)
                  )
                  Text(text = "10")
               }

               Spacer(modifier = Modifier.width(16.dp))

               // Play/Pause Button
               Card(
                  shape = RoundedCornerShape(50),
                  onClick = {
                     onPlaySession()
                  }
               ) {
                  Column(modifier = Modifier.padding(8.dp)) {
                     if (player.isPlaying) {
                        Icon(
                           painter = painterResource(id = R.drawable.ic_pause),
                           contentDescription = "Play",
                           modifier = Modifier
                              .size(56.dp),
                           tint = Sky900
                        )
                     } else {
                        Icon(
                           imageVector = Icons.Default.PlayArrow,
                           contentDescription = "Play",
                           modifier = Modifier
                              .size(56.dp)
                              .padding(4.dp),
                           tint = Sky900
                        )
                     }

                  }
               }

               Spacer(modifier = Modifier.width(16.dp))

               Column(
                  modifier = Modifier.clickable {
                     player.seekTo(currentPosition.longValue + 10000)
                  },
                  horizontalAlignment = Alignment.CenterHorizontally
               ) {
                  Icon(
                     imageVector = Icons.Default.ArrowForward,
                     contentDescription = "Back",
                     modifier = Modifier
                        .size(36.dp)
                  )
                  Text(text = "10")
               }


            }

            Card(
               modifier = Modifier.fillMaxWidth(),
               colors = CardDefaults.cardColors(
                  containerColor = Color.White,
               )
            ) {
               Column(
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(16.dp)
               ) {
                  Row(
                     modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceBetween,
                     verticalAlignment = Alignment.CenterVertically
                  ) {
                     Column {
                        Text(
                           text = session?.title ?: "Loading...",
                           style = MaterialTheme.typography.bodyMedium.copy(
                              fontWeight = FontWeight.Bold,
                              fontSize = 18.sp,
                              lineHeight = 24.sp,
                           )

                        )
                        Text(
                           text = if (session?.programName != null) "${session?.programName}/${session?.moduleName}" else "Loading...",
                           style = MaterialTheme.typography.bodyMedium.copy(
                              fontWeight = FontWeight.Normal,
                              fontSize = 14.sp,
                              lineHeight = 16.sp,
                           )

                        )

                     }

                     var isFavorite by remember { mutableStateOf(session?.isFavorite ?: false) }
                     if (isFavorite) {
                        Icon(
                           modifier = Modifier.clickable {
                              isFavorite = !isFavorite
                              session?.id?.let { viewModel.updateFavoriteSession(it) }
                           },
                           imageVector = Icons.Filled.Favorite,
                           contentDescription = "Favorite",
                           tint = Sky900,
                        )
                     } else {
                        Icon(
                           modifier = Modifier.clickable {
                              isFavorite = !isFavorite
                              session?.id?.let { viewModel.updateFavoriteSession(it) }
                           },
                           imageVector = Icons.Outlined.FavoriteBorder,
                           contentDescription = "Favorite",
                           tint = Sky900,
                        )
                     }
                  }

                  TrackSlider(
                     value = sliderPosition.longValue.toFloat(),
                     onValueChange = {
                        sliderPosition.longValue = it.toLong()
                     },
                     onValueChangeFinished = {
                        currentPosition.longValue = sliderPosition.longValue
                        player.seekTo(currentPosition.longValue)
                     },
                     songDuration = totalDuration.longValue.toFloat()
                  )

                  Row(
                     modifier = Modifier.fillMaxWidth(),
                  ) {

                     Text(
                        text = (currentPosition.longValue).convertToText(),
                        modifier = Modifier
                           .weight(1f)
                           .padding(8.dp),
                        color = Color.Black,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                     )

                     val remainTime = totalDuration.longValue - currentPosition.longValue
                     Text(
                        text = if (remainTime >= 0) remainTime.convertToText() else "",
                        modifier = Modifier
                           .padding(8.dp),
                        color = Color.Black,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                     )
                  }
               }
            }
         }
      }
   }
}

private fun Long.convertToText(): String {
   val sec = this / 1000
   val minutes = sec / 60
   val seconds = sec % 60

   val minutesString = if (minutes < 10) {
      "0$minutes"
   } else {
      minutes.toString()
   }
   val secondsString = if (seconds < 10) {
      "0$seconds"
   } else {
      seconds.toString()
   }
   return "$minutesString:$secondsString"
}

@Preview(showBackground = true)
@Composable
fun DetailSessionPreview() {
   JournalingTheme {
      DetailSessionScreen(navController = rememberNavController(), sessionId = "2")
   }
}
