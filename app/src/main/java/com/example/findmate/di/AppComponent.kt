package com.example.findmate.di

import com.example.findmate.ui.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}