package com.codinginflow.mvvmtodo.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.codinginflow.mvvmtodo.Data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao
) : ViewModel() {
    // Inject Dao

    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)

    // Combine to actually combine three methods in one function
    // Triple is to return three values at a time
    // Used Lambda functions at the end.
    private val taskFlow = combine(
        searchQuery,
        sortOrder,
        hideCompleted
    ){ query, sortOrder, hideCompleted ->
        Triple(query,sortOrder,hideCompleted) }
    .flatMapLatest { (query, sortOrder, hideCompleted) ->
        taskDao.getTasks(query, sortOrder, hideCompleted)
    }

    val tasks = taskFlow.asLiveData()
    // {previously} taskDao.getTasks("abc").asLiveData()
    // LiveData is single Instance of the change list
    // LiveData is LifeCycle aware
}

enum class SortOrder {
    BY_NAME , BY_DATE
}