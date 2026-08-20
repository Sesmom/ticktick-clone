package com.sesmom.ticktickclone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onAdd: (String, String, String, String) -> Unit) {
    val purple = Color(0xFF6C5CE7)
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf("#work") }
    var showTagPicker by remember { mutableStateOf(false) }
    var showNewTagInput by remember { mutableStateOf(false) }
    var newTagText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val categoryViewModel: CategoryViewModel = viewModel()
    val categories by categoryViewModel.categories.collectAsState()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    fun submit() {
        if (title.isNotBlank()) {
            onAdd(title, selectedTag, "No time", desc)
            onDismiss()
        }
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
                    .imePadding()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color.White)
                    .pointerInput(Unit) { detectTapGestures {} }
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp).padding(top = 20.dp, bottom = 24.dp)) {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("What would you like to do?", color = Color(0xFFB0B0B0), fontSize = 17.sp) },
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(fontSize = 17.sp)
                    )

                    TextField(
                        value = desc,
                        onValueChange = { desc = it },
                        placeholder = { Text("Description", color = Color(0xFFB0B0B0), fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(fontSize = 14.sp)
                    )

                    if (showTagPicker) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(categories.size) { i ->
                                    val cat = categories[i]
                                    val sel = cat.name == selectedTag
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (sel) purple else Color(0xFFF0F0F0))
                                            .clickable { selectedTag = cat.name; showTagPicker = false }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(cat.name, fontSize = 12.sp, color = if (sel) Color.White else Color(0xFF8A8A8A))
                                    }
                                }
                                item {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0xFFF0F0F0))
                                            .clickable { showNewTagInput = true }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Add, contentDescription = "New category", tint = Color(0xFF8A8A8A), modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("New", fontSize = 12.sp, color = Color(0xFF8A8A8A))
                                        }
                                    }
                                }
                            }
                        }

                        if (showNewTagInput) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TextField(
                                    value = newTagText,
                                    onValueChange = { newTagText = it },
                                    placeholder = { Text("Category name", fontSize = 13.sp, color = Color(0xFFB0B0B0)) },
                                    modifier = Modifier.weight(1f),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color(0xFFF7F7F7),
                                        focusedContainerColor = Color(0xFFF7F7F7),
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent
                                    ),
                                    textStyle = TextStyle(fontSize = 13.sp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(purple)
                                        .clickable {
                                            if (newTagText.isNotBlank()) {
                                                categoryViewModel.addCategory(newTagText)
                                                selectedTag = if (newTagText.startsWith("#")) newTagText else "#$newTagText"
                                                newTagText = ""
                                                showNewTagInput = false
                                                showTagPicker = false
                                            }
                                        }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Text("Add", color = Color.White, fontSize = 13.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            Icon(Icons.Default.DateRange, contentDescription = "Date", tint = Color(0xFF9A9A9A))
                            Icon(Icons.Default.Star, contentDescription = "Priority", tint = Color(0xFF9A9A9A))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { showTagPicker = !showTagPicker; showNewTagInput = false }
                            ) {
                                Icon(Icons.Default.Info, contentDescription = "Tag", tint = if (showTagPicker) purple else Color(0xFF9A9A9A))
                            }
                            Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color(0xFF9A9A9A))
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (title.isNotBlank()) purple else Color(0xFFE0E0E0))
                                .clickable { submit() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Add task",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
