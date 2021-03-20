package com.codinginflow.mvvmtodo.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.CheckBox
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmtodo.Data.Task
import com.codinginflow.mvvmtodo.databinding.ItemTaskBinding

class TasksAdapter(private val listener: onItemClickListener) : ListAdapter<Task, TasksAdapter.TasksVieHolder>(DiffCallBack()) {
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

    inner class TasksVieHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        // OnClick Events
        init {
            // It will initiate when this ViewHolder class is formed.
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }

                checkBoxCompleted.setOnClickListener{
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, checkBoxCompleted.isChecked)
                    }
                }
            }
        }

        fun bind(task:Task){
            binding.apply {
                checkBoxCompleted.isChecked = task.completed
                textViewName.text = task.name
                textViewName.paint.isStrikeThruText = task.completed
                labelPriority.isVisible = task.important
            }
        }
    }

    // Interface usage make this adapter reusable
    interface onItemClickListener{
        fun onItemClick(task : Task)
        fun onCheckBoxClick(task: Task, isChecked: Boolean)
    }

    class DiffCallBack : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean  = oldItem == newItem

    }
}