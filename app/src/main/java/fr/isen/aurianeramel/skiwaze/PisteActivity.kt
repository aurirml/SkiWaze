package fr.isen.aurianeramel.skiwaze
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import fr.isen.aurianeramel.skiwaze.database.Remontees
import androidx.compose.ui.Alignment
import com.google.firebase.database.FirebaseDatabase


class PisteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkiWazeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
                Greeting2()
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


@Composable
fun Greeting2() {
    val pistes = remember {
        mutableStateListOf<Pistes>()
    }
    LazyColumn {
        items(pistes.toList()) {piste ->
            Column {
                Text(
                    text = piste.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), // Ajoutez une marge pour l'esthétique
                )
                Divider() // Ajoute une ligne de séparation entre les éléments
                DropDownMenu(piste)
            }


            //Text(it.name)
        }
    }
    Log.d("database", "oui")
    GetData(pistes)
}

@Composable
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
                    FirebaseDatabase.getInstance().getReference("Pistes/${piste.id}").setValue(newAvalancheState)
                    Toast.makeText(context, "Avalanche: ${piste.avalanche}", Toast.LENGTH_SHORT)
                        .show()
                    expanded = false
                }
            )

        }
    }
    Log.d("database", "oui")
    GetData(pistes)
}