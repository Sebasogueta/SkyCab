package com.example.skycab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skycab.models.UserViewModel
import com.example.skycab.ui.theme.SkyCabTheme
import com.example.skycab.view.postlogin.EditProfile
import com.example.skycab.view.postlogin.HomePostlogin
import com.example.skycab.view.prelogin.HomePrelogin
import com.example.skycab.view.prelogin.Login
import com.example.skycab.view.postlogin.MyFlights
import com.example.skycab.view.prelogin.Register
import com.example.skycab.view.postlogin.Search
import com.example.skycab.view.SplashScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val userViewModel: UserViewModel = viewModel()

            var isLoading by remember { mutableStateOf(true) }
            LaunchedEffect(Unit) {
                delay(1000)
                isLoading = false
            }

            SkyCabTheme {
                // Usar AnimatedVisibility para mostrar el SplashScreen solo mientras isLoading es verdadero
                AnimatedVisibility(visible = isLoading) {
                    SplashScreen()
                }

                // Una vez que isLoading sea falso, mostrar el contenido principal
                AnimatedVisibility(visible = !isLoading) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = { MyBottomNavigation(navController, userViewModel) }
                    )
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = it.calculateBottomPadding()
                                )
                        ) {

                            NavHost(
                                navController = navController,
                                startDestination = if (userViewModel.auth.currentUser != null) "HomePostlogin" else "HomePrelogin"
                            ) {
                                //BottomBar
                                composable("MyFlights") {
                                    MyFlights(
                                        navController = navController,
                                        userViewModel
                                    )
                                }
                                composable("HomePostlogin") {
                                    HomePostlogin(
                                        navController = navController,
                                        userViewModel
                                    )
                                }
                                composable("Search") {
                                    Search(
                                        navController = navController,
                                        userViewModel
                                    )
                                }

                                composable("EditProfile") {
                                    EditProfile(
                                        navController = navController,
                                        userViewModel
                                    )
                                }

                                //Prelogin
                                composable("HomePrelogin") {
                                    HomePrelogin(
                                        navController = navController,
                                        userViewModel
                                    )
                                }
                                composable("Login") {
                                    Login(
                                        navController = navController,
                                        userViewModel
                                    )
                                }

                                composable("Register") {
                                    Register(
                                        navController = navController,
                                        userViewModel
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyBottomNavigation(navController: NavHostController, userViewModel: UserViewModel) {

    var selectedItem by remember { mutableStateOf("Home") }
    var isLoggedIn by remember { mutableStateOf(userViewModel.auth.currentUser != null) }

    // Visualiza cambios en el estado de la autenticación
    LaunchedEffect(userViewModel.auth) {
        userViewModel.auth.addAuthStateListener { auth ->
            isLoggedIn = auth.currentUser != null
        }
    }

    if (isLoggedIn) {
        BottomNavigation(backgroundColor = Color.Blue, contentColor = Color.White) {
            BottomNavigationItem(
                selected = selectedItem == "MyFlights",
                onClick = {
                    selectedItem = "MyFlights"
                    navController.navigate("MyFlights")
                },

                icon = {

                    Icon(
                        painter = painterResource(
                            id = if (selectedItem == "MyFlights") {
                                R.drawable.filled_plane
                            } else {
                                R.drawable.outlined_plane 
                            }
                        ),
                        contentDescription = "MyFlights",
                        tint = Color.White,
                        modifier = if (selectedItem == "MyFlights") {
                            Modifier.size(65.dp)
                        } else {
                            Modifier.size(40.dp)
                        }
                    )
                }
                //label = { Text("MyFlights", color = Color.White) }
            )
            BottomNavigationItem(
                selected = selectedItem == "Home",
                onClick = {
                    selectedItem = "Home"
                    navController.navigate("HomePostlogin")
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (selectedItem == "Home") {
                                R.drawable.filled_home
                            } else {
                                R.drawable.outlined_home
                            }
                        ),
                        contentDescription = "Home",
                        tint = Color.White,
                        modifier = if (selectedItem == "Home") {
                            Modifier.size(50.dp)
                        } else {
                            Modifier.size(40.dp)
                        }
                    )
                }
                //label = { Text("Home", color = Color.White) }
            )
            BottomNavigationItem(
                selected = selectedItem == "Search",
                onClick = {
                    selectedItem = "Search"
                    navController.navigate("Search")
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (selectedItem == "Search") {
                                R.drawable.filled_search
                            } else {
                                R.drawable.outlined_search
                            }
                        ),
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = if (selectedItem == "Search") {
                            Modifier.size(40.dp)
                        } else {
                            Modifier.size(30.dp)
                        }
                    )
                }
                //label = { Text("Search", color = Color.White) }
            )
        }
    }

}