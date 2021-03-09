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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.CountdownViewModel
import com.example.androiddevchallenge.TimerState
import com.example.androiddevchallenge.milliSecondsToHHMMSSDisplay
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@Composable
fun RemainingTimeDebugText(viewModel: CountdownViewModel = viewModel()) {
    val formattedTime by viewModel.remainingTimeStateFlow.collectAsState(0L)
    Text(text = "Remaining time millis: $formattedTime")
}

@Composable
fun InputRow(
    state: TimerState,
    seconds: Int,
    onSecondsChange: (String) -> Unit
) {
    CountDownInput(
        value = seconds.toString(),
        onValueChange = {
            onSecondsChange(it.padStart(1, '0'))
        },
        state = state,
    )
}

@Composable
fun TimeDisplayRow(viewModel: CountdownViewModel = viewModel()) {
    val remainingTime by viewModel.remainingTimeStateFlow.collectAsState()
    Text(text = remainingTime.milliSecondsToHHMMSSDisplay())
}

@Composable
fun CountDownInput(
    value: String,
    onValueChange: (String) -> Unit,
    state: TimerState,
) {
    Box(modifier = Modifier.width(60.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state != TimerState.InProgress,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            maxLines = 1

        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun CountDownScreen(viewModel: CountdownViewModel = viewModel()) {
    val timerState: TimerState by viewModel.timerStateFlow.collectAsState(TimerState.Uninitialized)
    val progress: Float by viewModel.progressStateFlow.collectAsState(0f)
    var seconds by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Chemex(progress = progress)
            RemainingTimeDebugText()
            InputRow(timerState, seconds, onSecondsChange = { seconds = it.toInt() })
            TimeDisplayRow()
            StartButton(
                timerState = timerState,
                onClick = {
                    val totalTime = (seconds.seconds + 1.seconds).toLongMilliseconds()
                    viewModel.updateTotalTime(totalTime)
                    viewModel.start(totalTime)
                }
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
