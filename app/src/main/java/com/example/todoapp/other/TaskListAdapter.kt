package com.example.todoapp.other

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.model.TaskModel

class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    private var data: ArrayList<TaskModel> = ArrayList()
    lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_entry, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return if (!data.isNullOrEmpty()) data.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = data[position]
        holder.checkBox.text = task.name
        holder.txtDescription.text = task.description
        if (task.status == 1) {
            holder.checkBox.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.checkBox.isChecked = true
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
        var txtDescription: TextView = itemView.findViewById(R.id.txtDescription)
        private var imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)
        private val cardView: CardView = itemView.findViewById(R.id.cvTask)

        init {
            checkBox.setOnClickListener {
                listener.onCheckBoxClick(
                    data[adapterPosition],it
                )
            }

            imgDelete.setOnClickListener {
                listener.onDeleteIconClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onCheckBoxClick(taskModel: TaskModel, view: View)
        fun onDeleteIconClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setData(tasks: List<TaskModel>) {
        if (data.isNotEmpty()) data.clear()
        this.data.addAll(tasks)
        notifyDataSetChanged()
    }

    fun removeTask(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size)
    }

    fun getTask(position: Int): TaskModel {
        return data[position]
    }

    fun restoreTask(task: TaskModel, position: Int) {
        data.add(position, task)
        notifyItemInserted(position)
    }
}