package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import com.example.ui.viewmodel.AppNavTab

data class BottomNavItem(
  val tab: AppNavTab,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val label: String
)

@Composable
fun AcademyBottomBar(
  currentTab: AppNavTab,
  onTabSelected: (AppNavTab) -> Unit
) {
  val items = listOf(
    BottomNavItem(
      tab = AppNavTab.HOME,
      selectedIcon = Icons.Filled.Home,
      unselectedIcon = Icons.Outlined.Home,
      label = "Home"
    ),
    BottomNavItem(
      tab = AppNavTab.COURSES,
      selectedIcon = Icons.Filled.PlayCircle,
      unselectedIcon = Icons.Outlined.PlayCircleOutline,
      label = "Courses"
    ),
    BottomNavItem(
      tab = AppNavTab.TESTS,
      selectedIcon = Icons.Filled.Assignment,
      unselectedIcon = Icons.Outlined.Assignment,
      label = "Tests"
    ),
    BottomNavItem(
      tab = AppNavTab.NOTES,
      selectedIcon = Icons.Filled.Description,
      unselectedIcon = Icons.Outlined.Description,
      label = "Notes"
    ),
    BottomNavItem(
      tab = AppNavTab.DOUBTS,
      selectedIcon = Icons.Filled.QuestionAnswer,
      unselectedIcon = Icons.Outlined.QuestionAnswer,
      label = "Doubts"
    ),
    BottomNavItem(
      tab = AppNavTab.PROFILE,
      selectedIcon = Icons.Filled.Person,
      unselectedIcon = Icons.Outlined.Person,
      label = "Profile"
    )
  )

  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = NavigationBarDefaults.Elevation
  ) {
    items.forEach { item ->
      val selected = currentTab == item.tab
      NavigationBarItem(
        icon = {
          Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.label
          )
        },
        label = { Text(item.label) },
        selected = selected,
        onClick = { onTabSelected(item.tab) },
        modifier = Modifier.testTag("nav_${item.tab.name.lowercase()}")
      )
    }
  }
}
