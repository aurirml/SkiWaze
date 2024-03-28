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

class PisteInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Récupérer l'ID de la piste depuis l'intent
        val pisteId = intent.getIntExtra("pisteId", -1)
        Log.d("PisteInfoActivity", "Piste ID: $pisteId")

        // Récupérer les informations de la piste correspondante en fonction de son ID
        val piste = getPisteById(pisteId)

        setContent {
            SkiWazeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Afficher les informations de la piste
                    DisplayPisteInfo(piste)
                }
            }
        }
    }

    // Fonction pour obtenir les informations de la piste à partir de son ID

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

@Composable
fun DisplayPisteInfo(piste: Pistes) {
    LazyColumn {
        item {
            Text("Nom de la piste : ${piste.name}")
        }
        item {
            Text("Couleur de la piste : ${piste.color}")
        }
    }
}