package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.network.askGeminiMentor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = MasterclassRepository(
        database.userDao(),
        database.courseDao(),
        database.noteDao(),
        database.chatDao()
    )

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allCourses: StateFlow<List<CourseEntity>> = repository.allCourses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarkedCourses: StateFlow<List<CourseEntity>> = repository.bookmarkedCourses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCategory = MutableStateFlow("সব")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleBookmark(courseId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.setBookmark(courseId, !currentStatus)
        }
    }

    fun updateCourseProgress(courseId: String, progress: Int) {
        viewModelScope.launch {
            repository.updateProgress(courseId, progress)
        }
    }

    fun updateProfile(name: String, headline: String, bio: String) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfileEntity()
            repository.updateUserProfile(
                current.copy(name = name, headline = headline, bio = bio)
            )
        }
    }

    fun getNotes(courseId: String): Flow<List<NoteEntity>> {
        return repository.getNotes(courseId)
    }

    fun addNote(courseId: String, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.addNote(NoteEntity(courseId = courseId, content = content))
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    fun sendAiMessage(prompt: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            repository.addChatMessage(ChatMessageEntity(prompt = prompt, response = ""))
            _isAiLoading.value = true
            val reply = askGeminiMentor(prompt)
            repository.addChatMessage(ChatMessageEntity(prompt = prompt, response = reply))
            _isAiLoading.value = false
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }
}
