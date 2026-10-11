package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.AcademyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppNavTab(val label: String) {
  HOME("Home"),
  COURSES("Courses"),
  TESTS("Tests"),
  NOTES("Notes"),
  DOUBTS("Doubts"),
  PROFILE("Profile")
}

data class ActiveQuizState(
  val quiz: Quiz,
  val currentQuestionIndex: Int = 0,
  val selectedAnswers: Map<Int, Int> = emptyMap(), // questionId -> optionIndex
  val remainingSeconds: Int = 300,
  val isSubmitted: Boolean = false,
  val score: Int = 0,
  val totalQuestions: Int = 0
)

class AcademyViewModel(
  private val repository: AcademyRepository = AcademyRepository()
) : ViewModel() {

  val studentProfile = repository.studentProfile
  val enrolledCourseIds = repository.enrolledCourseIds
  val completedLessonIds = repository.completedLessonIds
  val downloadedMaterialIds = repository.downloadedMaterialIds
  val bookmarkedLessonIds = repository.bookmarkedLessonIds
  val doubts = repository.doubts
  val notices = repository.notices
  val liveClasses = repository.liveClasses
  val courses = repository.courses
  val quizzes = repository.quizzes
  val studyMaterials = repository.studyMaterials

  private val _currentTab = MutableStateFlow(AppNavTab.HOME)
  val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

  // Selected course for detail screen
  private val _selectedCourse = MutableStateFlow<Course?>(null)
  val selectedCourse: StateFlow<Course?> = _selectedCourse.asStateFlow()

  // Active video lesson being watched
  private val _activeLesson = MutableStateFlow<Lesson?>(null)
  val activeLesson: StateFlow<Lesson?> = _activeLesson.asStateFlow()

  // Active quiz session
  private val _activeQuizState = MutableStateFlow<ActiveQuizState?>(null)
  val activeQuizState: StateFlow<ActiveQuizState?> = _activeQuizState.asStateFlow()

  // Selected study material for document preview modal
  private val _selectedMaterial = MutableStateFlow<StudyMaterial?>(null)
  val selectedMaterial: StateFlow<StudyMaterial?> = _selectedMaterial.asStateFlow()

  // Certificate modal
  private val _showingCertificateForCourse = MutableStateFlow<Course?>(null)
  val showingCertificateForCourse: StateFlow<Course?> = _showingCertificateForCourse.asStateFlow()

  // Course search / category filter
  private val _selectedCourseCategory = MutableStateFlow("All")
  val selectedCourseCategory: StateFlow<String> = _selectedCourseCategory.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  // Snackbar message
  private val _snackbarMessage = MutableSharedFlow<String>()
  val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

  private var quizTimerJob: Job? = null

  fun setTab(tab: AppNavTab) {
    _currentTab.value = tab
    _selectedCourse.value = null
    _selectedMaterial.value = null
  }

  fun setCourseCategory(category: String) {
    _selectedCourseCategory.value = category
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun openCourse(course: Course) {
    _selectedCourse.value = course
  }

  fun closeCourseDetail() {
    _selectedCourse.value = null
  }

  fun openLesson(lesson: Lesson) {
    _activeLesson.value = lesson
  }

  fun closeVideoPlayer() {
    _activeLesson.value = null
  }

  fun toggleEnrollCourse(courseId: String) {
    repository.toggleEnrollCourse(courseId)
    viewModelScope.launch {
      val isNowEnrolled = repository.enrolledCourseIds.value.contains(courseId)
      _snackbarMessage.emit(
        if (isNowEnrolled) "Enrolled in course successfully! 🎉" else "Course removed from enrolled"
      )
    }
  }

  fun toggleLessonCompleted(lessonId: String) {
    repository.toggleLessonCompleted(lessonId)
    viewModelScope.launch {
      _snackbarMessage.emit("Lesson progress saved! +25 Academy XP 🌟")
    }
  }

  fun toggleBookmarkLesson(lessonId: String) {
    repository.toggleBookmarkLesson(lessonId)
  }

  fun openMaterial(material: StudyMaterial) {
    _selectedMaterial.value = material
  }

  fun closeMaterial() {
    _selectedMaterial.value = null
  }

  fun toggleDownloadMaterial(materialId: String) {
    repository.toggleMaterialDownload(materialId)
    viewModelScope.launch {
      val isDownloaded = repository.downloadedMaterialIds.value.contains(materialId)
      _snackbarMessage.emit(
        if (isDownloaded) "Downloaded for offline study! 📥" else "Removed from device storage"
      )
    }
  }

  fun startQuiz(quiz: Quiz) {
    quizTimerJob?.cancel()
    _activeQuizState.value = ActiveQuizState(
      quiz = quiz,
      currentQuestionIndex = 0,
      selectedAnswers = emptyMap(),
      remainingSeconds = quiz.durationMinutes * 60,
      isSubmitted = false,
      score = 0,
      totalQuestions = quiz.questions.size
    )

    quizTimerJob = viewModelScope.launch {
      while (_activeQuizState.value?.isSubmitted == false &&
        (_activeQuizState.value?.remainingSeconds ?: 0) > 0
      ) {
        delay(1000)
        _activeQuizState.update { state ->
          state?.copy(remainingSeconds = state.remainingSeconds - 1)
        }
      }
      if (_activeQuizState.value?.isSubmitted == false &&
        (_activeQuizState.value?.remainingSeconds ?: 0) <= 0
      ) {
        submitQuiz()
      }
    }
  }

  fun answerQuizQuestion(questionId: Int, optionIndex: Int) {
    _activeQuizState.update { state ->
      state?.let {
        val updatedAnswers = it.selectedAnswers.toMutableMap()
        updatedAnswers[questionId] = optionIndex
        it.copy(selectedAnswers = updatedAnswers)
      }
    }
  }

  fun navigateQuizQuestion(index: Int) {
    _activeQuizState.update { state ->
      state?.copy(currentQuestionIndex = index)
    }
  }

  fun submitQuiz() {
    quizTimerJob?.cancel()
    _activeQuizState.update { state ->
      state?.let { s ->
        var calculatedScore = 0
        s.quiz.questions.forEach { q ->
          val chosen = s.selectedAnswers[q.id]
          if (chosen == q.correctOptionIndex) {
            calculatedScore++
          }
        }
        repository.recordQuizCompletion(calculatedScore, s.quiz.questions.size)
        s.copy(
          isSubmitted = true,
          score = calculatedScore
        )
      }
    }
    viewModelScope.launch {
      _snackbarMessage.emit("Quiz submitted! Check your score & solutions below 🏆")
    }
  }

  fun closeQuiz() {
    quizTimerJob?.cancel()
    _activeQuizState.value = null
  }

  fun showCertificate(course: Course) {
    _showingCertificateForCourse.value = course
  }

  fun closeCertificate() {
    _showingCertificateForCourse.value = null
  }

  fun postDoubt(studentClass: String, subject: String, question: String) {
    repository.postDoubt(
      studentName = studentProfile.value.name,
      studentClass = studentClass,
      subject = subject,
      question = question
    )
    viewModelScope.launch {
      _snackbarMessage.emit("Your doubt has been submitted to Deepak Sir! 💬")
    }
  }

  fun upvoteDoubt(id: String) {
    repository.upvoteDoubt(id)
  }
}
