package com.codinginflow.mvvmtodo.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmtodo.Data.Task
import com.codinginflow.mvvmtodo.databinding.ItemTaskBinding

class TasksAdapter : ListAdapter<Task, TasksAdapter.TasksVieHolder>(DiffCallBack()) {
    // DiffUtil do stuff at background
    // DiffUtilCall backs knows how to handle the changes


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksVieHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return TasksVieHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksVieHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class TasksVieHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task:Task){
            binding.apply {
                checkBoxCompleted.isChecked = task.completed
                textViewName.text = task.name
                textViewName.paint.isStrikeThruText = task.completed
                labelPriority.isVisible = task.important
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean  = oldItem == newItem

    }
}