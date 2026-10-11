package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyMaterial
import com.example.ui.theme.AcademyPrimary
import com.example.ui.theme.AcademySuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyMaterialViewer(
  material: StudyMaterial,
  isDownloaded: Boolean,
  onToggleDownload: () -> Unit,
  onClose: () -> Unit
) {
  BackHandler { onClose() }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = material.title,
              style = MaterialTheme.typography.titleMedium,
              maxLines = 1
            )
            Text(
              text = "${material.subject} • ${material.classStandard}",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onClose,
            modifier = Modifier.testTag("notes_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(onClick = onToggleDownload) {
            Icon(
              imageVector = if (isDownloaded) Icons.Default.Check else Icons.Default.Download,
              contentDescription = "Download Notes",
              tint = if (isDownloaded) AcademySuccess else MaterialTheme.colorScheme.primary
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
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Document Metadata Bar
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = material.type,
              style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
              color = AcademyPrimary
            )
            Text(
              text = "${material.pages} Pages • ${material.fileSizeMb} MB PDF",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          FilledTonalButton(
            onClick = onToggleDownload,
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(
              imageVector = if (isDownloaded) Icons.Default.Check else Icons.Default.Download,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isDownloaded) "Saved Offline" else "Download")
          }
        }
      }

      // High-Fidelity Content Reader Card
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Text(
            text = "DEEPAK ACADEMY STUDY RESOURCE",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            ),
            color = AcademyPrimary
          )

          Text(
            text = material.title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )

          HorizontalDivider()

          Text(
            text = material.contentPreview,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Detailed Study Highlights
          Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(
                text = "💡 Deepak Sir's Exam Tip:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = AcademyPrimary
              )
              Text(
                text = "Review these formulas 15 minutes before sleep and first thing in the morning. Writing each equation by hand twice builds permanent muscle memory for the examination hall!",
                style = MaterialTheme.typography.bodyMedium
              )
            }
          }
        }
      }
    }
  }
}
