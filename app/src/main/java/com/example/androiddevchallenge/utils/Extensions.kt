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
package com.example.androiddevchallenge.utils

fun Long?.toCountDownTimerString(): String {
    return this?.run {
        return when {
            this > 3600000 -> String.format(
                "%02d:%02d:%02d",
                ((this / 1000).toInt() / 3600),
                ((this / 1000).toInt().rem(3600).div(60)),
                ((this / 1000).toInt() % 60)
            )
            else -> String.format(
                "%02d:%02d",
                ((this / 1000).toInt() / 60),
                ((this / 1000).toInt() % 60)
            )
        }
    } ?: "00:00"
}
