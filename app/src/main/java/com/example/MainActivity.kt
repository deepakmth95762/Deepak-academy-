package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.DeepakAcademyTheme
import com.example.ui.viewmodel.AcademyViewModel
import com.example.ui.viewmodel.AppNavTab
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      DeepakAcademyTheme {
        DeepakAcademyApp()
      }
    }
  }
}

@Composable
fun DeepakAcademyApp(
  viewModel: AcademyViewModel = viewModel()
) {
  val snackbarHostState = remember { SnackbarHostState() }

  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val studentProfile by viewModel.studentProfile.collectAsStateWithLifecycle()
  val courses = viewModel.courses
  val enrolledCourseIds by viewModel.enrolledCourseIds.collectAsStateWithLifecycle()
  val completedLessonIds by viewModel.completedLessonIds.collectAsStateWithLifecycle()
  val downloadedMaterialIds by viewModel.downloadedMaterialIds.collectAsStateWithLifecycle()
  val doubts by viewModel.doubts.collectAsStateWithLifecycle()
  val notices = viewModel.notices
  val liveClasses = viewModel.liveClasses
  val quizzes = viewModel.quizzes
  val studyMaterials = viewModel.studyMaterials

  val selectedCourse by viewModel.selectedCourse.collectAsStateWithLifecycle()
  val activeLesson by viewModel.activeLesson.collectAsStateWithLifecycle()
  val activeQuizState by viewModel.activeQuizState.collectAsStateWithLifecycle()
  val selectedMaterial by viewModel.selectedMaterial.collectAsStateWithLifecycle()
  val showingCertificate by viewModel.showingCertificateForCourse.collectAsStateWithLifecycle()

  val selectedCategory by viewModel.selectedCourseCategory.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

  var showAskDoubtDialog by remember { mutableStateOf(false) }

  // Listen for snackbar notifications
  LaunchedEffect(Unit) {
    viewModel.snackbarMessage.collectLatest { msg ->
      snackbarHostState.showSnackbar(msg)
    }
  }

  // Handle hardware / predictive back press
  if (currentTab != AppNavTab.HOME && activeLesson == null && selectedCourse == null && activeQuizState == null && selectedMaterial == null) {
    BackHandler {
      viewModel.setTab(AppNavTab.HOME)
    }
  }

  // Render Full-Screen Overlays first if active
  if (activeLesson != null) {
    val currentLesson = activeLesson!!
    val courseTitle = selectedCourse?.title ?: "Deepak Academy Batch"
    val isCompleted = completedLessonIds.contains(currentLesson.id)

    VideoPlayerView(
      lesson = currentLesson,
      courseTitle = courseTitle,
      isCompleted = isCompleted,
      onToggleComplete = { viewModel.toggleLessonCompleted(currentLesson.id) },
      onClose = { viewModel.closeVideoPlayer() },
      onAskDoubt = {
        viewModel.closeVideoPlayer()
        viewModel.setTab(AppNavTab.DOUBTS)
      }
    )
    return
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
      AcademyTopBar(
        streakDays = studentProfile.streakDays,
        coins = studentProfile.coins,
        onNotificationsClick = {
          viewModel.setTab(AppNavTab.HOME)
        },
        onProfileClick = {
          viewModel.setTab(AppNavTab.PROFILE)
        }
      )
    },
    bottomBar = {
      AcademyBottomBar(
        currentTab = currentTab,
        onTabSelected = { tab -> viewModel.setTab(tab) }
      )
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentTab) {
        AppNavTab.HOME -> {
          HomeScreen(
            profile = studentProfile,
            notices = notices,
            liveClasses = liveClasses,
            courses = courses,
            enrolledCourseIds = enrolledCourseIds,
            onSelectTab = { tab -> viewModel.setTab(tab) },
            onOpenCourse = { course ->
              viewModel.setTab(AppNavTab.COURSES)
              viewModel.openCourse(course)
            },
            onOpenLiveClass = { liveClass ->
              // Map to first course / lesson for streaming
              courses.firstOrNull()?.let { course ->
                course.modules.firstOrNull()?.lessons?.firstOrNull()?.let { lesson ->
                  viewModel.openCourse(course)
                  viewModel.openLesson(lesson)
                }
              }
            },
            onOpenDoubtDialog = { showAskDoubtDialog = true }
          )
        }

        AppNavTab.COURSES -> {
          CoursesScreen(
            courses = courses,
            enrolledCourseIds = enrolledCourseIds,
            completedLessonIds = completedLessonIds,
            selectedCourse = selectedCourse,
            selectedCategory = selectedCategory,
            searchQuery = searchQuery,
            onCategorySelected = { viewModel.setCourseCategory(it) },
            onSearchQueryChanged = { viewModel.setSearchQuery(it) },
            onCourseClick = { viewModel.openCourse(it) },
            onCloseCourseDetail = { viewModel.closeCourseDetail() },
            onToggleEnroll = { viewModel.toggleEnrollCourse(it) },
            onOpenLesson = { viewModel.openLesson(it) },
            onShowCertificate = { viewModel.showCertificate(it) }
          )
        }

        AppNavTab.TESTS -> {
          TestSeriesScreen(
            quizzes = quizzes,
            activeQuizState = activeQuizState,
            onStartQuiz = { viewModel.startQuiz(it) },
            onAnswerQuestion = { qId, optIdx -> viewModel.answerQuizQuestion(qId, optIdx) },
            onNavigateQuestion = { viewModel.navigateQuizQuestion(it) },
            onSubmitQuiz = { viewModel.submitQuiz() },
            onCloseQuiz = { viewModel.closeQuiz() }
          )
        }

        AppNavTab.NOTES -> {
          StudyNotesScreen(
            studyMaterials = studyMaterials,
            downloadedMaterialIds = downloadedMaterialIds,
            selectedMaterial = selectedMaterial,
            onOpenMaterial = { viewModel.openMaterial(it) },
            onCloseMaterial = { viewModel.closeMaterial() },
            onToggleDownload = { viewModel.toggleDownloadMaterial(it) }
          )
        }

        AppNavTab.DOUBTS -> {
          DoubtsScreen(
            doubts = doubts,
            onPostDoubt = { cls, sub, q -> viewModel.postDoubt(cls, sub, q) },
            onUpvote = { viewModel.upvoteDoubt(it) }
          )
        }

        AppNavTab.PROFILE -> {
          ProfileScreen(
            profile = studentProfile,
            courses = courses,
            enrolledCourseIds = enrolledCourseIds,
            downloadedMaterialIds = downloadedMaterialIds,
            studyMaterials = studyMaterials,
            onOpenCourse = { course ->
              viewModel.setTab(AppNavTab.COURSES)
              viewModel.openCourse(course)
            },
            onShowCertificate = { viewModel.showCertificate(it) }
          )
        }
      }

      // Dialogs
      if (showAskDoubtDialog) {
        AskDoubtDialog(
          onDismiss = { showAskDoubtDialog = false },
          onSubmit = { cls, sub, q ->
            viewModel.postDoubt(cls, sub, q)
            showAskDoubtDialog = false
          }
        )
      }

      if (showingCertificate != null) {
        CertificateDialog(
          studentName = studentProfile.name,
          course = showingCertificate!!,
          onDismiss = { viewModel.closeCertificate() },
          onDownload = {
            viewModel.closeCertificate()
          }
        )
      }
    }
  }
}
