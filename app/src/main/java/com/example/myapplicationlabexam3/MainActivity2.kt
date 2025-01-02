package com.example.myapplicationlabexam3

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.example.myapplicationlabexam3.utils.ToDoPreferenceManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationlabexam3.adapter.ToDoAdapter
import com.example.myapplicationlabexam3.model.ToDoModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.provider.Settings

import com.example.myapplicationlabexam3.OnDialogCloseListener




class MainActivity2 : AppCompatActivity(),OnDialogCloseListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var toDoPreferenceManager: ToDoPreferenceManager
    private lateinit var mList: MutableList<ToDoModel>
    private lateinit var adapter: ToDoAdapter
    private lateinit var stopwatchImage: ImageView
    private lateinit var timerImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)



        createNotificationChannel()
        checkNotificationPermission()

        recyclerView = findViewById(R.id.recylerview)
        fab = findViewById(R.id.floatingActionButton3)
        toDoPreferenceManager = ToDoPreferenceManager(this)

        mList = mutableListOf()
        adapter = ToDoAdapter(toDoPreferenceManager, this)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(RecyclerViewTouchHelper(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Fetch tasks and update UI
        mList = toDoPreferenceManager.getAllTasks().toMutableList()
        mList.reverse()
        adapter.setTasks(mList)

      /*  fab.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)


            // For example, show a toast message
            Toast.makeText(this, "FAB Clicked", Toast.LENGTH_SHORT).show()
        }*/

       /*fab.setOnClickListener {
            // Show the dialog to add a new task
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)

            // Assume you get the task title from the dialog or another input method
            val taskTitle = "New Task" // Replace with actual input from dialog
            val nextId = mList.size + 1 // Generate next ID based on current list size
            val triggerTimeInMillis = System.currentTimeMillis() + 10000//3600000 // 1 hour from now

            // Call the method to add the task and schedule the notification
            addToDoItem(taskTitle, nextId, 0, triggerTimeInMillis) // Pass triggerTimeInMillis
            Toast.makeText(this, "Task added with notification scheduled", Toast.LENGTH_SHORT).show()
        }*/

        fab.setOnClickListener {
            val dialog = AddNewTask.newInstance()
            dialog.setOnTaskAddedListener(object : OnTaskAddedListener {
                override fun onTaskAdded(taskTitle: String) {
                    val nextId = mList.size + 1
                    val triggerTimeInMillis = System.currentTimeMillis() + 10000 // For testing

                    // Call the method to add the task and schedule the notification
                    addToDoItem(taskTitle, nextId, 0, triggerTimeInMillis)
                    Toast.makeText(this@MainActivity2, "Task added with notification scheduled", Toast.LENGTH_SHORT).show()
                }
            })
            dialog.show(supportFragmentManager, AddNewTask.TAG)
        }









        // Initialize stopwatchImage
        stopwatchImage = findViewById(R.id.stopwatch)

        // Set onClickListener for stopwatchImage
        stopwatchImage.setOnClickListener {
            val intent = Intent(this, stopwatch::class.java)
            startActivity(intent)
        }

        val timerImageView: ImageView = findViewById(R.id.timer)

        // Set an OnClickListener
        timerImageView.setOnClickListener {
            // Create an Intent to navigate to the Timer activity
            val intent = Intent(this, Timer::class.java)
            startActivity(intent) // Start the Timer activity
        }

    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101
                )
            }
        }
    }





   /* private fun scheduleTaskNotification(context: Context, taskTitle: String, triggerTimeInMillis: Long) {
        val intent = Intent(context, TaskNotificationReceiver::class.java).apply {
            putExtra("taskTitle", taskTitle)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent)
    }*/


    private fun scheduleTaskNotification(context: Context, taskTitle: String, triggerTimeInMillis: Long) {
        val intent = Intent(context, TaskNotificationReceiver::class.java).apply {
            putExtra("taskTitle", taskTitle)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Check for permission to schedule exact alarms
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val canScheduleExactAlarms = alarmManager.canScheduleExactAlarms()
            if (!canScheduleExactAlarms) {
                // Direct the user to settings to allow exact alarms
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                context.startActivity(intent)
                return // Stop execution if permission is not granted
            }
        }

        // Set the exact alarm
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent)
    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "TaskChannel"
            val descriptionText = "Channel for Task Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("TASK_CHANNEL_ID", name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }





    private fun addToDoItem(task: String, id: Int, status: Int,triggerTimeInMillis: Long) {
        val toDoModel = ToDoModel(task, id, status)
        toDoPreferenceManager.insertTask(toDoModel)
        scheduleTaskNotification(this, task, triggerTimeInMillis)
    }

    private fun getToDoItem(id: Int): ToDoModel? {
        return toDoPreferenceManager.getAllTasks().find { it.id == id }
    }




    override  fun onDialogClose(dialogInterface: DialogInterface) {
        // Refresh the list when the dialog closes
        mList = toDoPreferenceManager.getAllTasks().toMutableList()
        mList.reverse()
        adapter.setTasks(mList)
        adapter.notifyDataSetChanged()
        }



}






