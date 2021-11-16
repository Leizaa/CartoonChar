package com.example.cartoonchar

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

    private var job: Job? = null

    fun init (){
        val handler = Handler()

        job = GlobalScope.launch {

            handler.postDelayed(updateUi(), 10000)

//            val swipeTimer = Timer("",false)
//            swipeTimer.schedule(object : TimerTask() {
//                override fun run() {
//                    Log.d("timer", "called " + isInitiated)
//                    handler.post(updateUi())
//                }
//            }, 1000, 5000)
        }
    }

    fun set(runnable : Runnable) {
        Log.d("Timer", "A runner was provided")
        userRunnable = runnable
    }

    fun reset() {
        Log.d("timer", "reset timer called")
        if (job != null) {
            Log.d("timer", "reset timer!!")
            job!!.cancel()
            init()
        }
    }

    private fun updateUi(): Runnable{
        return userRunnable
    }

}