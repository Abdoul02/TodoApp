package com.example.todoapp

import android.app.Activity
import android.app.Application
import com.example.todoapp.di.component.ApplicationComponent
import com.example.todoapp.di.component.DaggerApplicationComponent
import com.example.todoapp.di.module.ContextModule
import com.example.todoapp.di.module.DatabaseModule

class MyApplication : Application() {

    private var component: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent
            .builder()
            .contextModule(ContextModule(this))
            .databaseModule(DatabaseModule(this))
            .build()
    }

    fun get(activity: Activity): MyApplication {
        return activity.application as MyApplication
    }

    fun getApplicationComponent(): ApplicationComponent? {
        return component
    }
}