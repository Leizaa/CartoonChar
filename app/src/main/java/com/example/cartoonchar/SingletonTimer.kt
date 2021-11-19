package com.example.cartoonchar

import android.os.CountDownTimer
import android.util.Log

object SingletonTimer {
    //You can supply your own runnable from Fragments
    private lateinit var callback: TimerCallback

    private var timer: CountDownTimer? = null

    fun init() {
        if(timer == null) {
            timer = object : CountDownTimer(30000, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    Log.d("timer", "seconds remaining: " + millisUntilFinished / 1000)
                }

                override fun onFinish() {
                    callback.timerCallback()
                    Log.d("timer", "done")
                }
            }.start()
        } else {
            Log.d("timer", "timer still exist")
        }
    }

    fun setCallback(callback: TimerCallback) {
        Log.d("timer", "A callback was provided")
        this.callback = callback
    }

    fun reset() {
        Log.d("timer", "reset timer called")
        if (timer != null) {
            Log.d("timer", "reset timer!!")
            timer!!.cancel()
            timer = null
            init()
        }
    }

    fun cancel() {
        if (timer != null) {
            Log.d("timer", "timer killed!!")
            timer!!.cancel()
            timer = null
        }
    }

    interface TimerCallback {
        fun timerCallback()
    }

}