package com.codinginflow.mvvmtodo.Data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

// Its handy to use val instead of var
// Making a SQL table by using @Entity
// Making it parcelable as well
// To also auto generate the ID of the Table items

@Entity(tableName = "task_table")
@Parcelize
data class Task(
    val name: String,
    val important : Boolean = false,
    val completed : Boolean = false,
    val created : Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id : Int = 0

) : Parcelable {
    // As we want to change the time format of the created variable
    // get will run for time when created has been changed.
    val createdDateFormatted : String
    get() = DateFormat.getDateInstance().format(created)
}