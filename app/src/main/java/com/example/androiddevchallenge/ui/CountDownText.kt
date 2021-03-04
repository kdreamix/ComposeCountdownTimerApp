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
    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        }
    )
}

@Composable
fun CountDownScreen(viewModel: CountdownViewModel = viewModel()) {
    val totalTime: String by viewModel.inputTimeStateFlow.collectAsState("")
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
        Button(
            onClick = {
                onClick()
            }
        ) {
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
        Button(
            onClick = {
                onClick()
            }
        ) {
            Text(text = "Clear")
        }
    }
}
