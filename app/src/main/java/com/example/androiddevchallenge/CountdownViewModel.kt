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
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

enum class TimerState {
    Uninitialized,
    InProgress,
    Paused,
}

class CountdownViewModel : ViewModel() {

    // The timer
    private lateinit var timer: CountDownTimer

    val remainingTimeStateFlow = MutableStateFlow(0L)
    val secStateFlow = MutableStateFlow(0)
    val minStateFlow = MutableStateFlow(0)
    val hourStateFlow = MutableStateFlow(0)
    val timerStateFlow = MutableStateFlow(TimerState.Uninitialized)

    val remainingMilliSeconds
        get() = toMilliSeconds(
            hourStateFlow.value,
            minStateFlow.value,
            secStateFlow.value
        )

    fun onSecChange(newSec: Int) {
        secStateFlow.value = newSec
    }

    fun onMinChange(newMin: Int) {
        minStateFlow.value = newMin
    }

    fun onHourChange(newHour: Int) {
        hourStateFlow.value = newHour
    }

    fun start(timeInFuture: Double = remainingMilliSeconds, interval: Long = 1L) {
        timerStateFlow.value = TimerState.InProgress

        timer = object : CountDownTimer(timeInFuture.toLong(), interval) {
            override fun onTick(millisUntilFinished: Long) {
                viewModelScope.launch {
                    remainingTimeStateFlow.emit(millisUntilFinished)
                    val (hour, min, sec) = milliSecondsToHHmmss(millisUntilFinished)
                    hourStateFlow.emit(hour)
                    minStateFlow.emit(min)
                    secStateFlow.emit(sec)
                }
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
        secStateFlow.value = 0
        minStateFlow.value = 0
        hourStateFlow.value = 0
        timerStateFlow.value = TimerState.Uninitialized
        remainingTimeStateFlow.value = 0
        timer.cancel()
    }
}
