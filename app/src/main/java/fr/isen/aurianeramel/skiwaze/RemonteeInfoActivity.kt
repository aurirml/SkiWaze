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
import fr.isen.aurianeramel.skiwaze.database.Remontees
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

class RemonteeInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val remonteeId = intent.getIntExtra("remonteeId", -1)

        if (remonteeId != -1) {
            getRemonteeById(remonteeId) { piste ->
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
                                        text = "Les remontées",
                                        fontFamily = stg,
                                        fontSize = 30.sp,
                                        color = colorResource(R.color.blue_gray),
                                        modifier = Modifier
                                    )
                                }
                                RemonteeInfoContent(remonteeId)
                            }
                        }
                    }
                } else {
                    // La piste n'a pas été trouvée, gérer l'erreur
                    Toast.makeText(this, "Remontée non trouvée", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } else {
            // L'ID de la piste n'a pas été passé, gérer l'erreur
            Toast.makeText(this, "ID de la remontée non fourni", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}

@Composable
fun RemonteeInfoContent(remonteeId: Int) {
    val context = LocalContext.current

    // Créez un état mutable pour stocker les informations de la piste
    val remonteeState = remember { mutableStateOf<Remontees?>(null) }
    val auth: FirebaseAuth = Firebase.auth

    LaunchedEffect(remonteeId) {
        getRemonteeById(remonteeId) { remontee ->
            remonteeState.value = remontee
        }
    }

    // Affichez les informations de la piste dans votre interface utilisateur
    remonteeState.value?.let { remontee ->
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
                        text = remontee.name,
                        fontFamily = comic_sans,
                        fontSize = 20.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.padding(end = 20.dp)
                    ) {
                        if (remontee.state) {
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
            var etat = remontee.state
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
                        val newStateState = !remontee.state

                        FirebaseDatabase
                            .getInstance()
                            .getReference("Pistes/${remontee.id - 1}/state")
                            .setValue(newStateState)

                        getRemonteeById(remonteeId) { remontee ->
                            remonteeState.value = remontee
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
                //Couleur(pisteId)
            }
            item {
                //Frequentation(pisteId)
            }
            item {
                //Avalanche(pisteId)
            }
            item {
                //Damee(pisteId)
            }
        }
        Comment(remonteeId)
        Spacer(Modifier.height(5.dp))
        ComListe2(remonteeId)
    }
}

/*@Composable
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
}*/

@Composable
fun FrequentationRemontee(remonteeId: Int) {
    var freq: Int? by remember { mutableStateOf(null) }

    // Récupérer la fréquence depuis Firebase Realtime Database
    val databaseReference =
        FirebaseDatabase.getInstance().getReference("Pistes/${remonteeId}/frequence")
    val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            freq = snapshot.getValue(Int::class.java)
        }

        override fun onCancelled(error: DatabaseError) {
            // Gérer les erreurs ici
        }
    }

    LaunchedEffect(remonteeId) {
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

/*@Composable
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
}*/
fun getRemonteeById(remonteeId: Int, callback: (Remontees?) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("Remontees")

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (childSnapshot in snapshot.children) {
                val remontee = childSnapshot.getValue(Remontees::class.java)
                if (remontee?.id == remonteeId) {
                    callback(remontee)
                    return
                }
            }
            callback(null)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Firebase", "Error fetching data", error.toException())
            callback(null)
        }
    })
}