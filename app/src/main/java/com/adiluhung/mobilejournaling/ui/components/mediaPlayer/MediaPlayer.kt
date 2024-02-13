package com.adiluhung.mobilejournaling.ui.components.mediaPlayer

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun VideoPlayer(modifier: Modifier = Modifier) {
   val context = LocalContext.current

   // create our player
   val exoPlayer = remember {
      SimpleExoPlayer.Builder(context).build().apply {
         this.prepare()
      }
   }

   ConstraintLayout(modifier = modifier) {
      val (title, videoPlayer) = createRefs()

      // video title
      Text(
         text = "Current Title",
         color = Color.White,
         modifier =
         Modifier.padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .constrainAs(title) {
               top.linkTo(parent.top)
               start.linkTo(parent.start)
               end.linkTo(parent.end)
            }
      )

      // player view
      DisposableEffect(
         AndroidView(
            modifier =
            Modifier.testTag("VideoPlayer")
               .constrainAs(videoPlayer) {
                  top.linkTo(parent.top)
                  start.linkTo(parent.start)
                  end.linkTo(parent.end)
                  bottom.linkTo(parent.bottom)
               },
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
      }
   }
}