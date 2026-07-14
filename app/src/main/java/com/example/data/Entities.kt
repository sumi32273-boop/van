package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "রাইয়ান আহমেদ",
    val headline: String = "AI Developer & Futurist",
    val bio: String = "পেশাদার AI ডেভেলপার এবং প্রম্পট ইঞ্জিনিয়ার। ভবিষ্যতে AI প্রযুক্তিতে মাস্টার্স করতে চাই।",
    val avatarUrl: String = "",
    val streakDays: Int = 7,
    val totalHours: Int = 24,
    val level: String = "অ্যাডভান্সড লার্নার"
)

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // "AI ডেভেলপমেন্ট", "ভিডিও জেনারেশন", "ইমেজ জেনারেশন", "প্রম্পট ইঞ্জিনিয়ারিং", "AI টুলস"
    val description: String,
    val instructor: String,
    val duration: String,
    val youtubeId: String, // YouTube Video ID for course masterclass
    val thumbnailUrl: String,
    val isBookmarked: Boolean = false,
    val progressPercent: Int = 0,
    val lessonsCount: Int = 10
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val courseId: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val prompt: String,
    val response: String,
    val timestamp: Long = System.currentTimeMillis()
)
