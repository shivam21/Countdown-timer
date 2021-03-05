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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.min

private const val STROKE_SIZE = 60
private const val SWEEP_ANGLE = 1f

@Composable
fun CircleTimer(
    modifier: Modifier = Modifier,
    elapsedTime: Long,
    totalTime: Long
) {
    Canvas(
        modifier = modifier.fillMaxSize(),
        onDraw = {
            val height = size.height
            val width = size.width
            val strokeSize = STROKE_SIZE.dp
            val radiusOffset = strokeSize.value
            val xCenter = width / 2f
            val yCenter = height / 2f
            val radius = min(xCenter, yCenter)
            val arcWidthHeight = ((radius - radiusOffset) * 2f)
            val arcSize = Size(arcWidthHeight, arcWidthHeight)
            val remainderColor = Color.White.copy(alpha = 0.25f)
            val timeLeftPercent = min(1f, elapsedTime.toFloat() / totalTime.toFloat())
            val timeCoveredPercent = 1 - timeLeftPercent
            var startAngle = 270f
            var shouldDrawArc = true
            for (i in 1..(timeCoveredPercent * 360f).toInt() step 2) {
                if (shouldDrawArc)
                    drawArc(
                        ShaderBrush(
                            SweepGradientShader(
                                Offset(xCenter, yCenter),
                                listOf(Color.Red, Color.Yellow, Color.Red)
                            )
                        ),
                        startAngle,
                        -SWEEP_ANGLE,
                        false,
                        topLeft = Offset(radiusOffset, radiusOffset),
                        size = arcSize,
                        style = Stroke(width = strokeSize.value)
                    )
                startAngle -= 2
                shouldDrawArc = !shouldDrawArc
            }
            for (i in 1..(timeLeftPercent * 360f).toInt() step 2) {
                if (shouldDrawArc)
                    drawArc(
                        remainderColor,
                        startAngle,
                        SWEEP_ANGLE,
                        false,
                        topLeft = Offset(radiusOffset, radiusOffset),
                        size = arcSize,
                        style = Stroke(width = strokeSize.value)
                    )
                startAngle -= 2
                shouldDrawArc = !shouldDrawArc
            }
        }
    )
}
