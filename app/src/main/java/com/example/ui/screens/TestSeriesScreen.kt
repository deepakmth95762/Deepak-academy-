package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Quiz
import com.example.data.model.QuizQuestion
import com.example.ui.theme.*
import com.example.ui.viewmodel.ActiveQuizState

@Composable
fun TestSeriesScreen(
  quizzes: List<Quiz>,
  activeQuizState: ActiveQuizState?,
  onStartQuiz: (Quiz) -> Unit,
  onAnswerQuestion: (Int, Int) -> Unit,
  onNavigateQuestion: (Int) -> Unit,
  onSubmitQuiz: () -> Unit,
  onCloseQuiz: () -> Unit
) {
  if (activeQuizState != null) {
    ActiveQuizView(
      state = activeQuizState,
      onAnswerQuestion = onAnswerQuestion,
      onNavigateQuestion = onNavigateQuestion,
      onSubmitQuiz = onSubmitQuiz,
      onCloseQuiz = onCloseQuiz
    )
  } else {
    QuizCatalogView(
      quizzes = quizzes,
      onStartQuiz = onStartQuiz
    )
  }
}

@Composable
fun QuizCatalogView(
  quizzes: List<Quiz>,
  onStartQuiz: (Quiz) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(
      text = "Practice Tests & Mock Series",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
    )

    // Daily Banner
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(
        containerColor = AcademyPrimary
      ),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Box(
          modifier = Modifier
            .size(48.dp)
            .background(AcademyAccent, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text("🏆", fontSize = 22.sp)
        }
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Daily Exam Speed Drills",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
          )
          Text(
            text = "Attempt timed mock tests to build board examination stamina and accuracy.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.8f)
          )
        }
      }
    }

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(quizzes) { quiz ->
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("quiz_card_${quiz.id}")
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                  text = quiz.subject,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
              }

              Surface(
                color = when (quiz.difficulty) {
                  "Easy" -> AcademySuccess.copy(alpha = 0.15f)
                  "Medium" -> AcademyAccent.copy(alpha = 0.15f)
                  else -> AcademyError.copy(alpha = 0.15f)
                },
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = quiz.difficulty,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = when (quiz.difficulty) {
                    "Easy" -> AcademySuccess
                    "Medium" -> AcademyAccentDark
                    else -> AcademyError
                  },
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
              }
            }

            Text(
              text = quiz.title,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "📝 ${quiz.totalQuestions} Questions • ⏱️ ${quiz.durationMinutes} Mins",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Button(
                onClick = { onStartQuiz(quiz) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AcademyPrimary)
              ) {
                Text("Start Test")
              }
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveQuizView(
  state: ActiveQuizState,
  onAnswerQuestion: (Int, Int) -> Unit,
  onNavigateQuestion: (Int) -> Unit,
  onSubmitQuiz: () -> Unit,
  onCloseQuiz: () -> Unit
) {
  BackHandler { onCloseQuiz() }

  val quiz = state.quiz
  val currentQ = quiz.questions.getOrNull(state.currentQuestionIndex) ?: quiz.questions.first()
  val minutesLeft = state.remainingSeconds / 60
  val secondsLeft = state.remainingSeconds % 60

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(quiz.title, style = MaterialTheme.typography.titleMedium, maxLines = 1)
        },
        navigationIcon = {
          IconButton(onClick = onCloseQuiz) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Exit Quiz")
          }
        },
        actions = {
          if (!state.isSubmitted) {
            Surface(
              color = if (state.remainingSeconds < 60) AcademyError.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer,
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.padding(end = 12.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  Icons.Default.Timer,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                  tint = if (state.remainingSeconds < 60) AcademyError else AcademyPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "%02d:%02d".format(minutesLeft, secondsLeft),
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = if (state.remainingSeconds < 60) AcademyError else AcademyPrimary
                )
              }
            }
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
      if (state.isSubmitted) {
        // SCORECARD SUMMARY
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text("🎉 Test Completed!", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Text(
              text = "${state.score} / ${state.totalQuestions} Correct",
              style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
              color = AcademyPrimary
            )
            val accuracy = if (state.totalQuestions > 0) (state.score * 100) / state.totalQuestions else 0
            Text(
              text = "Accuracy: $accuracy% • +${state.score * 20} Academy XP",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Button(
              onClick = onCloseQuiz,
              colors = ButtonDefaults.buttonColors(containerColor = AcademyPrimary)
            ) {
              Text("Back to Test Series")
            }
          }
        }

        Text(
          text = "Detailed Solutions & Explanations by Deepak Sir",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        quiz.questions.forEachIndexed { idx, q ->
          val chosen = state.selectedAnswers[q.id]
          val isCorrect = chosen == q.correctOptionIndex

          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(
              1.dp,
              if (isCorrect) AcademySuccess else AcademyError
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Question ${idx + 1}",
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
                Text(
                  text = if (isCorrect) "✓ Correct (+1)" else "✗ Incorrect (0)",
                  color = if (isCorrect) AcademySuccess else AcademyError,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }

              Text(text = q.questionText, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))

              Text(
                text = "Correct Answer: ${q.options[q.correctOptionIndex]}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = AcademySuccess
              )

              if (chosen != null && chosen != q.correctOptionIndex) {
                Text(
                  text = "Your Answer: ${q.options[chosen]}",
                  style = MaterialTheme.typography.bodySmall,
                  color = AcademyError
                )
              }

              Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Text(
                    text = "Explanation:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                  )
                  Text(
                    text = q.explanation,
                    style = MaterialTheme.typography.bodySmall
                  )
                }
              }
            }
          }
        }
      } else {
        // ACTIVE TEST QUESTIONS NAVIGATION
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Question ${state.currentQuestionIndex + 1} of ${quiz.questions.size}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = currentQ.topic,
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSecondaryContainer,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        // Question Card
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            Text(
              text = currentQ.questionText,
              style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
              lineHeight = 22.sp
            )

            HorizontalDivider()

            val selectedOption = state.selectedAnswers[currentQ.id]

            currentQ.options.forEachIndexed { optIdx, optText ->
              val isSelected = selectedOption == optIdx
              Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                  containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                ),
                border = BorderStroke(
                  1.dp,
                  if (isSelected) AcademyPrimary else Color.Transparent
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { onAnswerQuestion(currentQ.id, optIdx) }
                  .testTag("option_${currentQ.id}_$optIdx")
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                  RadioButton(
                    selected = isSelected,
                    onClick = { onAnswerQuestion(currentQ.id, optIdx) }
                  )
                  Text(
                    text = optText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    ),
                    modifier = Modifier.weight(1f)
                  )
                }
              }
            }
          }
        }

        // Question Navigator buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          OutlinedButton(
            onClick = {
              if (state.currentQuestionIndex > 0) {
                onNavigateQuestion(state.currentQuestionIndex - 1)
              }
            },
            enabled = state.currentQuestionIndex > 0
          ) {
            Text("Previous")
          }

          if (state.currentQuestionIndex < quiz.questions.size - 1) {
            Button(
              onClick = { onNavigateQuestion(state.currentQuestionIndex + 1) },
              colors = ButtonDefaults.buttonColors(containerColor = AcademyPrimary)
            ) {
              Text("Next Question")
            }
          } else {
            Button(
              onClick = onSubmitQuiz,
              colors = ButtonDefaults.buttonColors(containerColor = AcademySuccess),
              modifier = Modifier.testTag("submit_quiz_button")
            ) {
              Text("Submit Exam")
            }
          }
        }
      }
    }
  }
}
