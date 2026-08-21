package com.sesmom.ticktickclone

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarBadgeIcon(label: String, color: Color) {
    Box(modifier = Modifier.size(26.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(26.dp)) {
            val stroke = 1.6.dp.toPx()
            val top = size.height * 0.22f
            drawRoundRect(
                color = color,
                topLeft = Offset(0f, top),
                size = Size(size.width, size.height - top),
                cornerRadius = CornerRadius(4.dp.toPx()),
                style = Stroke(stroke)
            )
            drawLine(color, Offset(size.width * 0.28f, 0f), Offset(size.width * 0.28f, top + 2.dp.toPx()), stroke, StrokeCap.Round)
            drawLine(color, Offset(size.width * 0.72f, 0f), Offset(size.width * 0.72f, top + 2.dp.toPx()), stroke, StrokeCap.Round)
        }
        Text(label, fontSize = 8.sp, color = color, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
    }
}

@Composable
fun SunriseIcon(color: Color, fullSun: Boolean = false) {
    Canvas(modifier = Modifier.size(26.dp)) {
        val stroke = 1.6.dp.toPx()
        val cx = size.width / 2
        val cy = size.height * 0.62f
        val r = size.width * 0.24f

        if (fullSun) {
            drawCircle(color, radius = r, center = Offset(cx, cy), style = Stroke(stroke))
        } else {
            drawArc(
                color = color,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(cx - r, cy - r),
                size = Size(r * 2, r * 2),
                style = Stroke(stroke)
            )
        }

        drawLine(color, Offset(cx, cy - r - 3.dp.toPx()), Offset(cx, cy - r - 10.dp.toPx()), stroke, StrokeCap.Round)
        drawLine(color, Offset(cx - 3.dp.toPx(), cy - r - 8.dp.toPx()), Offset(cx, cy - r - 10.dp.toPx()), stroke, StrokeCap.Round)
        drawLine(color, Offset(cx + 3.dp.toPx(), cy - r - 8.dp.toPx()), Offset(cx, cy - r - 10.dp.toPx()), stroke, StrokeCap.Round)

        val dotRadius = 1.dp.toPx()
        listOf(-1, 0, 1).forEach { i ->
            drawCircle(color, radius = dotRadius, center = Offset(cx + i * r * 0.9f, cy + r + 3.dp.toPx()))
        }
    }
}
