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

data class Task(val id:Int, val title:String, val tag:String="", val time:String="", val priority:Int=0, val overdue:Boolean=false, var done:Boolean=false, val quadrant:Int=0, val hasSub:Boolean=false)

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
    val purple = Color(0xFF6D5BFF)
    val bg = Brush.verticalGradient(listOf(Color(0xFFF5F3FF), Color(0xFFEDE9FE)))

    // stable habit colors (not random each recomposition)
    val habitColors = remember { List(84){ i -> when((i*3+i/7)%5){0->Color(0xFFEBEDF0);1->Color(0xFFC6E48B);2->Color(0xFF7BC96F);3->Color(0xFF239A3B);else->Color(0xFF196127)} } }

    MaterialTheme{
        Box(Modifier.fillMaxSize().background(bg)){
            Scaffold(containerColor=Color.Transparent,
                topBar={
                    Box(Modifier.fillMaxWidth().background(purple).padding(20.dp)){
                        Text(when(tab){0->"Today\n${tasks.count{!it.done}} left • 2026-08-17";1->"Calendar";2->"Matrix";3->"Habits";else->"Pomo"}, color=Color.White, fontWeight=FontWeight.Bold, fontSize=if(tab==0)16.sp else 20.sp)
                    }
                },
                bottomBar={
                    Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment=Alignment.Center){
                        Card(Modifier.clip(RoundedCornerShape(30.dp)), colors=CardDefaults.cardColors(containerColor=Color(0xFF1A1A1A)), elevation=CardDefaults.cardElevation(12.dp)){
                            Row(Modifier.padding(8.dp), horizontalArrangement=Arrangement.spacedBy(4.dp)){
                                listOf("Today","Cal","Matrix","Habits","Pomo").forEachIndexed{ i, name ->
                                    val sel=i==tab
                                    Box(Modifier.clip(RoundedCornerShape(20.dp)).background(if(sel) Color.White else Color.Transparent).clickable{ tab=i }.padding(horizontal=16.dp, vertical=10.dp), contentAlignment=Alignment.Center){
                                        Text(name, fontSize=11.sp, color=if(sel) Color.Black else Color.White.copy(0.6f), fontWeight=if(sel) FontWeight.Bold else FontWeight.Normal)
                                    }
                                }
                            }
                        }
                    }
                }
            ){ pad ->
                Box(Modifier.padding(pad).fillMaxSize()){
                    when(tab){
                        0 -> LazyColumn(Modifier.fillMaxSize().padding(12.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
                            val overdue = tasks.filter{it.overdue &&!it.done}
                            val today = tasks.filter{!it.overdue &&!it.done}
                            if(overdue.isNotEmpty()){
                                item{ Card(Modifier.fillMaxWidth(), shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFFFE9E9))){ Column(Modifier.padding(12.dp)){ Text("Overdue • ${overdue.size}", color=Color(0xFFCC0000), fontWeight=FontWeight.Bold, fontSize=12.sp); overdue.forEach{ t-> TaskRow(t){ tasks=tasks.map{if(it.id==t.id) it.copy(done=!it.done) else it} } } } } }
                            }
                            item{ Card(Modifier.fillMaxWidth(), shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(4.dp)){ Column(Modifier.padding(12.dp)){ Text("Today", fontWeight=FontWeight.Bold); today.forEach{ t-> TaskRow(t){ tasks=tasks.map{if(it.id==t.id) it.copy(done=!it.done) else it} } } } } }
                            item{ Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.CenterVertically){ OutlinedTextField(value=input, onValueChange={input=it}, placeholder={Text("Add task...")}, modifier=Modifier.weight(1f), shape=RoundedCornerShape(16.dp)); Spacer(Modifier.width(8.dp)); Button(onClick={ if(input.isNotBlank()){ tasks=tasks+Task(tasks.size+100,input,"#work","Today",0, quadrant=0); input="" } }, shape=CircleShape, colors=ButtonDefaults.buttonColors(containerColor=purple)){ Text("+") } } }
                        }
                        1 -> Column(Modifier.fillMaxSize().padding(12.dp)){
                            Card(shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(4.dp)){
                                Column(Modifier.padding(16.dp)){
                                    Text("July 2025", fontWeight=FontWeight.Bold, fontSize=18.sp)
                                    Spacer(Modifier.height(12.dp))
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){ listOf("M","T","W","T","F","S","S").forEach{ Text(it, fontSize=12.sp, color=Color.Gray, modifier=Modifier.width(32.dp)) } }
                                    // FIXED: only 1-31, blanks after
                                    val days = (1..31).toList() + List(4){0}
                                    for(r in 0..4){
                                        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
                                            for(c in 0..6){
                                                val idx=r*7+c
                                                val day = if(idx<days.size) days[idx] else 0
                                                if(day==0) Box(Modifier.size(36.dp)) else {
                                                    val isSel=day==14
                                                    Box(Modifier.size(36.dp).clip(CircleShape).background(if(isSel) purple else Color.Transparent).clickable{}, contentAlignment=Alignment.Center){
                                                        Column(horizontalAlignment=Alignment.CenterHorizontally){ Text("$day", fontSize=13.sp, color=if(isSel) Color.White else Color.Black); if(day%3==0) Box(Modifier.size(4.dp).clip(CircleShape).background(purple)) }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Text("Tasks for July 14", fontWeight=FontWeight.Bold)
                            tasks.take(3).forEach{ TaskRow(it){} }
                        }
                        2 -> Column(Modifier.fillMaxSize().padding(8.dp), verticalArrangement=Arrangement.spacedBy(8.dp)){
                            Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                MatrixCard("Do First\nUrgent + Important", Color(0xFFFFD6D6), tasks.filter{it.quadrant==0}, Modifier.weight(1f).height(160.dp))
                                MatrixCard("Schedule\nNot Urgent + Important", Color(0xFFD6E8FF), tasks.filter{it.quadrant==1}, Modifier.weight(1f).height(160.dp))
                            }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                MatrixCard("Delegate\nUrgent + Not Important", Color(0xFFFFF0B3), tasks.filter{it.quadrant==2}, Modifier.weight(1f).height(160.dp))
                                MatrixCard("Eliminate\nNot Urgent + Not Important", Color(0xFFEDEDED), tasks.filter{it.quadrant==3}, Modifier.weight(1f).height(160.dp))
                            }
                            Spacer(Modifier.weight(1f))
                        }
                        3 -> Column(Modifier.fillMaxSize().padding(12.dp)){
                            Card(shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(4.dp)){
                                Column(Modifier.padding(16.dp)){
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){ Column{ Text("#work • Pushups", fontWeight=FontWeight.Bold); Text("Streak 🔥 7 days", fontSize=12.sp, color=Color.Gray) }; Text("84%", fontWeight=FontWeight.Bold, color=purple) }
                                    Spacer(Modifier.height(16.dp))
                                    // FIXED: stable colors
                                    Column(verticalArrangement=Arrangement.spacedBy(3.dp)){
                                        for(row in 0..6){
                                            Row(horizontalArrangement=Arrangement.spacedBy(3.dp)){
                                                for(col in 0..11){
                                                    val idx=row*12+col
                                                    if(idx<habitColors.size) Box(Modifier.size(14.dp).clip(RoundedCornerShape(3.dp)).background(habitColors[idx]))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        4 -> Column(Modifier.fillMaxSize(), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center){
                            Box(Modifier.size(220.dp).clip(CircleShape).background(Color.White).border(8.dp, purple, CircleShape), contentAlignment=Alignment.Center){ Column(horizontalAlignment=Alignment.CenterHorizontally){ Text("24:13", fontSize=36.sp, fontWeight=FontWeight.Bold); Text("Focus • Work", fontSize=12.sp, color=Color.Gray) } }
                            Spacer(Modifier.height(24.dp))
                            Button(onClick={}, shape=RoundedCornerShape(30.dp), modifier=Modifier.width(160.dp).height(48.dp), colors=ButtonDefaults.buttonColors(containerColor=purple)){ Text("Pause") }
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
    Card(mod, shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(containerColor=col)){ Column(Modifier.padding(12.dp)){ Text(title, fontWeight=FontWeight.Bold, fontSize=11.sp, lineHeight=12.sp); Spacer(Modifier.height(8.dp)); list.forEach{ Text("• ${it.title}", fontSize=11.sp, modifier=Modifier.padding(vertical=2.dp)) } } }
}

class MainActivity : ComponentActivity(){ override fun onCreate(savedInstanceState:Bundle?){ super.onCreate(savedInstanceState); setContent{ TickTickApp() } } }
