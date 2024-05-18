package com.example.skycab.view.prelogin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skycab.R
import com.example.skycab.models.UserViewModel
import com.example.skycab.ui.theme.FontTitle
import com.example.skycab.ui.theme.text

@Composable
fun HomePrelogin(
    navController: NavController,
    userViewModel: UserViewModel,
) {

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        item { HomePreloginScreen(navController, userViewModel) }
    }

}

@Composable
fun HomePreloginScreen(
    navController: NavController,
    userViewModel: UserViewModel,
) {

    var logged by remember { mutableStateOf(false) }

    if (userViewModel.auth.currentUser != null) {
        logged = true
    }

    var user by remember { mutableStateOf("") }

        if (logged) {
            userViewModel.getUser {
                user = it
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "SkyCab",
                fontSize = 35.sp,
                modifier = Modifier.padding(16.dp),
                color = text,
                fontFamily = FontTitle
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Image(
            painterResource(id = R.drawable.homephoto), /* TODO CAMBIAR FOTO */
            contentDescription = "Home Photo",
            modifier = Modifier.size(500.dp)
        )
        Text(
            text = "Welcome User!",
            color = text,
            fontSize = 35.sp,
            fontFamily = FontTitle
        )

        Column(modifier = Modifier.padding(15.dp)) {
            Button(
                onClick = {
                    navController.navigate("Login")
                },
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = "Log In", fontSize = 20.sp)
            }
            Button(onClick = {
                navController.navigate("Register")
            }, Modifier.width(150.dp)) {
                Text(text = "Register", fontSize = 20.sp)
            }

        }
    }
}



