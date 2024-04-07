package fr.isen.aurianeramel.skiwaze



import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.google.firebase.database.*

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import fr.isen.aurianeramel.skiwaze.database.Pistes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DownhillSkiing
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.sp
import fr.isen.aurianeramel.skiwaze.database.Chat

import fr.isen.aurianeramel.skiwaze.database.Comment
import fr.isen.aurianeramel.skiwaze.ui.theme.BluePiste
import fr.isen.aurianeramel.skiwaze.ui.theme.GreenPiste
import fr.isen.aurianeramel.skiwaze.ui.theme.Purple40
import fr.isen.aurianeramel.skiwaze.ui.theme.RedPiste
import fr.isen.aurianeramel.skiwaze.ui.theme.comic_sans
import fr.isen.aurianeramel.skiwaze.ui.theme.stg

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column {

            //ChatPage()
                ChatAffiche()
                androidx.compose.material.Divider(color = Color.Gray, thickness = 1.dp)
                Chatlist()
            }
        }
        Log.d("lifeCycle", "Menu Activity - OnCreate")
    }

    fun reload() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun GetChat(com: SnapshotStateList<Chat>) {
    DataBaseHelper.database.getReference("Chat")
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val _com = snapshot.children.mapNotNull { it.getValue(Chat::class.java) }
                com.addAll(_com)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("dataBase", error.toString())
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCHAT(onClose: () -> Unit) {
    val auth: FirebaseAuth = Firebase.auth
    val userName = auth.currentUser?.displayName
    Log.d("auth", userName ?: "")
    val context = LocalContext.current
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
                label = { androidx.compose.material3.Text("Saisissez votre message") },
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
                androidx.compose.material3.Text("Annuler")
            }
            Spacer(Modifier.width(10.dp))
            Button(
                modifier = Modifier.width(145.dp),
                shape = RoundedCornerShape(0.dp),
                onClick = {
                    val baseReference =
                        FirebaseDatabase.getInstance().getReference("Chat")

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
                androidx.compose.material3.Text("Envoyer")
            }
        }
}

@Composable
fun ChatAffiche() {
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
        androidx.compose.material3.Text(
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
            AddCHAT(
                onClose = { showComment = false },
            ) // Passer la fonction onClose pour fermer l'onglet du commentaire
        }
    }
}

@Composable
fun Chatlist() {
    val chat = remember {
        mutableStateListOf<Chat>()
    }
    LaPorte(chat)
    //GetChat(chat)
        LazyRow(horizontalArrangement = Arrangement.Center) {
            items(chat){ chat ->
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
                            androidx.compose.material3.Text(
                                text = chat.user_id,
                                fontFamily = comic_sans
                            )
                            androidx.compose.material3.Text(
                                text = chat.date,
                                fontFamily = comic_sans
                            )
                            androidx.compose.material3.Text(
                                text = chat.content,
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

fun LaPorte(chat: MutableList<Chat>) {
    val database = FirebaseDatabase.getInstance()
    val chatRef = database.getReference("Chat")

    chatRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val chats = mutableListOf<Chat>()
            for (snapshot in dataSnapshot.children) {
                val chat = snapshot.getValue(Chat::class.java)
                chat?.let { chats.add(it) }
            }
            chat.clear()
            chat.addAll(chats)

            // Ne plus appeler GetChat ici, car la liste de chat est mise à jour directement
            // GetChat(com)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("Error: ${databaseError.message}")
        }
    })
}