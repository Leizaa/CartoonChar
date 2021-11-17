package com.example.cartoonchar

import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object SingletonTimer {
    //You can supply your own runnable from Fragments
    private lateinit var userRunnable: Runnable

    private var timer: CountDownTimer? = null

    fun init() {
        timer = object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                Log.d("timer", "seconds remaining: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                Log.d("timer", "done")
                updateUi()
            }
        }.start()
    }

    fun set(runnable: Runnable) {
        Log.d("Timer", "A runner was provided")
        userRunnable = runnable
    }

    fun reset() {
        Log.d("timer", "reset timer called")
        if (timer != null) {
            Log.d("timer", "reset timer!!")
            timer!!.cancel()
            init()
        }
    }

    private fun updateUi(): Runnable {
        return userRunnable
    }

}