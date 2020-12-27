package com.example.chrono.main.timer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.chrono.R
import com.example.chrono.databinding.FragmentTimerBinding
import kotlin.math.roundToInt


class TimerFrag : Fragment() {
    private var bind: FragmentTimerBinding? = null

    enum class TimerState { INIT, RUNNING, PAUSED }

//    enum class RunningState { INITIAL, WORK, REST }

    private lateinit var countdown: CountDownTimer
    private var timerState: TimerState = TimerState.INIT

    //    private lateinit var runningState: RunningState
    private var secondsLeft: Float = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false)

        bind!!.startbutton.setOnClickListener {
            createTimer(10, false)
            timerState = TimerState.RUNNING
            updateButtonUI()
        }

        bind!!.pausebutton.setOnClickListener {
            countdown.cancel()
            timerState = TimerState.PAUSED
            updateButtonUI()
        }

        bind!!.stopbutton.setOnClickListener {
            countdown.cancel()
            timerState = TimerState.INIT
            updateButtonUI()
        }

        bind!!.resumebutton.setOnClickListener {
            createTimer(secondsLeft.toInt(), true)
            timerState = TimerState.RUNNING
            updateButtonUI()
        }

        bind!!.newCircuit.setOnClickListener {
            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Apply activity transition
                startActivity(Intent(requireContext(), TimerCreate::class.java))
            } else {
                // Swap without transition
                startActivity(Intent(requireContext(), TimerCreate::class.java))
            }

        }
        return bind!!.root
    }

    // Create a countdown timer based on parameters
    private fun createTimer(seconds: Int, wasPaused: Boolean) {
        if (wasPaused) {
            countdown = object : CountDownTimer((secondsLeft * 1000).toLong(), 250) {
                override fun onTick(p0: Long) {
                    if ((p0.toFloat().roundToInt() / 1000.0f) != secondsLeft) {
                        secondsLeft = (p0.toFloat() / 1000.0f).roundToInt().toFloat()
                        updateTimerUI()
                    }
                }

                override fun onFinish() {
                    Toast.makeText(context, "Timer completed.", Toast.LENGTH_LONG).show()
                    timerState = TimerState.INIT
                    updateButtonUI()
                }
            }.start()
        } else {
            countdown = object : CountDownTimer((seconds * 1000 + 250).toLong(), 250) {
                override fun onTick(p0: Long) {
                    if ((p0.toFloat().roundToInt() / 1000.0f) != secondsLeft) {
                        secondsLeft = (p0.toFloat() / 1000.0f).roundToInt().toFloat()
                        updateTimerUI()
                    }
                }

                override fun onFinish() {
                    Toast.makeText(context, "Timer completed.", Toast.LENGTH_LONG).show()
                    timerState = TimerState.INIT
                    updateButtonUI()
                }
            }.start()
        }
    }

    // Update UI for every tick
    fun updateTimerUI() {
        bind!!.countdown.text = (secondsLeft).toInt().toString()
    }

    // Update the buttons layout based on the current state of the timer
    private fun updateButtonUI() {
        when (timerState) {
            TimerState.INIT -> {
                bind!!.initButtonLay.visibility = View.VISIBLE
                bind!!.runButtonLay.visibility = View.GONE
                bind!!.pauseButtonLay.visibility = View.GONE
                bind!!.countdown.text = "0"
            }
            TimerState.RUNNING -> {
                bind!!.initButtonLay.visibility = View.GONE
                bind!!.runButtonLay.visibility = View.VISIBLE
                bind!!.pauseButtonLay.visibility = View.GONE
            }
            TimerState.PAUSED -> {
                bind!!.initButtonLay.visibility = View.GONE
                bind!!.runButtonLay.visibility = View.GONE
                bind!!.pauseButtonLay.visibility = View.VISIBLE
            }
        }
    }
}