package com.example.skycab.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun EditProfile(navController: NavController, userViewModel: UserViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier,
            state = rememberLazyListState(),
            contentPadding = PaddingValues(0.dp),
            reverseLayout = false,
            flingBehavior = ScrollableDefaults.flingBehavior(),
            userScrollEnabled = true
        ) {
            item {
                LogOut(navController, userViewModel)
            }
        }
    }
}

@Composable
private fun LogOut(navController: NavController, userViewModel: UserViewModel){
    val context = LocalContext.current
    Button(
        onClick = {
            userViewModel.auth.signOut()
            Toast.makeText(
                context,
                "You have successfully logged out!",
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate("HomePrelogin")
        }, modifier = Modifier
            .padding(15.dp)
            .width(150.dp)
    ) {
        Text(text = "Log Out", fontSize = 20.sp)
    }
}

