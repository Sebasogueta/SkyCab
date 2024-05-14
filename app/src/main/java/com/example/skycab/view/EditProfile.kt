package com.example.skycab.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@Composable
fun EditProfile(navController: NavController, userViewModel: UserViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "BackToHome",
                modifier = Modifier
                    .clickable {
                        navController.navigate("HomePostlogin")
                    }
                    .size(35.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Profile",
                color = text,
                fontSize = 35.sp,
                fontFamily = FontTitle
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Logout",
                modifier = Modifier
                    .clickable {
                        userViewModel.auth.signOut()
                        Toast
                            .makeText(
                                context,
                                "You have successfully logged out!",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        navController.navigate("HomePrelogin")
                    }
                    .size(35.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        LazyColumn(
            modifier = Modifier,
            state = rememberLazyListState(),
            contentPadding = PaddingValues(0.dp),
            reverseLayout = false,
            flingBehavior = ScrollableDefaults.flingBehavior(),
            userScrollEnabled = true
        ) {
            item {
                userData(userViewModel = userViewModel)
            }
        }
    }
}

@Composable
private fun userData(userViewModel: UserViewModel){
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    userViewModel.getUser { username = it }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Username:")
        Text(text = username)
    }
}

@Composable
private fun registerAsAPilot(navController: NavController, userViewModel: UserViewModel){

}

private fun changePassword(navController: NavController,userViewModel: UserViewModel){

}


