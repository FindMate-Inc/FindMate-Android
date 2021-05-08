package com.abstractclass.findmate.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.abstractclass.findmate.repositories.Api
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class AppModule(val context: Context) {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()

        client.addInterceptor { chain ->
            val request = chain.request()
            val bodyBuffer = okio.Buffer()
            request.body()?.writeTo(bodyBuffer)
            Log.e("Retrofit", "url = ${request.url()}")
            Log.e("Retrofit", "request: ${bodyBuffer.readUtf8()}")
            chain.proceed(request)
        }
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Api {
        val retrofitClient = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

        return retrofitClient.create(Api::class.java)
    }

    @Provides
    @Singleton
    fun providePreferences(): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    }

    companion object {
        const val BASE_URL = "https://find-mate-api.herokuapp.com/api/"
        const val SHARED_PREFERENCES_FILE = "com.example.findmate.settings"
    }
}