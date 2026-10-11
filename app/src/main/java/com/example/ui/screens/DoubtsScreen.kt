package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Doubt
import com.example.ui.theme.AcademyPrimary
import com.example.ui.theme.AcademySuccess

@Composable
fun DoubtsScreen(
  doubts: List<Doubt>,
  onPostDoubt: (String, String, String) -> Unit,
  onUpvote: (String) -> Unit
) {
  var showAskDialog by remember { mutableStateOf(false) }

  Scaffold(
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { showAskDialog = true },
        icon = { Icon(Icons.Default.AddComment, contentDescription = null) },
        text = { Text("Ask Deepak Sir") },
        containerColor = AcademyPrimary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.testTag("fab_ask_doubt")
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Text(
        text = "Doubts & Mentorship Forum",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
      )
      Text(
        text = "Post your questions and get instant verified solutions from Er. Deepak Kumar.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(doubts) { doubt ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.fillMaxWidth()
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
                    text = "${doubt.subject} • ${doubt.studentClass}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }

                Text(
                  text = doubt.timeAgo,
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Text(
                text = "Q: ${doubt.questionText}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
              )

              Text(
                text = "Asked by ${doubt.studentName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              if (doubt.isSolved && doubt.solutionText != null) {
                Surface(
                  color = MaterialTheme.colorScheme.surfaceVariant,
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AcademySuccess,
                        modifier = Modifier.size(16.dp)
                      )
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(
                        text = "Solution by ${doubt.solvedBy ?: "Er. Deepak Kumar"}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AcademyPrimary
                      )
                    }

                    Text(
                      text = doubt.solutionText,
                      style = MaterialTheme.typography.bodyMedium
                    )
                  }
                }
              }

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.clickable { onUpvote(doubt.id) }
                ) {
                  Icon(
                    Icons.Default.ThumbUp,
                    contentDescription = "Upvote",
                    tint = AcademyPrimary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${doubt.upvotes} Helpful",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                  )
                }

                Surface(
                  color = if (doubt.isSolved) AcademySuccess.copy(alpha = 0.15f) else MaterialTheme.colorScheme.secondaryContainer,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = if (doubt.isSolved) "Answered ✓" else "Pending Review",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (doubt.isSolved) AcademySuccess else MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  if (showAskDialog) {
    AskDoubtDialog(
      onDismiss = { showAskDialog = false },
      onSubmit = { cls, sub, q ->
        onPostDoubt(cls, sub, q)
        showAskDialog = false
      }
    )
  }
}

@Composable
fun AskDoubtDialog(
  onDismiss: () -> Unit,
  onSubmit: (String, String, String) -> Unit
) {
  var selectedClass by remember { mutableStateOf("Class 10") }
  var selectedSubject by remember { mutableStateOf("Mathematics") }
  var questionText by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("ask_doubt_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Ask Deepak Sir",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Text(
          text = "Specify subject & class to receive prioritized answer:",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Class selector
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          listOf("Class 10", "Class 12", "Foundation").forEach { cls ->
            FilterChip(
              selected = selectedClass == cls,
              onClick = { selectedClass = cls },
              label = { Text(cls) }
            )
          }
        }

        // Subject selector
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          listOf("Mathematics", "Physics", "Chemistry").forEach { sub ->
            FilterChip(
              selected = selectedSubject == sub,
              onClick = { selectedSubject = sub },
              label = { Text(sub) }
            )
          }
        }

        OutlinedTextField(
          value = questionText,
          onValueChange = { questionText = it },
          placeholder = { Text("Type your query or theorem doubt clearly...") },
          modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .testTag("doubt_question_input"),
          shape = RoundedCornerShape(12.dp)
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
          ) {
            Text("Cancel")
          }

          Button(
            onClick = {
              if (questionText.isNotBlank()) {
                onSubmit(selectedClass, selectedSubject, questionText)
              }
            },
            enabled = questionText.isNotBlank(),
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = AcademyPrimary)
          ) {
            Text("Submit")
          }
        }
      }
    }
  }
}
