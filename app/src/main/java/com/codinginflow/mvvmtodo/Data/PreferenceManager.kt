package com.codinginflow.mvvmtodo.Data

import android.content.Context
import android.preference.Preference
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferenceManager"

enum class SortOrder {
    BY_NAME, BY_DATE
}

data class FilterPreferences(val sortOrder: SortOrder, val hideCompleted: Boolean)

@Singleton
class PreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    // Create the DataStore
    private val dataStore = context.createDataStore("user_preferences")

    // To get the respective data from dataStore
    // map is used to go through each values separately and safe them accordingly

    val preferenceFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                // DataStore functions
                emit(emptyPreferences())
                Log.e(TAG, "Error Reading preferences", exception)
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name
            )

            val hideCompleted = preferences[PreferencesKeys.HIDE_COMPLETED] ?: false
            FilterPreferences(sortOrder, hideCompleted)
        }

    // To read the data
    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    // for just better organisation of the code
    private object PreferencesKeys {
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val HIDE_COMPLETED = preferencesKey<Boolean>("hide_completed")
    }
}