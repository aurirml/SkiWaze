package fr.isen.aurianeramel.skiwaze

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*


class RegisterActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        //database = Firebase.database.reference
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }

        setContent {
            Background()
            val mail = remember { mutableStateOf(TextFieldValue("")) }
            val password = remember { mutableStateOf(TextFieldValue("")) }
            val lastname = remember { mutableStateOf(TextFieldValue("")) }
            val firstname = remember { mutableStateOf(TextFieldValue("")) }
            var showPassword by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = lastname.value,
                    onValueChange = { lastname.value = it },
                    label = { Text("Nom") },
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = firstname.value,
                    onValueChange = { firstname.value = it },
                    label = { Text("Prénom") },
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

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
                Spacer(Modifier.height(5.dp))
                Button(
                    onClick = { addUser(mail.value.text, password.value.text, firstname.value.text, lastname.value.text) }
                ) {
                    Text("Créer un compte")
                }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Vous avez déjà un comte ?"
                    )
                    signIn()
                }
            }
        }
        Log.d("lifeCycle", "Menu Activity - OnCreate")
    }


    fun addUser(email: String, password: String, firstname:String, lastname:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    reload()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        val ref = DataBaseHelper.database.getReference("User")
        val user = ref.push() // Generate a unique key for each user
        val userData = HashMap<String, Any>()
        userData["firstname"] = firstname
        userData["lastname"] = lastname
        // Set user data in the database
        user.setValue(userData)
            .addOnSuccessListener {
                Log.d("AddUserToDatabase", "User added to database successfully")
            }
            .addOnFailureListener { e ->
                Log.e("AddUserToDatabase", "Error adding user to database", e)
            }
    }

    fun reload() {
        val intent = Intent(this, PisteActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun signIn() {
    val context = LocalContext.current
    TextButton(
        onClick = {

        }
    ) {
        Text("Se connecter.")
    }
}