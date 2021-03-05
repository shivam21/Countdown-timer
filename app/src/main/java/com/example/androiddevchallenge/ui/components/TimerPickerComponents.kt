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

import android.content.Context
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.viewmodels.CountDownTimerViewModel

@Composable
fun TimePickerView(viewModel: CountDownTimerViewModel) {
    Box(Modifier.fillMaxWidth().fillMaxHeight()) {
        Column(Modifier.align(Alignment.Center)) {

            NumberPicker(
                Modifier.align(Alignment.CenterHorizontally),
                LocalContext.current,
                onHourSelectListener = {
                    viewModel.hour = it
                },
                onMinSelectListener = {
                    viewModel.min = it
                },
                onSecSelectListener = {
                    viewModel.sec = it
                }
            )
            val context = LocalContext.current
            val str = stringResource(R.string.select_time)
            OutlinedButton(
                onClick = {
                    if (viewModel.getTotalTime() > 0)
                        viewModel.updateStateToTimer()
                    else
                        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Start Timer")
            }
        }
    }
}

@Composable
fun NumberPicker(
    modifier: Modifier,
    context: Context,
    onHourSelectListener: (value: Int) -> Unit,
    onMinSelectListener: (value: Int) -> Unit,
    onSecSelectListener: (value: Int) -> Unit
) {
    Row(
        modifier = Modifier.then(modifier),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hrs",
            modifier = Modifier.padding(horizontal = 10.dp),
            color = Color.White.copy(0.4f)
        )
        Text(
            text = "Mins",
            modifier = Modifier.padding(horizontal = 10.dp),
            color = Color.White.copy(0.4f)
        )
        Text(
            text = "Secs",
            modifier = Modifier.padding(horizontal = 10.dp),
            color = Color.White.copy(0.4f)
        )
    }
    AndroidView(
        factory = {
            val linearLayout = LinearLayout(context)
            linearLayout.addView(
                NumberPicker(context).apply {
                    minValue = 0
                    maxValue = 24
                    wrapSelectorWheel = true
                    setOnValueChangedListener { _, _, i2 -> onHourSelectListener(i2) }
                }
            )

            linearLayout.addView(
                NumberPicker(context).apply {
                    minValue = 0
                    maxValue = 59
                    wrapSelectorWheel = true
                    setOnValueChangedListener { _, _, i2 -> onMinSelectListener(i2) }
                }
            )

            linearLayout.addView(
                NumberPicker(context).apply {
                    minValue = 0
                    maxValue = 59
                    wrapSelectorWheel = true
                    setOnValueChangedListener { _, _, i2 -> onSecSelectListener(i2) }
                }
            )
            linearLayout
        }
    )
}
