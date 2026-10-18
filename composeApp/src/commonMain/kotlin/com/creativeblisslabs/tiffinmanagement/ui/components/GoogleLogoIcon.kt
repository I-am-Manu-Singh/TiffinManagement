package com.creativeblisslabs.tiffinmanagement.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Authentic 4-color Google "G" vector logo drawn programmatically in Compose Canvas.
 */
@Composable
fun GoogleLogoIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        val red = Color(0xFFEA4335)
        val blue = Color(0xFF4285F4)
        val yellow = Color(0xFFFBBC05)
        val green = Color(0xFF34A853)

        // Blue horizontal bar & right curve
        val bluePath = Path().apply {
            moveTo(w * 0.5f, h * 0.42f)
            lineTo(w * 0.95f, h * 0.42f)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(w * 0.05f, h * 0.05f, w * 0.95f, h * 0.95f),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 50f,
                forceMoveTo = false
            )
            lineTo(w * 0.5f, h * 0.58f)
            close()
        }
        drawPath(bluePath, blue, style = Fill)

        // Green bottom curve
        val greenPath = Path().apply {
            moveTo(w * 0.5f, h * 0.5f)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(w * 0.05f, h * 0.05f, w * 0.95f, h * 0.95f),
                startAngleDegrees = 50f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(greenPath, green, style = Fill)

        // Yellow left curve
        val yellowPath = Path().apply {
            moveTo(w * 0.5f, h * 0.5f)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(w * 0.05f, h * 0.05f, w * 0.95f, h * 0.95f),
                startAngleDegrees = 140f,
                sweepAngleDegrees = 80f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(yellowPath, yellow, style = Fill)

        // Red top curve
        val redPath = Path().apply {
            moveTo(w * 0.5f, h * 0.5f)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(w * 0.05f, h * 0.05f, w * 0.95f, h * 0.95f),
                startAngleDegrees = 220f,
                sweepAngleDegrees = 95f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(redPath, red, style = Fill)

        // Inner circle hole to make it a ring/G
        drawCircle(
            color = Color.White,
            radius = w * 0.28f,
            center = Offset(w * 0.5f, h * 0.5f)
        )

        // Draw cross bar of G
        drawRect(
            color = blue,
            topLeft = Offset(w * 0.46f, h * 0.42f),
            size = Size(w * 0.48f, h * 0.16f)
        )
    }
}
