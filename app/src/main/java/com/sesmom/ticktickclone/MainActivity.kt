package com.sesmom.ticktickclone
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

data class Sub(val title:String, val done:Boolean)
data class TaskM(val id:Int, val title:String, val tag:String, val tagColor:Color, val tagText:Color, val time:String, val pri:Int, val done:Boolean=false, val subs:List<Sub> = emptyList(), val quad:Int=0, val desc:String="")

@Composable
fun App(){
 var tab by remember { mutableStateOf(3) }
 val purple = Color(0xFF6D5BFF)
 val taskViewModel: TaskViewModel = viewModel()
 var showAddDialog by remember { mutableStateOf(false) }
 var addDialogQuadrant by remember { mutableStateOf(0) }
 val dbTasks by taskViewModel.tasks.collectAsState()
 val tasks = dbTasks.mapIndexed { idx, dbTask ->
  val subs = if(idx==2) listOf(Sub("Update Figma handoff",true), Sub("Prep talking points",false)) else emptyList()
  dbTask.toTaskM().copy(subs = subs)
 }
 var selectedDay by remember { mutableStateOf(15) }
 MaterialTheme{
  Box(Modifier.fillMaxSize().background(Color(0xFFF8F7FF))){
   Scaffold(containerColor=Color.Transparent, contentWindowInsets=WindowInsets(0,0,0,0),
    floatingActionButton={
     if(tab==0){
      Box(Modifier.offset(y=20.dp).size(56.dp).shadow(8.dp, CircleShape).clip(CircleShape).background(Color(0xFF6C5CE7)).clickable{ showAddDialog=true }, contentAlignment=Alignment.Center){
       Text("+", color=Color.White, fontSize=28.sp, fontWeight=FontWeight.Bold)
      }
     }
    },
    bottomBar={
     Box(Modifier.fillMaxWidth().padding(16.dp).padding(bottom=8.dp), contentAlignment=Alignment.Center){
      Card(Modifier.clip(RoundedCornerShape(32.dp)), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(6.dp)){
       Row(Modifier.padding(horizontal=8.dp, vertical=6.dp)){
        listOf("Today","Cal","Matrix","Habits","Focus").forEachIndexed{ i,name ->
         val sel=i==tab
         Box(Modifier.clip(RoundedCornerShape(24.dp)).background(if(sel) Color(0xFF6C5CE7) else Color.Transparent).clickable{ tab=i }.padding(horizontal=16.dp, vertical=8.dp), contentAlignment=Alignment.Center){
          Column(horizontalAlignment=Alignment.CenterHorizontally){
           Text(when(name){ "Today"->"☰" "Cal"->"📅" "Matrix"->"⊞" "Habits"->"↻" else->"⏱" }, fontSize=15.sp, color=if(sel) Color.White else Color(0xFF9E9E9E))
           Text(name, fontSize=10.sp, color=if(sel) Color.White else Color(0xFF9E9E9E), fontWeight=if(sel) FontWeight.Bold else FontWeight.Normal)
          }
         }
        }
       }
      }
     }
    }
   ){ pad ->
    when(tab){
     0 -> { // TODAY - RESTORED 100%
      Column(Modifier.padding(pad).fillMaxSize()){
       Column(Modifier.padding(horizontal=16.dp)){
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.Top){
         Column{
          Text("Today", fontSize=34.sp, fontWeight=FontWeight.Black)
          Spacer(Modifier.height(6.dp))
          Row{ Text("Tuesday, July 15 • 5 left • ", fontSize=13.sp, color=Color(0xFF8A8A8A)); Text("14% done", fontSize=13.sp, color=purple, fontWeight=FontWeight.Medium) }
         }
         Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFEEEEEE)), contentAlignment=Alignment.Center){ Text("...") }
        }
        Spacer(Modifier.height(14.dp))
        Box(Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFFEDE8FF))){ Box(Modifier.fillMaxWidth(0.14f).fillMaxHeight().clip(RoundedCornerShape(3.dp)).background(purple)) }
       }
       LazyColumn(Modifier.padding(horizontal=16.dp).padding(top=8.dp), verticalArrangement=Arrangement.spacedBy(20.dp)){
       item{
        Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFF4D4D))); Spacer(Modifier.width(8.dp)); Text("OVERDUE • ${dbTasks.count{it.overdue}}", color=Color(0xFFFF6B6B), fontWeight=FontWeight.Black, fontSize=14.sp, letterSpacing=1.2.sp) }
        Spacer(Modifier.height(10.dp))
        Card(shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFFFF3F2))){ Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(18.dp)){ val overdueIds = dbTasks.filter{it.overdue}.map{it.id}.toSet(); tasks.filter{it.id in overdueIds}.forEach{ ov -> OverdueRow(ov, onToggle={ taskViewModel.toggleDoneById(it) }) } } }
       }
       item{
        val overdueIds2 = dbTasks.filter{it.overdue}.map{it.id}.toSet()
        val todayTasks = tasks.filter{ it.id !in overdueIds2 }
        Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(purple)); Spacer(Modifier.width(8.dp)); Text("TODAY • ${todayTasks.size}", color=purple, fontWeight=FontWeight.Black, fontSize=14.sp, letterSpacing=1.2.sp) }
        Spacer(Modifier.height(10.dp))
        Card(shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(3.dp)){
         Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(22.dp)){
          todayTasks.forEach{ tt -> TodayRow(tt, onToggle={ taskViewModel.toggleDoneById(it) }) }
         }
        }
        Spacer(Modifier.height(100.dp))
       }
      }
      }
     }
     1 -> { // CAL - RESTORED 100%
      LazyColumn(Modifier.padding(pad).fillMaxSize().padding(horizontal=16.dp), verticalArrangement=Arrangement.spacedBy(16.dp)){
       item{ Spacer(Modifier.height(12.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){ Text("Calendar", fontSize=32.sp, fontWeight=FontWeight.Black); Box(Modifier.clip(RoundedCornerShape(16.dp)).background(Color(0xFFEEE9FF)).padding(horizontal=16.dp, vertical=8.dp)){ Text("July 2025", fontSize=13.sp, color=purple, fontWeight=FontWeight.Bold) } } }
       item{
        Card(shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(2.dp)){
         Column(Modifier.padding(20.dp)){
          Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){ listOf("M","T","W","T","F","S","S").forEach{ Text(it, fontSize=12.sp, color=Color(0xFF9A9A9A), modifier=Modifier.width(36.dp)) } }
          Spacer(Modifier.height(12.dp))
          for(row in 0..4){
           Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
            for(col in 0..6){
             val idx=row*7+col
             if(idx>=31) Box(Modifier.size(44.dp))
             else {
              val d=idx+1; val isSel=d==selectedDay; val dots=mapOf(10 to 1, 11 to 1, 15 to 1, 16 to 2); val dotCount=dots[d]?:0
              Box(Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(if(isSel) purple else Color.Transparent).clickable{ selectedDay=d }, contentAlignment=Alignment.Center){
               Column(horizontalAlignment=Alignment.CenterHorizontally){ Text("$d", fontSize=15.sp, color=if(isSel) Color.White else Color.Black, fontWeight=if(isSel) FontWeight.Bold else FontWeight.Normal); Row(horizontalArrangement=Arrangement.spacedBy(2.dp)){ if(isSel) Box(Modifier.size(4.dp).clip(CircleShape).background(Color.White.copy(0.8f))) else repeat(dotCount){ Box(Modifier.size(4.dp).clip(CircleShape).background(purple)) } } }
              }
             }
            }
           }
           Spacer(Modifier.height(4.dp))
          }
         }
        }
       }
       item{
        Row(verticalAlignment=Alignment.CenterVertically){ Text("🕒", fontSize=12.sp); Spacer(Modifier.width(6.dp)); Text("JULY $selectedDay • 3 TASKS", fontSize=12.sp, color=Color(0xFF8A8A8A), fontWeight=FontWeight.Bold) }
        Spacer(Modifier.height(12.dp))
        Card(shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(3.dp)){
         Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(24.dp)){ TodayRow(tasks[2], onToggle={ taskViewModel.toggleDoneById(it) }); TodayRow(tasks[3], onToggle={ taskViewModel.toggleDoneById(it) }) }
        }
        Spacer(Modifier.height(100.dp))
       }
      }
     }
     2 -> { // MATRIX - RESTORED 100%
      LazyColumn(Modifier.padding(pad).fillMaxSize().padding(horizontal=12.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
       item{ Spacer(Modifier.height(12.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){ Text("Matrix", fontSize=34.sp, fontWeight=FontWeight.Black); Box(Modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xFF121212)).padding(horizontal=16.dp, vertical=8.dp)){ Text("Eisenhower", color=Color.White, fontSize=12.sp) } } }
       item{
        val q0 = tasks.filter{it.quad==0}
        val q1 = tasks.filter{it.quad==1}
        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(12.dp)){
         Card(Modifier.weight(1f), shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFFFE0E0))){ Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(14.dp)){ Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFF4D4D))); Spacer(Modifier.width(8.dp)); Column{ Text("DO FIRST", fontWeight=FontWeight.Black, fontSize=16.sp); Text("Urgent & Important", fontSize=11.sp, color=Color(0xFF8A5A5A)) } }; q0.forEach{ tk -> MatrixCard(tk, onToggle={ taskViewModel.toggleDoneById(it) }) }; Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color.White.copy(0.5f)).clickable{ addDialogQuadrant=0; showAddDialog=true }.padding(vertical=10.dp), contentAlignment=Alignment.Center){ Row(verticalAlignment=Alignment.CenterVertically){ Text("+", fontSize=16.sp, color=Color(0xFF505050)); Spacer(Modifier.width(6.dp)); Text("Add task", fontSize=13.sp, color=Color(0xFF505050)) } } } }
         Card(Modifier.weight(1f), shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFDDEBFF))){ Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(14.dp)){ Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF4D8AFF))); Spacer(Modifier.width(8.dp)); Column{ Text("SCHEDULE", fontWeight=FontWeight.Black, fontSize=16.sp); Text("Not Urgent • Important", fontSize=11.sp) } }; q1.forEach{ tk -> MatrixCard(tk, onToggle={ taskViewModel.toggleDoneById(it) }) }; Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color.White.copy(0.5f)).clickable{ addDialogQuadrant=1; showAddDialog=true }.padding(vertical=10.dp), contentAlignment=Alignment.Center){ Row(verticalAlignment=Alignment.CenterVertically){ Text("+", fontSize=16.sp, color=Color(0xFF505050)); Spacer(Modifier.width(6.dp)); Text("Add task", fontSize=13.sp, color=Color(0xFF505050)) } } } }
        }
       }
       item{
        val q2 = tasks.filter{it.quad==2}
        val q3 = tasks.filter{it.quad==3}
        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(12.dp)){
         Card(Modifier.weight(1f), shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFFFF3B8))){ Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(14.dp)){ Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFFB300))); Spacer(Modifier.width(8.dp)); Column{ Text("DELEGATE", fontWeight=FontWeight.Black, fontSize=16.sp); Text("Urgent • Not Important", fontSize=11.sp) } }; q2.forEach{ tk -> MatrixCard(tk, onToggle={ taskViewModel.toggleDoneById(it) }) }; Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color.White.copy(0.5f)).clickable{ addDialogQuadrant=2; showAddDialog=true }.padding(vertical=10.dp), contentAlignment=Alignment.Center){ Row(verticalAlignment=Alignment.CenterVertically){ Text("+", fontSize=16.sp, color=Color(0xFF505050)); Spacer(Modifier.width(6.dp)); Text("Add task", fontSize=13.sp, color=Color(0xFF505050)) } } } }
         Card(Modifier.weight(1f), shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFF0F0F0))){ Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(14.dp)){ Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF9A9A9A))); Spacer(Modifier.width(8.dp)); Column{ Text("ELIMINATE", fontWeight=FontWeight.Black, fontSize=16.sp); Text("Neither", fontSize=11.sp) } }; q3.forEach{ tk -> MatrixCard(tk, onToggle={ taskViewModel.toggleDoneById(it) }) }; Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color.White.copy(0.5f)).clickable{ addDialogQuadrant=3; showAddDialog=true }.padding(vertical=10.dp), contentAlignment=Alignment.Center){ Row(verticalAlignment=Alignment.CenterVertically){ Text("+", fontSize=16.sp, color=Color(0xFF505050)); Spacer(Modifier.width(6.dp)); Text("Add task", fontSize=13.sp, color=Color(0xFF505050)) } } } }
        }
       }
       item{ Card(Modifier.fillMaxWidth(), shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFF121212))){ Row(Modifier.padding(16.dp), verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFF2A2A2A)), contentAlignment=Alignment.Center){ Text("i", color=Color.White, fontWeight=FontWeight.Bold) }; Spacer(Modifier.width(12.dp)); Text("Pro tip: Focus 60% of time in Q2 — that's where leverage lives.", color=Color.White.copy(0.8f), fontSize=13.sp) } }; Spacer(Modifier.height(100.dp)) }
      }
     }
     3 -> { // HABITS - 100% MOCKUP
      LazyColumn(Modifier.padding(pad).fillMaxSize().padding(horizontal=16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
       item{
        val habitViewModel: HabitViewModel = viewModel()
        val habits by habitViewModel.habits.collectAsState()
        val maxStreak = habits.maxOfOrNull{it.streakDays} ?: 0
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
         Column{ Text("Habits", fontSize=34.sp, fontWeight=FontWeight.Black); Text("Build streaks, not just tasks", fontSize=13.sp, color=Color(0xFF8A8A8A), modifier=Modifier.padding(top=4.dp)) }
         Box(Modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xFFFFF4E8)).border(1.dp, Color(0xFFFFE4C4), RoundedCornerShape(20.dp)).padding(horizontal=14.dp, vertical=8.dp)){ Row(verticalAlignment=Alignment.CenterVertically){ Text("🔥", fontSize=14.sp); Spacer(Modifier.width(6.dp)); Text("$maxStreak day streak", fontSize=12.sp, color=Color(0xFF8A4A2A), fontWeight=FontWeight.Bold) } }
        }
       }
       item{
        Card(shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(2.dp)){
         Column(Modifier.padding(16.dp)){
          Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.Top){
           Row(verticalAlignment=Alignment.CenterVertically){
            Box(Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFFF3E8FF)), contentAlignment=Alignment.Center){ Text("💪", fontSize=22.sp) }
            Spacer(Modifier.width(12.dp))
            Column{ Text("Pushups", fontSize=16.sp, fontWeight=FontWeight.Bold); Row(modifier=Modifier.padding(top=2.dp)){ Box(Modifier.clip(RoundedCornerShape(6.dp)).background(Color(0xFFF0F0F0)).padding(horizontal=6.dp, vertical=2.dp)){ Text("#work", fontSize=11.sp, color=Color(0xFF8A8A8A)) }; Spacer(Modifier.width(6.dp)); Text("Daily • 50\nreps", fontSize=12.sp, color=Color(0xFF8A8A8A), lineHeight=13.sp) } }
           }
           Column(horizontalAlignment=Alignment.End){ Text("84%", fontSize=24.sp, fontWeight=FontWeight.Black); Text("+6% VS LAST\nMONTH", fontSize=11.sp, color=Color(0xFF10B981), fontWeight=FontWeight.Bold, lineHeight=12.sp) }
          }
          Spacer(Modifier.height(16.dp))
          val levels = listOf(Color(0xFFF5F3FF), Color(0xFFEDE8FF), Color(0xFFDCCFFF), Color(0xFFA78BFA), Color(0xFF6D5BFF), Color(0xFF4C1D95))
          Column(verticalArrangement=Arrangement.spacedBy(6.dp)){
           for(r in 0..6){
            Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){
             for(c in 0..11){
              val forced = when{ r==0 && c==0 -> levels[4]; r==0 && c==1 -> levels[4]; r==0 && c==6 -> levels[4]; r==0 && c==8 -> levels[4]; r==1 && c==8 -> levels[4]; r==1 && c==9 -> levels[4]; r==1 && c==10 -> levels[4]; r==2 && c==1 -> levels[4]; r==2 && c==3 -> levels[4]; r==2 && c==6 -> levels[4]; r==2 && c==10 -> levels[4]; r==2 && c==11 -> levels[4]; r==3 && c==5 -> levels[4]; r==3 && c==6 -> levels[4]; r==3 && c==7 -> levels[3]; r==4 && c==0 -> levels[4]; r==4 && c==1 -> levels[4]; r==4 && c==4 -> levels[3]; r==4 && c==5 -> levels[4]; r==4 && c==8 -> levels[4]; r==4 && c==11 -> levels[4]; r==5 && c==3 -> levels[4]; r==5 && c==4 -> levels[4]; r==5 && c==5 -> levels[4]; r==5 && c==6 -> levels[4]; r==6 && c==3 -> levels[4]; r==6 && c==4 -> levels[4]; r==6 && c==5 -> levels[4]; r==6 && c==6 -> levels[5]; r==6 && c==10 -> levels[4]; r==6 && c==11 -> levels[5]; else -> if(Random.nextBoolean()) levels[1] else levels[2] }
              Box(Modifier.size(22.dp).clip(RoundedCornerShape(6.dp)).background(forced))
             }
            }
           }
          }
          Spacer(Modifier.height(12.dp))
          Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){ Text("Apr", fontSize=12.sp, color=Color(0xFFAAAAAA)); Row(verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.spacedBy(4.dp)){ Text("Less", fontSize=11.sp, color=Color(0xFFAAAAAA)); levels.take(5).forEach{ Box(Modifier.size(12.dp).clip(RoundedCornerShape(3.dp)).background(it)) }; Text("More", fontSize=11.sp, color=Color(0xFFAAAAAA)) } }
         }
        }
       }
       item{
        val habitViewModel2: HabitViewModel = viewModel()
        val habits2 by habitViewModel2.habits.collectAsState()
        habits2.forEachIndexed{ idx, h ->
         HabitRow(h, onToggle={ habitViewModel2.toggleCheckIn(it) })
         if(idx < habits2.size-1) Spacer(Modifier.height(8.dp))
        }
        Spacer(Modifier.height(100.dp))
       }
      }
     }
     4 -> { Box(Modifier.padding(pad).fillMaxSize()){ FocusScreen() } }
    }
   }
  }
 }
 if(showAddDialog){
  AddTaskDialog(onDismiss={ showAddDialog=false }, initialQuadrant=addDialogQuadrant, onAdd={ title,tag,time,desc,quad -> taskViewModel.addTask(title,tag,time,desc,quad) })
 }
}

