package fr.isen.aurianeramel.skiwaze

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import androidx.compose.ui.Modifier
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }

        setContent {
            Background()
            val mail = remember { mutableStateOf(TextFieldValue("")) }
            val username = remember { mutableStateOf(TextFieldValue("")) }
            val password = remember { mutableStateOf(TextFieldValue("")) }
            var showPassword by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = username.value,
                    onValueChange = { username.value = it },
                    label = { Text("Pseudo") },
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false
                    )
                )
                TextField(
                    value = mail.value,
                    onValueChange = { mail.value = it },
                    label = { Text("Adresse mail") },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Email
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Mot de passe") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Password
                    ),
                    trailingIcon = {
                        val passwordIcon = if (showPassword) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        }
                        val description = if (showPassword) {
                            "Hide Password"
                        } else {
                            "Show Password"
                        }
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(imageVector = passwordIcon, contentDescription = description)
                        }
                    }
                )
                Button(
                    onClick = { signIn(mail.value.text, password.value.text) }
                ){
                    Text("Se connecter")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Pas de compte ?"
                    )
                    createAccount()

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

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("auth", auth.currentUser?.displayName ?: "")
                    reload()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}

@Composable
fun createAccount(){
    val context = LocalContext.current
    TextButton(
        onClick = {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent) }
    ) {
        Text("En créer un.")
    }
}