package com.sesmom.ticktickclone
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

data class Sub(val title:String, val done:Boolean)
data class TaskM(val id:Int, val title:String, val tag:String, val tagColor:Color, val tagText:Color, val time:String, val pri:Int, val done:Boolean=false, val subs:List<Sub> = emptyList(), val quad:Int=0)

@Composable
fun App(){
 var tab by remember { mutableStateOf(3) }
 val purple = Color(0xFF6D5BFF)
 val tasks = listOf(
  TaskM(1,"Finalize Q3 roadmap deck","#work", Color(0xFFEEE9FF), Color(0xFF6D5BFF),"09:00", 0),
  TaskM(2,"Submit expense report","#finance", Color(0xFFE0F5FF), Color(0xFF0099CC),"Yesterday", 1),
  TaskM(3,"Morning review & standup notes","#work", Color(0xFFEEE9FF), Color(0xFF6D5BFF),"08:30", 0, subs=listOf(Sub("Update Figma handoff",true), Sub("Prep talking points",false))),
  TaskM(4,"Design system audit - components","#design", Color(0xFFFFE4F0), Color(0xFFCC4D8C),"14:00", 1),
  TaskM(5,"Read 30 pages - Deep Work","#learning", Color(0xFFFFF3CC), Color(0xFFB8860B),"21:00", 0),
  TaskM(6,"Grocery run & meal prep","#personal", Color(0xFFD9FFEE), Color(0xFF2ECC71),"18:00", 0, done=true, quad=2)
 )
 var selectedDay by remember { mutableStateOf(15) }
 MaterialTheme{
  Box(Modifier.fillMaxSize().background(Color(0xFFF8F7FF))){
   Scaffold(containerColor=Color.Transparent,
    bottomBar={
     Box(Modifier.fillMaxWidth().padding(16.dp).padding(bottom=8.dp), contentAlignment=Alignment.Center){
      Card(Modifier.clip(RoundedCornerShape(32.dp)), colors=CardDefaults.cardColors(containerColor=Color(0xFF121212))){
       Row(Modifier.padding(horizontal=8.dp, vertical=6.dp)){
        listOf("Today","Cal","Matrix","Habits","Focus").forEachIndexed{ i,name ->
         val sel=i==tab
         Box(Modifier.clip(RoundedCornerShape(24.dp)).background(if(sel) Color.White else Color.Transparent).clickable{ tab=i }.padding(horizontal=18.dp, vertical=10.dp), contentAlignment=Alignment.Center){
          Column(horizontalAlignment=Alignment.CenterHorizontally){
           Text(when(name){ "Today"->"☰" "Cal"->"📅" "Matrix"->"⊞" "Habits"->"↻" else->"⏱" }, fontSize=14.sp, color=if(sel) Color.Black else Color.White.copy(0.5f))
           Text(name, fontSize=10.sp, color=if(sel) Color.Black else Color.White.copy(0.5f), fontWeight=if(sel) FontWeight.Bold else FontWeight.Normal)
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
      LazyColumn(Modifier.padding(pad).fillMaxSize().padding(horizontal=16.dp), verticalArrangement=Arrangement.spacedBy(16.dp)){
       item{
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
       item{
        Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFF4D4D))); Spacer(Modifier.width(8.dp)); Text("OVERDUE • 2", color=Color(0xFFFF6B6B), fontWeight=FontWeight.Bold, fontSize=12.sp, letterSpacing=1.sp) }
        Spacer(Modifier.height(10.dp))
        Card(shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFFFF3F2))){ Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(18.dp)){ OverdueRow(tasks[0]); OverdueRow(tasks[1]) } }
       }
       item{
        Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(purple)); Spacer(Modifier.width(8.dp)); Text("TODAY • 4", color=purple, fontWeight=FontWeight.Bold, fontSize=12.sp, letterSpacing=1.sp) }
        Spacer(Modifier.height(10.dp))
        Card(shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(3.dp)){
         Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(22.dp)){
          TodayRow(tasks[2]); TodayRow(tasks[3]); TodayRow(tasks[4])
          Column{
           Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.CenterVertically){
            Box(Modifier.size(28.dp).clip(CircleShape).background(purple), contentAlignment=Alignment.Center){ Text("✓", color=Color.White, fontSize=14.sp, fontWeight=FontWeight.Bold) }
            Spacer(Modifier.width(12.dp))
            Text("Grocery run & meal prep", color=Color(0xFFB0B0B0), textDecoration=TextDecoration.LineThrough, fontSize=14.sp, modifier=Modifier.weight(1f))
            Box(Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFE6FFF0)).padding(horizontal=8.dp, vertical=4.dp)){ Text("#personal", fontSize=11.sp, color=Color(0xFF2ECC71)) }
           }
           Spacer(Modifier.height(6.dp))
           Row(Modifier.padding(start=40.dp), verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFD0D0D0))); Spacer(Modifier.width(8.dp)); Text("18:00", fontSize=12.sp, color=Color(0xFFB0B0B0)) }
          }
         }
        }
        Spacer(Modifier.height(24.dp))
        Card(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(6.dp)){
         Row(Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment=Alignment.CenterVertically){
          Box(Modifier.size(32.dp).clip(CircleShape).border(1.5.dp, Color(0xFFE0E0E0), CircleShape))
          Spacer(Modifier.width(12.dp))
          Text("Add a task, e.g. Review PR #42", color=Color(0xFF9E9E9E), fontSize=13.sp, modifier=Modifier.weight(1f))
          Button(onClick={}, shape=RoundedCornerShape(14.dp), colors=ButtonDefaults.buttonColors(containerColor=purple), contentPadding=PaddingValues(horizontal=18.dp, vertical=10.dp)){ Text("+ Add", fontSize=13.sp, fontWeight=FontWeight.Bold) }
         }
        }
        Spacer(Modifier.height(100.dp))
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
         Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(24.dp)){ TodayRow(tasks[2]); TodayRow(tasks[3]) }
        }
        Spacer(Modifier.height(100.dp))
       }
      }
     }
     2 -> { // MATRIX - RESTORED 100%
      LazyColumn(Modifier.padding(pad).fillMaxSize().padding(horizontal=12.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
       item{ Spacer(Modifier.height(12.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){ Text("Matrix", fontSize=34.sp, fontWeight=FontWeight.Black); Box(Modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xFF121212)).padding(horizontal=16.dp, vertical=8.dp)){ Text("Eisenhower", color=Color.White, fontSize=12.sp) } } }
       item{
        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(12.dp)){
         Card(Modifier.weight(1f), shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFFFE0E0))){ Column(Modifier.padding(12.dp), verticalArrangement=Arrangement.spacedBy(10.dp)){ Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFF4D4D))); Spacer(Modifier.width(8.dp)); Column{ Text("DO FIRST", fontWeight=FontWeight.Black, fontSize=13.sp); Text("Urgent & Important", fontSize=11.sp, color=Color(0xFF8A5A5A)) } }; MatrixCard("Finalize Q3 roadmap deck"); MatrixCard("Morning review & standup notes"); MatrixCard("Client call - Acme Inc") } }
         Card(Modifier.weight(1f), shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFDDEBFF))){ Column(Modifier.padding(12.dp), verticalArrangement=Arrangement.spacedBy(10.dp)){ Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF4D8AFF))); Spacer(Modifier.width(8.dp)); Column{ Text("SCHEDULE", fontWeight=FontWeight.Black, fontSize=13.sp); Text("Not Urgent • Important", fontSize=11.sp) } }; MatrixCard("Submit expense report"); MatrixCard("Design system audit - components") } }
        }
       }
       item{
        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(12.dp)){
         Card(Modifier.weight(1f), shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFFFF3B8))){ Column(Modifier.padding(12.dp), verticalArrangement=Arrangement.spacedBy(10.dp)){ Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFFB300))); Spacer(Modifier.width(8.dp)); Column{ Text("DELEGATE", fontWeight=FontWeight.Black, fontSize=13.sp); Text("Urgent • Not Important", fontSize=11.sp) } }; Card(shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(containerColor=Color.White)){ Row(Modifier.padding(12.dp), verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(20.dp).clip(CircleShape).background(purple), contentAlignment=Alignment.Center){ Text("✓", color=Color.White, fontSize=12.sp) }; Spacer(Modifier.width(8.dp)); Text("Grocery run & meal prep", fontSize=12.sp, color=Color(0xFF9A9A9A), textDecoration=TextDecoration.LineThrough) } }; Spacer(Modifier.height(120.dp)) } }
         Card(Modifier.weight(1f), shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFF0F0F0))){ Column(Modifier.padding(12.dp), verticalArrangement=Arrangement.spacedBy(10.dp)){ Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF9A9A9A))); Spacer(Modifier.width(8.dp)); Column{ Text("ELIMINATE", fontWeight=FontWeight.Black, fontSize=13.sp); Text("Neither", fontSize=11.sp) } }; MatrixCard("Read 30 pages - Deep Work"); Spacer(Modifier.height(120.dp)) } }
        }
       }
       item{ Card(Modifier.fillMaxWidth(), shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFF121212))){ Row(Modifier.padding(16.dp), verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFF2A2A2A)), contentAlignment=Alignment.Center){ Text("i", color=Color.White, fontWeight=FontWeight.Bold) }; Spacer(Modifier.width(12.dp)); Text("Pro tip: Focus 60% of time in Q2 — that's where leverage lives.", color=Color.White.copy(0.8f), fontSize=13.sp) } }; Spacer(Modifier.height(100.dp)) }
      }
     }
     3 -> { // HABITS - 100% MOCKUP
      LazyColumn(Modifier.padding(pad).fillMaxSize().padding(horizontal=16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
       item{
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
         Column{ Text("Habits", fontSize=34.sp, fontWeight=FontWeight.Black); Text("Build streaks, not just tasks", fontSize=13.sp, color=Color(0xFF8A8A8A), modifier=Modifier.padding(top=4.dp)) }
         Box(Modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xFFFFF4E8)).border(1.dp, Color(0xFFFFE4C4), RoundedCornerShape(20.dp)).padding(horizontal=14.dp, vertical=8.dp)){ Row(verticalAlignment=Alignment.CenterVertically){ Text("🔥", fontSize=14.sp); Spacer(Modifier.width(6.dp)); Text("12 day streak", fontSize=12.sp, color=Color(0xFF8A4A2A), fontWeight=FontWeight.Bold) } }
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
        HabitRow("📚","Read 30 pages","8 days", true)
        Spacer(Modifier.height(8.dp))
        HabitRow("🧘","Meditate","3 days", false)
        Spacer(Modifier.height(8.dp))
        HabitRow("💧","Drink 2L water","21 days", true)
        Spacer(Modifier.height(100.dp))
       }
      }
     }
     4 -> { Box(Modifier.padding(pad).fillMaxSize()){ FocusScreen() } }
    }
   }
  }
 }
}

