package fr.isen.aurianeramel.skiwaze

import android.content.Intent
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.isen.aurianeramel.skiwaze.Background
import fr.isen.aurianeramel.skiwaze.Connexion
import fr.isen.aurianeramel.skiwaze.Greeting
import fr.isen.aurianeramel.skiwaze.ui.theme.SkiWazeTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.isen.aurianeramel.skiwaze.database.Pistes

import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DownhillSkiing
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import fr.isen.aurianeramel.skiwaze.database.Remontees
import fr.isen.aurianeramel.skiwaze.ui.theme.comic_sans

class RemonteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkiWazeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Background()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        TopBar()
                        Greeting3()
                    }
                }
            }
        }
        Log.d("lifeCycle", "Menu Activity - OnCreate")
    }
}

@Composable
fun GetData2(remontees: SnapshotStateList<Remontees>) {
    Log.d("database", "a")
    DataBaseHelper.database.getReference("Remontees")
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("database", "b")
                val _Remontees = snapshot.children.mapNotNull { it.getValue(Remontees::class.java) }
                Log.d("database", remontees.toString())
                remontees.addAll(_Remontees)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("dataBase", error.toString())
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting3() {
    val context = LocalContext.current
    val remonteeees = remember {
        mutableStateListOf<Remontees>()
    }

    LazyColumn {
        items(remonteeees.toList()) { remontee ->
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp,
                ),
                modifier = Modifier
                    .size(
                        width = 250.dp,
                        height = 60.dp
                    )
                    .padding(top = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.powder_blue)
                ),
                onClick = {
                    val intent = Intent(context, RemonteeInfoActivity::class.java)
                    intent.putExtra(
                        "remonteeId",
                        remontee.id
                    ) // Envoyer l'identifiant de la piste à l'activité suivante
                    context.startActivity(intent)
                },
                content = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Row {}
                        Text(
                            text = remontee.name,
                            fontFamily = comic_sans
                        )
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.padding(end = 20.dp)
                        ) {
                            if (remontee.state) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = "Ouvert",
                                    tint = colorResource(R.color.medium_green),
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = null,
                                    tint = colorResource(R.color.red),
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
    Log.d("database", "oui")
    GetData2(remonteeees)
}

@Composable
fun DropDownMenuRemonte(remonte: Remontees) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Difficulté: ${remonte.type}") },
                onClick = {
                    Toast.makeText(context, "Difficulté: ${remonte.type}", Toast.LENGTH_SHORT)
                        .show()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Type: ${remonte.type}") },
                onClick = {
                    Toast.makeText(context, "Type: ${remonte.type}", Toast.LENGTH_SHORT)
                        .show()
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("Fréquentation: ${remonte.frequence}") },
                onClick = {
                    Toast.makeText(
                        context,
                        "Fréquentation: ${remonte.frequence}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    expanded = false
                }
            )

        }
    }
}