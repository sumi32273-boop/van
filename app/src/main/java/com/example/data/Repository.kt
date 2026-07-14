package com.example.data

import kotlinx.coroutines.flow.Flow

class MasterclassRepository(
    private val userDao: UserDao,
    private val courseDao: CourseDao,
    private val noteDao: NoteDao,
    private val chatDao: ChatDao
) {
    val userProfile: Flow<UserProfileEntity?> = userDao.getUserProfile()
    val allCourses: Flow<List<CourseEntity>> = courseDao.getAllCourses()
    val bookmarkedCourses: Flow<List<CourseEntity>> = courseDao.getBookmarkedCourses()
    val chatMessages: Flow<List<ChatMessageEntity>> = chatDao.getAllChatMessages()

    suspend fun updateUserProfile(profile: UserProfileEntity) {
        userDao.insertUserProfile(profile)
    }

    suspend fun getCourseById(courseId: String): CourseEntity? {
        return courseDao.getCourseById(courseId)
    }

    suspend fun setBookmark(courseId: String, isBookmarked: Boolean) {
        courseDao.updateBookmark(courseId, isBookmarked)
    }

    suspend fun updateProgress(courseId: String, progress: Int) {
        courseDao.updateProgress(courseId, progress)
    }

    fun getNotes(courseId: String): Flow<List<NoteEntity>> {
        return noteDao.getNotesForCourse(courseId)
    }

    suspend fun addNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(noteId: Long) {
        noteDao.deleteNote(noteId)
    }

    suspend fun addChatMessage(message: ChatMessageEntity) {
        chatDao.insertChatMessage(message)
    }

    suspend fun clearChat() {
        chatDao.clearChat()
    }
}
