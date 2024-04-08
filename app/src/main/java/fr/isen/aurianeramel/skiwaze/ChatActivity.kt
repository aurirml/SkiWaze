package fr.isen.aurianeramel.skiwaze

import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import fr.isen.aurianeramel.skiwaze.database.Chat
import fr.isen.aurianeramel.skiwaze.ui.theme.comic_sans
import fr.isen.aurianeramel.skiwaze.ui.theme.stg

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                // color = MaterialTheme.colorScheme.background
            ) {
                Background()
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .height(350.dp)
                        .fillMaxWidth()
                ) {
                    TopBar()
                    ChatAffiche()
                    Spacer(Modifier.height(10.dp))
                    Chatlist()

                }
                Column(verticalArrangement = Arrangement.Bottom) {
                    AddCHAT(
                        onClose = { },
                    )
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCHAT(onClose: () -> Unit) {
    val auth: FirebaseAuth = Firebase.auth
    val userName = auth.currentUser?.displayName
    Log.d("auth", userName ?: "")
    var commentaireText by remember { mutableStateOf("") } // Variable pour stocker le texte du commentaire

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start
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
                maxLines = 2,
                modifier = Modifier
                    .width(320.dp)
                    .background(colorResource(R.color.water)),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = colorResource(R.color.water),
                    unfocusedLabelColor = colorResource(R.color.opal),
                    focusedLabelColor = colorResource(R.color.opal)
                )
            )
            IconButton(
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
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    tint = colorResource(R.color.blue_gray),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
fun ChatAffiche() {
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
            text = "Chat",
            fontFamily = stg,
            fontSize = 30.sp,
            color = colorResource(R.color.blue_gray),
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}

@Composable
fun Chatlist() {
    lateinit var auth: FirebaseAuth
    auth = Firebase.auth

    val chat = remember {
        mutableStateListOf<Chat>()
    }

    LaPorte(chat)

    val listState = rememberLazyListState()

    // Appel de scrollToItem une seule fois lorsque la liste est prête
    LaunchedEffect(Unit) {
        if (chat.isNotEmpty()) {
            listState.animateScrollToItem(chat.size - 1)
        }
    }

    LazyColumn(
        modifier = Modifier.height(410.dp),
        state = listState
    ) {
        items(chat) { chat ->
            var alignText = if (chat.user_id == auth.currentUser?.displayName) {
                Alignment.End
            } else {
                Alignment.Start
            }
            Column(
                horizontalAlignment = alignText,
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .width(250.dp)
                        .height(80.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.water)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(start = 15.dp, top = 5.dp)
                    ) {
                        Text(
                            text = chat.user_id,
                            fontFamily = comic_sans,
                        )
                        Text(
                            text = chat.date,
                            fontFamily = comic_sans,
                        )
                        Text(
                            text = chat.content,
                            fontFamily = comic_sans,
                        )
                        Spacer(Modifier.width(10.dp))
                    }
                }
                Spacer(Modifier.height(10.dp))
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
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("Error: ${databaseError.message}")
        }
    })
}