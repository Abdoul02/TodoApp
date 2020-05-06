package com.example.todoapp.di.component

import android.content.Context
import com.example.todoapp.di.module.ContextModule
import com.example.todoapp.di.module.DatabaseModule
import com.example.todoapp.di.module.SharedPrefModule
import com.example.todoapp.other.ViewUtilities
import com.example.todoapp.ui.main.MainFragment
import com.example.weatherinfo.di.qualifier.ApplicationContext
import com.example.weatherinfo.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [ContextModule::class, DatabaseModule::class, SharedPrefModule::class])
interface ApplicationComponent {

    @ApplicationContext
    fun getContext(): Context?

    fun injectFragment(mainFragment: MainFragment)
}