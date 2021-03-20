package com.codinginflow.mvvmtodo.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmtodo.Data.PreferenceManager
import com.codinginflow.mvvmtodo.Data.SortOrder
import com.codinginflow.mvvmtodo.Data.Task
import com.codinginflow.mvvmtodo.Data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    private val preferenceManager: PreferenceManager // Injecting through Dagger
) : ViewModel() {
    // Inject Dao

    val searchQuery = MutableStateFlow("")
    val preferencesFlow = preferenceManager.preferenceFlow

//    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
//    val hideCompleted = MutableStateFlow(false)

    // Combine to actually combine three methods in one function
    // Triple is to return three values at a time
    // Used Lambda functions at the end.
    private val taskFlow = combine(
        searchQuery,
        preferencesFlow
    ){ query, filterPreferences ->
        Pair(query,filterPreferences) }
    .flatMapLatest { (query, filterPreferences) ->
        taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    // To Update the data at the fragments.
    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        // !Application context but it will remain until the ViewModel exists.
        preferenceManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClicked(hideCompleted : Boolean) = viewModelScope.launch {
        // Launch is coroutines function.
        preferenceManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(task: Task) {

    }

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) = viewModelScope.launch{
        // Due to mutable class we have to make copy of the objects
        taskDao.update(task.copy(completed = isChecked))
    }

    val tasks = taskFlow.asLiveData()
    // {previously} taskDao.getTasks("abc").asLiveData()
    // LiveData is single Instance of the change list
    // LiveData is LifeCycle aware
}
