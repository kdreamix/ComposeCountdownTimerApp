package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _totalTime = MutableLiveData("")
    val totalTime: LiveData<String> = _totalTime

    fun onTotalTimeChanged(newTime: String) {
        _totalTime.value = newTime
    }

    val remainingTimeStateFlow = MutableStateFlow(0L)
    val timerStateFlow = MutableStateFlow(TimerState.Uninitialized)

    fun start(timeInFuture: Long, interval: Long) {
        timerStateFlow.value = TimerState.InProgress

        timer = object : CountDownTimer(timeInFuture, interval) {
            override fun onTick(millisUntilFinished: Long) {
                viewModelScope.launch {
                    remainingTimeStateFlow.emit(millisUntilFinished)
                }
            }

            override fun onFinish() {
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