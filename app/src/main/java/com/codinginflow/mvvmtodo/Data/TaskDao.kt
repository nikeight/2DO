package com.codinginflow.mvvmtodo.Data

import androidx.room.*
import com.codinginflow.mvvmtodo.ui.tasks.SortOrder
import kotlinx.coroutines.flow.Flow

// Here we do all the Database operations queries
@Dao
interface TaskDao {

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when(sortOrder) {
            SortOrder.BY_NAME -> getTasksSortedByName(query,hideCompleted)
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted)
        }

    // SQL query
    // Flow is from coroutines which is a Asynchronous Stream data provider
    // We can also use Live Data.
    // SQL will sort first with important as high priority then name

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted or completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC , name ")
    fun getTasksSortedByName(searchQuery : String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted or completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC , created ")
    fun getTasksSortedByDateCreated(searchQuery : String, hideCompleted: Boolean): Flow<List<Task>>
    // Suspend is a modifier which is a simple a bg thread
    // Room take care of this thread operations
    // This "replace" method is for INSERT operations.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}