package com.example.skycab.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skycab.R
import com.example.skycab.ui.theme.FontTitle
import com.example.skycab.ui.theme.text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    navController: NavController,
    //userViewModel: UserViewModel,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var emailInput by remember { mutableStateOf("") }
        var passwordInput by remember { mutableStateOf("") }
        val context = LocalContext.current

        Image(
            painterResource(id = R.drawable.homephoto),
            contentDescription = "Home Photo",
            modifier = Modifier.size(350.dp)
        )

        Text(text = "Login", fontSize = 35.sp, modifier = Modifier.padding(16.dp), color = text, fontFamily = FontTitle)

        OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = {
                Text("Email:")
            },
            singleLine = true,
        )
        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text("Password:")
            },
            singleLine = true,
        )
        Row(modifier = Modifier.padding(15.dp)) {
            Button(onClick = {
                navController.navigate("Home")
            }) {
                Text(text = "Cancel")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = {
                if (emailInput.isNotEmpty()) {
                    if (passwordInput.isNotEmpty()) {
                        /*
                        userViewModel.signWithEmailAndPassword(
                            emailInput,
                            passwordInput,
                            context
                        ) { logged ->
                            if (logged) {
                                navController.navigate("Home")
                            } else {
                                passwordInput = ""
                            }
                        }
                         */
                    } else {
                        Toast.makeText(
                            context,
                            "Error: password field can't be empty!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Error: email field can't be empty!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            ) {
                Text(text = "Confirm")
            }
        }
    }
}
