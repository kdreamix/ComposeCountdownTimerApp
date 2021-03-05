package com.example.androiddevchallenge

import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.hours
import kotlin.time.minutes
import kotlin.time.seconds

fun milliSecondsToHHmmss(milliSeconds: Long): Triple<Int, Int, Int> {
    val hours = TimeUnit.MILLISECONDS.toHours(milliSeconds).toInt() % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds).toInt() % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds).toInt() % 60
    return Triple(
        hours, minutes, seconds
    )
}

fun Int.formatTimeUnit(): String {
    return String.format("%02d", this)
}

@OptIn(ExperimentalTime::class)
fun toMilliSeconds(hour: Int, min: Int, sec: Int): Double {
    val duration: Duration = hour.hours + min.minutes + sec.seconds
    return duration.inMilliseconds
}