package com.example.myapplicationlabexam3.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplicationlabexam3.model.ToDoModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ToDoPreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("ToDoPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val gson = Gson()

    private val TASK_LIST_KEY = "task_list"

    // Save individual task
    fun insertTask(model: ToDoModel) {
        val taskList = getAllTasks().toMutableList()
        taskList.add(model)
        saveTaskList(taskList)
    }

    // Update a specific task by ID
    fun updateTask(id: Int, task: String) {
        val taskList = getAllTasks().toMutableList()
        taskList.forEach {
            if (it.id == id) {
                it.task = task
            }
        }
        saveTaskList(taskList)
    }

    // Update the status of a task by ID
    fun updateStatus(id: Int, status: Int) {
        val taskList = getAllTasks().toMutableList()
        taskList.forEach {
            if (it.id == id) {
                it.status = status
            }
        }
        saveTaskList(taskList)
    }

    // Delete a task by ID
    fun deleteTask(id: Int) {
        val taskList = getAllTasks().toMutableList()
        taskList.removeAll { it.id == id }
        saveTaskList(taskList)
    }

    // Retrieve all tasks
    fun getAllTasks(): List<ToDoModel> {
        val json = sharedPreferences.getString(TASK_LIST_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<ToDoModel>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    // Save the list of tasks to SharedPreferences
    private fun saveTaskList(taskList: List<ToDoModel>) {
        val json = gson.toJson(taskList)
        editor.putString(TASK_LIST_KEY, json)
        editor.apply()
    }
}
