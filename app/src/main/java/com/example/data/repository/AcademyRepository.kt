package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AcademyRepository {

  private val _studentProfile = MutableStateFlow(
    StudentProfile(
      name = "Deepak Kumar",
      email = "deepakmth95762@gmail.com",
      rollNumber = "DA-2026-108",
      targetExam = "Class 10th & 12th Board / Foundation",
      streakDays = 14,
      totalStudyHours = 52,
      testsCompleted = 14,
      averageAccuracy = 94,
      coins = 920
    )
  )
  val studentProfile: StateFlow<StudentProfile> = _studentProfile.asStateFlow()

  private val _enrolledCourseIds = MutableStateFlow<Set<String>>(setOf("course_math_10", "course_phys_12"))
  val enrolledCourseIds: StateFlow<Set<String>> = _enrolledCourseIds.asStateFlow()

  private val _completedLessonIds = MutableStateFlow<Set<String>>(
    setOf("m10_l1", "m10_l2", "p12_l1")
  )
  val completedLessonIds: StateFlow<Set<String>> = _completedLessonIds.asStateFlow()

  private val _downloadedMaterialIds = MutableStateFlow<Set<String>>(
    setOf("mat_trig_formula", "mat_phys_handbook")
  )
  val downloadedMaterialIds: StateFlow<Set<String>> = _downloadedMaterialIds.asStateFlow()

  private val _bookmarkedLessonIds = MutableStateFlow<Set<String>>(setOf("m10_l3"))
  val bookmarkedLessonIds: StateFlow<Set<String>> = _bookmarkedLessonIds.asStateFlow()

  private val _doubts = MutableStateFlow<List<Doubt>>(
    listOf(
      Doubt(
        id = "d1",
        studentName = "Rahul Verma",
        studentClass = "Class 10",
        subject = "Mathematics",
        questionText = "How do we prove root 5 is an irrational number by contradiction method?",
        timeAgo = "10 mins ago",
        isSolved = true,
        solutionText = "Assume √5 is rational, so √5 = a/b where a and b are coprime integers. Squaring both sides: 5 = a²/b² => 5b² = a². Thus 5 divides a² and hence 5 divides a. Substitute a = 5k to show 5 also divides b, contradicting coprimality!",
        solvedBy = "Er. Deepak Kumar (Founder)",
        upvotes = 34
      ),
      Doubt(
        id = "d2",
        studentName = "Priya Sharma",
        studentClass = "Class 12",
        subject = "Physics",
        questionText = "What is the difference between EMF and Terminal Potential Difference in a closed circuit?",
        timeAgo = "1 hour ago",
        isSolved = true,
        solutionText = "EMF (Electromotive Force) is the maximum potential difference across the battery terminals when NO current is drawn. Terminal PD (V = E - Ir) is the voltage when current I flows, reduced due to internal resistance r.",
        solvedBy = "Er. Deepak Kumar (Founder)",
        upvotes = 28
      ),
      Doubt(
        id = "d3",
        studentName = "Amit Patel",
        studentClass = "Class 10",
        subject = "Science",
        questionText = "Why do stars twinkle but planets do not?",
        timeAgo = "3 hours ago",
        isSolved = true,
        solutionText = "Stars are point-sized distant light sources, so atmospheric refraction causes continuous shifts in ray paths, making intensity vary. Planets are closer extended light sources acting as collections of point sources that cancel out flickering.",
        solvedBy = "Er. Deepak Kumar (Founder)",
        upvotes = 19
      ),
      Doubt(
        id = "d4",
        studentName = "Neha Gupta",
        studentClass = "Class 12",
        subject = "Mathematics",
        questionText = "When applying L'Hopital's rule, can we differentiate multiple times if indeterminate form persists?",
        timeAgo = "5 hours ago",
        isSolved = true,
        solutionText = "Yes! You can apply L'Hopital's rule repeatedly as long as the limit continues to evaluate to indeterminate 0/0 or ∞/∞, provided derivatives exist.",
        solvedBy = "Er. Deepak Kumar (Founder)",
        upvotes = 42
      )
    )
  )
  val doubts: StateFlow<List<Doubt>> = _doubts.asStateFlow()

  val notices: List<Notice> = listOf(
    Notice(
      id = "n1",
      title = "🔥 Deepak Academy Mega Scholarship Test (DAMST)",
      message = "Register now for DAMST 2026. Top 50 students will receive 100% scholarship on all master batches and personalized mentorship from Deepak Sir.",
      date = "15 Oct 2026",
      isUrgent = true,
      category = "Scholarship"
    ),
    Notice(
      id = "n2",
      title = "New Board Exam 95%+ Target Batch Commencing",
      message = "Excellence batch for Class 10 & 12 Board Aspirants starts next Monday. Includes 500+ solved previous year questions and chapter-wise mock tests.",
      date = "12 Oct 2026",
      isUrgent = false,
      category = "Batch Update"
    ),
    Notice(
      id = "n3",
      title = "Special Sunday Live Session with Deepak Sir",
      message = "Join live this Sunday at 11:00 AM for 'Memory Techniques & How to Score 100/100 in Maths & Science' interactive masterclass.",
      date = "11 Oct 2026",
      isUrgent = false,
      category = "Webinar"
    )
  )

  val liveClasses: List<LiveClass> = listOf(
    LiveClass(
      id = "live_1",
      title = "High-Score Calculus & Quadratic Mastery",
      subject = "Mathematics",
      instructor = "Er. Deepak Kumar",
      timeText = "LIVE NOW",
      isLiveNow = true,
      attendeeCount = 482,
      topicOverview = "Live step-by-step shortcuts, formula derivation, and previous 10-year question hacks with real-time doubt clearing."
    ),
    LiveClass(
      id = "live_2",
      title = "Ray Optics: Critical Diagrams & Numerical Hacks",
      subject = "Physics",
      instructor = "Er. Deepak Kumar",
      timeText = "Today, 5:30 PM",
      isLiveNow = false,
      attendeeCount = 310,
      topicOverview = "Mirror formula, Lens Maker formula, total internal reflection numericals and guaranteed 5-mark board derivations."
    ),
    LiveClass(
      id = "live_3",
      title = "Complete Chemical Bonding & Molecular Structure",
      subject = "Chemistry",
      instructor = "Dr. Ananya Sen",
      timeText = "Tomorrow, 6:00 PM",
      isLiveNow = false,
      attendeeCount = 260,
      topicOverview = "VSEPR theory, hybridization, dipole moments, and molecular orbital diagrams simplified."
    )
  )

  val courses: List<Course> = listOf(
    Course(
      id = "course_math_10",
      title = "Class 10th Maths Board Topper Masterclass",
      subtitle = "Zero to 100/100 Complete Board Prep with Er. Deepak Kumar",
      category = "Class 10",
      rating = 4.98f,
      enrolledCount = 18400,
      totalLectures = 24,
      completedLectures = 14,
      durationHours = "38 Hours",
      badge = "Bestseller",
      description = "Comprehensive masterclass covering Real Numbers, Polynomials, Linear Equations, Quadratic Equations, Arithmetic Progressions, Triangles, Coordinate Geometry, Trigonometry, and Statistics. Includes NCERT Line-by-Line breakdown, Exemplar questions, and 100+ solved PYQs.",
      modules = listOf(
        CourseModule(
          id = "mod_1",
          title = "Chapter 1: Real Numbers & Fundamental Theorem",
          lessons = listOf(
            Lesson(
              id = "m10_l1",
              title = "1. Fundamental Theorem of Arithmetic & Prime Factorization",
              durationMinutes = 42,
              isCompleted = true,
              summary = "Every composite number can be uniquely expressed as a product of primes, apart from the order in which the prime factors occur.",
              keyPoints = listOf("HCF(a, b) × LCM(a, b) = a × b", "Unique factorization theorem", "Standard board exam questions on finding HCF & LCM")
            ),
            Lesson(
              id = "m10_l2",
              title = "2. Proofs of Irrationality (√2, √3, √5)",
              durationMinutes = 48,
              isCompleted = true,
              summary = "Master the rigorous contradiction method for proving irrational numbers, a guaranteed 3-mark question in board exams.",
              keyPoints = listOf("Definition of Coprime numbers", "Contradiction hypothesis setup", "Common mistakes in board answers")
            ),
            Lesson(
              id = "m10_l3",
              title = "3. Decimals, Rational Fractions & Practice Quiz",
              durationMinutes = 35,
              isCompleted = false,
              summary = "Terminating vs Non-terminating recurring decimal expansions of p/q where q = 2^n * 5^m.",
              keyPoints = listOf("Denominator prime factor test", "Short tricks for quick identification", "PYQ 2020-2025 analysis")
            )
          )
        ),
        CourseModule(
          id = "mod_2",
          title = "Chapter 2: Polynomials & Quadratic Mastery",
          lessons = listOf(
            Lesson(
              id = "m10_l4",
              title = "4. Geometrical Meaning of Zeroes of a Polynomial",
              durationMinutes = 40,
              isCompleted = false,
              summary = "Number of zeroes equals number of intersection points on the X-axis for y = p(x).",
              keyPoints = listOf("Parabola opening upward vs downward", "At most n zeroes for degree n", "Board graph questions")
            ),
            Lesson(
              id = "m10_l5",
              title = "5. Relationship Between Zeroes and Coefficients",
              durationMinutes = 52,
              isCompleted = false,
              summary = "Sum of roots α + β = -b/a and product of roots αβ = c/a for ax² + bx + c.",
              keyPoints = listOf("Forming quadratic from roots: k[x² - (α+β)x + αβ]", "Symmetric expressions of roots: α² + β², 1/α + 1/β", "High-frequency exam problems")
            )
          )
        ),
        CourseModule(
          id = "mod_3",
          title = "Chapter 8: Introduction to Trigonometry & Applications",
          lessons = listOf(
            Lesson(
              id = "m10_l6",
              title = "6. Trigonometric Ratios & Values Table Made Easy",
              durationMinutes = 55,
              isCompleted = false,
              summary = "Never memorize blindly! Learn Deepak Sir's 30-second palm trick to write all trigonometric values effortlessly.",
              keyPoints = listOf("sin, cos, tan, cosec, sec, cot definitions", "Values at 0°, 30°, 45°, 60°, 90°", "Deepak Sir's Palm Trick memory mnemonic")
            ),
            Lesson(
              id = "m10_l7",
              title = "7. Trigonometric Identities & Proving Hacks",
              durationMinutes = 60,
              isCompleted = false,
              summary = "sin²θ + cos²θ = 1, 1 + tan²θ = sec²θ, 1 + cot²θ = cosec²θ. Complete step-by-step identity proofs.",
              keyPoints = listOf("Convert into sine and cosine first trick", "Rationalization method for root expressions", "5-mark standard questions")
            )
          )
        )
      )
    ),
    Course(
      id = "course_phys_12",
      title = "Class 12th Physics Excellence & Derivations Batch",
      subtitle = "Electrostatics, Optics, Magnetism & Modern Physics with Deepak Sir",
      category = "Class 12",
      rating = 4.95f,
      enrolledCount = 14200,
      totalLectures = 28,
      completedLectures = 8,
      durationHours = "45 Hours",
      badge = "Comprehensive",
      description = "Master all 35+ essential board exam derivations, conceptual reasoning, and numerical problem solving with Er. Deepak Kumar.",
      modules = listOf(
        CourseModule(
          id = "p_mod_1",
          title = "Unit 1: Electrostatics & Electric Potential",
          lessons = listOf(
            Lesson(
              id = "p12_l1",
              title = "1. Coulomb's Law, Vector Form & Principle of Superposition",
              durationMinutes = 50,
              isCompleted = true,
              summary = "Fundamental electric electrostatic forces between point charges in vacuum and medium.",
              keyPoints = listOf("Vector notation F₁₂ = -F₂₁", "Dielectric constant and relative permittivity", "Equilateral triangle charge equilibrium problems")
            ),
            Lesson(
              id = "p12_l2",
              title = "2. Electric Field on Axial & Equatorial Line of Dipole",
              durationMinutes = 55,
              isCompleted = false,
              summary = "Derivation of electric field intensity for short dipole: E_axial = 2kp/r³ and E_equatorial = kp/r³.",
              keyPoints = listOf("Step-by-step neat diagram marking", "Vector direction comparison", "Torque on dipole in uniform field: τ = p × E")
            ),
            Lesson(
              id = "p12_l3",
              title = "3. Gauss's Law & 3 Important Applications",
              durationMinutes = 62,
              isCompleted = false,
              summary = "Total electric flux through closed surface equals q_enclosed / ε₀. Deriving field due to infinitely long wire, plane sheet, and spherical shell.",
              keyPoints = listOf("Gaussian surface selection criteria", "Infinite wire: E = λ / (2πε₀r)", "Infinite thin sheet: E = σ / (2ε₀)")
            )
          )
        )
      )
    ),
    Course(
      id = "course_foundation",
      title = "Foundation Math & Science (Olympiad & NTSE)",
      subtitle = "Building Rock-Solid Conceptual Foundation for Class 8, 9 & 10",
      category = "Foundation",
      rating = 4.92f,
      enrolledCount = 8900,
      totalLectures = 18,
      completedLectures = 0,
      durationHours = "28 Hours",
      badge = "Advanced",
      description = "Advanced problem-solving techniques, mental mathematics, algebra speed tricks, and basic mechanics for Olympiad aspirants.",
      modules = listOf(
        CourseModule(
          id = "f_mod_1",
          title = "Speed Mathematics & Number Theory",
          lessons = listOf(
            Lesson(
              id = "f_l1",
              title = "1. Vedic Math Multiplication & Square Root Shortcuts",
              durationMinutes = 35,
              isCompleted = false,
              summary = "Calculate 3-digit multiplications and square roots mentally in under 5 seconds.",
              keyPoints = listOf("Base method multiplication", "Duplex method for square roots", "Speed drills")
            )
          )
        )
      )
    ),
    Course(
      id = "course_coding",
      title = "Python, Data Structures & Logic Building Bootcamp",
      subtitle = "From Beginner to Confident Programmer with Er. Deepak Kumar",
      category = "Coding",
      rating = 4.97f,
      enrolledCount = 6800,
      totalLectures = 20,
      completedLectures = 0,
      durationHours = "30 Hours",
      badge = "Trending",
      description = "Learn modern Python programming, algorithms, pattern printing, OOPs concepts, and build 5 real-world desktop and AI projects.",
      modules = listOf(
        CourseModule(
          id = "c_mod_1",
          title = "Python Basics & Algorithmic Thinking",
          lessons = listOf(
            Lesson(
              id = "c_l1",
              title = "1. Python Installation, Variables, Data Types & I/O",
              durationMinutes = 45,
              isCompleted = false,
              summary = "Set up Python environment and understand dynamic typing, strings, integers, lists, and user input.",
              keyPoints = listOf("Interactive shell vs scripts", "Type casting and f-strings", "Building first interactive calculator")
            )
          )
        )
      )
    )
  )

  val quizzes: List<Quiz> = listOf(
    Quiz(
      id = "quiz_math_speed",
      title = "Daily Quick Challenge: Quadratic & Trigonometry",
      subject = "Mathematics",
      targetClass = "Class 10 & 12",
      totalQuestions = 5,
      durationMinutes = 5,
      difficulty = "Medium",
      questions = listOf(
        QuizQuestion(
          id = 1,
          questionText = "If α and β are the zeroes of polynomial p(x) = x² - 5x + 6, what is the value of (1/α + 1/β)?",
          options = listOf("5/6", "6/5", "-5/6", "-6/5"),
          correctOptionIndex = 0,
          explanation = "α + β = -(-5)/1 = 5, and αβ = 6/1 = 6. Therefore, 1/α + 1/β = (α + β) / (αβ) = 5/6.",
          topic = "Polynomials"
        ),
        QuizQuestion(
          id = 2,
          questionText = "What is the value of (sin 30° + cos 60°) - tan 45°?",
          options = listOf("1", "0", "1/2", "√3/2"),
          correctOptionIndex = 1,
          explanation = "sin 30° = 1/2, cos 60° = 1/2, tan 45° = 1. So (1/2 + 1/2) - 1 = 1 - 1 = 0.",
          topic = "Trigonometry"
        ),
        QuizQuestion(
          id = 3,
          questionText = "The discriminant of the quadratic equation 2x² - 4x + 3 = 0 is:",
          options = listOf("8", "-8", "16", "0"),
          correctOptionIndex = 1,
          explanation = "D = b² - 4ac = (-4)² - 4(2)(3) = 16 - 24 = -8. Since D < 0, roots are non-real (imaginary).",
          topic = "Quadratic Equations"
        ),
        QuizQuestion(
          id = 4,
          questionText = "If HCF(306, 657) = 9, what is LCM(306, 657)?",
          options = listOf("22338", "21450", "22500", "20998"),
          correctOptionIndex = 0,
          explanation = "LCM(a, b) = (a × b) / HCF(a, b) = (306 × 657) / 9 = 34 × 657 = 22,338.",
          topic = "Real Numbers"
        ),
        QuizQuestion(
          id = 5,
          questionText = "Which term of the AP: 3, 8, 13, 18, ... is 78?",
          options = listOf("15th", "16th", "17th", "18th"),
          correctOptionIndex = 1,
          explanation = "a = 3, d = 8 - 3 = 5. a_n = a + (n - 1)d => 78 = 3 + (n - 1)5 => 75 = 5(n - 1) => n - 1 = 15 => n = 16.",
          topic = "Arithmetic Progressions"
        )
      )
    ),
    Quiz(
      id = "quiz_phys_mock",
      title = "Physics Board Special: Electrostatics & Current",
      subject = "Physics",
      targetClass = "Class 12",
      totalQuestions = 4,
      durationMinutes = 6,
      difficulty = "Hard",
      questions = listOf(
        QuizQuestion(
          id = 1,
          questionText = "Two point charges +q and -q are separated by distance 2a. What is the electric potential at any point on its equatorial plane?",
          options = listOf("Zero", "kq/a", "2kq/a²", "Depends on distance from center"),
          correctOptionIndex = 0,
          explanation = "At any point on the equatorial plane, the distance from +q and -q is equal. Potential V = kq/r + k(-q)/r = 0.",
          topic = "Electric Potential"
        ),
        QuizQuestion(
          id = 2,
          questionText = "SI unit of electric flux is:",
          options = listOf("N m² C⁻¹ (or V·m)", "N C⁻¹", "N m C⁻¹", "V m⁻¹"),
          correctOptionIndex = 0,
          explanation = "Φ = E · A = (N/C) × m² = N m² C⁻¹. Also since E is in V/m, Φ = (V/m) × m² = V·m.",
          topic = "Gauss's Law"
        ),
        QuizQuestion(
          id = 3,
          questionText = "If a wire of resistance R is stretched uniformly to double its original length, its new resistance becomes:",
          options = listOf("2R", "4R", "R/2", "R/4"),
          correctOptionIndex = 1,
          explanation = "Volume remains constant: A'L' = AL => L' = 2L so A' = A/2. New resistance R' = ρ(2L)/(A/2) = 4(ρL/A) = 4R.",
          topic = "Current Electricity"
        ),
        QuizQuestion(
          id = 4,
          questionText = "A capacitor of 10 μF is charged to 100 V. The energy stored in the capacitor is:",
          options = listOf("0.05 J", "0.5 J", "5 J", "0.005 J"),
          correctOptionIndex = 0,
          explanation = "U = (1/2)CV² = 0.5 × (10 × 10⁻⁶) × (100)² = 0.5 × 10⁻⁵ × 10000 = 0.05 J.",
          topic = "Capacitance"
        )
      )
    )
  )

  val studyMaterials: List<StudyMaterial> = listOf(
    StudyMaterial(
      id = "mat_trig_formula",
      title = "Trigonometry Super Formula & Cheat Sheet",
      subject = "Mathematics",
      classStandard = "Class 10 & 12",
      type = "Formula Sheet",
      pages = 4,
      fileSizeMb = 1.4,
      isDownloaded = true,
      contentPreview = "Complete table of Ratios, Reciprocal identities, Pythagorean identities, Sum-Product rules, Values at 0°, 30°, 45°, 60°, 90°, and Deepak Sir's palm angle mnemonics."
    ),
    StudyMaterial(
      id = "mat_phys_handbook",
      title = "Physics 35 Essential Derivations Hand-Written Notes",
      subject = "Physics",
      classStandard = "Class 12",
      type = "PDF Notes",
      pages = 18,
      fileSizeMb = 4.8,
      isDownloaded = true,
      contentPreview = "Neat hand-written notes by Er. Deepak Kumar highlighting exact step-by-step board marking scheme derivations for Electrostatics, Magnetism, and Wave Optics."
    ),
    StudyMaterial(
      id = "mat_chem_pyq",
      title = "Class 10 Science 10-Year Solved Board PYQs",
      subject = "Science",
      classStandard = "Class 10",
      type = "PYQ Papers",
      pages = 32,
      fileSizeMb = 6.2,
      isDownloaded = false,
      contentPreview = "Chapter-wise sorted questions from CBSE & State Boards 2015-2025 with model answers, common mistakes alerts, and marking keys."
    ),
    StudyMaterial(
      id = "mat_py_guide",
      title = "Python 100 Code Recipes & Algorithm Handbook",
      subject = "Coding",
      classStandard = "Foundation & College",
      type = "Coding Guide",
      pages = 22,
      fileSizeMb = 3.1,
      isDownloaded = false,
      contentPreview = "Practical code snippets: Palindrome check, Prime sieve, Fibonacci generator, Bubble/Merge sort, File I/O, OOP classes, and mini-projects."
    )
  )

  fun toggleEnrollCourse(courseId: String) {
    _enrolledCourseIds.update { set ->
      if (set.contains(courseId)) set - courseId else set + courseId
    }
  }

  fun toggleLessonCompleted(lessonId: String) {
    _completedLessonIds.update { set ->
      val updated = if (set.contains(lessonId)) set - lessonId else set + lessonId
      // update study hours and coins
      if (updated.contains(lessonId)) {
        _studentProfile.update {
          it.copy(
            totalStudyHours = it.totalStudyHours + 1,
            coins = it.coins + 25
          )
        }
      }
      updated
    }
  }

  fun toggleBookmarkLesson(lessonId: String) {
    _bookmarkedLessonIds.update { set ->
      if (set.contains(lessonId)) set - lessonId else set + lessonId
    }
  }

  fun toggleMaterialDownload(materialId: String) {
    _downloadedMaterialIds.update { set ->
      if (set.contains(materialId)) set - materialId else set + materialId
    }
  }

  fun recordQuizCompletion(score: Int, total: Int) {
    _studentProfile.update {
      val newTests = it.testsCompleted + 1
      val newCoins = it.coins + (score * 20)
      it.copy(
        testsCompleted = newTests,
        coins = newCoins
      )
    }
  }

  fun postDoubt(studentName: String, studentClass: String, subject: String, question: String) {
    val newDoubt = Doubt(
      id = "d_${System.currentTimeMillis()}",
      studentName = studentName.ifBlank { "Deepak Student" },
      studentClass = studentClass,
      subject = subject,
      questionText = question,
      timeAgo = "Just now",
      isSolved = true,
      solutionText = "Quick Solution from Deepak Sir: Great query! Review our Chapter formula notes or check definition: remember to test boundary conditions first. Detailed video solution will be addressed in upcoming Sunday live session!",
      solvedBy = "Er. Deepak Kumar (Founder)",
      upvotes = 1
    )
    _doubts.update { listOf(newDoubt) + it }
  }

  fun upvoteDoubt(doubtId: String) {
    _doubts.update { list ->
      list.map {
        if (it.id == doubtId) it.copy(upvotes = it.upvotes + 1) else it
      }
    }
  }
}
