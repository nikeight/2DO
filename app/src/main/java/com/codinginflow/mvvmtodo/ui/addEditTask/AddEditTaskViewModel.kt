package com.codinginflow.mvvmtodo.ui.addEditTask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codinginflow.mvvmtodo.Data.Task
import com.codinginflow.mvvmtodo.Data.TaskDao

// Getting the data directly to the Respective ViewModel using
// saved state methods

class AddEditTaskViewModel
    @ViewModelInject constructor(
    private val taskDao : TaskDao,
    @Assisted private val state : SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")

    // Splitting the task state so that we can change that if needed.
    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
    set(value) {
        field = value
        state.set("taskName",value)
    }

    var taskImportant = state.get<Boolean>("taskImportant") ?: task?.important ?: false
        set(value) {
            field = value
            state.set("taskImportant",value)
        }
}