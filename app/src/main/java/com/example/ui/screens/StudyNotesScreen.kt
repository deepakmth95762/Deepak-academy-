package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyMaterial
import com.example.ui.components.StudyMaterialViewer
import com.example.ui.theme.AcademyPrimary
import com.example.ui.theme.AcademySuccess

@Composable
fun StudyNotesScreen(
  studyMaterials: List<StudyMaterial>,
  downloadedMaterialIds: Set<String>,
  selectedMaterial: StudyMaterial?,
  onOpenMaterial: (StudyMaterial) -> Unit,
  onCloseMaterial: () -> Unit,
  onToggleDownload: (String) -> Unit
) {
  if (selectedMaterial != null) {
    StudyMaterialViewer(
      material = selectedMaterial,
      isDownloaded = downloadedMaterialIds.contains(selectedMaterial.id),
      onToggleDownload = { onToggleDownload(selectedMaterial.id) },
      onClose = onCloseMaterial
    )
  } else {
    StudyNotesCatalogView(
      studyMaterials = studyMaterials,
      downloadedMaterialIds = downloadedMaterialIds,
      onOpenMaterial = onOpenMaterial,
      onToggleDownload = onToggleDownload
    )
  }
}

@Composable
fun StudyNotesCatalogView(
  studyMaterials: List<StudyMaterial>,
  downloadedMaterialIds: Set<String>,
  onOpenMaterial: (StudyMaterial) -> Unit,
  onToggleDownload: (String) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedSubject by remember { mutableStateOf("All") }
  val subjects = listOf("All", "Mathematics", "Physics", "Science", "Coding")

  val filteredList = remember(studyMaterials, searchQuery, selectedSubject) {
    studyMaterials.filter { mat ->
      val matchesSub = selectedSubject == "All" || mat.subject.equals(selectedSubject, ignoreCase = true)
      val matchesSearch = searchQuery.isBlank() ||
        mat.title.contains(searchQuery, ignoreCase = true) ||
        mat.contentPreview.contains(searchQuery, ignoreCase = true) ||
        mat.classStandard.contains(searchQuery, ignoreCase = true)
      matchesSub && matchesSearch
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Text(
      text = "Free Notes & Formula Sheets",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
    )

    // Search input
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("notes_search_input"),
      placeholder = { Text("Search formulas, derivations, PYQs...") },
      leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search") },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(Icons.Default.Close, contentDescription = "Clear")
          }
        }
      },
      shape = RoundedCornerShape(12.dp),
      singleLine = true
    )

    // Subject Filters
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(subjects) { sub ->
        FilterChip(
          selected = selectedSubject == sub,
          onClick = { selectedSubject = sub },
          label = { Text(sub) }
        )
      }
    }

    // Material list
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.weight(1f)
    ) {
      items(filteredList) { mat ->
        val isDownloaded = downloadedMaterialIds.contains(mat.id)
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenMaterial(mat) }
            .testTag("material_card_${mat.id}")
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
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "${mat.subject} • ${mat.classStandard}",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
              }

              Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = mat.type,
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSecondaryContainer,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Text(
              text = mat.title,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Text(
              text = mat.contentPreview,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 2
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${mat.pages} Pages • ${mat.fileSizeMb} MB PDF",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = { onToggleDownload(mat.id) }) {
                  Icon(
                    imageVector = if (isDownloaded) Icons.Default.CheckCircle else Icons.Default.Download,
                    contentDescription = "Download",
                    tint = if (isDownloaded) AcademySuccess else AcademyPrimary
                  )
                }

                Button(
                  onClick = { onOpenMaterial(mat) },
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = AcademyPrimary)
                ) {
                  Text("Read")
                }
              }
            }
          }
        }
      }
    }
  }
}
