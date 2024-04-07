package fr.isen.aurianeramel.skiwaze

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DownhillSkiing
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.*
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import fr.isen.aurianeramel.skiwaze.ui.theme.comic_sans
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TopBar()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(R.color.bright_gray))
                                .border(
                                    width = 2.dp,
                                    color = colorResource(R.color.bright_gray)
                                ),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "Les pistes",
                                fontFamily = stg,
                                fontSize = 30.sp,
                                color = colorResource(R.color.blue_gray),
                                modifier = Modifier
                            )
                        }
                        PistesListe()
                    }

                }
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = Icons.Default.DownhillSkiing,
                contentDescription = "Ouvert",
                tint = colorResource(id = R.color.medium_green), // Utilisation de la couleur verte pour "ouvert"
                modifier = Modifier.size(26.dp)
            )

            Text(
                text = "Ouvert",
                color = colorResource(id = R.color.medium_green),
                fontFamily = comic_sans,
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 20.dp)
            )

            Text(
                text = "Fermé",
                color = colorResource(id = R.color.red),
                fontFamily = comic_sans,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 20.dp)
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Fermé",
                tint = colorResource(id = R.color.red), // Utilisation de la couleur rouge pour "fermé"
                modifier = Modifier.size(26.dp)
            )
        }


        LazyColumn {
            items(pistes.toList()) { piste ->
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
                        val intent = Intent(context, PisteInfoActivity::class.java)
                        intent.putExtra(
                            "pisteId",
                            piste.id
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
                                text = piste.name,
                                fontFamily = comic_sans
                                )
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.padding(end = 20.dp)
                            ) {
                                if (piste.state) {
                                    Icon(
                                        imageVector = Icons.Default.DownhillSkiing,
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
    }
}