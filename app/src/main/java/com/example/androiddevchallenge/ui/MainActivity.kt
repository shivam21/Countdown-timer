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

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.components.CountDownTimerView
import com.example.androiddevchallenge.ui.components.MySurface
import com.example.androiddevchallenge.ui.components.TimePickerView
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.viewmodels.CountDownTimerViewModel

var TIMER_TOTAL_TIME = 60000L
const val TIMER_STEP_INTERVAL = 5L

enum class TimerState {
    PLAY, PAUSE
}

enum class ViewState {
    TIMER_PICKER, TIMER
}

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<CountDownTimerViewModel>()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSoundHelpers()
        setContent {
            MyTheme(true) {
                MyApp()
            }
        }
    }

    override fun onBackPressed() {
        if (viewModel.currentViewState.value == ViewState.TIMER_PICKER) {
            viewModel.showTimer()
        } else {
            super.onBackPressed()
        }
    }

    private fun initSoundHelpers() {
        viewModel.vibrator = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        viewModel.tts = TextToSpeech(this) { }
    }
}

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun MyApp() {
    MySurface {
        Surface(color = MaterialTheme.colors.background) {
            val viewModel = viewModel(CountDownTimerViewModel::class.java)
            val viewState by viewModel.currentViewState.observeAsState()
            AnimatedVisibility(visible = viewState == ViewState.TIMER_PICKER) {
                TimePickerView(
                    viewModel
                )
            }
            AnimatedVisibility(visible = viewState == ViewState.TIMER) {
                CountDownTimerView(
                    viewModel
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@ExperimentalAnimationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
