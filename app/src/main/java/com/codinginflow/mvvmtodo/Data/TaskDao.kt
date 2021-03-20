package com.codinginflow.mvvmtodo.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Here we do all the Database operations queries
@Dao
interface TaskDao {

    // SQL query
    // Flow is from coroutines which is a Asynchronous Stream data provider
    // We can also use Live Data.
    @Query("SELECT * FROM task_table WHERE  name LIKE '%' || :searchQuery || '%' ORDER BY important DESC ")
    fun getTasks(searchQuery : String): Flow<List<Task>>

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