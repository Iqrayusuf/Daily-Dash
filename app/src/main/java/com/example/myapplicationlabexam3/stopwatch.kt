package com.example.myapplicationlabexam3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class stopwatch : AppCompatActivity() {

    // Stopwatch variables
    private var seconds: Int = 0
    private var minutes: Int = 0
    private var milliSeconds: Int = 0
    private var millisecondTime: Long = 0L
    private var startTime: Long = 0L
    private var timeBuff: Long = 0L
    private var updateTime: Long = 0L

    private lateinit var handler: Handler
    private lateinit var startButton: ImageView
    private lateinit var stopButton: ImageView
    private lateinit var resetButton: ImageView
    private lateinit var timerTextView: TextView

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            millisecondTime = SystemClock.uptimeMillis() - startTime
            updateTime = timeBuff + millisecondTime
            seconds = (updateTime / 1000).toInt()
            minutes = seconds / 60
            seconds %= 60
            milliSeconds = (updateTime % 1000).toInt()

            timerTextView.text = String.format(
                Locale.getDefault(), "%02d:%02d:%03d", minutes, seconds, milliSeconds
            )
            handler.postDelayed(this, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()
        setContentView(R.layout.activity_stopwatch)

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

        handler = Handler(Looper.getMainLooper())

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
            timeBuff += millisecondTime
            handler.removeCallbacks(runnable)
            resetButton.isEnabled = true
            stopButton.isEnabled = false
            startButton.isEnabled = true
        }

        // Reset button functionality
        resetButton.setOnClickListener {
            millisecondTime = 0L
            startTime = 0L
            timeBuff = 0L
            updateTime = 0L
            seconds = 0
            minutes = 0
            milliSeconds = 0
            timerTextView.text = "00:00:000"
        }

        // Initialize the timer text view
        timerTextView.text = "00:00:000"
    }
}




