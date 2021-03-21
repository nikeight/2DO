package com.codinginflow.mvvmtodo.ui.tasks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.codinginflow.mvvmtodo.Data.PreferenceManager
import com.codinginflow.mvvmtodo.Data.SortOrder
import com.codinginflow.mvvmtodo.Data.Task
import com.codinginflow.mvvmtodo.Data.TaskDao
import com.codinginflow.mvvmtodo.ui.ADD_TASK_RESULT_OK
import com.codinginflow.mvvmtodo.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle,
    private val preferenceManager: PreferenceManager // Injecting through Dagger
) : ViewModel() {
    // Inject Dao

    // AS in the beginning we don't wan to search for anything
    // It update itself we just need to read it
    val searchQuery = state.getLiveData("searchQuery","")

    val preferencesFlow = preferenceManager.preferenceFlow

    private val taskEventChannel = Channel<TasksEvent>()

    // Generating yet another flow to share data with fragments. Kotlin Class
    val tasksEvent = taskEventChannel.receiveAsFlow()

//    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
//    val hideCompleted = MutableStateFlow(false)

    // Combine to actually combine three methods in one function
    // Triple is to return three values at a time
    // Used Lambda functions at the end.
    private val taskFlow = combine(
        searchQuery.asFlow(),
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

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) = viewModelScope.launch{
        // Due to mutable class we have to make copy of the objects
        taskDao.update(task.copy(completed = isChecked))
    }

    fun onTaskSwipe(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        taskEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    fun onAddNewTaskClick() = viewModelScope.launch{
        // ViewModel instruct the Fragments
        taskEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onAddEditResult(result: Int){
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task Added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task updated")
        }
    }

    private fun showTaskSavedConfirmationMessage(text: String) = viewModelScope.launch{
        taskEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(text))
    }

    fun onDeleteAllCompletedClick() = viewModelScope.launch{
        taskEventChannel.send(TasksEvent.NavigateToDeleteAllCompletedScreen)
    }

    sealed class TasksEvent {
        // objects bcoz it doesn't take any arguments.
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen : TasksEvent()
    }

    val tasks = taskFlow.asLiveData()
    // {previously} taskDao.getTasks("abc").asLiveData()
    // LiveData is single Instance of the change list
    // LiveData is LifeCycle aware
}