@Composable
fun MatrixCard(t:TaskM, onToggle:(Int)->Unit = {}){
 Card(Modifier.fillMaxWidth(), shape=RoundedCornerShape(18.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(1.dp)){
  Row(Modifier.padding(14.dp), verticalAlignment=Alignment.CenterVertically){
   Box(Modifier.size(24.dp).clip(CircleShape).border(1.5.dp, if(t.done) Color.Transparent else Color(0xFFE0E0E0), CircleShape).background(if(t.done) Color(0xFF6D5BFF) else Color.White).clickable{ onToggle(t.id) }, contentAlignment=Alignment.Center){ if(t.done) Text("✓", color=Color.White, fontSize=12.sp) }
   Spacer(Modifier.width(10.dp))
   Text(t.title, fontSize=15.sp, fontWeight=FontWeight.Medium, lineHeight=18.sp, color=if(t.done) Color(0xFF9A9A9A) else Color.Black, textDecoration=if(t.done) TextDecoration.LineThrough else null)
  }
 }
}
@Composable
fun HabitRow(h:Habit, onToggle:(Int)->Unit = {}){
 val purple = Color(0xFF6D5BFF)
 Card(shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(1.dp)){
  Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.SpaceBetween){
   Row(verticalAlignment=Alignment.CenterVertically){
    Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFF8F7FF)), contentAlignment=Alignment.Center){ Text(h.emoji, fontSize=18.sp) }
    Spacer(Modifier.width(12.dp))
    Column{ Text(h.title, fontSize=15.sp, fontWeight=FontWeight.Medium); Row(modifier=Modifier.padding(top=2.dp), verticalAlignment=Alignment.CenterVertically){ Text("🔥", fontSize=11.sp); Spacer(Modifier.width(4.dp)); Text("${h.streakDays} days", fontSize=12.sp, color=Color(0xFFAAAAAA)) } }
   }
   Box(Modifier.size(36.dp).clip(CircleShape).background(if(h.checkedToday) purple else Color.White).border(1.dp, if(h.checkedToday) purple else Color(0xFFE0E0E0), CircleShape).clickable{ onToggle(h.id) }, contentAlignment=Alignment.Center){ Text("✓", color=if(h.checkedToday) Color.White else Color(0xFF8A8A8A), fontSize=14.sp, fontWeight=FontWeight.Bold) }
  }
 }
}
@Composable
fun OverdueRow(t:TaskM, onToggle:(Int)->Unit = {}){
 Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.Top){
  Box(Modifier.size(28.dp).clip(CircleShape).border(2.dp, Color(0xFFE0E0E0), CircleShape).background(Color.White).clickable{ onToggle(t.id) })
  Spacer(Modifier.width(12.dp))
  Column(Modifier.weight(1f)){
   Row(verticalAlignment=Alignment.CenterVertically){ Text(t.title, fontWeight=FontWeight.Medium, fontSize=14.sp); Spacer(Modifier.width(8.dp)); Box(Modifier.clip(RoundedCornerShape(8.dp)).background(t.tagColor).padding(horizontal=8.dp, vertical=4.dp)){ Text(t.tag, fontSize=11.sp, color=t.tagText) } }
   if(t.desc.isNotEmpty()){ Spacer(Modifier.height(4.dp)); Text(t.desc, fontSize=12.sp, color=Color(0xFF9A9A9A), maxLines=1, overflow=androidx.compose.ui.text.style.TextOverflow.Ellipsis) }
   Spacer(Modifier.height(6.dp)); Row{ Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFF4D4D))); Spacer(Modifier.width(8.dp)); Text(t.time, fontSize=12.sp, color=Color(0xFFFF6B6B)) }
  }
 }
}
@Composable
fun TodayRow(t:TaskM, onToggle:(Int)->Unit = {}){
 Column{
  Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.Top){
   Box(Modifier.size(28.dp).clip(CircleShape).border(2.dp, Color(0xFFE0E0E0), CircleShape).background(if(t.done) Color(0xFF6D5BFF) else Color.White).clickable{ onToggle(t.id) })
   Spacer(Modifier.width(12.dp))
   Column(Modifier.weight(1f)){
    Row(verticalAlignment=Alignment.CenterVertically){ Text(t.title, fontWeight=FontWeight.Medium, fontSize=14.sp, color=if(t.done) Color(0xFFB0B0B0) else Color.Black, textDecoration=if(t.done) TextDecoration.LineThrough else null, modifier=Modifier.weight(1f, false)); Spacer(Modifier.width(8.dp)); Box(Modifier.clip(RoundedCornerShape(8.dp)).background(t.tagColor).padding(horizontal=8.dp, vertical=4.dp)){ Text(t.tag, fontSize=11.sp, color=t.tagText) } }
    if(t.desc.isNotEmpty()){ Spacer(Modifier.height(4.dp)); Text(t.desc, fontSize=12.sp, color=Color(0xFF9A9A9A), maxLines=1, overflow=androidx.compose.ui.text.style.TextOverflow.Ellipsis) }
    Spacer(Modifier.height(6.dp)); Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(if(t.pri==0) Color(0xFFFF4D4D) else Color(0xFFFFC107))); Spacer(Modifier.width(8.dp)); Text(t.time, fontSize=12.sp, color=Color(0xFF8A8A8A)); if(t.subs.isNotEmpty()){ Spacer(Modifier.width(12.dp)); Text("${t.subs.count{it.done}}/${t.subs.size}", fontSize=12.sp, color=Color(0xFF8A8A8A)) } }
    if(t.subs.isNotEmpty()){
     Spacer(Modifier.height(14.dp)); Row{ Box(Modifier.width(2.dp).height(56.dp).background(Color(0xFFF0F0F0))); Spacer(Modifier.width(14.dp)); Column(verticalArrangement=Arrangement.spacedBy(12.dp)){ t.subs.forEach{ s-> Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(22.dp).clip(CircleShape).background(if(s.done) Color(0xFFEDE8FF) else Color.White).border(1.5.dp, if(s.done) Color(0xFF6D5BFF) else Color(0xFFE0E0E0), CircleShape), contentAlignment=Alignment.Center){ if(s.done) Text("✓", fontSize=10.sp, color=Color(0xFF6D5BFF)) }; Spacer(Modifier.width(8.dp)); Text(s.title, fontSize=13.sp, color=if(s.done) Color(0xFFA0A0A0) else Color(0xFF505050), textDecoration=if(s.done) TextDecoration.LineThrough else null) } } } }
    }
   }
  }
 }
}
class MainActivity : ComponentActivity(){ override fun onCreate(savedInstanceState:Bundle?){ super.onCreate(savedInstanceState); window.statusBarColor = AndroidColor.parseColor("#F8F7FF"); WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true; setContent{ App() } } }
