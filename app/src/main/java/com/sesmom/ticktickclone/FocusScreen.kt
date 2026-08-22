package com.sesmom.ticktickclone

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private const val WORK_SECONDS = 25 * 60

@Composable
fun FocusScreen() {
    val purple = Color(0xFF6C5CE7)

    var secondsLeft by remember { mutableStateOf(WORK_SECONDS) }
    var isRunning by remember { mutableStateOf(false) }
    var session by remember { mutableStateOf(1) }

    LaunchedEffect(isRunning) {
        while (isRunning && secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        }
        if (secondsLeft == 0) {
            isRunning = false
            session = if (session >= 4) 1 else session + 1
            secondsLeft = WORK_SECONDS
        }
    }

    fun reset() {
        isRunning = false
        secondsLeft = WORK_SECONDS
    }

    val minutes = secondsLeft / 60
    val secs = secondsLeft % 60
    val timeLabel = String.format("%02d:%02d", minutes, secs)
    val progress = 1f - (secondsLeft.toFloat() / WORK_SECONDS.toFloat())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F7FF))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Focus", fontSize = 34.sp, fontWeight = FontWeight.Black)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("POMODORO", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(260.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = 18.dp.toPx()
                    val s = Size(size.width - stroke, size.height - stroke)
                    val o = Offset(stroke / 2, stroke / 2)
                    drawArc(
                        color = Color(0xFFEDE8FF),
                        startAngle = 8f, sweepAngle = 344f, useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round),
                        size = s, topLeft = o
                    )
                    drawArc(
                        color = purple,
                        startAngle = 8f, sweepAngle = 344f * progress, useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round),
                        size = s, topLeft = o
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("FOCUS • WORK", fontSize = 13.sp, color = purple, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(timeLabel, fontSize = 56.sp, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFEDE8FF))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Session $session of 4", fontSize = 13.sp, color = purple, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { reset() },
                    modifier = Modifier.size(56.dp).clip(CircleShape).background(Color(0xFFF2F2F2))
                ) { Icon(Icons.Default.Refresh, contentDescription = "Reset") }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(
                    onClick = { isRunning = !isRunning },
                    modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.Black)
                ) {
                    Icon(
                        if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(
                    onClick = { },
                    modifier = Modifier.size(56.dp).clip(CircleShape).background(Color(0xFFF2F2F2))
                ) { Icon(Icons.Default.MoreVert, contentDescription = "More") }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("TODAY", "2h 14m", Modifier.weight(1f))
                StatCard("FOCUS", "86%", Modifier.weight(1f))
                StatCard("STREAK", "6 days", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF2A2A2A)),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.Info, contentDescription = null, tint = Color.White) }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("Deep Focus • Lo-fi", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Playing now", color = Color(0xFF9A9A9A), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF7F7F7))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontSize = 11.sp, color = Color.Gray, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
