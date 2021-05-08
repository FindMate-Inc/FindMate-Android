package com.abstractclass.findmate.di

import com.abstractclass.findmate.ui.create.CreatePostActivity
import com.abstractclass.findmate.ui.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(createPostActivity: CreatePostActivity)
}