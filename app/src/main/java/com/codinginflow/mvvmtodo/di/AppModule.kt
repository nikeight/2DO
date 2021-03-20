package com.codinginflow.mvvmtodo.di

import android.app.Application
import androidx.room.Room
import com.codinginflow.mvvmtodo.Data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

// Here we gives Dagger instructions to do respective
// Dependencies Injection

@Module
@InstallIn(ApplicationComponent::class)
object AppModule  {

    // Instructions functions.
    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: TaskDatabase.CallBack
    ) =
        // Simple way to return in Kotlin.
        // Construct Database
        Room.databaseBuilder(app,TaskDatabase::class.java,"task_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

    @ApplicationScope
    @Provides
    @Singleton
    // We creating our own Coroutines scope that remain until our application
    // Coroutines fails whenever any respective child process gets failed.
    // SuperVisor Class prevents happening from this behavior
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope