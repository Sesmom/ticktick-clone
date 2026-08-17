package com.sesmom.ticktickclone
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

data class Sub(val title:String, val done:Boolean)
data class TaskM(val id:Int, val title:String, val tag:String, val tagColor:Color, val tagText:Color, val time:String, val pri:Int, val done:Boolean=false, val subs:List<Sub> = emptyList(), val overdue:Boolean=false, val days:List<Int> = emptyList())

@Composable
fun App(){
 var tab by remember { mutableStateOf(1) } // start on Cal to test
 val purple = Color(0xFF6D5BFF)
 val tasks = listOf(
  TaskM(1,"Finalize Q3 roadmap deck","#work", Color(0xFFEEE9FF), Color(0xFF6D5BFF),"09:00", 0, overdue=true, days=listOf()),
  TaskM(2,"Submit expense report","#finance", Color(0xFFE0F5FF), Color(0xFF0099CC),"Yesterday", 1, overdue=true),
  TaskM(3,"Morning review & standup notes","#work", Color(0xFFEEE9FF), Color(0xFF6D5BFF),"08:30", 0, subs=listOf(Sub("Update Figma handoff",true), Sub("Prep talking points",false)), days=listOf(10,15,16)),
  TaskM(4,"Design system audit - components","#design", Color(0xFFFFE4F0), Color(0xFFCC4D8C),"14:00", 1, days=listOf(11,16)),
  TaskM(5,"Read 30 pages - Deep Work","#learning", Color(0xFFFFF3CC), Color(0xFFB8860B),"21:00", 0),
  TaskM(6,"Grocery run & meal prep","#personal", Color(0xFFD9FFEE), Color(0xFF2ECC71),"18:00", 0, done=true)
 )
 var selectedDay by remember { mutableStateOf(15) }
 MaterialTheme{
  Box(Modifier.fillMaxSize().background(Color(0xFFF8F7FF))){
   Scaffold(containerColor=Color.Transparent,
    bottomBar={
     Box(Modifier.fillMaxWidth().padding(16.dp).padding(bottom=8.dp), contentAlignment=Alignment.Center){
      Card(Modifier.clip(RoundedCornerShape(32.dp)), colors=CardDefaults.cardColors(containerColor=Color(0xFF121212))){
       Row(Modifier.padding(horizontal=8.dp, vertical=6.dp), horizontalArrangement=Arrangement.spacedBy(2.dp)){
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
    if(tab==0){
     // TODAY (locked, same as before)
     LazyColumn(Modifier.padding(pad).fillMaxSize().padding(horizontal=16.dp), verticalArrangement=Arrangement.spacedBy(16.dp)){
      item{
       Spacer(Modifier.height(16.dp))
       Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){ Column{ Text("Today", fontSize=34.sp, fontWeight=FontWeight.Black); Row{ Text("Tuesday, July 15 • 5 left • ", fontSize=13.sp, color=Color.Gray); Text("14% done", fontSize=13.sp, color=purple) } }; Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFEEEEEE)), contentAlignment=Alignment.Center){ Text("...") } }
       Spacer(Modifier.height(14.dp)); Box(Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFFEDE8FF))){ Box(Modifier.fillMaxWidth(0.14f).fillMaxHeight().background(purple)) }
      }
      item{
       Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFF4D4D))); Spacer(Modifier.width(8.dp)); Text("OVERDUE • 2", color=Color(0xFFFF6B6B), fontWeight=FontWeight.Bold, fontSize=12.sp) }
       Spacer(Modifier.height(10.dp))
       Card(shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFFFF3F2))){ Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(18.dp)){ OverdueRow(tasks[0]); OverdueRow(tasks[1]) } }
      }
      item{
       Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(purple)); Spacer(Modifier.width(8.dp)); Text("TODAY • 4", color=purple, fontWeight=FontWeight.Bold, fontSize=12.sp) }
       Spacer(Modifier.height(10.dp))
       Card(shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(3.dp)){
        Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(22.dp)){
         TodayRow(tasks[2]); TodayRow(tasks[3]); TodayRow(tasks[4])
         Column{ Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(28.dp).clip(CircleShape).background(purple), contentAlignment=Alignment.Center){ Text("✓", color=Color.White) }; Spacer(Modifier.width(12.dp)); Text("Grocery run & meal prep", color=Color(0xFFB0B0B0), textDecoration=TextDecoration.LineThrough, modifier=Modifier.weight(1f)); Box(Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFE6FFF0)).padding(horizontal=8.dp, vertical=4.dp)){ Text("#personal", fontSize=11.sp, color=Color(0xFF2ECC71)) } }; Row(Modifier.padding(start=40.dp)){ Box(Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFD0D0D0))); Spacer(Modifier.width(8.dp)); Text("18:00", fontSize=12.sp, color=Color(0xFFB0B0B0)) } }
        }
       }
       Spacer(Modifier.height(24.dp))
       Card(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(6.dp)){
        Row(Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(32.dp).clip(CircleShape).border(1.5.dp, Color(0xFFE0E0E0), CircleShape)); Spacer(Modifier.width(12.dp)); Text("Add a task, e.g. Review PR #42", color=Color(0xFF9E9E9E), fontSize=13.sp, modifier=Modifier.weight(1f)); Button(onClick={}, shape=RoundedCornerShape(14.dp), colors=ButtonDefaults.buttonColors(containerColor=purple)){ Text("+ Add") } }
       }
       Spacer(Modifier.height(100.dp))
      }
     }
    } else if(tab==1){
     // CALENDAR - EXACT MOCKUP
     LazyColumn(Modifier.padding(pad).fillMaxSize().padding(horizontal=16.dp), verticalArrangement=Arrangement.spacedBy(16.dp)){
      item{
       Spacer(Modifier.height(12.dp))
       Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
        Text("Calendar", fontSize=32.sp, fontWeight=FontWeight.Black, color=Color.Black)
        Box(Modifier.clip(RoundedCornerShape(16.dp)).background(Color(0xFFEEE9FF)).padding(horizontal=16.dp, vertical=8.dp)){ Text("July 2025", fontSize=13.sp, color=purple, fontWeight=FontWeight.Bold) }
       }
      }
      item{
       Card(shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(2.dp)){
        Column(Modifier.padding(20.dp)){
         Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){ listOf("M","T","W","T","F","S","S").forEach{ Text(it, fontSize=12.sp, color=Color(0xFF9A9A9A), fontWeight=FontWeight.Medium, modifier=Modifier.width(36.dp)) } }
         Spacer(Modifier.height(12.dp))
         // Days grid
         val days = (1..31).toList()
         val dots = mapOf(10 to 1, 11 to 1, 15 to 1, 16 to 2)
         for(row in 0..4){
          Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
           for(col in 0..6){
            val idx = row*7+col
            if(idx>=days.size){ Box(Modifier.size(44.dp)) } else {
             val d = days[idx]
             val isSel = d==selectedDay
             val dotCount = dots[d]?: 0
             Box(Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(if(isSel) purple else Color.Transparent).clickable{ selectedDay=d }, contentAlignment=Alignment.Center){
              Column(horizontalAlignment=Alignment.CenterHorizontally){
               Text("$d", fontSize=15.sp, color=if(isSel) Color.White else Color.Black, fontWeight=if(isSel) FontWeight.Bold else FontWeight.Normal)
               Spacer(Modifier.height(2.dp))
               Row(horizontalArrangement=Arrangement.spacedBy(2.dp)){
                if(isSel){ Box(Modifier.size(4.dp).clip(CircleShape).background(Color.White.copy(0.8f))) }
                else { repeat(dotCount){ Box(Modifier.size(4.dp).clip(CircleShape).background(purple)) } }
               }
              }
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
       Row(verticalAlignment=Alignment.CenterVertically){ Text("🕒", fontSize=12.sp); Spacer(Modifier.width(6.dp)); Text("JULY $selectedDay • 3 TASKS", fontSize=12.sp, color=Color(0xFF8A8A8A), fontWeight=FontWeight.Bold, letterSpacing=1.sp) }
       Spacer(Modifier.height(12.dp))
       Card(shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(3.dp)){
        Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(24.dp)){
         TodayRow(tasks[2])
         TodayRow(tasks[3])
         Column{ Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(28.dp).clip(CircleShape).background(Color(0xFFDCCFFF)), contentAlignment=Alignment.Center){ Text("✓", color=purple) }; Spacer(Modifier.width(12.dp)); Text("Grocery run & meal prep", color=Color(0xFFB0B0B0), textDecoration=TextDecoration.LineThrough, modifier=Modifier.weight(1f)); Box(Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFE6FFF0)).padding(horizontal=8.dp, vertical=4.dp)){ Text("#personal", fontSize=11.sp, color=Color(0xFF2ECC71)) } }; Row(Modifier.padding(start=40.dp)){ Box(Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFD0D0D0))); Spacer(Modifier.width(8.dp)); Text("18:00", fontSize=12.sp, color=Color(0xFFB0B0B0)) } }
        }
       }
       Spacer(Modifier.height(100.dp))
      }
     }
    } else {
     Box(Modifier.padding(pad).fillMaxSize(), contentAlignment=Alignment.Center){ Text("${listOf("Today","Cal","Matrix","Habits","Focus")[tab]} coming next - send mockup", color=Color.Gray) }
    }
   }
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
   Spacer(Modifier.height(6.dp)); Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(if(t.pri==0) Color(0xFFFF4D4D) else Color(0xFFFFC107))); Spacer(Modifier.width(8.dp)); Text(t.time, fontSize=12.sp, color=Color(0xFFFF6B6B)) }
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

class MainActivity : ComponentActivity(){ override fun onCreate(savedInstanceState:Bundle?){ super.onCreate(savedInstanceState); setContent{ App() } } }
