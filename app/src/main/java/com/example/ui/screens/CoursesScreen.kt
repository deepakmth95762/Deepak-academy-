package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.data.model.Course
import com.example.data.model.Lesson
import com.example.ui.theme.*

@Composable
fun CoursesScreen(
  courses: List<Course>,
  enrolledCourseIds: Set<String>,
  completedLessonIds: Set<String>,
  selectedCourse: Course?,
  selectedCategory: String,
  searchQuery: String,
  onCategorySelected: (String) -> Unit,
  onSearchQueryChanged: (String) -> Unit,
  onCourseClick: (Course) -> Unit,
  onCloseCourseDetail: () -> Unit,
  onToggleEnroll: (String) -> Unit,
  onOpenLesson: (Lesson) -> Unit,
  onShowCertificate: (Course) -> Unit
) {
  if (selectedCourse != null) {
    CourseDetailView(
      course = selectedCourse,
      isEnrolled = enrolledCourseIds.contains(selectedCourse.id),
      completedLessonIds = completedLessonIds,
      onClose = onCloseCourseDetail,
      onToggleEnroll = { onToggleEnroll(selectedCourse.id) },
      onOpenLesson = onOpenLesson,
      onShowCertificate = { onShowCertificate(selectedCourse) }
    )
  } else {
    CourseCatalogView(
      courses = courses,
      enrolledCourseIds = enrolledCourseIds,
      completedLessonIds = completedLessonIds,
      selectedCategory = selectedCategory,
      searchQuery = searchQuery,
      onCategorySelected = onCategorySelected,
      onSearchQueryChanged = onSearchQueryChanged,
      onCourseClick = onCourseClick
    )
  }
}

@Composable
fun CourseCatalogView(
  courses: List<Course>,
  enrolledCourseIds: Set<String>,
  completedLessonIds: Set<String>,
  selectedCategory: String,
  searchQuery: String,
  onCategorySelected: (String) -> Unit,
  onSearchQueryChanged: (String) -> Unit,
  onCourseClick: (Course) -> Unit
) {
  val categories = listOf("All", "Class 10", "Class 12", "Foundation", "Coding")

  val filteredCourses = remember(courses, selectedCategory, searchQuery) {
    courses.filter { course ->
      val matchesCat = selectedCategory == "All" || course.category == selectedCategory
      val matchesQuery = searchQuery.isBlank() ||
        course.title.contains(searchQuery, ignoreCase = true) ||
        course.subtitle.contains(searchQuery, ignoreCase = true) ||
        course.description.contains(searchQuery, ignoreCase = true)
      matchesCat && matchesQuery
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Text(
      text = "All Academy Courses",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
    )

    // Search input
    OutlinedTextField(
      value = searchQuery,
      onValueChange = onSearchQueryChanged,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("course_search_input"),
      placeholder = { Text("Search topics, subjects, derivations...") },
      leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search") },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { onSearchQueryChanged("") }) {
            Icon(Icons.Default.Close, contentDescription = "Clear")
          }
        }
      },
      shape = RoundedCornerShape(12.dp),
      singleLine = true
    )

    // Category Chips
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(categories) { cat ->
        FilterChip(
          selected = selectedCategory == cat,
          onClick = { onCategorySelected(cat) },
          label = { Text(cat) },
          modifier = Modifier.testTag("filter_chip_$cat")
        )
      }
    }

    // Course List
    if (filteredCourses.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "No courses found matching '$searchQuery'",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    } else {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.weight(1f)
      ) {
        items(filteredCourses) { course ->
          val isEnrolled = enrolledCourseIds.contains(course.id)
          val totalLessons = course.modules.sumOf { it.lessons.size }
          val completedCount = course.modules.flatMap { it.lessons }.count { completedLessonIds.contains(it.id) }
          val progress = if (totalLessons > 0) completedCount.toFloat() / totalLessons.toFloat() else 0f

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onCourseClick(course) }
              .testTag("course_card_${course.id}"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder()
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
                    text = course.category,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                  )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = AcademyAccent,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${course.rating} (${course.enrolledCount.div(1000)}k+)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Text(
                text = course.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )

              Text(
                text = course.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
              )

              if (isEnrolled) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(
                      text = "Course Progress",
                      fontSize = 11.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                      text = "$completedCount / $totalLessons Completed",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = AcademySuccess
                    )
                  }
                  LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(6.dp)
                      .clip(RoundedCornerShape(3.dp)),
                    color = AcademySuccess
                  )
                }
              }

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "👨‍🏫 ${course.instructorName}",
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                  onClick = { onCourseClick(course) },
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = AcademyPrimary)
                ) {
                  Text(if (isEnrolled) "Continue" else "Explore")
                }
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
fun CourseDetailView(
  course: Course,
  isEnrolled: Boolean,
  completedLessonIds: Set<String>,
  onClose: () -> Unit,
  onToggleEnroll: () -> Unit,
  onOpenLesson: (Lesson) -> Unit,
  onShowCertificate: () -> Unit
) {
  BackHandler { onClose() }

  val totalLessons = course.modules.sumOf { it.lessons.size }
  val completedCount = course.modules.flatMap { it.lessons }.count { completedLessonIds.contains(it.id) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = course.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1
          )
        },
        navigationIcon = {
          IconButton(onClick = onClose) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
      // Course Header Card
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
              color = AcademyPrimary,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = course.category,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
            Text(
              text = "⭐ ${course.rating} Rating",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }

          Text(
            text = course.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
          )

          Text(
            text = course.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            Text(
              text = "📹 ${course.totalLectures} Lectures",
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
              text = "⏱️ ${course.durationHours}",
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
              text = "👥 ${course.enrolledCount} Students",
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
            )
          }

          // Enrollment / Certificate Button
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = onToggleEnroll,
              modifier = Modifier.weight(1f),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isEnrolled) AcademySecondary else AcademyPrimary
              )
            ) {
              Icon(
                imageVector = if (isEnrolled) Icons.Default.Check else Icons.Default.BookmarkAdd,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(if (isEnrolled) "Enrolled in Batch" else "Enroll for Free")
            }

            OutlinedButton(
              onClick = onShowCertificate,
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.WorkspacePremium, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Certificate")
            }
          }
        }
      }

      // SYLLABUS / MODULES LIST
      Text(
        text = "📖 Course Syllabus & Lectures",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )

      course.modules.forEach { module ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text(
              text = module.title,
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = AcademyPrimary
            )

            HorizontalDivider()

            module.lessons.forEach { lesson ->
              val isLessonDone = completedLessonIds.contains(lesson.id)
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { onOpenLesson(lesson) }
                  .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                      if (isLessonDone) AcademySuccess.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = if (isLessonDone) Icons.Default.Check else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = if (isLessonDone) AcademySuccess else AcademyPrimary,
                    modifier = Modifier.size(20.dp)
                  )
                }

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                  )
                  Text(
                    text = "${lesson.durationMinutes} mins • Video Lesson",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                Icon(
                  Icons.Default.ChevronRight,
                  contentDescription = "Open Lesson",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }
  }
}
