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

    val inputTimeStateFlow = MutableStateFlow("")
    val remainingTimeStateFlow = MutableStateFlow(0L)
    val timerStateFlow = MutableStateFlow(TimerState.Uninitialized)

    fun onTotalTimeChanged(newTime: String) {
        inputTimeStateFlow.value = newTime
    }

    fun start(timeInFuture: Long, interval: Long) {
        timerStateFlow.value = TimerState.InProgress

        timer = object : CountDownTimer(timeInFuture, interval) {
            override fun onTick(millisUntilFinished: Long) {
                viewModelScope.launch {
                    remainingTimeStateFlow.emit(millisUntilFinished)
                }
            }

            override fun onFinish() {
                viewModelScope.launch {
                    remainingTimeStateFlow.emit(0)
                }
            }
        }

        timer.start()
    }

    fun resume() {
        start(remainingTimeStateFlow.value, 1L)
    }

    fun pause() {
        timerStateFlow.value = TimerState.Paused
        timer.cancel()
    }

    fun clear() {
        timerStateFlow.value = TimerState.Uninitialized
        remainingTimeStateFlow.value = 0
        timer.cancel()
        onTotalTimeChanged("0")
    }
}