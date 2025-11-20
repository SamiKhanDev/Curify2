package com.saim.curify.di

import com.saim.curify.chat.ChatService
import com.saim.curify.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.time.Duration


@Module
@InstallIn(SingletonComponent::class)
object ChatModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val req = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " +
                        when {
                            BuildConfig.OPENROUTER_API_KEY.isNotEmpty() -> BuildConfig.OPENROUTER_API_KEY
                            BuildConfig.GROQ_API_KEY.isNotEmpty() -> BuildConfig.GROQ_API_KEY
                            else -> BuildConfig.OPENAI_API_KEY
                        }
                    )
                    .addHeader("HTTP-Referer", "com.adeel.curify")
                    .addHeader("X-Title", "Curify Chat")
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(req)
            })
            .addInterceptor(logging)
            .connectTimeout(java.time.Duration.ofSeconds(20, ))
            .readTimeout(java.time.Duration.ofSeconds(60))
            .writeTimeout(java.time.Duration.ofSeconds(60))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(
                when {
                    BuildConfig.OPENROUTER_API_KEY.isNotEmpty() -> "https://openrouter.ai/api/"
                    BuildConfig.GROQ_API_KEY.isNotEmpty() -> "https://api.groq.com/openai/"
                    else -> "https://api.openai.com/"
                }
            )
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideChatService(retrofit: Retrofit): ChatService = retrofit.create(ChatService::class.java)
}

