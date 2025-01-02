/*package com.example.myapplicationlabexam3.Adapter
import com.example.myapplicationlabexam3.utils.ToDoPreferenceManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationlabexam3.AddNewTask
import com.example.myapplicationlabexam3.MainActivity2
import com.example.myapplicationlabexam3.model.ToDoModel
import com.example.myapplicationlabexam3.R



class ToDoAdapter {
    class ToDoAdapter(
        private val toDoPreferenceManager: ToDoPreferenceManager,
        private val activity: MainActivity2
    ) : RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

        private var mList: List<ToDoModel> = listOf()

        @NonNull
        override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
            return MyViewHolder(v)
        }

        override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
            val item = mList[position]
            holder.mCheckBox.text = item.task
            holder.mCheckBox.isChecked = toBoolean(item.status)
            holder.mCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    toDoPreferenceManager.updateStatus(item.id, 1)
                } else {
                    toDoPreferenceManager.updateStatus(item.id, 0)
                }
            }
        }

        private fun toBoolean(num: Int): Boolean {
            return num != 0
        }

        fun getContext(): Context {
            return activity
        }

        fun setTasks(list: List<ToDoModel>) {
            mList = list
            notifyDataSetChanged()
        }

        fun deleteTask(position: Int) {
            val item = mList[position]
            toDoPreferenceManager.deleteTask(item.id)
            val updatedList = mList.toMutableList()
            updatedList.removeAt(position)
            mList = updatedList
            notifyItemRemoved(position)
        }

        fun editItem(position: Int) {
            val item = mList[position]
            val bundle = Bundle().apply {
                putInt("id", item.id)
                putString("task", item.task)
            }
            val task = AddNewTask().apply {
                arguments = bundle
            }// need to add class
            task.show(activity.supportFragmentManager, task.tag)
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val mCheckBox: CheckBox = itemView.findViewById(R.id.mcheckbox)
        }
    }
}*/


package com.example.myapplicationlabexam3.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationlabexam3.AddNewTask
import com.example.myapplicationlabexam3.MainActivity2
import com.example.myapplicationlabexam3.R
import com.example.myapplicationlabexam3.model.ToDoModel
import com.example.myapplicationlabexam3.utils.ToDoPreferenceManager

class ToDoAdapter(
    private val toDoPreferenceManager: ToDoPreferenceManager,
    private val activity: MainActivity2
) : RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

    private var mList: List<ToDoModel> = listOf()

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
        val item = mList[position]
        holder.mCheckBox.text = item.task
        holder.mCheckBox.isChecked = toBoolean(item.status)

        holder.mCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                toDoPreferenceManager.updateStatus(item.id, 1)
            } else {
                toDoPreferenceManager.updateStatus(item.id, 0)
            }
        }
    }

    private fun toBoolean(num: Int): Boolean {
        return num != 0
    }

    fun setTasks(list: List<ToDoModel>) {
       mList = list

        //mList.clear()
        //mList.addAll(list)
        notifyDataSetChanged()
    }

    fun deleteTask(position: Int) {
        val item = mList[position]
        toDoPreferenceManager.deleteTask(item.id)
        val updatedList = mList.toMutableList().apply {
            removeAt(position)
        }
        mList = updatedList
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item = mList[position]
        val bundle = Bundle().apply {
            putInt("id", item.id)
            putString("task", item.task)
        }

        val task = AddNewTask().apply {
            arguments = bundle
        }

        task.show(activity.supportFragmentManager, task.tag)


    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mCheckBox: CheckBox = itemView.findViewById(R.id.mcheckbox)
    }

    fun getContext(): Context {
        return activity
    }
}
