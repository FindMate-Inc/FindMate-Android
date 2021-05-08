package com.abstractclass.findmate

import android.app.Application
import com.abstractclass.findmate.di.AppComponent
import com.abstractclass.findmate.di.AppModule
import com.abstractclass.findmate.di.DaggerAppComponent

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