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
package com.example.androiddevchallenge.viewmodels

import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.ui.TIMER_STEP_INTERVAL
import com.example.androiddevchallenge.ui.TIMER_TOTAL_TIME
import com.example.androiddevchallenge.ui.TimerState
import com.example.androiddevchallenge.ui.ViewState

enum class AudioState {
    MUTE,
    VIBRATE,
    SOUND
}

class CountDownTimerViewModel : ViewModel() {

    private val _currentViewState = MutableLiveData<ViewState>(ViewState.TIMER)
    val currentViewState: LiveData<ViewState> = _currentViewState

    var hour: Int = 0
    var min: Int = 0
    var sec: Int = 0

    private val _timerState = MutableLiveData<TimerState>(TimerState.PLAY)
    val timerState: LiveData<TimerState> = _timerState

    private val _timer = MutableLiveData<Long>(TIMER_TOTAL_TIME)
    val timer: LiveData<Long> = _timer

    var tts: TextToSpeech? = null
    var vibrator: Vibrator? = null

    var mytimer: CountDownTimer? = null

    var millisLeft = TIMER_TOTAL_TIME

    private var lastSecondTimestamp = 0L

    val audioState = MutableLiveData<AudioState>(AudioState.SOUND)

    private fun startTimer() {
        _timerState.postValue(TimerState.PAUSE)
        mytimer?.cancel()
        lastSecondTimestamp = millisLeft / 1000
        mytimer = object : CountDownTimer(millisLeft, TIMER_STEP_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                millisLeft = millisUntilFinished
                _timer.postValue(millisUntilFinished)
                if ((millisUntilFinished / 1000) <= lastSecondTimestamp - 1) {
                    lastSecondTimestamp -= 1L
                    if (lastSecondTimestamp <= 3L)
                        speakOrVibrate(
                            tts = tts,
                            vibrator = vibrator,
                            audioState = audioState.value ?: AudioState.SOUND,
                            sayText = lastSecondTimestamp.toString(),
                            vibrationLength = 200L
                        )
                }
            }

            override fun onFinish() {
                resetTimer()
            }
        }
        mytimer?.start()
    }

    private fun pauseTimer() {
        _timerState.postValue(TimerState.PLAY)
        mytimer?.cancel()
    }

    fun resetTimer() {
        _currentViewState.postValue(ViewState.TIMER_PICKER)
        mytimer?.cancel()
    }

    fun toggleTimer() {
        if (timerState.value == TimerState.PLAY) {
            startTimer()
        } else {
            pauseTimer()
        }
    }

    fun speakOrVibrate(
        tts: TextToSpeech?,
        vibrator: Vibrator?,
        audioState: AudioState,
        sayText: String,
        vibrationLength: Long
    ) {
        when (audioState) {
            AudioState.MUTE -> return
            AudioState.VIBRATE -> vibrate(vibrator, vibrationLength)
            AudioState.SOUND -> ttsSpeak(tts, sayText)
        }
    }

    private fun vibrate(vibrator: Vibrator?, ms: Long) {
        if (vibrator?.hasVibrator() == true) { // Vibrator availability checking
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        ms,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(ms) // Vibrate method for below API Level 26
            }
        }
    }

    private fun ttsSpeak(tts: TextToSpeech?, message: String) {
        tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    fun updateAudioState(currentAudioState: AudioState?) {
        when (currentAudioState) {
            AudioState.SOUND -> audioState.postValue(AudioState.VIBRATE)
            AudioState.VIBRATE -> audioState.postValue(AudioState.MUTE)
            AudioState.MUTE -> audioState.postValue(AudioState.SOUND)
        }
    }

    fun updateStateToTimer() {
        TIMER_TOTAL_TIME = getTotalTime()
        millisLeft = TIMER_TOTAL_TIME
        _currentViewState.postValue(ViewState.TIMER)
        startTimer()
        hour = 0
        min = 0
        sec = 0
    }

    fun getTotalTime(): Long {
        return (hour * 3600 + min * 60 + sec) * 1000L
    }

    fun showTimer() {
        _currentViewState.postValue(ViewState.TIMER)
    }
}
