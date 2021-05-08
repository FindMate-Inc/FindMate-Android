package com.abstractclass.findmate.di

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
class AppModule {

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
            Log.e("RETROFITICH",
                "\nrequest:\n${request.body().toString()}\nheaders:\n${request.headers()
                    .toString()}"
            )
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

    companion object {
        const val BASE_URL = "https://find-mate-api.herokuapp.com/api/"
    }
}