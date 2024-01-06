package com.example.jettodo2.module

import android.content.Context
import androidx.room.Room
import com.example.jettodo2.data.repository.LocalTaskRepository
import com.example.jettodo2.data.repository.TaskRepository
import com.example.jettodo2.database.AppDatabase
import com.example.jettodo2.database.dao.TaskDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "task_db",
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: AppDatabase): TaskDao {
        return db.taskDao()
    }

//    @Provides
//    @Singleton
//    fun provideStringResouces(
//        @ApplicationContext context: Context,
//    ): StringResourcesProvider {
//        return StringResourcesProvider(context = context)
//    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class MainModule {
        @Binds
        @Singleton
        abstract fun bindTaskRepository(impl: LocalTaskRepository): TaskRepository
    }
}
