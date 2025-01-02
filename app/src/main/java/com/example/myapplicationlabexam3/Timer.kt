package com.example.myapplicationlabexam3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

class Timer : AppCompatActivity() {

    // Timer variables
    private var totalTimeInMilliseconds: Long = 60000L // Default countdown from 1 minute
    private var seconds: Int = 0
    private var minutes: Int = 0
    private var milliSeconds: Int = 0
    private var millisecondTime: Long = 0L
    private var startTime: Long = 0L
    private var timeRemaining: Long = 0L
    private var updateTime: Long = 0L

    private lateinit var handler: Handler
    private lateinit var startButton: ImageView
    private lateinit var stopButton: ImageView
    private lateinit var resetButton: ImageView
    private lateinit var timerTextView: TextView
    private lateinit var editMinutes: EditText
    private lateinit var editSeconds: EditText
    private lateinit var setButton: Button

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            millisecondTime = SystemClock.uptimeMillis() - startTime
            updateTime = totalTimeInMilliseconds - millisecondTime

            if (updateTime > 0) {
                seconds = (updateTime / 1000).toInt()
                minutes = seconds / 60
                seconds %= 60
                milliSeconds = (updateTime % 1000).toInt()

                timerTextView.text = String.format(
                    Locale.getDefault(), "%02d:%02d:%03d", minutes, seconds, milliSeconds
                )
                handler.postDelayed(this, 0)
            } else {
                // Timer has finished
                timerTextView.text = "00:00:000"
                handler.removeCallbacks(this)
                startButton.isEnabled = true
                stopButton.isEnabled = false
                resetButton.isEnabled = true
                Toast.makeText(this@Timer, "Timer Finished", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()
        setContentView(R.layout.activity_timer2)

        // Apply system insets for proper padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI components
        startButton = findViewById(R.id.start)
        stopButton = findViewById(R.id.stop)
        resetButton = findViewById(R.id.reset)
        timerTextView = findViewById(R.id.textView)
        editMinutes = findViewById(R.id.editMinutes)
        editSeconds = findViewById(R.id.editSeconds)
        setButton = findViewById(R.id.setButton)

        handler = Handler(Looper.getMainLooper())

        // Set button functionality to allow user to input minutes and seconds
        setButton.setOnClickListener {
            val inputMinutes = editMinutes.text.toString().toIntOrNull()
            val inputSeconds = editSeconds.text.toString().toIntOrNull()

            if (inputMinutes == null || inputSeconds == null || inputSeconds >= 60) {
                Toast.makeText(this, "Please enter valid time values", Toast.LENGTH_SHORT).show()
            } else {
                // Convert minutes and seconds to milliseconds for the timer
                totalTimeInMilliseconds = (inputMinutes * 60 * 1000 + inputSeconds * 1000).toLong()
                timerTextView.text = String.format(
                    Locale.getDefault(), "%02d:%02d:000", inputMinutes, inputSeconds
                )
            }
        }

        // Start button functionality
        startButton.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(runnable, 0)
            resetButton.isEnabled = false
            stopButton.isEnabled = true
            startButton.isEnabled = false
        }

        // Stop button functionality
        stopButton.setOnClickListener {
            timeRemaining = totalTimeInMilliseconds - millisecondTime
            totalTimeInMilliseconds = timeRemaining
            handler.removeCallbacks(runnable)
            resetButton.isEnabled = true
            stopButton.isEnabled = false
            startButton.isEnabled = true
        }

        // Reset button functionality
        resetButton.setOnClickListener {
            millisecondTime = 0L
            startTime = 0L
            timeRemaining = 0L
            totalTimeInMilliseconds = 60000L // Reset to 1 minute by default or user-set time
            timerTextView.text = "01:00:000"  // Reset the timer display
            editMinutes.setText("1")          // Reset the input fields to default
            editSeconds.setText("0")
        }

        // Initialize the timer text view with default or user-set time
        timerTextView.text = "01:00:000" // Set the initial countdown time (e.g., 1 minute)
    }
}
