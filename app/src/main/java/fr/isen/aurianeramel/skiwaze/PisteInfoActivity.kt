package fr.isen.aurianeramel.skiwaze

import android.content.Intent
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.isen.aurianeramel.skiwaze.database.Pistes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DownhillSkiing
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.google.firebase.database.FirebaseDatabase
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import fr.isen.aurianeramel.skiwaze.database.Chat
import fr.isen.aurianeramel.skiwaze.database.Comment
import fr.isen.aurianeramel.skiwaze.ui.theme.BluePiste
import fr.isen.aurianeramel.skiwaze.ui.theme.GreenPiste
import fr.isen.aurianeramel.skiwaze.ui.theme.Purple40
import fr.isen.aurianeramel.skiwaze.ui.theme.RedPiste
import fr.isen.aurianeramel.skiwaze.ui.theme.comic_sans
import fr.isen.aurianeramel.skiwaze.ui.theme.stg
import java.text.SimpleDateFormat
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
                        Surface(
                            modifier = Modifier
                                .fillMaxSize(),
                            // color = MaterialTheme.colorScheme.background
                        ) {
                            Background()
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
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
                                PisteInfoContent(pisteId)
                            }
                        }
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
    val pisteState = remember { mutableStateOf<Pistes?>(null) }

    LaunchedEffect(pisteId) {
        getPisteById(pisteId) { piste ->
            pisteState.value = piste
        }
    }

    // Affichez les informations de la piste dans votre interface utilisateur
    pisteState.value?.let { piste ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                border = BorderStroke(1.dp, colorResource(R.color.bright_gray)),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.bright_gray)
                ),
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp)
                    .padding(top = 8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    //juste pour faire beau dans la card
                    Row {
                        Text("\t \t")
                    }
                    Text(
                        text = piste.name,
                        fontFamily = comic_sans,
                        fontSize = 20.sp
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
                                modifier = Modifier.size(28.dp)
                            )
                        } else {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = null,
                                tint = colorResource(R.color.red),
                                modifier = Modifier
                                    .size(28.dp)
                            )
                        }
                    }
                }
            }
            var etat = piste.state
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (etat) {
                    Text(
                        text = "La piste est maintenant fermée ?",
                        fontFamily = comic_sans,
                        color = colorResource(R.color.black)
                    )
                } else {
                    Text(
                        text = "La piste est maintenant ouverte ?",
                        fontFamily = comic_sans,
                        color = colorResource(R.color.black)
                    )
                }
                TextButton(
                    onClick = {
                        val newStateState = !piste.state

                        FirebaseDatabase
                            .getInstance()
                            .getReference("Pistes/${piste.id - 1}/state")
                            .setValue(newStateState)

                        getPisteById(pisteId) { piste ->
                            pisteState.value = piste
                        }
                    }
                )
                {
                    Text(
                        text = "Changer l'état.",
                        color = colorResource(R.color.bright_gray),
                        fontFamily = comic_sans
                    )
                }
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(50.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier
                .widthIn(max = 300.dp)
                .height(300.dp)
                .padding(top = 20.dp)
        ) {
            item {
                Couleur(pisteId)
            }
            item {
                Frequentation(pisteId)
            }
            item {
                Avalanche(pisteId)
            }
            item {
                Damee(pisteId)
            }
        }
        Comment(pisteId)
        Spacer(Modifier.height(5.dp))
        ComListe2(pisteId)
    }
}

@Composable
fun Couleur(pisteId: Int) {
    val pisteState = remember { mutableStateOf<Pistes?>(null) }
    LaunchedEffect(pisteId) {
        getPisteById(pisteId) { piste ->
            pisteState.value = piste
        }
    }

    val couleurs = Couleurs()

    val couleurBox = when (val piste = pisteState.value) {
        is Pistes -> {
            val couleurPiste = CouleurPiste.fromValue(piste.color)
            convertirEnCouleurGraphique(couleurPiste, couleurs)
        }

        else -> null
    }

    Card(
        modifier = Modifier.size(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.bright_gray)
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texte au-dessus
            Text(
                text = "Type de piste",
                textAlign = TextAlign.Center,
                fontFamily = comic_sans,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Couleur en dessous
            couleurBox?.let { color ->
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .background(color)
                ) {}
            }
        }
    }
}

