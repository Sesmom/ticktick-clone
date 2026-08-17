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
import kotlin.random.Random

data class Sub(val title:String, val done:Boolean)
data class TaskM(val id:Int, val title:String, val tag:String, val tagColor:Color, val tagText:Color, val time:String, val pri:Int, val done:Boolean=false, val subs:List<Sub> = emptyList())

@Composable
fun App(){
 var tab by remember { mutableStateOf(3) }
 val purple = Color(0xFF6D5BFF)
 val tasks = listOf(
  TaskM(1,"Finalize Q3 roadmap deck","#work", Color(0xFFEEE9FF), Color(0xFF6D5BFF),"09:00", 0),
  TaskM(2,"Morning review & standup notes","#work", Color(0xFFEEE9FF), Color(0xFF6D5BFF),"08:30", 0, subs=listOf(Sub("Update Figma handoff",true), Sub("Prep talking points",false))),
  TaskM(3,"Design system audit - components","#design", Color(0xFFFFE4F0), Color(0xFFCC4D8C),"14:00", 1)
 )
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
     3 -> {
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
            Column{
             Text("Pushups", fontSize=16.sp, fontWeight=FontWeight.Bold)
             Row(verticalAlignment=Alignment.CenterVertically, modifier=Modifier.padding(top=2.dp)){ Box(Modifier.clip(RoundedCornerShape(6.dp)).background(Color(0xFFF0F0F0)).padding(horizontal=6.dp, vertical=2.dp)){ Text("#work", fontSize=11.sp, color=Color(0xFF8A8A8A)) }; Spacer(Modifier.width(6.dp)); Text("Daily • 50\nreps", fontSize=12.sp, color=Color(0xFF8A8A8A), lineHeight=13.sp) }
            }
           }
           Column(horizontalAlignment=Alignment.End){
            Text("84%", fontSize=24.sp, fontWeight=FontWeight.Black)
            Text("+6% VS LAST\nMONTH", fontSize=11.sp, color=Color(0xFF10B981), fontWeight=FontWeight.Bold, lineHeight=12.sp)
           }
          }
          Spacer(Modifier.height(16.dp))
          // Heatmap exact 7x12
          val levels = listOf(Color(0xFFF5F3FF), Color(0xFFEDE8FF), Color(0xFFDCCFFF), Color(0xFFA78BFA), Color(0xFF6D5BFF), Color(0xFF4C1D95))
          Column(verticalArrangement=Arrangement.spacedBy(6.dp)){
           for(r in 0..6){
            Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){
             for(c in 0..11){
              val v = remember{ Random.nextInt(0,6) }
              val col = if(c<2 && r<1) levels[4] else if(v==0) Color(0xFFF5F3FF) else if(v<2) Color(0xFFDCCFFF) else if(v<4) levels[2] else if(v<5) levels[3] else levels[4]
              // force pattern like mockup
              val forced = when{ r==0 && c==0 -> levels[4]; r==0 && c==1 -> levels[4]; r==0 && c==8 -> levels[4]; r==1 && c==3 -> levels[3]; r==1 && c==4 -> levels[3]; r==1 && c==8 -> levels[4]; r==2 && c==3 -> levels[4]; r==2 && c==10 -> levels[4]; r==2 && c==11 -> levels[4]; r==3 && c==5 -> levels[4]; r==3 && c==6 -> levels[4]; r==4 && c==1 -> levels[4]; r==4 && c==8 -> levels[4]; r==5 && c==3 -> levels[4]; r==5 && c==4 -> levels[4]; r==6 && c==2 -> levels[2]; r==6 && c==3 -> levels[4]; r==6 && c==4 -> levels[4]; r==6 && c==5 -> levels[4]; r==6 && c==6 -> levels[5]; r==6 && c==10 -> levels[4]; r==6 && c==11 -> levels[5]; else -> col }
              Box(Modifier.size(22.dp).clip(RoundedCornerShape(6.dp)).background(forced))
             }
            }
           }
          }
          Spacer(Modifier.height(12.dp))
          Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
           Text("Apr", fontSize=12.sp, color=Color(0xFFAAAAAA))
           Row(verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.spacedBy(4.dp)){
            Text("Less", fontSize=11.sp, color=Color(0xFFAAAAAA))
            levels.take(5).forEach{ Box(Modifier.size(12.dp).clip(RoundedCornerShape(3.dp)).background(it)) }
            Text("More", fontSize=11.sp, color=Color(0xFFAAAAAA))
           }
          }
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
     else -> Box(Modifier.padding(pad).fillMaxSize(), contentAlignment=Alignment.Center){ Text("Tab ${listOf("Today","Cal","Matrix","Habits","Focus")[tab]} locked, now Habits 100%") }
    }
   }
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
    Column{
     Text(title, fontSize=15.sp, fontWeight=FontWeight.Medium)
     Row(verticalAlignment=Alignment.CenterVertically, modifier=Modifier.padding(top=2.dp)){ Text("🔥", fontSize=11.sp); Spacer(Modifier.width(4.dp)); Text(days, fontSize=12.sp, color=Color(0xFFAAAAAA)) }
    }
   }
   Box(Modifier.size(36.dp).clip(CircleShape).background(if(done) purple else Color.White).border(1.dp, if(done) purple else Color(0xFFE0E0E0), CircleShape), contentAlignment=Alignment.Center){
    Text("✓", color=if(done) Color.White else Color(0xFF8A8A8A), fontSize=14.sp, fontWeight=FontWeight.Bold)
   }
  }
 }
}

@Composable fun OverdueRow(t:TaskM){ Row(Modifier.fillMaxWidth()){ Box(Modifier.size(28.dp).clip(CircleShape).border(2.dp, Color(0xFFE0E0E0), CircleShape)); Spacer(Modifier.width(12.dp)); Text(t.title, fontSize=14.sp) } }
@Composable fun TodayRow(t:TaskM){ Row(Modifier.fillMaxWidth()){ Box(Modifier.size(28.dp).clip(CircleShape).border(2.dp, Color(0xFFE0E0E0), CircleShape)); Spacer(Modifier.width(12.dp)); Text(t.title, fontSize=14.sp) } }

class MainActivity : ComponentActivity(){ override fun onCreate(savedInstanceState:Bundle?){ super.onCreate(savedInstanceState); setContent{ App() } } }
