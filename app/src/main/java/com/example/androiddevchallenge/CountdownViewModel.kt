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
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

enum class TimerState {
    Uninitialized,
    InProgress,
    Paused,
}

class CountdownViewModel : ViewModel() {

    // The timer
    private lateinit var timer: CountDownTimer

    val remainingTimeStateFlow = MutableStateFlow(0L)

    val timerStateFlow = MutableStateFlow(TimerState.Uninitialized)

    fun start(timeInFuture: Long = remainingTimeStateFlow.value, interval: Long = 1000L) {
        timerStateFlow.value = TimerState.InProgress
        remainingTimeStateFlow.value = timeInFuture
        timer = object : CountDownTimer(timeInFuture, interval) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeStateFlow.value = remainingTimeStateFlow.value - interval
            }

            override fun onFinish() {
                clear()
            }
        }

        timer.start()
    }

    fun resume() {
        start()
    }

    fun pause() {
        timerStateFlow.value = TimerState.Paused
        timer.cancel()
    }

    fun clear() {
        timerStateFlow.value = TimerState.Uninitialized
        remainingTimeStateFlow.value
        timer.cancel()
    }
}
