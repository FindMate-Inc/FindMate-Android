package com.example.findmate

import android.app.Application
import com.example.findmate.di.AppComponent
import com.example.findmate.di.AppModule
import com.example.findmate.di.DaggerAppComponent

class FindMateApplication: Application() {

    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().appModule(AppModule()).build()
    }

    fun getComponent(): AppComponent {
        return appComponent
    }
}