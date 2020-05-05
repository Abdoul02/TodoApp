package com.example.todoapp.di.module

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.TaskDataBase
import com.example.todoapp.data.dao.TaskDao
import com.example.weatherinfo.di.qualifier.ApplicationContext
import com.example.weatherinfo.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module(includes = [ViewModelModule::class])
class DatabaseModule(@ApplicationContext context: Context) {
    @ApplicationContext
    private val mContext: Context = context

    @ApplicationScope
    @Provides
    fun provideDataBase(): TaskDataBase {
        return Room.databaseBuilder<TaskDataBase>(
            mContext,
            TaskDataBase::class.java,
            "taskDatabase.db"
        ).fallbackToDestructiveMigration().build()
    }

    @ApplicationScope
    @Provides
    fun provideTaskDao(db: TaskDataBase): TaskDao {
        return db.taskDao()
    }
}