package com.example.androiddevchallenge.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun Chemex() {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(480.dp)
            .background(bgColor)
            .padding(16.dp)

    ) {
        val insidePadding = with(LocalDensity.current) { 24.dp.toPx() }

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
            val coffeeCurvature = 0.68f

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
                bottomCoffee(insidePadding, canvasWidth, canvasHeight)
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
    padding: Float,
    canvasWidth: Float,
    canvasHeight: Float,
    color: Color = coffeeColor,
    style: DrawStyle = Fill,
) {
    val path = Path().apply {
        moveTo(center.x, center.y + padding)
        cubicTo(0f, center.y + padding, center.x, center.y + padding, 0f, canvasHeight)
        lineTo(canvasWidth, canvasHeight)
        cubicTo(center.x, center.y + padding, canvasWidth, center.y + padding, center.x, center.y + padding)
        // cubicTo(canvasWidth, canvasHeight, center.x, center.y, canvasWidth,0f)
        // lineTo(0f,0f)
    }

    drawPath(
        path,
        color = color,
        style = style,
    )
}