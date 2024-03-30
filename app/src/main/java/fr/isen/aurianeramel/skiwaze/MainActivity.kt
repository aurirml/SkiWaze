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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.material3.*
import android.content.Intent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.DownhillSkiing
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.DownhillSkiing
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Map
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.isen.aurianeramel.skiwaze.ui.theme.hand_marker
import fr.isen.aurianeramel.skiwaze.ui.theme.stg

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    //À décommenter si on veut utiliser un badge de compte sur les icons (comme pour le nombre de chat non lu par exemple)
    /*
    val hasNews: Boolean,
    val badgeCount: Int? = null
    */
)

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        val currentUser = auth.currentUser

        setContent {
            SkiWazeTheme {
                val scrollState = rememberScrollState()
                val items = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home
                    ),
                    BottomNavigationItem(
                        title = "Ski",
                        selectedIcon = Icons.Filled.DownhillSkiing,
                        unselectedIcon = Icons.Outlined.DownhillSkiing
                    ),
                    BottomNavigationItem(
                        title = "Remontées",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home
                    ),
                    BottomNavigationItem(
                        title = "Map",
                        selectedIcon = Icons.Filled.Map,
                        unselectedIcon = Icons.Outlined.Map
                    ),
                    BottomNavigationItem(
                        title = "Log Out",
                        selectedIcon = Icons.AutoMirrored.Outlined.Logout,
                        unselectedIcon = Icons.AutoMirrored.Outlined.Logout
                    )
                )
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var selectedItemIndex by rememberSaveable {
                        mutableIntStateOf(0)
                    }
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
                                            //navController.navigate(item.title)
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        icon = {

                                        })
                                }
                            }
                        }
                    ) {

                    }
                    Background()
                    if (currentUser == null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Greeting()
                            Spacer(Modifier.height(20.dp))
                            CoMessage()
                        }
                    } else {
                        Column(
                            Modifier.verticalScroll(scrollState),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.height(30.dp))
                            Row {
                                Text(
                                    text = "Bienvenue \t",
                                    fontFamily = stg,
                                    fontSize = 30.sp,
                                    modifier = Modifier
                                )
                                auth.currentUser?.displayName?.let {
                                    Text(
                                        text = it,
                                        fontFamily = stg,
                                        fontSize = 30.sp
                                    )
                                }
                            }
                            Spacer(Modifier.height(20.dp))
                            Piste()
                            Spacer(Modifier.height(20.dp))
                            Remontee()
                            Spacer(Modifier.height(20.dp))
                            Piste()
                            Spacer(Modifier.height(20.dp))
                            deco()
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
                // containerColor = colorResource(R.color.alice_blue),
                //  contentColor = colorResource(R.color.dark_slate_blue)
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
            colorResource(R.color.light_blue),
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
                width = 380.dp,
                height = 230.dp
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
                width = 380.dp,
                height = 230.dp
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
                painter = painterResource(R.drawable.remontee_ski),
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
            //containerColor = colorResource(R.color.alice_blue),
            //contentColor = colorResource(R.color.dark_slate_blue)
        ),
        modifier = Modifier
            .height(40.dp)
            .width(250.dp)
    ) {
        Text("Map")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SkiWazeTheme {
        Greeting()
    }
}