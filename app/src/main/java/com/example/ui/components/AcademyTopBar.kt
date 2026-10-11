package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AcademyAccent
import com.example.ui.theme.AcademyAccentDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademyTopBar(
  streakDays: Int,
  coins: Int,
  onNotificationsClick: () -> Unit,
  onProfileClick: () -> Unit
) {
  TopAppBar(
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.ic_academy_logo),
          contentDescription = "Deepak Academy Logo",
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
        )
        Column {
          Text(
            text = "DEEPAK ACADEMY",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Black,
              letterSpacing = 0.5.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Learn • Practice • Succeed",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    },
    actions = {
      // Streak Badge
      Surface(
        color = AcademyAccent.copy(alpha = 0.15f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .padding(end = 6.dp)
          .testTag("streak_badge")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(text = "🔥", fontSize = 13.sp)
          Text(
            text = "${streakDays}d",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = AcademyAccentDark
          )
        }
      }

      // Coins Chip
      Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .padding(end = 4.dp)
          .testTag("coins_badge")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(text = "🪙", fontSize = 12.sp)
          Text(
            text = "$coins",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
        }
      }

      // Notifications Button
      IconButton(
        onClick = onNotificationsClick,
        modifier = Modifier.testTag("notification_button")
      ) {
        BadgedBox(
          badge = {
            Badge { Text("3") }
          }
        ) {
          Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = "Notices & Announcements"
          )
        }
      }

      // Profile Button
      IconButton(
        onClick = onProfileClick,
        modifier = Modifier.testTag("top_profile_button")
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "D",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
  )
}
