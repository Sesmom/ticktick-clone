package com.sesmom.ticktickclone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.util.Calendar

data class PickedSchedule(val label: String, val timeLabel: String)

@Composable
fun DateTimePickerDialog(onDismiss: () -> Unit, onConfirm: (PickedSchedule) -> Unit) {
    val purple = Color(0xFF6C5CE7)
    var tab by remember { mutableStateOf(0) } // 0 = Date, 1 = Duration
    val density = LocalDensity.current
    var dateHeightPx by remember { mutableStateOf(0) }

    val cal = remember { Calendar.getInstance() }
    val today = remember { Calendar.getInstance() }
    var viewMonth by remember { mutableStateOf(cal.get(Calendar.MONTH)) }
    var viewYear by remember { mutableStateOf(cal.get(Calendar.YEAR)) }
    var selectedDay by remember { mutableStateOf(cal.get(Calendar.DAY_OF_MONTH)) }
    var selectedMonth by remember { mutableStateOf(cal.get(Calendar.MONTH)) }
    var selectedYear by remember { mutableStateOf(cal.get(Calendar.YEAR)) }

    val monthNames = listOf("January","February","March","April","May","June","July","August","September","October","November","December")
    val shortMonths = listOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")

    fun daysInMonth(month: Int, year: Int): Int {
        val c = Calendar.getInstance()
        c.set(year, month, 1)
        return c.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun firstWeekday(month: Int, year: Int): Int {
        val c = Calendar.getInstance()
        c.set(year, month, 1)
        val dow = c.get(Calendar.DAY_OF_WEEK) // 1=Sun..7=Sat
        return ((dow + 5) % 7) // convert to Mon=0..Sun=6
    }

    fun pickQuick(daysFromToday: Int) {
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, daysFromToday)
        selectedDay = c.get(Calendar.DAY_OF_MONTH)
        selectedMonth = c.get(Calendar.MONTH)
        selectedYear = c.get(Calendar.YEAR)
        viewMonth = selectedMonth
        viewYear = selectedYear
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.BottomCenter
        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .heightIn(min = if (dateHeightPx > 0) with(density) { dateHeightPx.toDp() } else 0.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color(0xFFF8F7FF))
                .clickable(enabled = false) {}
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .onGloballyPositioned { coordinates ->
                    if (tab == 0) dateHeightPx = coordinates.size.height
                }
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.clickable(onClick = onDismiss))

                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Date", fontSize = 17.sp, color = if (tab == 0) purple else Color(0xFF9A9A9A), fontWeight = if (tab == 0) FontWeight.Bold else FontWeight.Normal, modifier = Modifier.clickable { tab = 0 })
                            if (tab == 0) Box(Modifier.padding(top = 4.dp).width(28.dp).height(2.dp).background(purple))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Duration", fontSize = 17.sp, color = if (tab == 1) purple else Color(0xFF9A9A9A), fontWeight = if (tab == 1) FontWeight.Bold else FontWeight.Normal, modifier = Modifier.clickable { tab = 1 })
                            if (tab == 1) Box(Modifier.padding(top = 4.dp).width(50.dp).height(2.dp).background(purple))
                        }
                    }

                    Icon(Icons.Default.Check, contentDescription = "Confirm", tint = Color.Black, modifier = Modifier.clickable {
                        val label = "${shortMonths[selectedMonth]} $selectedDay"
                        onConfirm(PickedSchedule(label, "$label, 08:00"))
                        onDismiss()
                    })
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (tab == 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(monthNames[viewMonth], fontSize = 20.sp, fontWeight = FontWeight.Black)
                        Row {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Prev", modifier = Modifier.clickable {
                                if (viewMonth == 0) { viewMonth = 11; viewYear -= 1 } else viewMonth -= 1
                            })
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next", modifier = Modifier.clickable {
                                if (viewMonth == 11) { viewMonth = 0; viewYear += 1 } else viewMonth += 1
                            })
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun").forEach {
                            Text(it, fontSize = 12.sp, color = Color(0xFFAAAAAA), modifier = Modifier.width(40.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val leading = firstWeekday(viewMonth, viewYear)
                    val totalDays = daysInMonth(viewMonth, viewYear)
                    val rows = 6

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        for (r in 0 until rows) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                for (c in 0..6) {
                                    val dayNum = r * 7 + c - leading + 1
                                    Box(modifier = Modifier.width(40.dp).height(40.dp), contentAlignment = Alignment.Center) {
                                        if (dayNum in 1..totalDays) {
                                            val isSel = dayNum == selectedDay && viewMonth == selectedMonth && viewYear == selectedYear
                                            val isToday = dayNum == today.get(Calendar.DAY_OF_MONTH) && viewMonth == today.get(Calendar.MONTH) && viewYear == today.get(Calendar.YEAR)
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isSel) purple else Color.Transparent)
                                                    .clickable {
                                                        selectedDay = dayNum
                                                        selectedMonth = viewMonth
                                                        selectedYear = viewYear
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    "$dayNum",
                                                    fontSize = 15.sp,
                                                    color = if (isSel) Color.White else if (isToday) purple else Color.Black,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(modifier = Modifier.padding(horizontal = 20.dp).clip(RoundedCornerShape(16.dp)).background(Color.White)) {
                        ScheduleRow("Time", "None", Icons.Default.DateRange)
                        ScheduleRow("Reminder", "None", Icons.Default.Notifications)
                        ScheduleRow("Repeat", "None", Icons.Default.Refresh)
                    }
                } else {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(16.dp)) {
                                Column {
                                    Text("Date", fontSize = 13.sp, color = Color(0xFF9A9A9A))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("${shortMonths[selectedMonth]}, $selectedDay", fontSize = 16.sp, color = purple, fontWeight = FontWeight.Bold)
                                    Text("Today", fontSize = 12.sp, color = Color(0xFFB0B0B0))
                                }
                            }
                            Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(16.dp)) {
                                Column {
                                    Text("Time", fontSize = 13.sp, color = Color(0xFF9A9A9A))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("8:00am - 9:00am", fontSize = 15.sp, color = purple, fontWeight = FontWeight.Bold)
                                    Text("Duration: 1 hour", fontSize = 12.sp, color = Color(0xFFB0B0B0))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White).padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("All day", fontSize = 15.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(Color.White)) {
                            ScheduleRow("Reminder", "On time", Icons.Default.Notifications)
                            ScheduleRow("Repeat", "None", Icons.Default.Refresh)
                        }
                    }
                }
            }
        }
        }
    }
}

@Composable
fun QuickPickCustom2(label: String, iconContent: @Composable () -> Unit, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = onClick).width(72.dp)) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFEEE9FF)),
            contentAlignment = Alignment.Center
        ) {
            iconContent()
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, fontSize = 12.sp, color = Color(0xFF505050), textAlign = androidx.compose.ui.text.style.TextAlign.Center, lineHeight = 14.sp)
    }
}

@Composable
fun ScheduleRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = label, tint = Color(0xFF6C5CE7), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(label, fontSize = 15.sp)
        }
        Text(value, fontSize = 15.sp, color = Color(0xFFB0B0B0))
    }
}
