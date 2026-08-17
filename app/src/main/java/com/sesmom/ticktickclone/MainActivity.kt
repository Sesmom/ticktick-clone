package com.sesmom.ticktickclone
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TaskX(val id:Int, var title:String, var done:Boolean=false, var priority:Int=0, var quadrant:Int=0, var habit:String?=null)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(){
    var tab by remember { mutableStateOf(0) }
    var tasks by remember { mutableStateOf(listOf(
        TaskX(1,"Design Today UI - TickTick style",false,2,0,"#work"),
        TaskX(2,"Build Eisenhower Matrix",false,1,1),
        TaskX(3,"Habit tracker #work - pushups",false,0,0,"#work"),
        TaskX(4,"Pomodoro 25/5 timer",false,2,2),
        TaskX(5,"Calendar month view",true,0,0)
    )) }
    var input by remember { mutableStateOf("") }
    var pomoRunning by remember { mutableStateOf(false) }
    var pomoSec by remember { mutableStateOf(25*60) }
    val tabs = listOf("Today","Calendar","Matrix","Habits","Pomo")

    MaterialTheme(colorScheme = lightColorScheme()) {
        Scaffold(
            topBar = { TopAppBar(title={Text(tabs[tab], fontWeight=FontWeight.Bold)}, colors=TopAppBarDefaults.topAppBarColors(containerColor=Color(0xFF6D5BFF), titleContentColor=Color.White)) },
            bottomBar = {
                NavigationBar {
                    tabs.forEachIndexed { i, t ->
                        NavigationBarItem(selected=i==tab, onClick={tab=i}, label={Text(t, fontSize=10.sp)}, icon={Text(when(i){0->"☑";1->"📅";2->"⊞";3->"📈";else->"⏱"})})
                    }
                }
            }
        ){ pad ->
            Column(Modifier.padding(pad).fillMaxSize().background(Color(0xFFF7F7FF)).padding(12.dp)){
                when(tab){
                    0 -> {
                        Text("Today • ${tasks.count{it.done==false}} left", fontWeight=FontWeight.Bold, fontSize=18.sp)
                        Spacer(Modifier.height(12.dp))
                        LazyColumn(Modifier.weight(1f)){
                            items(tasks.filter{it.done==false}, key={it.id}){ taskItem ->
                                Card(Modifier.fillMaxWidth().padding(vertical=4.dp).clip(RoundedCornerShape(12.dp)).clickable{ tasks=tasks.map{ x-> if(x.id==taskItem.id) x.copy(done=true) else x } }, elevation=CardDefaults.cardElevation(2.dp)){
                                    Row(Modifier.padding(12.dp), verticalAlignment=Alignment.CenterVertically){
                                        Checkbox(checked=taskItem.done, onCheckedChange={ isChecked -> tasks=tasks.map{ x-> if(x.id==taskItem.id) x.copy(done=isChecked) else x } })
                                        Column(Modifier.weight(1f).padding(start=8.dp)){
                                            Text(taskItem.title, fontWeight=FontWeight.Medium)
                                            if(taskItem.habit!=null) Text(taskItem.habit!!, fontSize=12.sp, color=Color(0xFF6D5BFF))
                                        }
                                        Text(when(taskItem.priority){2->"🔴";1->"🟡";else->"⚪"})
                                    }
                                }
                            }
                        }
                        Row(verticalAlignment=Alignment.CenterVertically){
                            OutlinedTextField(value=input, onValueChange={input=it}, placeholder={Text("Add task like the HTML")}, modifier=Modifier.weight(1f), shape=RoundedCornerShape(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Button(onClick={ if(input.isNotBlank()){ tasks=tasks+TaskX(tasks.size+10,input); input="" } }, shape=RoundedCornerShape(16.dp)){ Text("+ Add") }
                        }
                    }
                    1 -> {
                        Text("Calendar", fontWeight=FontWeight.Bold, fontSize=18.sp)
                        Spacer(Modifier.height(12.dp))
                        LazyRow{ items(listOf("Mon 12","Tue 13","Wed 14","Thu 15","Fri 16","Sat 17","Sun 18")){ d-> Card(Modifier.padding(4.dp).width(80.dp), colors=CardDefaults.cardColors(containerColor=Color.White)){ Column(Modifier.padding(12.dp), horizontalAlignment=Alignment.CenterHorizontally){ Text(d, fontSize=12.sp); Text("${tasks.size} tasks", fontSize=10.sp, color=Color.Gray) } } } }
                        Spacer(Modifier.height(12.dp))
                        LazyColumn{ items(tasks){ t-> Text("• ${t.title}", modifier=Modifier.padding(6.dp)) } }
                    }
                    2 -> {
                        Text("Eisenhower Matrix", fontWeight=FontWeight.Bold, fontSize=18.sp)
                        Spacer(Modifier.height(8.dp))
                        val quads = listOf("Do First\nUrgent+Important","Schedule\nNot Urgent+Important","Delegate\nUrgent+Not Important","Eliminate\nNot Urgent+Not Important")
                        Column(Modifier.weight(1f)){
                            Row(Modifier.weight(1f)){
                                quads.take(2).forEachIndexed{ idx, q ->
                                    Card(Modifier.weight(1f).fillMaxHeight().padding(4.dp), colors=CardDefaults.cardColors(containerColor=when(idx){0->Color(0xFFFFE0E0); else->Color(0xFFE0F0FF)})){ Column(Modifier.padding(8.dp)){ Text(q, fontWeight=FontWeight.Bold, fontSize=12.sp); tasks.filter{it.quadrant==idx}.forEach{ Text("• ${it.title}", fontSize=11.sp) } } }
                                }
                            }
                            Row(Modifier.weight(1f)){
                                quads.drop(2).forEachIndexed{ idx, q ->
                                    val realIdx = idx+2
                                    Card(Modifier.weight(1f).fillMaxHeight().padding(4.dp), colors=CardDefaults.cardColors(containerColor=when(realIdx){2->Color(0xFFFFF5CC); else->Color(0xFFE8E8E8)})){ Column(Modifier.padding(8.dp)){ Text(q, fontWeight=FontWeight.Bold, fontSize=12.sp); tasks.filter{it.quadrant==realIdx}.forEach{ Text("• ${it.title}", fontSize=11.sp) } } }
                                }
                            }
                        }
                    }
                    3 -> {
                        Text("Habit Tracker #work", fontWeight=FontWeight.Bold, fontSize=18.sp)
                        Spacer(Modifier.height(12.dp))
                        LazyRow{ items((1..30).toList()){ d-> Box(Modifier.padding(2.dp).size(22.dp).clip(RoundedCornerShape(4.dp)).background(if(d%3==0) Color(0xFF6D5BFF) else Color(0xFFE0E0E0))) } }
                        Spacer(Modifier.height(16.dp))
                        tasks.filter{it.habit!=null}.forEach{ t-> Row(Modifier.fillMaxWidth().padding(vertical=6.dp), horizontalArrangement=Arrangement.SpaceBetween){ Text(t.title); Text(if(t.done)"✅" else "⬜") } }
                    }
                    4 -> {
                        Column(Modifier.fillMaxSize(), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center){
                            Text("Pomodoro", fontWeight=FontWeight.Bold, fontSize=24.sp)
                            Spacer(Modifier.height(20.dp))
                            Card(Modifier.size(180.dp), shape=RoundedCornerShape(90.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFF6D5BFF))){ Box(Modifier.fillMaxSize(), contentAlignment=Alignment.Center){ Text(String.format("%02d:%02d", pomoSec/60, pomoSec%60), color=Color.White, fontSize=32.sp, fontWeight=FontWeight.Bold) } }
                            Spacer(Modifier.height(20.dp))
                            Button(onClick={ pomoRunning =!pomoRunning }){ Text(if(pomoRunning)"Pause" else "Start 25:00") }
                        }
                    }
                }
            }
        }
    }
}

class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContent{ App() }
    }
}
