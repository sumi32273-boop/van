package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.CourseEntity
import com.example.data.NoteEntity
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    courseId: String,
    viewModel: com.example.viewmodel.MainViewModel,
    onBack: () -> Unit
) {
    val courses by viewModel.allCourses.collectAsState()
    val course = courses.find { it.id == courseId }
    val notes by viewModel.getNotes(courseId).collectAsState(initial = emptyList())

    var newNoteText by remember { mutableStateOf("") }
    var showPlayerDialog by remember { mutableStateOf(false) }

    if (course == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepSpace),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = NeonCyan)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "মাস্টারক্লাস ডিটেইলস", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleBookmark(course.id, course.isBookmarked) }) {
                        Icon(
                            imageVector = if (course.isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (course.isBookmarked) NeonCyan else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepSpace)
            )
        },
        containerColor = DeepSpace
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Video Thumbnail & Player Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(SurfaceCard)
                ) {
                    AsyncImage(
                        model = course.thumbnailUrl,
                        contentDescription = course.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { showPlayerDialog = true },
                            modifier = Modifier
                                .size(64.dp)
                                .background(NeonCyan, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play Video",
                                tint = DeepSpace,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Surface(
                        color = DeepSpace.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = course.duration,
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonCyan,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Title & Instructor
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = NeonPurple.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = course.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonPurple,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Instructor", tint = TextSecondary, modifier = Modifier.size(18.dp))
                        Text(
                            text = "প্রধান প্রশিক্ষক: ${course.instructor}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Progress Bar Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "কোর্স অগ্রগতি", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(text = "${course.progressPercent}%", style = MaterialTheme.typography.bodyMedium, color = NeonCyan, fontWeight = FontWeight.Bold)
                        }
                        LinearProgressIndicator(
                            progress = { course.progressPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = NeonCyan,
                            trackColor = SurfaceDark
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = { viewModel.updateCourseProgress(course.id, 0) }) {
                                Text("০%", color = TextSecondary, fontSize = 12.sp)
                            }
                            TextButton(onClick = { viewModel.updateCourseProgress(course.id, 50) }) {
                                Text("৫০%", color = TextSecondary, fontSize = 12.sp)
                            }
                            TextButton(onClick = { viewModel.updateCourseProgress(course.id, 100) }) {
                                Text("১০০% সম্পন্ন", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Description
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "কোর্স বিবরণী",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = course.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            // Notes Section
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "আপনার নোটসমূহ (${notes.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    OutlinedTextField(
                        value = newNoteText,
                        onValueChange = { newNoteText = it },
                        placeholder = { Text("এই ক্লাসের গুরুত্বপূর্ণ পয়েন্ট নোট করুন...", color = TextSecondary) },
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.addNote(course.id, newNoteText)
                                newNoteText = ""
                            }) {
                                Icon(Icons.Default.Send, contentDescription = "Add Note", tint = NeonCyan)
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceCard,
                            unfocusedContainerColor = SurfaceCard,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            items(notes) { note ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = note.content,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.deleteNote(note.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = NeonPink)
                        }
                    }
                }
            }
        }
    }

    // Video Player Simulation Dialog
    if (showPlayerDialog) {
        AlertDialog(
            onDismissRequest = { showPlayerDialog = false },
            confirmButton = {
                TextButton(onClick = { showPlayerDialog = false }) {
                    Text("বন্ধ করুন", color = NeonCyan)
                }
            },
            title = { Text("YouTube মাস্টারক্লাস প্লেয়ার", color = Color.White) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = course.thumbnailUrl,
                            contentDescription = course.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Surface(
                            color = NeonCyan,
                            shape = CircleShape,
                            modifier = Modifier.size(50.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Playing", tint = DeepSpace)
                            }
                        }
                    }
                    Text(
                        text = "ইউটিউব থেকে স্ট্রিমিং হচ্ছে: ${course.title}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            },
            containerColor = SurfaceCard
        )
    }
}
