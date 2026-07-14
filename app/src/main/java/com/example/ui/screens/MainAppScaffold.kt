package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@Composable
fun MainAppScaffold(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val userProfile by viewModel.userProfile.collectAsState()
    val courses by viewModel.allCourses.collectAsState()
    val bookmarkedCourses by viewModel.bookmarkedCourses.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    val showBottomBar = currentRoute in listOf("home", "courses", "ai_mentor", "profile")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = SurfaceDark,
                    tonalElevation = 8.dp,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    NavigationBarItem(
                        icon = { Icon(if (currentRoute == "home") Icons.Default.Home else Icons.Outlined.Home, contentDescription = "Home") },
                        label = { Text("হোম") },
                        selected = currentRoute == "home",
                        onClick = {
                            if (currentRoute != "home") {
                                navController.navigate("home") { popUpTo("home") { inclusive = true } }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonCyan,
                            unselectedIconColor = TextSecondary,
                            selectedTextColor = NeonCyan,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = SurfaceCard
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(if (currentRoute == "courses") Icons.Default.PlayLesson else Icons.Outlined.PlayLesson, contentDescription = "Courses") },
                        label = { Text("কোর্স") },
                        selected = currentRoute == "courses",
                        onClick = {
                            if (currentRoute != "courses") {
                                navController.navigate("courses") { popUpTo("home") }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonCyan,
                            unselectedIconColor = TextSecondary,
                            selectedTextColor = NeonCyan,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = SurfaceCard
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(if (currentRoute == "ai_mentor") Icons.Default.SmartToy else Icons.Outlined.SmartToy, contentDescription = "AI Mentor") },
                        label = { Text("AI মেন্টর") },
                        selected = currentRoute == "ai_mentor",
                        onClick = {
                            if (currentRoute != "ai_mentor") {
                                navController.navigate("ai_mentor") { popUpTo("home") }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonCyan,
                            unselectedIconColor = TextSecondary,
                            selectedTextColor = NeonCyan,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = SurfaceCard
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(if (currentRoute == "profile") Icons.Default.Person else Icons.Outlined.Person, contentDescription = "Profile") },
                        label = { Text("প্রোফাইল") },
                        selected = currentRoute == "profile",
                        onClick = {
                            if (currentRoute != "profile") {
                                navController.navigate("profile") { popUpTo("home") }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonCyan,
                            unselectedIconColor = TextSecondary,
                            selectedTextColor = NeonCyan,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = SurfaceCard
                        )
                    )
                }
            }
        },
        containerColor = DeepSpace
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(
                        userProfile = userProfile,
                        courses = courses,
                        onCourseClick = { courseId -> navController.navigate("course_detail/$courseId") },
                        onNavigateToCourses = { navController.navigate("courses") },
                        onNavigateToAiMentor = { navController.navigate("ai_mentor") }
                    )
                }
                composable("courses") {
                    CoursesScreen(
                        courses = courses,
                        selectedCategory = selectedCategory,
                        searchQuery = searchQuery,
                        onCategorySelected = { viewModel.selectCategory(it) },
                        onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
                        onToggleBookmark = { id, bookmarked -> viewModel.toggleBookmark(id, bookmarked) },
                        onCourseClick = { courseId -> navController.navigate("course_detail/$courseId") }
                    )
                }
                composable("ai_mentor") {
                    AiMentorScreen(
                        chatMessages = chatMessages,
                        isAiLoading = isAiLoading,
                        onSendMessage = { viewModel.sendAiMessage(it) },
                        onClearChat = { viewModel.clearChat() }
                    )
                }
                composable("profile") {
                    ProfileScreen(
                        userProfile = userProfile,
                        bookmarkedCourses = bookmarkedCourses,
                        onUpdateProfile = { name, headline, bio -> viewModel.updateProfile(name, headline, bio) },
                        onCourseClick = { courseId -> navController.navigate("course_detail/$courseId") }
                    )
                }
                composable(
                    route = "course_detail/{courseId}",
                    arguments = listOf(navArgument("courseId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                    CourseDetailScreen(
                        courseId = courseId,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
