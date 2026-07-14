package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UserProfileEntity::class, CourseEntity::class, NoteEntity::class, ChatMessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun courseDao(): CourseDao
    abstract fun noteDao(): NoteDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ai_masterclass_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(database: AppDatabase) {
            database.userDao().insertUserProfile(UserProfileEntity())

            val initialCourses = listOf(
                CourseEntity(
                    id = "c1",
                    title = "AI ডেভেলপিং মাস্টারক্লাস: Python ও LLM ইন্টিগ্রেশন",
                    category = "AI ডেভেলপমেন্ট",
                    description = "পাইথন এবং লেটেস্ট লার্জ ল্যাঙ্গুয়েজ মডেল (LLM) ব্যবহার করে রিয়েল-ওয়ার্ল্ড AI অ্যাপ্লিকেশন তৈরি করা শিখুন। API কল, প্রম্পট চেইনিং এবং এজেন্টস তৈরির সম্পূর্ণ গাইড।",
                    instructor = "ড. তানভীর রহমান",
                    duration = "৪ ঘণ্টা ৩০ মিনিট",
                    youtubeId = "dQw4w9WgXcQ", // Representative YouTube ID
                    thumbnailUrl = "https://images.unsplash.com/photo-1677442136019-21780efad99a?q=80&w=600&auto=format&fit=crop",
                    progressPercent = 35,
                    lessonsCount = 12
                ),
                CourseEntity(
                    id = "c2",
                    title = "AI দিয়ে সিনেমাটিক ভিডিও তৈরি: Sora ও Runway Masterclass",
                    category = "ভিডিও জেনারেশন",
                    description = "OpenAI Sora, Runway Gen-2 এবং Pika ল্যাব ব্যবহার করে কীভাবে হাই-এন্ড সিনেমাটিক ভিডিও এবং অ্যানিমেশন তৈরি করবেন, তার মাস্টারক্লাস।",
                    instructor = "নাফিস ইকবাল",
                    duration = "৩ ঘণ্টা ১৫ মিনিট",
                    youtubeId = "jNQXAC9IVRw",
                    thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=600&auto=format&fit=crop",
                    progressPercent = 60,
                    lessonsCount = 8
                ),
                CourseEntity(
                    id = "c3",
                    title = "AI ইমেজ জেনারেশন প্রো: Midjourney & Stable Diffusion",
                    category = "ইমেজ জেনারেশন",
                    description = "Midjourney v6 এবং Stable Diffusion XL (SDXL) দিয়ে অবিশ্বাস্য ফোটোরিয়্যালিস্টিক এবং আর্টওয়ার্ক তৈরির অ্যাডভান্সড প্রম্পটিং ও স্টাইল টেকনিক।",
                    instructor = "ফারহানা সুলতানা",
                    duration = "৫ ঘণ্টা",
                    youtubeId = "3JZ_D3ELwOQ",
                    thumbnailUrl = "https://images.unsplash.com/photo-1620712943543-bcc4688e7485?q=80&w=600&auto=format&fit=crop",
                    progressPercent = 10,
                    lessonsCount = 15
                ),
                CourseEntity(
                    id = "c4",
                    title = "প্রম্পট ইঞ্জিনিয়ারিং মাস্টারক্লাস: জিরো থেকে হিরো",
                    category = "প্রম্পট ইঞ্জিনিয়ারিং",
                    description = "ChatGPT, Claude এবং Gemini থেকে নিখুঁত আউটপুট পাওয়ার জন্য অ্যাডভান্সড প্রম্পট স্ট্রাকচার, চেইন-অফ-থট এবং রোল-প্লেয়িং টেকনিক।",
                    instructor = "আসিফ মাহমুদ",
                    duration = "২ ঘণ্টা ৪৫ মিনিট",
                    youtubeId = "kJQP7kiw5Fk",
                    thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=600&auto=format&fit=crop",
                    progressPercent = 0,
                    lessonsCount = 10
                ),
                CourseEntity(
                    id = "c5",
                    title = "অটোমেশন ও AI এজেন্টস: LangChain & AutoGPT",
                    category = "AI টুলস",
                    description = "কোডিং ছাড়াই বা পাইথন কোড দিয়ে নিজস্ব AI এজেন্ট তৈরি করুন যা আপনার দৈনন্দিন কাজ স্বয়ংক্রিয়ভাবে সম্পন্ন করবে।",
                    instructor = "সাদমান সাকিব",
                    duration = "৬ ঘণ্টা",
                    youtubeId = "9bZkp7q19f0",
                    thumbnailUrl = "https://images.unsplash.com/photo-1677442136019-21780efad99a?q=80&w=600&auto=format&fit=crop",
                    progressPercent = 0,
                    lessonsCount = 18
                )
            )
            database.courseDao().insertCourses(initialCourses)
        }
    }
}
