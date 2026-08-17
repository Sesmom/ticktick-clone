package com.sesmom.ticktickclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Task(val id:Int, val title:String, val tag:String="", val time:String="", val priority:Int=0, val overdue:Boolean=false, val done:Boolean=false, val quadrant:Int=0, val hasSub:Boolean=false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TickTickApp(){
    var tab by remember { mutableStateOf(0) }
    var tasks by remember { mutableStateOf(listOf(
        Task(1,"Finish TickTick clone UI", "#work", "9:00 AM", 2, true),
        Task(2,"Review PRs", "#work", "10:30 AM", 1, true, hasSub=true),
        Task(3,"Design Today list with overdue", "#design", "Today", 2, quadrant=0),
        Task(4,"Build Eisenhower Matrix", "#code", "Today", 1, quadrant=1),
        Task(5,"Push to GitHub", "#work", "Today", 0, quadrant=2),
        Task(6,"Habit: Pushups 30", "#work", "", 0, quadrant=0),
        Task(7,"Delegate meeting notes", "", "", 0, quadrant=2),
        Task(8,"Eliminate distractions", "", "", 0, quadrant=3)
    )) }
    var input by remember { mutableStateOf("") }
    var pomo by remember { mutableStateOf(24*60+13) }

    val bg = Brush.verticalGradient(listOf(Color(0xFFF5F3FF), Color(0xFFEDE9FE)))
    val purple = Color(0xFF6D5BFF)

    MaterialTheme{
        Box(Modifier.fillMaxSize().background(bg)){
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    when(tab){
                        0 -> Column(Modifier.fillMaxWidth().background(purple).padding(20.dp)){
                            Text("Today", color=Color.White, fontSize=28.sp, fontWeight=FontWeight.Bold)
                            Text("${tasks.count{!it.done}} left • ${java.time.LocalDate.now()}", color=Color.White.copy(0.8f), fontSize=13.sp)
                        }
                        1 -> TopAppBar(title={Text("Calendar", fontWeight=FontWeight.Bold)}, colors=TopAppBarDefaults.topAppBarColors(containerColor=purple, titleContentColor=Color.White))
                        2 -> TopAppBar(title={Text("Matrix", fontWeight=FontWeight.Bold)}, colors=TopAppBarDefaults.topAppBarColors(containerColor=purple, titleContentColor=Color.White))
                        3 -> TopAppBar(title={Text("Habits", fontWeight=FontWeight.Bold)}, colors=TopAppBarDefaults.topAppBarColors(containerColor=purple, titleContentColor=Color.White))
                        4 -> TopAppBar(title={Text("Pomo", fontWeight=FontWeight.Bold)}, colors=TopAppBarDefaults.topAppBarColors(containerColor=purple, titleContentColor=Color.White))
                    }
                },
                bottomBar = {
                    Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment=Alignment.Center){
                        Card(Modifier.clip(RoundedCornerShape(30.dp)), colors=CardDefaults.cardColors(containerColor=Color(0xFF1A1A1A)), elevation=CardDefaults.cardElevation(12.dp)){
                            Row(Modifier.padding(horizontal=12.dp, vertical=8.dp), horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                listOf("Today" to "☑","Cal" to "📅","Matrix" to "⊞","Habits" to "📈","Pomo" to "⏱").forEachIndexed{ i, p ->
                                    val sel = i==tab
                                    Box(Modifier.clip(RoundedCornerShape(20.dp)).background(if(sel) Color.White else Color.Transparent).clickable{ tab=i }.padding(horizontal=14.dp, vertical=8.dp), contentAlignment=Alignment.Center){
                                        Column(horizontalAlignment=Alignment.CenterHorizontally){
                                            Text(p.second, fontSize=14.sp)
                                            Text(p.first, fontSize=9.sp, color=if(sel) Color.Black else Color.White.copy(0.7f), fontWeight=if(sel) FontWeight.Bold else FontWeight.Normal)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            ){ pad ->
                Box(Modifier.padding(pad).fillMaxSize()){
                    when(tab){
                        0 -> {
                            LazyColumn(Modifier.fillMaxSize().padding(12.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
                                val overdue = tasks.filter{it.overdue && !it.done}
                                val today = tasks.filter{!it.overdue && !it.done}
                                if(overdue.isNotEmpty()){
                                    item{
                                        Card(Modifier.fillMaxWidth(), shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFFFE9E9))){
                                            Column(Modifier.padding(12.dp)){
                                                Text("Overdue • ${overdue.size}", color=Color(0xFFCC0000), fontWeight=FontWeight.Bold, fontSize=12.sp)
                                                overdue.forEach{ t-> TaskRow(t){ tasks=tasks.map{if(it.id==t.id) it.copy(done=!it.done) else it} }
                                                }
                                            }
                                        }
                                    }
                                }
                                item{
                                    Card(Modifier.fillMaxWidth(), shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(4.dp)){
                                        Column(Modifier.padding(12.dp)){
                                            Text("Today", fontWeight=FontWeight.Bold)
                                            Spacer(Modifier.height(8.dp))
                                            today.forEach{ t-> TaskRow(t){ tasks=tasks.map{if(it.id==t.id) it.copy(done=!it.done) else it} } }
                                            if(today.isEmpty()) Text("All done! 🎉", color=Color.Gray, modifier=Modifier.padding(8.dp))
                                        }
                                    }
                                }
                                item{
                                    Row(Modifier.fillMaxWidth().padding(top=8.dp), verticalAlignment=Alignment.CenterVertically){
                                        OutlinedTextField(value=input, onValueChange={input=it}, placeholder={Text("Add task...")}, modifier=Modifier.weight(1f), shape=RoundedCornerShape(16.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Button(onClick={ if(input.isNotBlank()){ tasks=tasks+Task(tasks.size+100,input,"#work","Today",0, quadrant=0); input="" } }, shape=CircleShape, colors=ButtonDefaults.buttonColors(containerColor=purple)){ Text("+") }
                                    }
                                }
                            }
                        }
                        1 -> {
                            Column(Modifier.fillMaxSize().padding(12.dp)){
                                Card(shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(4.dp)){
                                    Column(Modifier.padding(16.dp)){
                                        Text("July 2025", fontWeight=FontWeight.Bold, fontSize=18.sp)
                                        Spacer(Modifier.height(12.dp))
                                        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){ listOf("M","T","W","T","F","S","S").forEach{ Text(it, fontSize=12.sp, color=Color.Gray, modifier=Modifier.width(32.dp)) } }
                                        for(r in 0..4){
                                            Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
                                                for(c in 0..6){
                                                    val day = r*7+c+1
                                                    val isSel = day==14
                                                    Box(Modifier.size(36.dp).clip(CircleShape).background(if(isSel) purple else Color.Transparent).clickable{}, contentAlignment=Alignment.Center){
                                                        Column(horizontalAlignment=Alignment.CenterHorizontally){
                                                            Text("$day", fontSize=13.sp, color=if(isSel) Color.White else Color.Black)
                                                            if(day%3==0) Box(Modifier.size(4.dp).clip(CircleShape).background(purple))
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(Modifier.height(12.dp))
                                Text("Tasks for July 14", fontWeight=FontWeight.Bold)
                                LazyColumn{ items(tasks.take(3)){ TaskRow(it){} } }
                            }
                        }
                        2 -> {
                            Column(Modifier.fillMaxSize().padding(8.dp), verticalArrangement=Arrangement.spacedBy(8.dp)){
                                Row(Modifier.weight(1f), horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                    MatrixCard("Do First\nUrgent + Important", Color(0xFFFFD6D6), tasks.filter{it.quadrant==0}, Modifier.weight(1f))
                                    MatrixCard("Schedule\nNot Urgent + Important", Color(0xFFD6E8FF), tasks.filter{it.quadrant==1}, Modifier.weight(1f))
                                }
                                Row(Modifier.weight(1f), horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                    MatrixCard("Delegate\nUrgent + Not Important", Color(0xFFFFF0B3), tasks.filter{it.quadrant==2}, Modifier.weight(1f))
                                    MatrixCard("Eliminate\nNot Urgent + Not Important", Color(0xFFEDEDED), tasks.filter{it.quadrant==3}, Modifier.weight(1f))
                                }
                            }
                        }
                        3 -> {
                            Column(Modifier.fillMaxSize().padding(12.dp)){
                                Card(shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(4.dp)){
                                    Column(Modifier.padding(16.dp)){
                                        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
                                            Column{ Text("#work • Pushups", fontWeight=FontWeight.Bold); Text("Streak 🔥 7 days", fontSize=12.sp, color=Color.Gray) }
                                            Text("84%", fontWeight=FontWeight.Bold, color=purple)
                                        }
                                        Spacer(Modifier.height(16.dp))
                                        Row(horizontalArrangement=Arrangement.spacedBy(3.dp)){ for(i in 1..84){ val lvl = (0..4).random(); val col = when(lvl){0->Color(0xFFEBEDF0);1->Color(0xFFC6E48B);2->Color(0xFF7BC96F);3->Color(0xFF239A3B);else->Color(0xFF196127)}; if(i%12==0) Spacer(Modifier.width(4.dp)); Box(Modifier.size(12.dp).clip(RoundedCornerShape(3.dp)).background(col)) } }
                                    }
                                }
                            }
                        }
                        4 -> {
                            Column(Modifier.fillMaxSize(), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center){
                                Box(Modifier.size(220.dp).clip(CircleShape).background(Color.White).border(8.dp, purple, CircleShape), contentAlignment=Alignment.Center){
                                    Column(horizontalAlignment=Alignment.CenterHorizontally){
                                        Text(String.format("%02d:%02d", pomo/60, pomo%60), fontSize=36.sp, fontWeight=FontWeight.Bold)
                                        Text("Focus • Work", fontSize=12.sp, color=Color.Gray)
                                    }
                                }
                                Spacer(Modifier.height(24.dp))
                                Button(onClick={}, shape=RoundedCornerShape(30.dp), modifier=Modifier.width(160.dp).height(48.dp), colors=ButtonDefaults.buttonColors(containerColor=purple)){ Text("Pause") }
                                Spacer(Modifier.height(12.dp))
                                Text("Same ring as HTML mockup - timer logic next", fontSize=12.sp, color=Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskRow(t:Task, onToggle:()->Unit){
    Row(Modifier.fillMaxWidth().padding(vertical=6.dp).clickable{ onToggle() }, verticalAlignment=Alignment.CenterVertically){
        Box(Modifier.size(20.dp).clip(CircleShape).border(2.dp, if(t.done) Color(0xFF6D5BFF) else Color(0xFFD0D0D0), CircleShape).background(if(t.done) Color(0xFF6D5BFF) else Color.Transparent), contentAlignment=Alignment.Center){ if(t.done) Text("✓", color=Color.White, fontSize=10.sp) }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)){
            Text(t.title, textDecoration=if(t.done) TextDecoration.LineThrough else null, color=if(t.done) Color.Gray else Color.Black, fontSize=14.sp)
            if(t.tag.isNotEmpty() || t.time.isNotEmpty()) Row{ if(t.tag.isNotEmpty()) Text(t.tag, fontSize=11.sp, color=Color(0xFF6D5BFF), modifier=Modifier.background(Color(0xFFEEEAFF), RoundedCornerShape(6.dp)).padding(horizontal=6.dp, vertical=2.dp)); if(t.time.isNotEmpty()){ Spacer(Modifier.width(6.dp)); Text(t.time, fontSize=11.sp, color=Color.Gray) } }
            if(t.hasSub) Text("↳ 2 subtasks", fontSize=11.sp, color=Color.Gray, modifier=Modifier.padding(start=12.dp, top=2.dp))
        }
        if(t.priority==2) Box(Modifier.size(8.dp).clip(CircleShape).background(Color.Red)) else if(t.priority==1) Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFFC107)))
    }
}

@Composable
fun MatrixCard(title:String, col:Color, list:List<Task>, mod:Modifier){
    Card(mod, shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(containerColor=col)){
        Column(Modifier.padding(12.dp)){
            Text(title, fontWeight=FontWeight.Bold, fontSize=11.sp, lineHeight=12.sp)
            Spacer(Modifier.height(8.dp))
            list.forEach{ Text("• ${it.title}", fontSize=11.sp, modifier=Modifier.padding(vertical=2.dp)) }
        }
    }
}

class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContent{ TickTickApp() }
    }
}
