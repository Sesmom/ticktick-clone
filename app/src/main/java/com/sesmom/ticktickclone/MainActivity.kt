package com.sesmom.ticktickclone
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
data class TaskM(val id:Int, val title:String, val tag:String, val tagColor:Color, val time:String, val timeRed:Boolean=false, val pri:Int, val done:Boolean=false, val subs:List<Sub> = emptyList(), val overdue:Boolean=false)

@Composable
fun App(){
 var tab by remember { mutableStateOf(0) }
 val purple = Color(0xFF6D5BFF)
 val tasks = listOf(
  TaskM(1,"Finalize Q3 roadmap deck","#work", Color(0xFFEEE9FF),"09:00", true, 0, overdue=true),
  TaskM(2,"Submit expense report","#finance", Color(0xFFE0F5FF),"Yesterday", true, 1, overdue=true),
  TaskM(3,"Morning review & standup notes","#work", Color(0xFFEEE9FF),"08:30", false, 0, subs=listOf(Sub("Update Figma handoff",true), Sub("Prep talking points",false))),
  TaskM(4,"Design system audit - components","#design", Color(0xFFFFE4F0),"14:00", false, 1),
  TaskM(5,"Grocery run & meal prep","#personal", Color(0xFFD9FFEE),"18:00", false, 0, done=true),
  TaskM(6,"Read 30 pages - Deep Work","#learning", Color(0xFFFFF3CC),"21:00", false, 0)
 )
 MaterialTheme{
  Box(Modifier.fillMaxSize().background(Color(0xFFF8F7FF))){
   Scaffold(containerColor=Color.Transparent,
    bottomBar={
     Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment=Alignment.Center){
      Card(Modifier.clip(RoundedCornerShape(30.dp)), colors=CardDefaults.cardColors(containerColor=Color(0xFF121212))){
       Row(Modifier.padding(8.dp), horizontalArrangement=Arrangement.spacedBy(4.dp)){
        listOf("Today" to "≡","Cal" to "📅","Matrix" to "⊞","Habits" to "↻","Focus" to "⏱").forEachIndexed{ i,p ->
         val sel=i==tab
         Box(Modifier.clip(RoundedCornerShape(24.dp)).background(if(sel) Color.White else Color.Transparent).padding(horizontal=18.dp, vertical=10.dp), contentAlignment=Alignment.Center){
          Column(horizontalAlignment=Alignment.CenterHorizontally){ Text(p.second, fontSize=14.sp, color=if(sel) Color.Black else Color.White.copy(0.6f)); Text(p.first, fontSize=10.sp, color=if(sel) Color.Black else Color.White.copy(0.6f), fontWeight=if(sel) FontWeight.Bold else FontWeight.Normal) }
         }
        }
       }
      }
     }
    },
    floatingActionButton={
     Card(Modifier.fillMaxWidth().padding(horizontal=16.dp).clip(RoundedCornerShape(24.dp)), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(8.dp)){
      Row(Modifier.padding(8.dp).fillMaxWidth(), verticalAlignment=Alignment.CenterVertically){
       Box(Modifier.size(36.dp).clip(CircleShape).border(1.5.dp, Color(0xFFE0E0E0), CircleShape))
       Spacer(Modifier.width(12.dp))
       Text("Add a task, e.g. Review PR #42", color=Color.Gray, fontSize=13.sp, modifier=Modifier.weight(1f))
       Button(onClick={}, shape=RoundedCornerShape(16.dp), colors=ButtonDefaults.buttonColors(containerColor=purple), contentPadding=PaddingValues(horizontal=20.dp, vertical=12.dp)){ Text("+ Add") }
      }
     }
    },
    floatingActionButtonPosition=FabPosition.Center
   ){ pad ->
    LazyColumn(Modifier.padding(pad).fillMaxSize().padding(horizontal=16.dp), verticalArrangement=Arrangement.spacedBy(16.dp)){
     item{
      Spacer(Modifier.height(16.dp))
      Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
       Column{
        Text("Today", fontSize=32.sp, fontWeight=FontWeight.ExtraBold, color=Color.Black)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment=Alignment.CenterVertically){ Text("Tuesday, July 15 • 5 left", fontSize=13.sp, color=Color.Gray); Text(" • ", fontSize=13.sp, color=Color.Gray); Text("14% done", fontSize=13.sp, color=purple, fontWeight=FontWeight.Medium) }
       }
       Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF0F0F0)), contentAlignment=Alignment.Center){ Text("...", fontWeight=FontWeight.Bold) }
      }
      Spacer(Modifier.height(12.dp))
      // progress bar
      Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFFEDE8FF))){ Box(Modifier.fillMaxWidth(0.14f).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(purple)) }
     }
     item{
      Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFF4D4D))); Spacer(Modifier.width(8.dp)); Text("OVERDUE • 2", color=Color(0xFFFF4D4D), fontWeight=FontWeight.Bold, fontSize=12.sp, letterSpacing=1.sp) }
      Spacer(Modifier.height(8.dp))
      Card(shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFFFFF5F5)), elevation=CardDefaults.cardElevation(0.dp)){
       Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(16.dp)){
        tasks.filter{it.overdue}.forEach{ t-> OverdueRow(t) }
       }
      }
     }
     item{
      Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(purple)); Spacer(Modifier.width(8.dp)); Text("TODAY • 4", color=purple, fontWeight=FontWeight.Bold, fontSize=12.sp, letterSpacing=1.sp) }
      Spacer(Modifier.height(8.dp))
      Card(shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color.White), elevation=CardDefaults.cardElevation(2.dp)){
       Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(20.dp)){
        tasks.filter{!it.overdue && !it.done}.forEach{ t-> TodayRow(t) }
        // done one
        Row(verticalAlignment=Alignment.CenterVertically, modifier=Modifier.fillMaxWidth()){
         Box(Modifier.size(28.dp).clip(CircleShape).background(purple), contentAlignment=Alignment.Center){ Text("✓", color=Color.White, fontSize=14.sp, fontWeight=FontWeight.Bold) }
         Spacer(Modifier.width(12.dp))
         Text("Grocery run & meal prep", color=Color(0xFFB0B0B0), textDecoration=TextDecoration.LineThrough, fontSize=14.sp, modifier=Modifier.weight(1f))
         Box(Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFE6FFF0)).padding(horizontal=8.dp, vertical=4.dp)){ Text("#personal", fontSize=11.sp, color=Color(0xFF2ECC71)) }
        }
        Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(4.dp).clip(CircleShape).background(Color(0xFFD0D0D0))); Spacer(Modifier.width(8.dp)); Row(verticalAlignment=Alignment.CenterVertically){ Text("🕒", fontSize=10.sp); Spacer(Modifier.width(4.dp)); Text("18:00", fontSize=12.sp, color=Color(0xFFB0B0B0)) } }
       }
      }
      Spacer(Modifier.height(100.dp))
     }
    }
   }
  }
 }
}

@Composable
fun OverdueRow(t:TaskM){
 Row(verticalAlignment=Alignment.Top, modifier=Modifier.fillMaxWidth()){
  Box(Modifier.size(28.dp).clip(CircleShape).border(2.dp, Color(0xFFE0E0E0), CircleShape))
  Spacer(Modifier.width(12.dp))
  Column(Modifier.weight(1f)){
   Row(verticalAlignment=Alignment.CenterVertically){ Text(t.title, fontWeight=FontWeight.Medium, fontSize=14.sp); Spacer(Modifier.width(8.dp)); Box(Modifier.clip(RoundedCornerShape(8.dp)).background(t.tagColor).padding(horizontal=8.dp, vertical=4.dp)){ Text(t.tag, fontSize=11.sp, color=Color(0xFF6D5BFF)) } }
   Spacer(Modifier.height(6.dp))
   Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(if(t.pri==0) Color(0xFFFF4D4D) else Color(0xFFFFC107))); Spacer(Modifier.width(8.dp)); Text("🕒", fontSize=11.sp); Spacer(Modifier.width(4.dp)); Text(t.time, fontSize=12.sp, color=Color(0xFFFF4D4D)) }
  }
 }
}

@Composable
fun TodayRow(t:TaskM){
 Column{
  Row(verticalAlignment=Alignment.Top, modifier=Modifier.fillMaxWidth()){
   Box(Modifier.size(28.dp).clip(CircleShape).border(2.dp, Color(0xFFE0E0E0), CircleShape))
   Spacer(Modifier.width(12.dp))
   Column(Modifier.weight(1f)){
    Row(verticalAlignment=Alignment.CenterVertically){ Text(t.title, fontWeight=FontWeight.Medium, fontSize=14.sp, modifier=Modifier.weight(1f, false)); Spacer(Modifier.width(8.dp)); Box(Modifier.clip(RoundedCornerShape(8.dp)).background(t.tagColor).padding(horizontal=8.dp, vertical=4.dp)){ Text(t.tag, fontSize=11.sp, color=when(t.tag){ "#finance"->Color(0xFF0099CC); "#design"->Color(0xFFCC4D8C); else->Color(0xFF6D5BFF)}) } }
    Spacer(Modifier.height(6.dp))
    Row(verticalAlignment=Alignment.CenterVertically){ Box(Modifier.size(8.dp).clip(CircleShape).background(if(t.pri==0) Color(0xFFFF4D4D) else Color(0xFFFFC107))); Spacer(Modifier.width(8.dp)); Text("🕒", fontSize=11.sp); Spacer(Modifier.width(4.dp)); Text(t.time, fontSize=12.sp, color=Color.Gray); if(t.subs.isNotEmpty()){ Spacer(Modifier.width(12.dp)); Text("${t.subs.count{it.done}}/${t.subs.size}", fontSize=12.sp, color=Color.Gray) } }
    if(t.subs.isNotEmpty()){
     Spacer(Modifier.height(12.dp))
     Box(Modifier.padding(start=14.dp).border(width=1.dp, color=Color(0xFFF0F0F0)).padding(start=16.dp)){
      Column(verticalArrangement=Arrangement.spacedBy(10.dp)){
       t.subs.forEach{ s->
        Row(verticalAlignment=Alignment.CenterVertically){
         Box(Modifier.size(22.dp).clip(CircleShape).background(if(s.done) Color(0xFFEDE8FF) else Color.Transparent).border(1.5.dp, if(s.done) Color(0xFF6D5BFF) else Color(0xFFE0E0E0), CircleShape), contentAlignment=Alignment.Center){ if(s.done) Text("✓", fontSize=10.sp, color=Color(0xFF6D5BFF)) }
         Spacer(Modifier.width(8.dp))
         Text(s.title, fontSize=13.sp, color=if(s.done) Color(0xFFA0A0A0) else Color(0xFF505050), textDecoration=if(s.done) TextDecoration.LineThrough else null)
        }
       }
      }
     }
    }
   }
  }
 }
}

class MainActivity : ComponentActivity(){ override fun onCreate(savedInstanceState:Bundle?){ super.onCreate(savedInstanceState); setContent{ App() } } }
