package fr.isen.aurianeramel.skiwaze

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
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkiWazeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Background()
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
        Column {
            Text(
                text = "Bienvenue sur SkiWaze !",
                fontSize = 20.sp,
                modifier = modifier
            )
            Connexion()
            Spacer(Modifier.height(20.dp))
            Piste()
            Remonte()

        }
    }
    Row {

    }
}

@Composable
fun Background(){
    Box(modifier = Modifier
        .background(color = colorResource(R.color.alice_blue))
        .alpha(0.4f)
    ){
        Image(
            painter = painterResource(R.drawable.ski_image),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
        )
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
            containerColor = colorResource(R.color.alice_blue),
            contentColor = colorResource(R.color.dark_slate_blue)),
        modifier = Modifier
            .height(40.dp)
            .width(250.dp)
    ) {
        Text("Connectez-vous")
    }
}

@Composable
fun Piste() {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(context, PisteActivity::class.java)
            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.alice_blue),
            contentColor = colorResource(R.color.dark_slate_blue)),
        modifier = Modifier
            .height(40.dp)
            .width(250.dp)
    ) {
        Text("Piste")
    }
}

@Composable
fun Remonte() {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(context, RemonteActivity::class.java)
            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.alice_blue),
            contentColor = colorResource(R.color.dark_slate_blue)),
        modifier = Modifier
            .height(40.dp)
            .width(250.dp)
    ) {
        Text("Remontés")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SkiWazeTheme {
        Greeting()
    }
}