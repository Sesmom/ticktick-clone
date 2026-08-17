package com.sesmom.ticktickclone
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Task(val id: Int, val title: String, var done: Boolean = false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@Composable
fun App() {
    var tab by remember { mutableStateOf(0) }
    var tasks by remember { mutableStateOf(listOf(Task(1,"Design Today UI"), Task(2,"Build Calendar"), Task(3,"Habit tracker #work"))) }
    var input by remember { mutableStateOf("") }
    val tabs = listOf("Today","Calendar","Matrix","Habits","Pomo")
    MaterialTheme {
        Scaffold(
            topBar = { SmallTopAppBar(title={Text(tabs[tab])}) },
            bottomBar = {
                NavigationBar {
                    tabs.forEachIndexed { i, t -> NavigationBarItem(selected=i==tab, onClick={tab=i}, label={Text(t)}, icon={Text("")}) }
                }
            }
        ) { p ->
            Column(Modifier.padding(p).padding(16.dp).fillMaxSize()) {
                if(tab==0){
                    LazyColumn(Modifier.weight(1f)){
                        items(tasks){ t ->
                            Row(Modifier.padding(8.dp)){
                                Checkbox(checked=t.done, onCheckedChange={ tasks=tasks.map{if(it.id==t.id) it.copy(done=!it.done) else it} })
                                Text(t.title, Modifier.padding(start=8.dp))
                            }
                        }
                    }
                    Row {
                        OutlinedTextField(value=input, onValueChange={input=it}, modifier=Modifier.weight(1f), placeholder={Text("Add task")})
                        Button(onClick={ if(input.isNotBlank()){ tasks=tasks+Task(tasks.size+10,input); input="" } }, modifier=Modifier.padding(start=8.dp)){ Text("Add") }
                    }
                } else {
                    Text("${tabs[tab]} screen - full UI will be built next iteration. Base is ready for GitHub build.")
                }
            }
        }
    }
}
