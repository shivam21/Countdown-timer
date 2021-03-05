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
package com.example.androiddevchallenge.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.TIMER_TOTAL_TIME
import com.example.androiddevchallenge.ui.TimerState
import com.example.androiddevchallenge.utils.toCountDownTimerString
import com.example.androiddevchallenge.viewmodels.AudioState
import com.example.androiddevchallenge.viewmodels.CountDownTimerViewModel
import kotlin.math.min

@ExperimentalAnimationApi
@Composable
fun CountDownTimerView(viewModel: CountDownTimerViewModel) {
    val state by viewModel.timer.observeAsState()
    val timerState by viewModel.timerState.observeAsState()
    Box(Modifier.fillMaxWidth().fillMaxHeight().padding(top = 100.dp)) {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val maxRadius by remember { mutableStateOf(min(screenHeight, screenWidth)) }
        TopView(maxRadius, state)
        BottomView(
            viewModel,
            timerState
        )
    }
}

@Composable
fun MySurface(content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.countdown_timer),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        },
        modifier = Modifier,
        content = {
            content()
        }
    )
}

@ExperimentalAnimationApi
@Composable
fun TopView(maxRadius: Int, state: Long?, modifier: Modifier = Modifier) {
    val showPortraitView = LocalConfiguration.current.screenWidthDp.dp < 600.dp
    if (showPortraitView) {
        Box(
            modifier = Modifier.size(maxRadius.dp).padding(8.dp).then(modifier)
        ) {

            CircleTimer(
                Modifier.align(Alignment.Center),
                elapsedTime = state ?: TIMER_TOTAL_TIME,
                totalTime = TIMER_TOTAL_TIME
            )
            Column(Modifier.align(Alignment.Center)) {
                Text(text = state.toCountDownTimerString(), style = MaterialTheme.typography.h2)
            }
        }
    } else {
        Box(Modifier.fillMaxWidth().fillMaxHeight()) {
            Text(
                text = state.toCountDownTimerString(),
                style = MaterialTheme.typography.h2,
                modifier = Modifier.align(
                    Alignment.TopCenter
                )
            )
        }
    }
}

@Composable
fun BottomView(viewModel: CountDownTimerViewModel, timerState: TimerState?) {
    Box(Modifier.fillMaxHeight().fillMaxWidth()) {
        Row(Modifier.padding(bottom = 50.dp).align(Alignment.BottomCenter)) {
            ResetTimerView(viewModel, Modifier.weight(1f).align(Alignment.CenterVertically))
            PlayPauseTimerView(
                viewModel,
                timerState,
                Modifier.weight(1f)
            )
            AudioStateView(viewModel, Modifier.weight(1f).align(Alignment.CenterVertically))
        }
    }
}

@Composable
fun AudioStateView(viewModel: CountDownTimerViewModel, modifier: Modifier) {
    val audioState by viewModel.audioState.observeAsState()
    Box(modifier) {
        FloatingActionButton(
            modifier = Modifier.sizeIn(40.dp, 40.dp).align(Alignment.Center),
            onClick = { viewModel.updateAudioState(audioState) },
            backgroundColor = Color.DarkGray
        ) {
            Icon(
                when (audioState) {
                    AudioState.SOUND -> Icons.Filled.VolumeUp
                    AudioState.VIBRATE -> Icons.Filled.Vibration
                    else -> Icons.Filled.VolumeOff
                },
                "",
                modifier = Modifier.align(Alignment.Center).size(16.dp),
                tint = Color.White.copy(0.8f)
            )
        }
    }
}

@Composable
fun PlayPauseTimerView(
    viewModel: CountDownTimerViewModel,
    timerState: TimerState?,
    modifier: Modifier
) {
    FloatingActionButton(
        modifier = Modifier
            .clip(CircleShape)
            .then(modifier),
        onClick = { viewModel.toggleTimer() },
        shape = MaterialTheme.shapes.large,
        backgroundColor = Color.White.copy(0.8f),
        elevation = FloatingActionButtonDefaults.elevation(24.dp, 36.dp)
    ) {
        Text(
            text = if (timerState == TimerState.PLAY) "PLAY" else "PAUSE",
            color = Color.Red
        )
    }
}

@Composable
fun ResetTimerView(viewModel: CountDownTimerViewModel, modifier: Modifier) {
    Box(modifier) {
        FloatingActionButton(
            modifier = Modifier.sizeIn(40.dp, 40.dp).align(Alignment.Center),
            onClick = { viewModel.resetTimer() },
            backgroundColor = Color.DarkGray
        ) {
            Icon(
                Icons.Filled.Edit,
                "",
                modifier = Modifier.size(16.dp),
                tint = Color.White.copy(0.8f)
            )
        }
    }
}
