package com.sesmom.ticktickclone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onAdd: (String, String, String) -> Unit) {
    val purple = Color(0xFF6C5CE7)
    var title by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf("#work") }
    var time by remember { mutableStateOf("") }
    val tags = listOf("#work", "#finance", "#design", "#learning", "#personal")

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
            Column {
                Text("New Task", fontSize = 20.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("What do you need to do?") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    placeholder = { Text("Time (e.g. 14:00)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Tag", fontSize = 13.sp, color = Color(0xFF8A8A8A))
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    tags.forEach { tag ->
                        val sel = tag == selectedTag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (sel) purple else Color(0xFFF0F0F0))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                tag,
                                fontSize = 12.sp,
                                color = if (sel) Color.White else Color(0xFF8A8A8A)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color(0xFF8A8A8A))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onAdd(title, selectedTag, time.ifBlank { "No time" })
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = purple),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Add Task", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
