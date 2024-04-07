package fr.isen.aurianeramel.skiwaze

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import fr.isen.aurianeramel.skiwaze.ui.theme.SkiWazeTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.colorResource
import androidx.compose.material3.*
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.isen.aurianeramel.skiwaze.ui.theme.stg
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import fr.isen.aurianeramel.skiwaze.ui.theme.comic_sans


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        val currentUser = auth.currentUser

        setContent {

            SkiWazeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    // color = MaterialTheme.colorScheme.background
                ) {
                    Background()
                    if (currentUser != null) {
                        val scrollState = rememberScrollState()
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            TopBar()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(colorResource(R.color.bright_gray))
                                    .border(width = 2.dp, color = colorResource(R.color.bright_gray)),
                                horizontalArrangement = Arrangement.SpaceBetween, // Espacement égal entre les éléments
                                verticalAlignment = Alignment.CenterVertically // Aligner les éléments verticalement au centre
                            ) {
                                Row {}
                                Row(
                                    modifier = Modifier.padding(start = 50.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically // Aligner les éléments verticalement au centre
                                ) {
                                    Text(
                                        text = "Bienvenue \t",
                                        fontFamily = stg,
                                        textAlign = TextAlign.Center,
                                        fontSize = 40.sp,
                                        color = colorResource(R.color.blue_gray)
                                    )
                                    auth.currentUser?.displayName?.let {
                                        Text(
                                            text = it,
                                            fontFamily = stg,
                                            textAlign = TextAlign.Center,
                                            color = colorResource(R.color.blue_gray),
                                            fontSize = 40.sp
                                        )
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically // Aligner les éléments verticalement au centre
                                ) {
                                    IconButton(
                                        onClick = {
                                            Firebase.auth.signOut()
                                            reload()
                                        },
                                        modifier = Modifier
                                            .size(70.dp)
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Logout,
                                            tint = colorResource(R.color.blue_gray),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .background(color = colorResource(R.color.columbina_blue)),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                WeatherScreen()
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
                                        .widthIn(max = 350.dp)
                                        .height(400.dp)
                                        .padding(top = 50.dp)
                                ) {
                                    item {
                                        Piste()
                                    }
                                    item {
                                        Remontee()
                                    }
                                    item {
                                        Map()
                                    }
                                    item {
                                        Chat()
                                    }
                                }
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Background()
                            Greeting()
                            Spacer(Modifier.height(20.dp))
                            CoMessage()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun deco() {
        Button(
            onClick = {
                Firebase.auth.signOut()
                reload()
            },
            colors = ButtonDefaults.buttonColors(
            ),
            modifier = Modifier
                .height(40.dp)
                .width(250.dp)
        ) {
            Text("Déconnexion")
        }
    }

    fun reload() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Spacer(Modifier.height(20.dp))
    Text(
        text = stringResource(R.string.app_name),
        fontFamily = stg,
        fontSize = 60.sp,
        color = colorResource(R.color.bright_gray)
    )

}

@Composable
fun CoMessage(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.skiwaze_logo),
            contentDescription = null,
            modifier = Modifier
        )
    }

    Spacer(modifier.height(20.dp))
    Connexion()
    Spacer(modifier.height(20.dp))
    Register()
}

@Composable
fun Background() {
    val linear = Brush.linearGradient(
        listOf(
            colorResource(R.color.blue_gray),
            colorResource(R.color.water)
        )
    )

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .background(linear)

    ) {
    }
}

@Composable
fun Connexion() {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.medium_grey)
        ),
        modifier = Modifier
            .height(45.dp)
            .width(240.dp)
    ) {
        Text(stringResource(R.string.connexion))
    }
}

@Composable
fun Register() {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.medium_grey)
        ),
        modifier = Modifier
            .height(45.dp)
            .width(240.dp)
    ) {
        Text(stringResource(R.string.register))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Piste(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(
                width = 140.dp,
                height = 140.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.powder_blue)
        ),
        onClick = {
            val intent = Intent(context, PisteActivity::class.java)
            context.startActivity(intent)
        },
        content = {
            Image(
                painter = painterResource(R.drawable.piste_ski),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
            ) // Taille fixe pour le Box contenant le texte
            Text(
                text = stringResource(R.string.piste),
                fontFamily = comic_sans,
                color = colorResource(R.color.black),
                fontSize = 20.sp
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Remontee(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(
                width = 140.dp,
                height = 140.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.powder_blue)
        ),
        onClick = {
            val intent = Intent(context, RemonteActivity::class.java)
            context.startActivity(intent)
        },
        content = {
            Image(
                painter = painterResource(R.drawable.remonte_ski),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.remontee),
                color = colorResource(R.color.black),
                fontFamily = comic_sans,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chat(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(
                width = 140.dp,
                height = 140.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.powder_blue)
        ),
        onClick = {
             val intent = Intent(context, ChatActivity::class.java)
            context.startActivity(intent)
        },
        content = {
            Image(
                painter = painterResource(R.drawable.message),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.chat),
                color = colorResource(R.color.black),
                fontFamily = comic_sans,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    )
}

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 5.dp)
        ) {
            Icon(
                Icons.Filled.AcUnit,
                contentDescription = null,
                tint = colorResource(R.color.bright_gray),
                modifier = Modifier
                    .size(70.dp)
                    .padding(top = 5.dp)
            )
            Text(
                text = stringResource(R.string.app_name),
                fontFamily = stg,
                fontSize = 60.sp,
                color = colorResource(R.color.bright_gray),
                modifier = Modifier
            )
            Spacer(Modifier.height(40.dp))
            Icon(
                Icons.Filled.AcUnit,
                contentDescription = null,
                tint = colorResource(R.color.bright_gray),
                modifier = Modifier
                    .size(70.dp)
                    .padding(top = 5.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Map(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(
                width = 140.dp,
                height = 140.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.powder_blue)
        ),
        onClick = {
            val intent = Intent(context, MapActivity::class.java)
            context.startActivity(intent)
        },
        content = {
            Image(
                painter = painterResource(R.drawable.map_ski),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.map),
                color = colorResource(R.color.black),
                fontSize = 20.sp,
                fontFamily = comic_sans
            )
        }
    )
}

@Composable
fun WeatherScreen(modifier: Modifier = Modifier) {
    val weather = remember { getWeatherForToday() }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.sunny),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 30.dp)
        ) {
            if (weather != null) {
                Text(
                    text = "Température: ${weather.temperature}°C",
                    fontFamily = comic_sans,
                    fontSize = 15.sp,
                )
                Text(
                    text = "Humidité: ${weather.humidity}%",
                    fontFamily = comic_sans,
                    fontSize = 15.sp
                )
                // Ajoutez d'autres détails météorologiques si nécessaire
            } else {
                Text(
                    text = "Impossible de charger les données météorologiques",
                    fontSize = 15.sp
                )
            }
        }

    }
}

// Fonction factice pour obtenir les données météorologiques du jour
fun getWeatherForToday(): Weather? {
    // Ici, vous devriez faire un appel à une API météo pour récupérer les données
    // Pour cet exemple, nous allons simplement retourner des données factices
    return Weather(25, 60) // Température: 25°C, Humidité: 60%
}

// Modèle pour représenter les données météorologiques
data class Weather(val temperature: Int, val humidity: Int)