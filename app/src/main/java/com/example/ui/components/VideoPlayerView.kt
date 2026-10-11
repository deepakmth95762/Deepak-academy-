package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Lesson
import com.example.ui.theme.AcademyAccent
import com.example.ui.theme.AcademyPrimary
import com.example.ui.theme.AcademySuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerView(
  lesson: Lesson,
  courseTitle: String,
  isCompleted: Boolean,
  onToggleComplete: () -> Unit,
  onClose: () -> Unit,
  onAskDoubt: () -> Unit
) {
  BackHandler { onClose() }

  var isPlaying by remember { mutableStateOf(true) }
  var progressSeconds by remember { mutableIntStateOf(340) }
  val totalSeconds = remember(lesson) { lesson.durationMinutes * 60 }
  var playbackSpeed by remember { mutableStateOf("1.0x") }
  var videoQuality by remember { mutableStateOf("1080p") }
  var showSpeedMenu by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = lesson.title,
              style = MaterialTheme.typography.titleMedium,
              maxLines = 1
            )
            Text(
              text = courseTitle,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onClose,
            modifier = Modifier.testTag("video_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(onClick = onToggleComplete) {
            Icon(
              imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.CheckCircleOutline,
              contentDescription = "Mark Complete",
              tint = if (isCompleted) AcademySuccess else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .verticalScroll(rememberScrollState())
    ) {
      // VIDEO PLAYER STAGE
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(230.dp)
          .background(Color.Black)
      ) {
        // Video poster background
        Image(
          painter = painterResource(id = R.drawable.academy_hero_banner),
          contentDescription = "Video Poster",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize().alpha(0.6f)
        )

        // Gradient overlay
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
              )
            )
        )

        // Center Controls (Rewind 10, Play/Pause, Forward 10)
        Row(
          modifier = Modifier.align(Alignment.Center),
          horizontalArrangement = Arrangement.spacedBy(28.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = { progressSeconds = (progressSeconds - 10).coerceAtLeast(0) },
            modifier = Modifier
              .size(44.dp)
              .background(Color.Black.copy(alpha = 0.5f), CircleShape)
          ) {
            Icon(
              Icons.Default.Replay10,
              contentDescription = "Rewind 10s",
              tint = Color.White
            )
          }

          IconButton(
            onClick = { isPlaying = !isPlaying },
            modifier = Modifier
              .size(64.dp)
              .background(AcademyPrimary, CircleShape)
              .testTag("video_play_pause_button")
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = if (isPlaying) "Pause" else "Play",
              tint = Color.White,
              modifier = Modifier.size(36.dp)
            )
          }

          IconButton(
            onClick = { progressSeconds = (progressSeconds + 10).coerceAtMost(totalSeconds) },
            modifier = Modifier
              .size(44.dp)
              .background(Color.Black.copy(alpha = 0.5f), CircleShape)
          ) {
            Icon(
              Icons.Default.Forward10,
              contentDescription = "Forward 10s",
              tint = Color.White
            )
          }
        }

        // Top badges inside video
        Row(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(12.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            color = Color.Black.copy(alpha = 0.7f),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.clickable { showSpeedMenu = true }
          ) {
            Text(
              text = playbackSpeed,
              color = Color.White,
              fontSize = 12.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          Surface(
            color = Color.Black.copy(alpha = 0.7f),
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = videoQuality,
              color = Color.White,
              fontSize = 12.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        DropdownMenu(
          expanded = showSpeedMenu,
          onDismissRequest = { showSpeedMenu = false }
        ) {
          listOf("0.75x", "1.0x", "1.25x", "1.5x", "2.0x").forEach { sp ->
            DropdownMenuItem(
              text = { Text(sp) },
              onClick = {
                playbackSpeed = sp
                showSpeedMenu = false
              }
            )
          }
        }

        // Bottom scrubbing timeline bar
        Column(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
          Slider(
            value = progressSeconds.toFloat(),
            onValueChange = { progressSeconds = it.toInt() },
            valueRange = 0f..totalSeconds.toFloat(),
            colors = SliderDefaults.colors(
              thumbColor = AcademyAccent,
              activeTrackColor = AcademyAccent,
              inactiveTrackColor = Color.White.copy(alpha = 0.3f)
            ),
            modifier = Modifier.height(20.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            val curMin = progressSeconds / 60
            val curSec = progressSeconds % 60
            val totMin = totalSeconds / 60
            val totSec = totalSeconds % 60

            Text(
              text = "%02d:%02d / %02d:%02d".format(curMin, curSec, totMin, totSec),
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Text(text = "Deepak Academy HD Stream", color = Color.LightGray, fontSize = 11.sp)
            }
          }
        }
      }

      // LECTURE CONTENT & DETAILS
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Teacher attribution card
        Card(
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Image(
              painter = painterResource(id = R.drawable.instructor_deepak),
              contentDescription = "Er. Deepak Kumar",
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
              contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Er. Deepak Kumar",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                  Icons.Default.Verified,
                  contentDescription = "Verified Educator",
                  tint = AcademyPrimary,
                  modifier = Modifier.size(16.dp)
                )
              }
              Text(
                text = "Lead Faculty & Founder • Deepak Academy",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        // Action Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = onToggleComplete,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isCompleted) AcademySuccess else AcademyPrimary
            )
          ) {
            Icon(
              imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.CheckCircleOutline,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isCompleted) "Completed" else "Mark Complete")
          }

          OutlinedButton(
            onClick = onAskDoubt,
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              Icons.Default.HelpOutline,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Ask Doubt")
          }
        }

        // Lecture Summary
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          border = CardDefaults.outlinedCardBorder()
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text(
              text = "📌 Lecture Summary & Concept Notes",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = lesson.summary,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider()

            Text(
              text = "Key Board Exam Takeaways:",
              style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )

            lesson.keyPoints.forEachIndexed { index, point ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Text(
                  text = "•",
                  color = AcademyPrimary,
                  fontWeight = FontWeight.Black
                )
                Text(
                  text = point,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }
    }
  }
}
