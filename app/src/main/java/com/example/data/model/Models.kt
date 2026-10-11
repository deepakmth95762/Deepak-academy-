package com.example.data.model

data class Course(
  val id: String,
  val title: String,
  val subtitle: String,
  val category: String,
  val instructorName: String = "Er. Deepak Kumar",
  val instructorRole: String = "Lead Educator & Mentor",
  val rating: Float = 4.9f,
  val enrolledCount: Int = 12450,
  val totalLectures: Int,
  val completedLectures: Int = 0,
  val durationHours: String,
  val badge: String? = null,
  val description: String,
  val modules: List<CourseModule>
)

data class CourseModule(
  val id: String,
  val title: String,
  val lessons: List<Lesson>
)

data class Lesson(
  val id: String,
  val title: String,
  val durationMinutes: Int,
  val isCompleted: Boolean = false,
  val isLocked: Boolean = false,
  val summary: String,
  val keyPoints: List<String>
)

data class LiveClass(
  val id: String,
  val title: String,
  val subject: String,
  val instructor: String = "Er. Deepak Kumar",
  val timeText: String,
  val isLiveNow: Boolean = false,
  val attendeeCount: Int = 340,
  val topicOverview: String
)

data class Quiz(
  val id: String,
  val title: String,
  val subject: String,
  val targetClass: String,
  val totalQuestions: Int,
  val durationMinutes: Int,
  val difficulty: String, // Easy, Medium, Hard
  val questions: List<QuizQuestion>
)

data class QuizQuestion(
  val id: Int,
  val questionText: String,
  val options: List<String>,
  val correctOptionIndex: Int,
  val explanation: String,
  val topic: String
)

data class QuizResult(
  val quizId: String,
  val quizTitle: String,
  val score: Int,
  val totalQuestions: Int,
  val userAnswers: Map<Int, Int>,
  val timeTakenSeconds: Int,
  val accuracyPercentage: Int
)

data class StudyMaterial(
  val id: String,
  val title: String,
  val subject: String,
  val classStandard: String,
  val type: String, // PDF Notes, Formula Sheet, PYQ (Previous Year), Mind Map
  val pages: Int,
  val fileSizeMb: Double,
  val isDownloaded: Boolean = false,
  val contentPreview: String
)

data class Doubt(
  val id: String,
  val studentName: String,
  val studentClass: String,
  val subject: String,
  val questionText: String,
  val timeAgo: String,
  val isSolved: Boolean = false,
  val solutionText: String? = null,
  val solvedBy: String? = null,
  val upvotes: Int = 0
)

data class Notice(
  val id: String,
  val title: String,
  val message: String,
  val date: String,
  val isUrgent: Boolean = false,
  val category: String
)

data class StudentProfile(
  val name: String = "Deepak Student",
  val email: String = "deepakmth95762@gmail.com",
  val rollNumber: String = "DA-2026-108",
  val targetExam: String = "Class 10th & 12th Board / Foundation",
  val streakDays: Int = 14,
  val totalStudyHours: Int = 48,
  val testsCompleted: Int = 12,
  val averageAccuracy: Int = 92,
  val coins: Int = 850
)
