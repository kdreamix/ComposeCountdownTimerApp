package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CountdownViewModel : ViewModel() {

    private val _totalTime = MutableLiveData("")
    val totalTime: LiveData<String> = _totalTime

    fun onTotalTimeChanged(newTime: String) {
        _totalTime.value = newTime
    }

    val totalTimeStateFlow = MutableStateFlow(0L)
    val remainingTimeStateFlow = MutableStateFlow(0L)

    fun startCountdownTimer(timeInFuture: Long, interval: Long) {
        viewModelScope.launch {
            totalTimeStateFlow.emit(timeInFuture)
        }

        val timer = object : CountDownTimer(timeInFuture, interval) {
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
}