package com.example.skycab.view

import android.service.autofill.OnClickAction
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skycab.R
import com.example.skycab.models.UserViewModel
import com.example.skycab.ui.theme.FontTitle
import com.example.skycab.ui.theme.text
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@Composable
fun HomePostlogin(
    navController: NavController,
    userViewModel: UserViewModel,
) {

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        item { HomePostloginScreen(navController, userViewModel) }
    }

}

@Composable
fun HomePostloginScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
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
                modifier = Modifier.clickable { navController.navigate("EditProfile") }
                    .size(35.dp)
                    .align(Alignment.CenterVertically)
            )
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
}

