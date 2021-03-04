package com.example.androiddevchallenge.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.CountdownViewModel
import com.example.androiddevchallenge.TimerState

@Composable
fun CountDownText(viewModel: CountdownViewModel = viewModel()) {
    val state by viewModel.remainingTimeStateFlow.collectAsState(0L)
    Text(text = state.toString())
}

@Composable
fun CountDownInput(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(value = value, onValueChange = {
        onValueChange(it)
    })
}

@Composable
fun CountDownScreen(viewModel: CountdownViewModel = viewModel()) {
    val totalTime: String by viewModel.totalTime.observeAsState("")
    val timerState: TimerState by viewModel.timerStateFlow.collectAsState(TimerState.Uninitialized)
    Column {
        CountDownText()
        CountDownInput(
            value = totalTime,
            onValueChange = { viewModel.onTotalTimeChanged(it) }
        )
        StartButton(
            timerState = timerState,
            onClick = { viewModel.start(totalTime.toLong(), 1L) }
        )
        PauseResumeToggle(
            timerState = timerState,
            onClick = {
                when (timerState) {
                    TimerState.InProgress -> viewModel.pause()
                    TimerState.Paused -> viewModel.resume()
                }
            }
        )
        ClearButton(
            timerState = timerState,
            onClick = { viewModel.clear() }
        )

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StartButton(timerState: TimerState, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = when (timerState) {
            TimerState.Uninitialized -> true
            TimerState.InProgress, TimerState.Paused -> false
        }
    ) {
        Button(onClick = {
            onClick()
        }) {
            Text(text = "Start")
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PauseResumeToggle(timerState: TimerState, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = when (timerState) {
            TimerState.Uninitialized -> false
            TimerState.InProgress, TimerState.Paused -> true
        }
    ) {
        Button(onClick = { onClick() }) {
            Text(
                text = when (timerState) {
                    TimerState.Uninitialized -> ""
                    TimerState.InProgress -> "viewModel.pause()"
                    TimerState.Paused -> "viewModel.resume()"
                }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ClearButton(timerState: TimerState, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = when (timerState) {
            TimerState.Uninitialized -> false
            TimerState.InProgress, TimerState.Paused -> true
        }
    ) {
        Button(onClick = {
            onClick()
        }) {
            Text(text = "Clear")
        }
    }
}