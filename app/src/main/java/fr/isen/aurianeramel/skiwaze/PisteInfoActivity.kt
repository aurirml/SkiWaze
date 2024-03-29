package fr.isen.aurianeramel.skiwaze
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import fr.isen.aurianeramel.skiwaze.database.Remontees
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.google.firebase.database.FirebaseDatabase
import androidx.compose.material.icons.materialIcon

class PisteInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pisteId = intent.getIntExtra("pisteId", -1)

        if (pisteId != -1) {
            getPisteById(pisteId) { piste ->
                if (piste != null) {
                    setContent {
                        PisteInfoContent(pisteId)
                    }
                } else {
                    // La piste n'a pas été trouvée, gérer l'erreur
                    Toast.makeText(this, "Piste non trouvée", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } else {
            // L'ID de la piste n'a pas été passé, gérer l'erreur
            Toast.makeText(this, "ID de la piste non fourni", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}


@Composable
fun PisteInfoContent(pisteId: Int) {
    val context = LocalContext.current

    // Créez un état mutable pour stocker les informations de la piste
    val pisteState = remember { mutableStateOf<Pistes?>(null) }

    // Utilisez une coroutine pour exécuter la fonction getPisteById
    LaunchedEffect(pisteId) {
        getPisteById(pisteId) { piste ->
            pisteState.value = piste
        }
    }

    // Affichez les informations de la piste dans votre interface utilisateur
    pisteState.value?.let { piste ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("${piste.name}")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Piste ${CouleurPiste.fromValue(piste.color)}")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Frequentation : ${piste.frequence}")
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(piste.damne)Text("La piste est damnée")
                else Text("La piste n'est pas damnée")
                Spacer(modifier = Modifier.width(8.dp)) // Ajouter un espace entre le texte et l'icône du bouton
                Icon(
                    imageVector = Icons.Default.Autorenew, // Utiliser une icône pour indiquer qu'il est possible de modifier
                    contentDescription = "Modifier Damnage",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            val newDamnageState = !piste.damne
                            FirebaseDatabase.getInstance().getReference("Pistes/${piste.id - 1}/damne")
                                .setValue(newDamnageState)
                            Toast.makeText(context, "Damnée: $newDamnageState", Toast.LENGTH_SHORT).show()
                            getPisteById(pisteId) { piste ->
                                pisteState.value = piste
                            }

                        }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Avalanche : ${piste.avalanche}")
                Spacer(modifier = Modifier.width(8.dp)) // Ajouter un espace entre le texte et l'icône du bouton
                Icon(
                    imageVector = Icons.Default.Autorenew, // Utiliser une icône pour indiquer qu'il est possible de modifier
                    contentDescription = "Modifier Avalanche",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            val newAvalancheState = !piste.avalanche
                            FirebaseDatabase.getInstance().getReference("Pistes/${piste.id - 1}/avalanche")
                                .setValue(newAvalancheState)
                            Toast.makeText(context, "Avalanche: $newAvalancheState", Toast.LENGTH_SHORT).show()
                            getPisteById(pisteId) { piste ->
                                pisteState.value = piste
                            }

                        }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(piste.state)Text("La piste est ouverte")
                else Text("La piste est fermée")
                Spacer(modifier = Modifier.width(8.dp)) // Ajouter un espace entre le texte et l'icône du bouton
                Icon(
                    imageVector = Icons.Default.Autorenew, // Utiliser une icône pour indiquer qu'il est possible de modifier
                    contentDescription = "Modifier ouverture",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            val newStateState = !piste.state
                            FirebaseDatabase.getInstance().getReference("Pistes/${piste.id - 1}/state")
                                .setValue(newStateState)
                            Toast.makeText(context, "State: $newStateState", Toast.LENGTH_SHORT).show()
                            getPisteById(pisteId) { piste ->
                                pisteState.value = piste
                            }

                        }
                )
            }
        }
    }
}



fun getPisteById(pisteId: Int, callback: (Pistes?) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("Pistes")

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (childSnapshot in snapshot.children) {
                val piste = childSnapshot.getValue(Pistes::class.java)
                if (piste?.id == pisteId) {
                    callback(piste)
                    return
                }
            }
            // Si la piste n'est pas trouvée, retourne null
            callback(null)
        }

        override fun onCancelled(error: DatabaseError) {
            // Gérer les erreurs
            Log.e("Firebase", "Error fetching data", error.toException())
            callback(null)
        }
    })
}

// Définissez votre énumération CouleurPiste ici
enum class CouleurPiste(val value: Int) {
    verte(1),
    bleue(2),
    rouge(3),
    noire(4),
    snowpark(5),
    raquettes(0);

    companion object {
        // Méthode pour récupérer l'énumération à partir de la valeur entière
        fun fromValue(value: Int): CouleurPiste {
            return values().find { it.value == value } ?: raquettes
        }
    }
}