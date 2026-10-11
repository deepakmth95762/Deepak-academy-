package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Course
import com.example.data.model.StudentProfile
import com.example.data.model.StudyMaterial
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
  profile: StudentProfile,
  courses: List<Course>,
  enrolledCourseIds: Set<String>,
  downloadedMaterialIds: Set<String>,
  studyMaterials: List<StudyMaterial>,
  onOpenCourse: (Course) -> Unit,
  onShowCertificate: (Course) -> Unit
) {
  val enrolledCourses = courses.filter { enrolledCourseIds.contains(it.id) }
  val downloadedList = studyMaterials.filter { downloadedMaterialIds.contains(it.id) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(
      text = "Student Profile & Progress",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
    )

    // User Avatar Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = CardDefaults.outlinedCardBorder(),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Box(
          modifier = Modifier
            .size(68.dp)
            .clip(CircleShape)
            .background(AcademyPrimary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "D",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = profile.name,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
          )
          Text(
            text = profile.email,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Roll No: ${profile.rollNumber}",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = AcademyPrimary
          )
        }
      }
    }

    // LEARNING PERFORMANCE STATS
    Text(
      text = "Academic Milestones",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      StatCard(
        modifier = Modifier.weight(1f),
        icon = "🔥",
        title = "${profile.streakDays} Days",
        subtitle = "Active Streak",
        color = AcademyAccent
      )
      StatCard(
        modifier = Modifier.weight(1f),
        icon = "📝",
        title = "${profile.testsCompleted}",
        subtitle = "Tests Given",
        color = AcademySecondary
      )
      StatCard(
        modifier = Modifier.weight(1f),
        icon = "🪙",
        title = "${profile.coins}",
        subtitle = "XP Coins",
        color = AcademyAccentDark
      )
    }

    // ENROLLED COURSES & CERTIFICATES
    Text(
      text = "My Enrolled Batches (${enrolledCourses.size})",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )

    if (enrolledCourses.isEmpty()) {
      Text(
        text = "No enrolled batches yet. Explore courses to enroll!",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    } else {
      enrolledCourses.forEach { course ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = course.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
              )
              Surface(
                color = AcademySuccess.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "Active",
                  color = AcademySuccess,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Button(
                onClick = { onOpenCourse(course) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = AcademyPrimary)
              ) {
                Text("Continue Study")
              }

              OutlinedButton(
                onClick = { onShowCertificate(course) },
                modifier = Modifier.weight(1f)
              ) {
                Icon(Icons.Default.WorkspacePremium, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Certificate")
              }
            }
          }
        }
      }
    }

    // OFFLINE DOWNLOADS
    Text(
      text = "Saved Offline Study Materials (${downloadedList.size})",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )

    downloadedList.forEach { mat ->
      Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Icon(Icons.Default.DownloadDone, contentDescription = null, tint = AcademySuccess)
          Column(modifier = Modifier.weight(1f)) {
            Text(text = mat.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = "${mat.subject} • ${mat.pages} Pages", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }

    // ACADEMY CONTACT & HELPLINE CARD
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
      ),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = "📞 Deepak Academy Helpline & Mentorship",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = AcademyPrimary
        )
        Text(
          text = "Email: deepakmth95762@gmail.com",
          style = MaterialTheme.typography.bodySmall
        )
        Text(
          text = "Helpline: +91 95762 XXXXX • Mon-Sat 9 AM - 8 PM",
          style = MaterialTheme.typography.bodySmall
        )
        Text(
          text = "Campus: Deepak Academy Education Center, India",
          style = MaterialTheme.typography.bodySmall
        )
      }
    }
  }
}
