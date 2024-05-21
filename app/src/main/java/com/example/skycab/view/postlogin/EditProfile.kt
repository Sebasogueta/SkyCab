package com.example.skycab.view.postlogin

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skycab.models.UserViewModel
import com.example.skycab.ui.theme.FontHeader
import com.example.skycab.ui.theme.FontTitle
import com.example.skycab.ui.theme.text
import com.example.skycab.ui.theme.textfields

@Composable
fun EditProfile(navController: NavController, userViewModel: UserViewModel) {
    var bio by remember { mutableStateOf(TextFieldValue("")) }
    var pilotLicense by remember { mutableStateOf(TextFieldValue("")) }

    val isPilotView by userViewModel.isPilotView.collectAsState()
    val isPilot by userViewModel.isPilot.collectAsState()


    // LaunchedEffect para inicializar bio y pilotLicense cuando el Composable se carga
    LaunchedEffect(Unit) {
        userViewModel.getUserBio { bioText ->
            bio = TextFieldValue(bioText)
        }
        userViewModel.getUserLicense { licenseText ->
            pilotLicense = TextFieldValue(licenseText)
        }
    }

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
                fontFamily = FontHeader
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
        // Toggle View
        if (isPilot) {
            Text(
                text = if(isPilotView) "pilot" else "user",
                color = text,
                fontSize = 25.sp,
                fontFamily = FontTitle
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { userViewModel.changeView() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = if (isPilotView) "Switch to User View" else "Switch to Pilot View")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(16.dp),
            reverseLayout = false,
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                UserData(userViewModel = userViewModel)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Add a bio for users to know you better!",
                    color = Color.DarkGray,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                BioSection(bio = bio, onBioChange = { bio = it })
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Are you a pilot? Add your license to start posting flights!",
                    color = Color.DarkGray,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                PilotLicenseSection(
                    pilotLicense = pilotLicense,
                    onPilotLicenseChange = { pilotLicense = it })
                Spacer(modifier = Modifier.height(32.dp))
                SaveButton(
                    navController = navController,
                    userViewModel = userViewModel,
                    bio = bio.text,
                    pilotLicense = pilotLicense.text
                )
            }
        }
    }
}

@Composable
private fun UserData(userViewModel: UserViewModel) {
    var username by remember { mutableStateOf("") }
    userViewModel.getUser { username = it }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Username:",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = username,
            fontSize = 20.sp,
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BioSection(bio: TextFieldValue, onBioChange: (TextFieldValue) -> Unit) {
    val maxBioChar = 150

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = bio,
            onValueChange = {
                if (it.text.length <= maxBioChar) { // Limite de caracteres
                    onBioChange(it)
                }
            },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = textfields
            ),
            trailingIcon = {
                if (bio.text.isNotEmpty()) {
                    IconButton(onClick = { onBioChange(TextFieldValue("")) }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${bio.text.length}/$maxBioChar",
            fontSize = 14.sp,
            color = if(bio.text.length == maxBioChar) Color.Red else Color.Gray,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PilotLicenseSection(
    pilotLicense: TextFieldValue,
    onPilotLicenseChange: (TextFieldValue) -> Unit
) {
    val maxLicenseChar = 20
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = pilotLicense,
                onValueChange = {
                    if (it.text.length <= maxLicenseChar) { // Limite de caracteres
                        onPilotLicenseChange(it)
                    }
                },
                label = { Text("Pilot License Number") },
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    if (pilotLicense.text.isNotEmpty()) {
                        IconButton(onClick = { onPilotLicenseChange(TextFieldValue("")) }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = textfields
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${pilotLicense.text.length}/$maxLicenseChar",
            fontSize = 14.sp,
            color = if(pilotLicense.text.length == maxLicenseChar) Color.Red else Color.Gray,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun SaveButton(
    navController: NavController,
    userViewModel: UserViewModel,
    bio: String,
    pilotLicense: String
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                // Save the changes
                userViewModel.editProfile(bio, pilotLicense) { result ->
                    if (result) {
                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate("HomePostlogin")
                    } else {
                        Toast.makeText(context, "Error updating profile", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
        ) {
            Text(text = "Save Changes", fontSize = 18.sp)
        }
    }
}
