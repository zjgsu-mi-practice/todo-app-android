package com.zjgsu.todoapp

import android.app.Application
import com.zjgsu.todoapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@TodoApplication)
            modules(appModule)
        }
    }
}