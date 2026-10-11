package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppNavTab

@Composable
fun HomeScreen(
  profile: StudentProfile,
  notices: List<Notice>,
  liveClasses: List<LiveClass>,
  courses: List<Course>,
  enrolledCourseIds: Set<String>,
  onSelectTab: (AppNavTab) -> Unit,
  onOpenCourse: (Course) -> Unit,
  onOpenLiveClass: (LiveClass) -> Unit,
  onOpenDoubtDialog: () -> Unit
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(bottom = 24.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // HERO BANNER
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(190.dp)
    ) {
      Image(
        painter = painterResource(id = R.drawable.academy_hero_banner),
        contentDescription = "Academy Hero Banner",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.Transparent,
                Color(0xCC0F172A),
                Color(0xF00F172A)
              )
            )
          )
      )
      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(16.dp)
      ) {
        Surface(
          color = AcademyAccent,
          shape = RoundedCornerShape(4.dp),
          modifier = Modifier.padding(bottom = 6.dp)
        ) {
          Text(
            text = "DEEPAK ACADEMY 2026-27",
            color = Color.Black,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
          )
        }
        Text(
          text = "Namaste, ${profile.name}! 👋",
          color = Color.White,
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
          text = "Target: ${profile.targetExam}",
          color = Color.LightGray,
          style = MaterialTheme.typography.bodySmall
        )
      }
    }

    // LEARNING STATS ROW
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      StatCard(
        modifier = Modifier.weight(1f),
        icon = "🔥",
        title = "${profile.streakDays} Days",
        subtitle = "Daily Streak",
        color = AcademyAccent
      )
      StatCard(
        modifier = Modifier.weight(1f),
        icon = "⏱️",
        title = "${profile.totalStudyHours}h",
        subtitle = "Hours Studied",
        color = AcademyPrimaryLight
      )
      StatCard(
        modifier = Modifier.weight(1f),
        icon = "🎯",
        title = "${profile.averageAccuracy}%",
        subtitle = "Accuracy",
        color = AcademySuccess
      )
    }

    // LIVE CLASS HIGHLIGHT
    val currentLive = liveClasses.firstOrNull { it.isLiveNow } ?: liveClasses.firstOrNull()
    if (currentLive != null) {
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "🔴 Interactive Live Classes",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Text(
            text = "${currentLive.attendeeCount} Students Active",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
          )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
          ),
          border = BorderStroke(1.5.dp, AcademyPrimaryLight),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("live_class_card")
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                color = if (currentLive.isLiveNow) AcademyError else AcademyPrimary,
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = if (currentLive.isLiveNow) "● LIVE NOW" else currentLive.timeText,
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
              Text(
                text = currentLive.subject,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = AcademyPrimary
              )
            }

            Text(
              text = currentLive.title,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Text(
              text = currentLive.topicOverview,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  Icons.Default.School,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                  tint = AcademyPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = currentLive.instructor,
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                )
              }

              Button(
                onClick = { onOpenLiveClass(currentLive) },
                colors = ButtonDefaults.buttonColors(containerColor = AcademyPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("join_live_button")
              ) {
                Text("Join Stream")
              }
            }
          }
        }
      }
    }

    // QUICK SHORTCUTS GRID
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
      Text(
        text = "⚡ Quick Learning Tools",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        QuickActionItem(
          modifier = Modifier.weight(1f),
          title = "Courses",
          icon = Icons.Default.PlayCircle,
          color = AcademyPrimary,
          onClick = { onSelectTab(AppNavTab.COURSES) }
        )
        QuickActionItem(
          modifier = Modifier.weight(1f),
          title = "Mock Tests",
          icon = Icons.Default.Assignment,
          color = AcademySecondary,
          onClick = { onSelectTab(AppNavTab.TESTS) }
        )
        QuickActionItem(
          modifier = Modifier.weight(1f),
          title = "PDF Notes",
          icon = Icons.Default.Description,
          color = AcademyAccentDark,
          onClick = { onSelectTab(AppNavTab.NOTES) }
        )
        QuickActionItem(
          modifier = Modifier.weight(1f),
          title = "Ask Doubt",
          icon = Icons.Default.QuestionAnswer,
          color = AcademySuccess,
          onClick = onOpenDoubtDialog
        )
      }
    }

    // FEATURED COURSES SECTION
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "📚 Recommended Batches",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        TextButton(onClick = { onSelectTab(AppNavTab.COURSES) }) {
          Text("View All")
        }
      }

      courses.take(2).forEach { course ->
        val isEnrolled = enrolledCourseIds.contains(course.id)
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onOpenCourse(course) }
            .testTag("featured_course_${course.id}"),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AcademyPrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                Icons.Default.MenuBook,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
              )
            }

            Column(modifier = Modifier.weight(1f)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Surface(
                  color = MaterialTheme.colorScheme.primaryContainer,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = course.category,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
                if (isEnrolled) {
                  Text(
                    text = "Enrolled ✓",
                    fontSize = 11.sp,
                    color = AcademySuccess,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = course.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 1
              )
              Text(
                text = "${course.totalLectures} Lectures • ${course.durationHours}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }

    // MEET DEEPAK SIR (MENTOR PROFILE SPOTLIGHT)
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      )
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.instructor_deepak),
          contentDescription = "Er. Deepak Kumar - Founder",
          modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(12.dp)),
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
              contentDescription = "Verified",
              tint = AcademyPrimary,
              modifier = Modifier.size(16.dp)
            )
          }
          Text(
            text = "Founder & Head of Academics",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "“Our goal is to make every complex concept lucid, intuitive, and exam-ready!”",
            style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    // NOTICES & ANNOUNCEMENTS
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
      Text(
        text = "📢 Notice Board",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
      Spacer(modifier = Modifier.height(8.dp))

      notices.forEach { notice ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
          shape = RoundedCornerShape(10.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (notice.isUrgent) AcademyAccentLight.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
          ),
          border = CardDefaults.outlinedCardBorder()
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = notice.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = if (notice.isUrgent) AcademyAccentDark else MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = notice.date,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Text(
              text = notice.message,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}

@Composable
fun StatCard(
  modifier: Modifier = Modifier,
  icon: String,
  title: String,
  subtitle: String,
  color: Color
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = CardDefaults.outlinedCardBorder()
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(text = icon, fontSize = 20.sp)
      Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun QuickActionItem(
  modifier: Modifier = Modifier,
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  onClick: () -> Unit
) {
  Card(
    modifier = modifier
      .clickable { onClick() }
      .testTag("quick_${title.lowercase()}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = CardDefaults.outlinedCardBorder()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = color,
          modifier = Modifier.size(22.dp)
        )
      }
      Text(
        text = title,
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1
      )
    }
  }
}
