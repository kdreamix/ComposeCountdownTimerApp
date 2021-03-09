package com.example.androiddevchallenge.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
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
import com.example.androiddevchallenge.ui.theme.handleColor
import com.example.androiddevchallenge.ui.theme.outlineColor

@Preview
@Composable
fun Chemex(progress: Float = 0.5f) {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(480.dp)
            .background(bgColor)
            .padding(16.dp)

    ) {
        val insidePadding = with(LocalDensity.current) { 24.dp.toPx() }

        val progress: Float by animateFloatAsState(progress)

        Canvas(modifier = Modifier
            .fillMaxSize(), onDraw = {

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

            chemexBottle(l, r, t, b, bottleCurvature, color = outlineColor)

            withTransform({
                scale(0.78f)
                translate(top = handleHeight / 2)
            }, {
                bottomCoffee(
                    progress = progress,
                    padding = insidePadding,
                    canvasWidth = canvasWidth,
                    canvasHeight = canvasHeight
                )
            })

            chemexBottle(
                handleL,
                handleR,
                handleT,
                handleB,
                handleCurvature,
                color = handleColor,
                Fill,
            )

        })
    }
}

private fun DrawScope.chemexBottle(
    l: Float,
    r: Float,
    t: Float,
    b: Float,
    bottleCurvature: Float,
    color: Color,
    style: DrawStyle = Stroke(width = 6.dp.value, cap = StrokeCap.Round, join = StrokeJoin.Round)
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
        color = color,
        style = style
    )
}

fun DrawScope.bottomCoffee(
    progress: Float = 1f,
    padding: Float,
    canvasWidth: Float,
    canvasHeight: Float,
    color: Color = coffeeColor,
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
        reverseDiff,
        color = color,
        style = style,
        alpha = 0.5f
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