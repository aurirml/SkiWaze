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
import fr.isen.aurianeramel.skiwaze.database.Comment

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
import androidx.compose.ui.res.colorResource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
    val auth: FirebaseAuth = Firebase.auth
    val currentUser = auth.currentUser
    val userName = auth.currentUser?.displayName
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
                            FirebaseDatabase
                                .getInstance()
                                .getReference("Pistes/${piste.id - 1}/damne")
                                .setValue(newDamnageState)
                            Toast
                                .makeText(context, "Damnée: $newDamnageState", Toast.LENGTH_SHORT)
                                .show()
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
                            FirebaseDatabase
                                .getInstance()
                                .getReference("Pistes/${piste.id - 1}/avalanche")
                                .setValue(newAvalancheState)
                            Toast
                                .makeText(
                                    context,
                                    "Avalanche: $newAvalancheState",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
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


                            FirebaseDatabase
                                .getInstance()
                                .getReference("Pistes/${piste.id - 1}/state")
                                .setValue(newStateState)
                            Toast
                                .makeText(context, "State: $newStateState", Toast.LENGTH_SHORT)
                                .show()
                            getPisteById(pisteId) { piste ->
                                pisteState.value = piste
                            }
                        }
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            getPisteById(pisteId) { piste ->
                pisteState.value = piste
            }
            Comment(pisteId)
            Spacer(modifier = Modifier.height(20.dp))
            ShowComment(userName,pisteId)
        }
    }
}

@Composable
fun Comment(pisteId: Int) {
    //var comment by remember { mutableStateOf(0) }
    var showComment by remember { mutableStateOf(false) }

    Column {
        Text("Commentaires")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                //comment = 1
                showComment = !showComment
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.alice_blue),
                contentColor = colorResource(R.color.dark_slate_blue)
            ),
            modifier = Modifier
                .height(40.dp)
                .width(250.dp)
        ){
            Text("Ajouter un commentaire")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (showComment) {
            AddComment(onClose = { showComment = false }, pisteId) // Passer la fonction onClose pour fermer l'onglet du commentaire
        }
    }
}

@Composable
fun AddComment(onClose: () -> Unit, pisteId: Int){
    val auth: FirebaseAuth = Firebase.auth
    val currentUser = auth.currentUser
    val userName = auth.currentUser?.displayName
    Log.d("auth", userName ?: "")
    val context = LocalContext.current


    val pisteState = remember { mutableStateOf<Pistes?>(null) }


    LaunchedEffect(pisteId) {
        getPisteById(pisteId) { piste ->
            pisteState.value = piste
        }
    }
    pisteState.value?.let { piste ->
        var commentaireText by remember { mutableStateOf("") } // Variable pour stocker le texte du commentaire

        Column {
            // Afficher le champ de texte pour saisir le commentaire
            TextField(
                value = commentaireText,
                onValueChange = { commentaireText = it },
                label = { Text("Saisissez votre commentaire") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Afficher les boutons pour soumettre ou annuler le commentaire
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        // Réinitialiser le contenu du champ de texte
                        commentaireText = ""
                        // Fermer l'onglet du commentaire
                        onClose()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.alice_blue),
                        contentColor = colorResource(R.color.dark_slate_blue)
                    )
                ) {
                    Text("Annuler")
                }
                Button(
                    onClick = {

                        val baseReference = FirebaseDatabase.getInstance().getReference("Comment/${userName}/${piste.id - 1}")

                        baseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                var i = 1 // Initialiser un compteur pour les commentaires
                                var newReference: DatabaseReference? = null // Référence pour le nouveau commentaire

                                // Parcourir les enfants pour trouver un nœud disponible
                                while (dataSnapshot.hasChild("comment$i")) {
                                    i++
                                }

                                // Créer une nouvelle référence pour le nouveau commentaire
                                val commentReference = baseReference.child("comment$i")
                                val contentReference = commentReference.child("content")
                                val dateReference = commentReference.child("date")

                                // Obtenir la date actuelle
                                val currentDate = Date()

                                // Créer un formateur de date avec le format souhaité
                                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRENCH)
                                dateFormat.timeZone = TimeZone.getTimeZone("Europe/Paris")
                                // Formater la date actuelle en utilisant le formateur de date
                                val formattedTime = dateFormat.format(currentDate)

                                // Écrire le nouveau commentaire et sa date
                                contentReference.setValue(commentaireText)
                                    .addOnSuccessListener {
                                        // Réinitialiser le champ de texte du commentaire
                                        commentaireText = ""
                                        // Écrire la date du commentaire
                                        dateReference.setValue(formattedTime)
                                            .addOnSuccessListener {
                                                onClose() // Fermer l'onglet du commentaire
                                            }
                                            .addOnFailureListener { exception ->
                                                // Gérer les erreurs d'écriture de la date
                                                Log.e("Firebase", "Error writing date: $exception")
                                            }
                                    }
                                    .addOnFailureListener { exception ->
                                        // Gérer les erreurs d'écriture du commentaire
                                        Log.e("Firebase", "Error writing comment: $exception")
                                    }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Gérer les erreurs d'annulation éventuelles
                                Log.e("Firebase", "Database error: $databaseError")
                            }
                        })



                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.alice_blue),
                        contentColor = colorResource(R.color.dark_slate_blue)
                    )
                ) {
                    Text("Envoyer")
                }
            }
        }
    }
}

@Composable
fun ShowComment(userName: String?, pisteId: Int) {
    val commentsReference = FirebaseDatabase.getInstance().getReference("Comment/$userName/$pisteId")
    var commentText by remember { mutableStateOf("") }


    // Récupérer les commentaires de la base de données
    LaunchedEffect(key1 = commentsReference) {
        val comments = mutableListOf<String>()
        commentsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (commentSnapshot in dataSnapshot.children) {
                    val commentId = commentSnapshot.key
                    val commentContent = commentSnapshot.child("content").getValue(String::class.java)
                    val commentDate = commentSnapshot.child("date").getValue(Date::class.java)

                    // Ajouter les informations du commentaire à la liste des commentaires
                    val commentInfo = "Commentaire ID: $commentId, Contenu: $commentContent, Date et heure: $commentDate"
                    comments.add(commentInfo)

                    // Afficher les informations du commentaire dans le logcat
                    Log.d("Commentaires", commentInfo)
                }
                // Convertir la liste des commentaires en une seule chaîne
                commentText = comments.joinToString("\n")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Gérer les erreurs d'annulation éventuelles
                Log.e("Firebase", "Database error: $databaseError")
            }
        })
    }

    // Afficher les commentaires dans un composant Text
    Text(text = commentText)
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
fun getcomdata(pisteee: SnapshotStateList<Pistes>) {
    val auth: FirebaseAuth = Firebase.auth
    val currentUser = auth.currentUser
    val userName = auth.currentUser?.displayName
    Log.d("database", "a")
    DataBaseHelper.database.getReference("Comment/$userName/")
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