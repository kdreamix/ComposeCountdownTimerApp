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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.CountdownViewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.TimerState
import com.example.androiddevchallenge.milliSecondsToHHMMSSDisplay
import com.example.androiddevchallenge.ui.theme.bgColor
import com.example.androiddevchallenge.ui.theme.typography
import kotlin.time.ExperimentalTime


@Composable
fun TimeDisplayRow(viewModel: CountdownViewModel) {
    val remainingTime by viewModel.remainingTimeStateFlow.collectAsState()
    val timerState: TimerState by viewModel.timerStateFlow.collectAsState(TimerState.Uninitialized)
    val totalTime by viewModel.totalTimeStateFlow.collectAsState()
    Text(
        text = when (timerState) {
            TimerState.Uninitialized -> totalTime.milliSecondsToHHMMSSDisplay()
            else -> remainingTime.milliSecondsToHHMMSSDisplay()

        }, fontSize = 24.sp
    )
}

@OptIn(ExperimentalTime::class)
@Composable
fun CountDownScreen(viewModel: CountdownViewModel = viewModel()) {
    val progress: Float by viewModel.progressStateFlow.collectAsState(0f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Chemex countdown",
                style = typography.h3,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Chemex(
                modifier = Modifier
                    .padding(horizontal = 64.dp, vertical = 32.dp)
                    .height(480.dp), progress = progress
            )
            ElectronScale()
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun ElectronScale(viewModel: CountdownViewModel = viewModel()) {
    val timerState: TimerState by viewModel.timerStateFlow.collectAsState(TimerState.Uninitialized)

    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .border(8.dp, Color.White, shape = RoundedCornerShape(16.dp))

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Image(
                painterResource(id = R.drawable.ic_baseline_remove_circle_outline_24),
                contentDescription = "minus",
                modifier = Modifier.clickable {
                    viewModel.minusSec()
                }
            )

            TimeDisplayRow(viewModel)

            Image(
                painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                contentDescription = "add",
                modifier = Modifier.clickable {
                    viewModel.addSec()
                }
            )

            PauseResumeToggle(
                timerState = timerState,
                onClick = {
                    when (timerState) {
                        TimerState.Uninitialized -> viewModel.start()
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
fun PauseResumeToggle(timerState: TimerState, onClick: () -> Unit) {
    val res = when (timerState) {
        TimerState.Uninitialized -> R.drawable.ic_baseline_play_circle_outline_24
        TimerState.InProgress -> R.drawable.ic_baseline_pause_circle_outline_24
        TimerState.Paused -> R.drawable.ic_baseline_play_circle_outline_24
    }
    Image(
        painter = painterResource(id = res),
        contentDescription = "",
        modifier = Modifier.clickable { onClick() })
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ClearButton(onClick: () -> Unit) {
    Image(
        painter = painterResource(R.drawable.ic_baseline_refresh_24),
        contentDescription = "",
        Modifier.clickable {
            onClick()
        }
    )
}