@Composable
fun Frequentation(pisteId: Int) {
    var freq: Int? by remember { mutableStateOf(null) }

    // Récupérer la fréquence depuis Firebase Realtime Database
    val databaseReference =
        FirebaseDatabase.getInstance().getReference("Pistes/${pisteId}/frequence")
    val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            freq = snapshot.getValue(Int::class.java)
        }

        override fun onCancelled(error: DatabaseError) {
            // Gérer les erreurs ici
        }
    }

    LaunchedEffect(pisteId) {
        databaseReference.addValueEventListener(valueEventListener)
    }

    Card(
        modifier = Modifier.size(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.bright_gray)
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Fréquentation",
                fontFamily = comic_sans,
                fontSize = 15.sp
            )
            freq?.let { // Vérifiez si freq n'est pas null
                Text(
                    text = "$it",
                    fontFamily = comic_sans,
                    fontSize = 30.sp
                )
            }
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            freq?.let { currentFreq ->
                                if (currentFreq > 1) {
                                    databaseReference.setValue(currentFreq - 1)
                                }
                            }
                        }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            freq?.let { currentFreq ->
                                if (currentFreq < 10) {
                                    databaseReference.setValue(currentFreq + 1)
                                }
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun Avalanche(pisteId: Int) {
    val pisteState = remember { mutableStateOf<Pistes?>(null) }


    LaunchedEffect(pisteId) {
        getPisteById(pisteId) { piste ->
            pisteState.value = piste
        }
    }

    pisteState.value?.let { piste ->
        if (piste != null) {
            var avalanche = piste.avalanche

            Card(
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.bright_gray)
                ),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Avalanche",
                        fontFamily = comic_sans,
                        fontSize = 15.sp
                    )
                    Image(
                        painter = painterResource(R.drawable.avalanche_image),
                        contentDescription = null
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (avalanche) {
                            Text(
                                text = "En cours",
                                fontFamily = comic_sans,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 30.dp)
                            )
                        } else {
                            Text(
                                text = "Aucun risque",
                                fontFamily = comic_sans,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 15.dp)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Autorenew, // Utiliser une icône pour indiquer qu'il est possible de modifier
                            contentDescription = "Modifier Avalanche",
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 4.dp)
                                .clickable {
                                    val newAvalancheState = !piste?.avalanche!!
                                    if (piste != null) {
                                        FirebaseDatabase
                                            .getInstance()
                                            .getReference("Pistes/${piste.id - 1}/avalanche")
                                            .setValue(newAvalancheState)
                                    }

                                    getPisteById(pisteId) { piste ->
                                        pisteState.value = piste
                                    }

                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Damee(pisteId: Int) {
    val pisteState = remember { mutableStateOf<Pistes?>(null) }
    LaunchedEffect(pisteId) {
        getPisteById(pisteId) { piste ->
            pisteState.value = piste
        }
    }

    pisteState.value?.let { piste ->
        if (piste != null) {
            var damee = piste.damne

            Card(
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.bright_gray)
                ),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Damée",
                        fontFamily = comic_sans,
                        fontSize = 15.sp
                    )
                    Image(
                        painter = painterResource(R.drawable.damee),
                        contentDescription = null
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (damee) {
                            Text(
                                text = "Oui",
                                fontFamily = comic_sans,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 50.dp)
                            )
                        } else {
                            Text(
                                text = "Non",
                                fontFamily = comic_sans,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 50.dp)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Autorenew, // Utiliser une icône pour indiquer qu'il est possible de modifier
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 4.dp)
                                .clickable {
                                    val newAvalancheState = !piste?.damne!!
                                    if (piste != null) {
                                        FirebaseDatabase
                                            .getInstance()
                                            .getReference("Pistes/${piste.id - 1}/damne")
                                            .setValue(newAvalancheState)
                                    }

                                    getPisteById(pisteId) { piste ->
                                        pisteState.value = piste
                                    }

                                }
                        )
                    }
                }
            }
        }
    }
}

private fun convertirEnCouleurGraphique(
    couleurPiste: CouleurPiste,
    couleurs: Couleurs
): Color? {
    return when (couleurPiste) {
        CouleurPiste.verte -> couleurs.vert
        CouleurPiste.bleue -> couleurs.bleu
        CouleurPiste.rouge -> couleurs.rouge
        CouleurPiste.noire -> couleurs.noir
        else -> null
    }
}

