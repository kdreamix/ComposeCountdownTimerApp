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

import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.hours
import kotlin.time.minutes
import kotlin.time.seconds

fun Long.milliSecondsToHHMMSSDisplay(): String {
    val hours = TimeUnit.MILLISECONDS.toHours(this).toInt() % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this).toInt() % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this).toInt() % 60
    return when {
        hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, seconds)
        minutes > 0 -> String.format("%02d:%02d", minutes, seconds)
        seconds > 0 -> String.format("00:%02d", seconds)
        else -> {
            "00:00"
        }
    }
}

fun Int.formatTimeUnit(): String {
    return String.format("%02d", this)
}

@OptIn(ExperimentalTime::class)
fun toMilliSeconds(hour: Int, min: Int, sec: Int): Double {
    val duration: Duration = hour.hours + min.minutes + sec.seconds
    return duration.inMilliseconds
}

@OptIn(ExperimentalTime::class)
fun toMilliSeconds(sec: Int): Double {
    val duration: Duration = sec.seconds
    return duration.inMilliseconds
}
