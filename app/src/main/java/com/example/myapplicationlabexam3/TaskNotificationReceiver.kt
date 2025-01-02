package com.example.myapplicationlabexam3

import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TaskNotificationReceiver:BroadcastReceiver()  {

    override fun onReceive(context: Context, intent: Intent) {
        val taskTitle = intent.getStringExtra("taskTitle") ?: "Task Reminder"

        // Build and show the notification
        val notificationBuilder = NotificationCompat.Builder(context, "TASK_CHANNEL_ID")
            .setSmallIcon(R.drawable.baseline_notifications)
            .setContentTitle("Upcoming Task")
            .setContentText(taskTitle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }





}