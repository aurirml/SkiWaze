package fr.isen.aurianeramel.skiwaze

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.isen.aurianeramel.skiwaze.ui.theme.SkiWazeTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.isen.aurianeramel.skiwaze.database.Pistes
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DownhillSkiing
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.DownhillSkiing
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import fr.isen.aurianeramel.skiwaze.ui.theme.stg


class PisteActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkiWazeTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    // color = MaterialTheme.colorScheme.background
                ) {
                    Background()
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(modifier = Modifier.padding(top = 16.dp)) {
                            Icon(
                                Icons.Filled.AcUnit,
                                contentDescription = null,
                                tint = colorResource(R.color.gray),
                                modifier = Modifier.size(50.dp)
                            )
                            Text(
                                text = stringResource(R.string.app_name),
                                fontFamily = stg,
                                fontSize = 40.sp,
                                color = colorResource(R.color.gray),
                                modifier = Modifier
                            )
                            Spacer(Modifier.height(40.dp))
                            Icon(
                                Icons.Filled.AcUnit,
                                contentDescription = null,
                                tint = colorResource(R.color.gray),
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(R.color.nude))
                                .border(
                                    width = 2.dp,
                                    color = colorResource(R.color.nude)
                                ), // Ajouter la bordure autour de la Row
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "Les pistes \t",
                                fontFamily = stg,
                                fontSize = 30.sp,
                                color = colorResource(R.color.blue_gray),
                                modifier = Modifier
                            )

                        }
                    }
                }
                Background()
                PistesListe()
            }

        }
        Log.d("lifeCycle", "Menu Activity - OnCreate")
    }
}

@Composable
fun GetData(pisteee: SnapshotStateList<Pistes>) {
    Log.d("database", "a")
    DataBaseHelper.database.getReference("Pistes")
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("database", "b")
                val _Pistes = snapshot.children.mapNotNull { it.getValue(Pistes::class.java) }
                Log.d("database", pisteee.toString())
                pisteee.addAll(_Pistes)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("dataBase", error.toString())
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PistesListe() {
    val pistes = remember {
        mutableStateListOf<Pistes>()
    }
    val context = LocalContext.current
    // Obtenir les données des pistes
    GetData(pistes)

    LazyColumn {
        items(pistes.toList()) { piste ->
            Column {
                Button(
                    onClick = {
                        val intent = Intent(context, PisteInfoActivity::class.java)
                        intent.putExtra(
                            "pisteId",
                            piste.id
                        ) // Envoyer l'identifiant de la piste à l'activité suivante
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.alice_blue),
                        contentColor = colorResource(R.color.dark_slate_blue)
                    )
                ) {
                    Text(piste.name)
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 100.dp)
    ) {
        Spacer(Modifier.height(20.dp))
        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            /*Icon(
                imageVector = Icons.Default.DownhillSkiing,
                contentDescription = "Ouvert",
                tint = colorResource(id = R.color.green), // Utilisation de la couleur verte pour "ouvert"
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = "Ouvert",
                color = colorResource(id = R.color.green), // Utilisation de la couleur verte pour "ouvert"
                fontFamily = stg,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 4.dp)
            )

            Icon(
                imageVector = Icons.Default.ArrowOutward,
                contentDescription = "Fermé",
                tint = colorResource(id = R.color.red), // Utilisation de la couleur rouge pour "fermé"
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = "Fermé",
                color = colorResource(id = R.color.red), // Utilisation de la couleur rouge pour "fermé"
                fontFamily = stg,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 4.dp)
            )*/


            Icon(
                imageVector = Icons.Default.DownhillSkiing,
                contentDescription = "Ouvert",
                tint = colorResource(id = R.color.green), // Utilisation de la couleur verte pour "ouvert"
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = "Ouvert",
                color = colorResource(id = R.color.green),
                fontFamily = stg,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 20.dp)
            )

            Text(
                text = "Fermé",
                color = colorResource(id = R.color.red),
                fontFamily = stg,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 20.dp)
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Fermé",
                tint = colorResource(id = R.color.red), // Utilisation de la couleur rouge pour "fermé"
                modifier = Modifier.size(24.dp)
            )


            LazyColumn {
                items(pistes.toList()) { piste ->
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp
                        ),
                        modifier = Modifier
                            .size(
                                width = 380.dp,
                                height = 50.dp
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.powder_blue)
                        ),
                        onClick = {
                            val intent = Intent(context, PisteInfoActivity::class.java)
                            intent.putExtra(
                                "pisteId",
                                piste.id
                            ) // Envoyer l'identifiant de la piste à l'activité suivante
                            context.startActivity(intent)
                        },
                        content = {
                            Row(
                                horizontalArrangement = Arrangement.Center, // Centrer horizontalement le contenu
                                modifier = Modifier.fillMaxWidth() // Prendre toute la largeur disponible
                            ) {
                                Text(piste.name)
                                Row() {
                                    Text("La piste est actuellement ")
                                    if (piste.state) Text("ouverte")
                                    else Text("fermée")
                                    Spacer(Modifier.height(2.dp))
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}



/*@Composable
fun DropDownMenu(piste: Pistes) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val pistes = remember {
        mutableStateListOf<Pistes>()
    }

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


                text = {
                    val stateString = if (piste.state) "OUVERT" else "FERME"
                    Text(stateString)
                },
                onClick = {

                    val newState = !piste.state
                    FirebaseDatabase.getInstance().getReference("Pistes/${piste.id-1}/state")
                        .setValue(newState)
                    val stateString = if (piste.state) "OUVERT" else "FERME"
                    Toast.makeText(context, stateString, Toast.LENGTH_SHORT).show()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Niveau: ${piste.color}") },
                onClick = {
                    Toast.makeText(context, "Niveau: ${piste.color}", Toast.LENGTH_SHORT).show()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Fréquentation: ${piste.frequence}") },
                onClick = {
                    Toast.makeText(context, "Fréquentation: ${piste.frequence}", Toast.LENGTH_SHORT)
                        .show()
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("Etat: ${piste.damne}") },
                onClick = {
                    Toast.makeText(context, "Etat: ${piste.damne}", Toast.LENGTH_SHORT).show()
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("Avalanche: ${piste.avalanche}") },
                onClick = {
                    val newAvalancheState = !piste.avalanche
                    FirebaseDatabase.getInstance().getReference("Pistes/${piste.id-1}/avalanche")
                        .setValue(newAvalancheState)
                    Toast.makeText(context, "Avalanche: $newAvalancheState", Toast.LENGTH_SHORT).show()
                    expanded = false
                }

            )

         }
    }
    Log.d("database", "oui")
    GetData(pistes)
}
*/



