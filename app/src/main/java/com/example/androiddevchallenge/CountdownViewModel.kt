/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

enum class TimerState {
    Uninitialized,
    InProgress,
    Paused,
}

class CountdownViewModel : ViewModel() {

    // The timer
    private lateinit var timer: CountDownTimer

    val remainingTimeStateFlow = MutableStateFlow(0L)
    val progressStateFlow = MutableStateFlow(0f)
    val totalTimeStateFlow = MutableStateFlow(0L)
    val timerStateFlow = MutableStateFlow(TimerState.Uninitialized)

    @OptIn(ExperimentalTime::class)
    fun start(
        timeInFuture: Long = totalTimeStateFlow.value + 1.seconds.toLongMilliseconds(),
        interval: Long = 100L
    ) {
        if (totalTimeStateFlow.value == 0L) return
        timerStateFlow.value = TimerState.InProgress
        remainingTimeStateFlow.value = timeInFuture
        timer = object : CountDownTimer(timeInFuture, interval) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeStateFlow.value = remainingTimeStateFlow.value - interval
                val remainingTime = remainingTimeStateFlow.value
                val totalTime = totalTimeStateFlow.value
                val progress =
                    ((totalTime.toFloat() - remainingTime.toFloat()) / totalTime.toFloat())
                Log.v("Progress", "Progress: $progress")
                progressStateFlow.value = progress
            }

            override fun onFinish() {
                clear()
            }
        }

        timer.start()
    }

    @OptIn(ExperimentalTime::class)
    fun addSec() {
        totalTimeStateFlow.value = totalTimeStateFlow.value + 1.seconds.toLongMilliseconds()
    }

    @OptIn(ExperimentalTime::class)
    fun minusSec() {
        totalTimeStateFlow.value =
            (totalTimeStateFlow.value - 1.seconds.toLongMilliseconds()).coerceAtLeast(0)
    }

    fun resume() {
        start(remainingTimeStateFlow.value)
    }

    fun pause() {
        timerStateFlow.value = TimerState.Paused
        timer.cancel()
    }

    fun clear(resetProgress: Boolean = false) {
        if (resetProgress) {
            progressStateFlow.value = 0f
        }
        timerStateFlow.value = TimerState.Uninitialized
        totalTimeStateFlow.value = 0
        remainingTimeStateFlow.value
        timer.cancel()
    }
}
