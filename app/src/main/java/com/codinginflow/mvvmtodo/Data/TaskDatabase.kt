package com.codinginflow.mvvmtodo.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codinginflow.mvvmtodo.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task :: class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    //Room create code for this class

    abstract fun taskDao(): TaskDao

    // Inject and Providers annotation are same methods
    // Providers are for Third party lib
    // Inject are for our own methods.

    class CallBack @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope : CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Respective Database operations.
            // Database is created when the AppModule build function is completed.
            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Use this app daily.",important = true))
                dao.insert(Task("Share this app with others.",important = true))
                dao.insert(Task("Provides some feedback.",important = true))
                dao.insert(Task("Swipe left or right to delete a particular task."))
                dao.insert(Task("Use above options for sorting tasks."))
                dao.insert(Task("Search via key words."))
            }

        }
    }
}