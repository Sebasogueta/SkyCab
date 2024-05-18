package com.example.skycab.view.postlogin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skycab.models.UserViewModel
import com.example.skycab.ui.theme.FontTitle
import com.example.skycab.ui.theme.text


@Composable
fun HomePostlogin(
    navController: NavController,
    userViewModel: UserViewModel,
) {
    val isPilotView by userViewModel.isPilotView.collectAsState()

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
                color = text,
                fontSize = 35.sp,
                fontFamily = FontTitle
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "EditProfile",
                modifier = Modifier
                    .clickable { navController.navigate("EditProfile") }
                    .size(35.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(16.dp),
            reverseLayout = false,
        ) {
            if (isPilotView) {
                item {
                    Text(text = "HomePostlogin en vista piloto")
                }
            } else {
                item {
                    Text(text = "HomePostlogin en vista usuario")
                }
            }
        }
    }
}

    /*
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(id = R.drawable.homephoto), /* TODO CAMBIAR FOTO */
            contentDescription = "Home Photo",
            modifier = Modifier.size(500.dp)
        )
        Text(
            text = if (logged) "Welcome $user!" else "Welcome User!",
            color = text,
            fontSize = 35.sp,
            fontFamily = FontTitle
        )
        */

/*
        if (logged) {
            val context = LocalContext.current
            Button(
                onClick = {
                    userViewModel.auth.signOut()
                    user = "User"
                    logged = !logged
                    Toast.makeText(
                        context,
                        "You have successfully logged out!",
                        Toast.LENGTH_SHORT
                    ).show()
                }, modifier = Modifier
                    .padding(15.dp)
                    .width(150.dp)
            ) {
                Text(text = "Log Out", fontSize = 20.sp)
            }
        } */


