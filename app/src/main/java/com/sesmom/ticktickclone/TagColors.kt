package com.sesmom.ticktickclone

import androidx.compose.ui.graphics.Color

fun tagBgColor(tag: String): Color = when (tag) {
    "#work" -> Color(0xFFEEE9FF)
    "#finance" -> Color(0xFFE0F5FF)
    "#design" -> Color(0xFFFFE4F0)
    "#learning" -> Color(0xFFFFF3CC)
    "#personal" -> Color(0xFFD9FFEE)
    else -> Color(0xFFF0F0F0)
}

fun tagTextColor(tag: String): Color = when (tag) {
    "#work" -> Color(0xFF6D5BFF)
    "#finance" -> Color(0xFF0099CC)
    "#design" -> Color(0xFFCC4D8C)
    "#learning" -> Color(0xFFB8860B)
    "#personal" -> Color(0xFF2ECC71)
    else -> Color(0xFF8A8A8A)
}

fun Task.toTaskM(): TaskM = TaskM(
    id = id,
    title = title,
    tag = tag,
    tagColor = tagBgColor(tag),
    tagText = tagTextColor(tag),
    time = time,
    pri = 0,
    done = done,
    quad = quadrant,
    desc = desc
)
