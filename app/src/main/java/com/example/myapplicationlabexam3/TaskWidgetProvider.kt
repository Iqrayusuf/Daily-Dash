package com.example.myapplicationlabexam3

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.myapplicationlabexam3.utils.ToDoPreferenceManager

class TaskWidgetProvider: AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    @SuppressLint("RemoteViewLayout")
    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget)

        // Retrieve upcoming tasks from preferences or database
        val taskList = getUpcomingTasks(context)

        // Update the ListView in the widget
        views.setCharSequence(R.id.widget_task_list, "setText", taskList.joinToString("\n"))

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getUpcomingTasks(context: Context): List<String> {
        // Fetch tasks from your storage mechanism (e.g., SharedPreferences or Database)
        val toDoPreferenceManager = ToDoPreferenceManager(context)
        return toDoPreferenceManager.getAllTasks().map { it.task } // Modify as needed
    }


}