@Composable
fun Comment(pisteId: Int) {
    var showComment by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.bright_gray))
            .border(
                width = 2.dp,
                color = colorResource(R.color.bright_gray)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Commentaires",
            fontFamily = stg,
            fontSize = 30.sp,
            color = colorResource(R.color.blue_gray),
            modifier = Modifier.padding(start = 20.dp)
        )
        Spacer(Modifier.width(40.dp))
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = colorResource(R.color.blue_gray),
            modifier = Modifier.clickable {
                showComment = !showComment
            }
        )
    }
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        if (showComment) {
            AddComment(
                onClose = { showComment = false },
                pisteId
            ) // Passer la fonction onClose pour fermer l'onglet du commentaire
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddComment(onClose: () -> Unit, pisteId: Int) {
    val auth: FirebaseAuth = Firebase.auth
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Afficher le champ de texte pour saisir le commentaire
            TextField(
                value = commentaireText,
                onValueChange = { commentaireText = it },
                label = { Text("Saisissez votre commentaire") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .width(300.dp)
                    .background(colorResource(R.color.water)),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = colorResource(R.color.water),
                    unfocusedLabelColor = colorResource(R.color.opal),
                    focusedLabelColor = colorResource(R.color.opal)
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        // Afficher les boutons pour soumettre ou annuler le commentaire
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                shape = RoundedCornerShape(0.dp),
                onClick = {
                    // Réinitialiser le contenu du champ de texte
                    commentaireText = ""
                    // Fermer l'onglet du commentaire
                    onClose()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.bright_gray),
                    contentColor = colorResource(R.color.blue_gray)
                ),
                modifier = Modifier.width(145.dp)
            ) {
                Text("Annuler")
            }
            Spacer(Modifier.width(10.dp))
            Button(
                modifier = Modifier.width(145.dp),
                shape = RoundedCornerShape(0.dp),
                onClick = {
                    val baseReference =
                        FirebaseDatabase.getInstance().getReference("Comment")

                    baseReference.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            var i = 1 // Initialiser un compteur pour les commentaires
                            var newReference: DatabaseReference? =
                                null // Référence pour le nouveau commentaire

                            // Parcourir les enfants pour trouver un nœud disponible
                            while (dataSnapshot.hasChild("$i")) {
                                i++
                            }

                            // Créer une nouvelle référence pour le nouveau commentaire
                            val commentReference = baseReference.child("$i")
                            val contentReference = commentReference.child("content")
                            val dateReference = commentReference.child("date")
                            val userReference = commentReference.child("user_id")
                            val pisteReference = commentReference.child("piste_id")

                            // Obtenir la date actuelle
                            val currentDate = Date()

                            // Créer un formateur de date avec le format souhaité
                            val dateFormat =
                                SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRENCH)
                            dateFormat.timeZone = TimeZone.getTimeZone("Europe/Paris")
                            // Formater la date actuelle en utilisant le formateur de date
                            val formattedTime = dateFormat.format(currentDate)

                            // Écrire le nouveau commentaire et sa date
                            userReference.setValue(userName)
                            pisteReference.setValue(pisteId)
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
                                            Log.e(
                                                "Firebase",
                                                "Error writing date: $exception"
                                            )
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
                    containerColor = colorResource(R.color.bright_gray),
                    contentColor = colorResource(R.color.blue_gray)
                )
            ) {
                Text("Envoyer")
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

class Couleurs {
    val rouge: Color = RedPiste
    val vert: Color = GreenPiste
    val bleu: Color = BluePiste
    val noir: Color = Color.Black
}

@Composable
fun ComListe2(id: Int) {
    val com = remember {
        mutableStateListOf<Comment>()
    }
    LaPorteCom(com)

    Box(modifier = Modifier.padding(bottom = 15.dp)) {
        LazyRow(horizontalArrangement = Arrangement.Center) {
            item {
                Spacer(Modifier.width(20.dp))
            }
            items(com.filter { it.piste_id == id }) { com ->
                Row(horizontalArrangement = Arrangement.Center) {
                    Card(
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .width(180.dp)
                            .height(80.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.water)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(start = 5.dp)
                        ) {
                            Text(
                                text = com.user_id,
                                fontFamily = comic_sans
                            )
                            Text(
                                text = com.date,
                                fontFamily = comic_sans
                            )
                            Text(
                                text = com.content,
                                fontFamily = comic_sans
                            )
                            Spacer(Modifier.width(10.dp))
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                }
            }
        }
    }
}
fun LaPorteCom(com: MutableList<Comment>) {
    val database = FirebaseDatabase.getInstance()
    val chatRef = database.getReference("Comment")

    chatRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val coms = mutableListOf<Comment>()
            for (snapshot in dataSnapshot.children) {
                val com = snapshot.getValue(Comment::class.java)
                com?.let { coms.add(it) }
            }
            com.clear()
            com.addAll(coms)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("Error: ${databaseError.message}")
        }
    })
}

