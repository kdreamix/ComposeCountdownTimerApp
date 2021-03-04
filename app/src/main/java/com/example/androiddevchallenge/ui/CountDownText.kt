package com.example.androiddevchallenge.ui

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

@Composable
fun CountDownText(viewModel: CountdownViewModel = viewModel()) {
    val state = viewModel.remainingTimeStateFlow.collectAsState(0L)
    Text(text = state.value.toString())
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
    Column {
        CountDownText()
        CountDownInput(value = totalTime) { viewModel.onTotalTimeChanged(it) }
        Button(onClick = {
            viewModel.startCountdownTimer(totalTime.toLong(), 1L)
        }) {
            Text(text = "Start")
        }
    }
}