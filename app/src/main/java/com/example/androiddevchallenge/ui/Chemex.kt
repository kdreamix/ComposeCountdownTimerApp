package com.example.androiddevchallenge.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Chemex() {

    Canvas(modifier = Modifier
        .width(280.dp)
        .height(480.dp)
        .background(Color.White), onDraw = {

        val canvasSize = size
        val canvasWidth = size.width
        val canvasHeight = size.height
        val l = 0f
        val r = canvasWidth
        val centerW = canvasWidth / 2
        val centerH = canvasHeight / 2

        val bottleWidth = canvasWidth * 0.8f
        val bottleHeight = canvasHeight * 0.8f

        val bottleCurvature = 0.6f
        val handleCurvature = 0.28f

        chemexBottle(l, r, 0f, canvasHeight, bottleCurvature, color = Color.Gray)
        chemexBottle(
            canvasWidth * 0.2f,
            canvasWidth * 0.8f,
            canvasHeight * 0.4f,
            canvasHeight * 0.6f,
            handleCurvature,
            color = Color.Red,
            Fill,
        )

    })
}

private fun DrawScope.chemexBottle(
    l: Float,
    r: Float,
    t: Float,
    b: Float,
    bottleCurvature: Float,
    color: Color,
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
        color = color,
        style = style
    )
}

private fun DrawScope.handle(
    r: Float,
    handleCurvature: Float,
    canvasHeight: Float,
    canvasWidth: Float,
    l: Float
) {
}

// fun DrawScope.filterPaper() {
//     upperCone()
// }
//
// fun DrawScope.redUpperBottle() {
//     withTransform({
//         translate(top = 100f)
//         scale(1.1f)
//     }) {
//         upperCone(color = Color.Red, style = Fill)
//     }
// }
//
// fun DrawScope.upperCone(color: Color = Color.Gray, style: DrawStyle = Stroke(width = 20f)) {
//
//     val canvasWidth = size.width
//     val canvasHeight = size.height
//
//     val bottleWidth = canvasWidth * 0.9f
//     val bottleHeight = canvasHeight * 0.9f
//     drawArc(
//         startAngle = -45f,
//         sweepAngle = -90f,
//         color = color,
//         useCenter = true,
//         size = Size(bottleWidth, bottleHeight / 2),
//         topLeft = Offset(x = (canvasWidth - bottleWidth) / 2, (canvasHeight - bottleHeight) / 2),
//         style = style
//     )
// }