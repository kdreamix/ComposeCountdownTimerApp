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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.bgColor
import com.example.androiddevchallenge.ui.theme.coffeeColor
import com.example.androiddevchallenge.ui.theme.coffeeColor2
import com.example.androiddevchallenge.ui.theme.handleColor
import com.example.androiddevchallenge.ui.theme.handleColor2
import com.example.androiddevchallenge.ui.theme.outlineColor

@Preview
@Composable
fun Chemex(
    modifier: Modifier = Modifier,
    progress: Float = 0.5f,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)

    ) {
        val insidePadding = with(LocalDensity.current) { 24.dp.toPx() }

        val progress: Float by animateFloatAsState(
            progress.coerceAtMost(0.99f),
            animationSpec = spring(stiffness = 20f)
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize(),
            onDraw = {

                val canvasWidth = size.width
                val canvasHeight = size.height

                val l = 0f
                val r = canvasWidth
                val t = 0f
                val b = canvasHeight

                val bottleCurvature = 0.6f
                val handleCurvature = 0.28f

                val handleL = canvasWidth * 0.2f
                val handleR = canvasWidth * 0.8f
                val handleT = canvasHeight * 0.4f
                val handleB = canvasHeight * 0.6f
                val handleHeight = handleB - handleT

                chemexBottle(
                    l,
                    r,
                    t,
                    b,
                    bottleCurvature,
                    colors = listOf(outlineColor, outlineColor)
                )

                withTransform(
                    {
                        scale(0.78f)
                        translate(top = handleHeight / 2)
                    },
                    {
                        bottomCoffee(
                            progress = progress,
                            padding = insidePadding,
                            canvasWidth = canvasWidth,
                            canvasHeight = canvasHeight
                        )
                    }
                )

                chemexBottle(
                    handleL,
                    handleR,
                    handleT,
                    handleB,
                    handleCurvature,
                    colors = listOf(handleColor, handleColor2),
                    Fill,
                )
            }
        )
    }
}

private fun DrawScope.chemexBottle(
    l: Float,
    r: Float,
    t: Float,
    b: Float,
    bottleCurvature: Float,
    colors: List<Color>,
    style: DrawStyle = Stroke(width = 12.dp.value, cap = StrokeCap.Round, join = StrokeJoin.Round)
) {
    val bottlePath = Path().apply {
        moveTo(l, t)
        cubicTo(l, t, r * bottleCurvature, center.y, l, b)
        lineTo(r, b)
        cubicTo(
            r,
            b,
            l + r - (r * bottleCurvature),
            center.y,
            r,
            t
        )
        lineTo(l, t)
    }

    drawPath(
        bottlePath,
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset(size.width / 2 - 64, size.height / 2 - 64),
            end = Offset(size.width / 2 + 64, size.height / 2 + 64),
            tileMode = TileMode.Clamp,
        ),
        style = style
    )
}

fun DrawScope.bottomCoffee(
    progress: Float = 1f,
    padding: Float,
    canvasWidth: Float,
    canvasHeight: Float,
    style: DrawStyle = Fill,
) {
    val top = center.y + padding
    val coffeeHeight = canvasHeight - top
    val bottom = canvasHeight - coffeeHeight * progress
    val path = Path().apply {
        moveTo(center.x, top)
        cubicTo(0f, top, center.x, top, 0f, canvasHeight)
        lineTo(canvasWidth, canvasHeight)
        cubicTo(center.x, top, canvasWidth, top, center.x, top)
    }

    val rect = Path().apply {
        addRect(Rect(0f, top, canvasWidth, bottom))
    }

    val reverseDiff = Path().apply {
        op(rect, path, PathOperation.reverseDifference)
    }

    drawPath(
        path = reverseDiff,
        brush = Brush.linearGradient(
            colors = listOf(coffeeColor2, coffeeColor),
            start = Offset(center.x, top),
            end = Offset(canvasWidth + 64, canvasHeight),
            tileMode = TileMode.Clamp,
        ),
        style = style,
        alpha = 1f,
    )

    // drawPath(
    //     path,
    //     color = Color.Red,
    //     style = style,
    // )
    //
    // drawPath(
    //     rect,
    //     color = Color.Blue,
    //     style = style,
    // )
}
