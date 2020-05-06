package com.example.todoapp.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.weatherinfo.di.qualifier.ApplicationContext
import com.example.weatherinfo.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPrefModule(@ApplicationContext context: Context) {

    @ApplicationContext
    private val mContext: Context = context

    @ApplicationScope
    @Provides
    fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
    }
}