package com.example.network

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class Part(
    val text: String
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    val candidates: List<Candidate>? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    val content: Content? = null
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiNetwork {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service: GeminiApiService by lazy {
        retrofit.create(GeminiApiService::class.java)
    }
}

suspend fun askGeminiMentor(prompt: String): String = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
        return@withContext "দুঃখিত, Gemini API Key সেট করা নেই। দয়া করে AI Studio Secrets প্যানেলে আপনার API Key যুক্ত করুন।"
    }

    try {
        val request = GenerateContentRequest(
            systemInstruction = Content(
                parts = listOf(Part("তুমি একজন বন্ধুসুলভ এবং অভিজ্ঞ AI মাস্টারক্লাস মেন্টর। শিক্ষার্থীদের AI ডেভেলপমেন্ট, ভিডিও ও ইমেজ জেনারেশন এবং প্রম্পট ইঞ্জিনিয়ারিং শেখাতে সহায়তা করো। সবসময় বাংলায় উৎসাহব্যঞ্জক ও তথ্যবহুল উত্তর দাও।"))
            ),
            contents = listOf(
                Content(parts = listOf(Part(prompt)))
            )
        )
        val response = GeminiNetwork.service.generateContent(apiKey, request)
        val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        reply ?: "দুঃখিত, কোনো উত্তর পাওয়া যায়নি।"
    } catch (e: Exception) {
        "ত্রুটি ঘটেছে: ${e.localizedMessage ?: "নেটওয়ার্ক সমস্যা"}"
    }
}
