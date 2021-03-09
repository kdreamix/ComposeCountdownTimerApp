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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.CountdownViewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.TimerState
import com.example.androiddevchallenge.milliSecondsToHHMMSSDisplay
import com.example.androiddevchallenge.ui.theme.bgColor
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
fun TimeDisplayRow(viewModel: CountdownViewModel) {
    val remainingTime by viewModel.remainingTimeStateFlow.collectAsState()
    val timerState: TimerState by viewModel.timerStateFlow.collectAsState(TimerState.Uninitialized)
    val totalTime by viewModel.totalTimeStateFlow.collectAsState()
    Text(
        text = when (timerState) {
            TimerState.Uninitialized -> totalTime.milliSecondsToHHMMSSDisplay()
            else -> remainingTime.milliSecondsToHHMMSSDisplay()

        }
    )
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
    val progress: Float by viewModel.progressStateFlow.collectAsState(0f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Chemex(modifier = Modifier.height(480.dp), progress = progress)
            ElectronScale()
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun ElectronScale(viewModel: CountdownViewModel = viewModel()) {
    val timerState: TimerState by viewModel.timerStateFlow.collectAsState(TimerState.Uninitialized)

    RemainingTimeDebugText()
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .border(8.dp, Color.White, shape = RoundedCornerShape(16.dp))

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { viewModel.addSec() }) {
                Image(
                    painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                    contentDescription = "add"
                )
            }
            TimeDisplayRow(viewModel)
            Button(onClick = { viewModel.minusSec() }) {
                Image(
                    painterResource(id = R.drawable.ic_baseline_remove_circle_outline_24),
                    contentDescription = "minus"
                )
            }
            StartButton(
                timerState = timerState,
                onClick = {
                    viewModel.start()
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
            ClearButton { viewModel.clear() }
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
            Image(
                painter = painterResource(R.drawable.ic_baseline_play_circle_outline_24),
                contentDescription = ""
            )
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
            when (timerState) {
                TimerState.Uninitialized -> Image(
                    painter = painterResource(R.drawable.ic_baseline_play_circle_outline_24),
                    contentDescription = ""
                )
                TimerState.InProgress -> Image(
                    painter = painterResource(R.drawable.ic_baseline_pause_circle_outline_24),
                    contentDescription = ""
                )
                TimerState.Paused -> Image(
                    painter = painterResource(R.drawable.ic_baseline_play_circle_outline_24),
                    contentDescription = ""
                )
            }

        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ClearButton(onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        }
    ) {
        Image(
            painter = painterResource(R.drawable.ic_baseline_refresh_24),
            contentDescription = ""
        )
    }
}