@Composable
fun MatrixCard(title:String){
 Card(shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(1.dp)){
  Row(Modifier.padding(12.dp), verticalAlignment=Alignment.CenterVertically){
   Box(Modifier.size(20.dp).clip(CircleShape).border(1.5.dp, Color(0xFFE0E0E0), CircleShape).background(Color.White))
   Spacer(Modifier.width(8.dp))
   Text(title, fontSize=12.sp, fontWeight=FontWeight.Medium, lineHeight=14.sp)
  }
 }
}
@Composable
fun HabitRow(emoji:String, title:String, days:String, done:Boolean){
 val purple = Color(0xFF6D5BFF)
 Card(shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(1.dp)){
  Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.SpaceBetween){
   Row(verticalAlignment=Alignment.CenterVertically){
    Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFF8F7FF)), contentAlignment=Alignment.Center){ Text(emoji, fontSize=18.sp) }
    Spacer(Modifier.width(12.dp))
    Column{ Text(title, fontSize=15.sp, fontWeight=FontWeight.Medium); Row(modifier=Modifier.padding(top=2.dp), verticalAlignment=Alignment.CenterVertically){ Text("🔥", fontSize=11.sp); Spacer(Modifier.width(4.dp)); Text(days, fontSize=12.sp, color=Color(0xFFAAAAAA)) } }
   }
   Box(Modifier.size(36.dp).clip(CircleShape).background(if(done) purple else Color.White).border(1.dp, if(done) purple else Color(0xFFE0E0E0), CircleShape), contentAlignment=Alignment.Center){ Text("✓", color=if(done) Color.White else Color(0xFF8A8A8A), fontSize=14.sp, fontWeight=FontWeight.Bold) }
  }
 }
}
@Composable
fun OverdueRow(t:TaskM){
 Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.Top){
  Box(Modifier.size(28.dp).clip(CircleShape).border(2.dp, Color(0xFFE0E0E0), CircleShape).background(Color.White))
  Spacer(Modifier.width(12.dp))
  Column(Modifier.weight(1f)){
   Row(verticalAlignment=Alignment.CenterVertically){ Text(t.title, fontWeight=FontWeight.Medium, fontSize=14.sp); Spacer(Modifier.width(8.dp)); Box(Modifier.clip(RoundedCornerShape(8.dp)).background(t.tagColor).padding(horizontal=8.dp, vertical=4.dp)){ Text(t.tag, fontSize=11.sp, color=t.tagText) } }
   Spacer(Modifier.height(6.dp)); Row{ Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFF4D4D))); Spacer(Modifier.width(8.dp)); Text(t.time, fontSize=12.sp, color=Color(0xFFFF6B6B)) }
  }
 }
}
@Composable
fun TodayRow(t:TaskM){
 Column{
  Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.Top){
   Box(Modifier.size(28.dp).clip(CircleShape).border(2.dp, Color(0xFFE0E0E0), CircleShape).background(Color.White))
   Spacer(Modifier.width(12.dp))
   Column(Modifier.weight(1f)){
    Row(verticalAlignment=Alignment.CenterVertically){ Text(t.title, fontWeight=FontWeight.Medium, fontSize=14.sp, modifier=Modifier.weight(1f, false)); Spacer(Modifier.width(8.dp)); Box(Modifier.clip(RoundedCornerShape(8.dp)).background(t.tagColor).padding(horizontal=8.dp, vertical=4.dp)){ Text(t.tag, fontSize=11.sp, color=t.tagText) } }
    Spacer(Modifier.height(6.dp)); Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(if(t.pri==0) Color(0xFFFF4D4D) else Color(0xFFFFC107))); Spacer(Modifier.width(8.dp)); Text(t.time, fontSize=12.sp, color=Color(0xFF8A8A8A)); if(t.subs.isNotEmpty()){ Spacer(Modifier.width(12.dp)); Text("${t.subs.count{it.done}}/${t.subs.size}", fontSize=12.sp, color=Color(0xFF8A8A8A)) } }
    if(t.subs.isNotEmpty()){
     Spacer(Modifier.height(14.dp)); Row{ Box(Modifier.width(2.dp).height(56.dp).background(Color(0xFFF0F0F0))); Spacer(Modifier.width(14.dp)); Column(verticalArrangement=Arrangement.spacedBy(12.dp)){ t.subs.forEach{ s-> Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(22.dp).clip(CircleShape).background(if(s.done) Color(0xFFEDE8FF) else Color.White).border(1.5.dp, if(s.done) Color(0xFF6D5BFF) else Color(0xFFE0E0E0), CircleShape), contentAlignment=Alignment.Center){ if(s.done) Text("✓", fontSize=10.sp, color=Color(0xFF6D5BFF)) }; Spacer(Modifier.width(8.dp)); Text(s.title, fontSize=13.sp, color=if(s.done) Color(0xFFA0A0A0) else Color(0xFF505050), textDecoration=if(s.done) TextDecoration.LineThrough else null) } } } }
    }
   }
  }
 }
}
class MainActivity : ComponentActivity(){ override fun onCreate(savedInstanceState:Bundle?){ super.onCreate(savedInstanceState); window.statusBarColor = AndroidColor.parseColor("#F5F3FF"); WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true; setContent{ App() } } } }
