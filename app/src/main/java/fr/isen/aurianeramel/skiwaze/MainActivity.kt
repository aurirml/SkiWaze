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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import fr.isen.aurianeramel.skiwaze.ui.theme.SkiWazeTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.colorResource
import androidx.compose.material3.*
import android.content.Intent
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.draw.drawBehind


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
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(modifier = Modifier.padding(top = 16.dp)) {
                                Icon(
                                    Icons.Filled.AcUnit,
                                    contentDescription = null,
                                    tint = colorResource(R.color.gray),
                                    modifier = Modifier.size(50.dp)
                                )
                                Text(
                                    text = stringResource(R.string.app_name),
                                    fontFamily = stg,
                                    fontSize = 40.sp,
                                    color = colorResource(R.color.gray),
                                    modifier = Modifier
                                )
                                Spacer(Modifier.height(40.dp))
                                Icon(
                                    Icons.Filled.AcUnit,
                                    contentDescription = null,
                                    tint = colorResource(R.color.gray),
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(colorResource(R.color.nude))
                                    .border(
                                        width = 2.dp,
                                        color = colorResource(R.color.nude)
                                    ), // Ajouter la bordure autour de la Row
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    text = "Bienvenue \t",
                                    fontFamily = stg,
                                    fontSize = 30.sp,
                                    color = colorResource(R.color.blue_gray),
                                    modifier = Modifier
                                )
                                auth.currentUser?.displayName?.let {
                                    Text(
                                        text = it,
                                        fontFamily = stg,
                                        color = colorResource(R.color.blue_gray),
                                        fontSize = 30.sp
                                    )
                                }
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(30.dp),
                                verticalArrangement = Arrangement.spacedBy(30.dp),
                                modifier = Modifier.widthIn(max = 325.dp)
                            ) {
                                item {
                                    Piste()
                                }
                                item {
                                    Remontee()
                                }
                                item {
                                    Piste()
                                }
                                item {
                                    Remontee()
                                }
                            }
                        }

                        Column(verticalArrangement = Arrangement.Bottom) {
                            deco()
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
        color = colorResource(R.color.gray)
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
            containerColor = colorResource(R.color.medium_grey),
            // contentColor = colorResource(R.color.dark_slate_blue)
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
            )
            Text(
                text = stringResource(R.string.piste),
                color = colorResource(R.color.gray),
                modifier = Modifier.padding(4.dp)
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
                color = colorResource(R.color.gray),
                modifier = Modifier.padding(7.dp)
            )
        }
    )
}

@Composable
fun MapButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(context, MapActivity::class.java)
            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(
        ),
        modifier = Modifier
            .height(40.dp)
            .width(250.dp)
    ) {
        Text("Map")
    }
}