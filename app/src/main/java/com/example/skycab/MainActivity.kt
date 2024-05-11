package com.example.skycab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skycab.models.UserViewModel
import com.example.skycab.ui.theme.SkyCabTheme
import com.example.skycab.view.Home
import com.example.skycab.view.Login
import com.example.skycab.view.MyFlights
import com.example.skycab.view.Register
import com.example.skycab.view.Search
import com.example.skycab.view.SplashScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            //val userViewModel: UserViewModel = viewModel()

            var isLoading by remember { mutableStateOf(true) }
            LaunchedEffect(Unit) {
                delay(1000)
                isLoading = false
            }

            SkyCabTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { MyBottomNavigation(navController) }
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
                        // Usar AnimatedVisibility para mostrar el SplashScreen solo mientras isLoading es verdadero
                        AnimatedVisibility(visible = isLoading) {
                            SplashScreen()
                        }

                        // Una vez que isLoading sea falso, mostrar el contenido principal
                        AnimatedVisibility(visible = !isLoading) {
                        }

                        NavHost(
                                navController = navController, startDestination = "Home"
                            ) {
                            composable("MyFlights") {
                                MyFlights(
                                    navController = navController,
                                    //userViewModel
                                )
                            }
                            composable("Home") {
                                Home(
                                    navController = navController,
                                    //userViewModel
                                )
                            }
                            composable("Search") {
                                Search(
                                    navController = navController,
                                    //userViewModel
                                )
                            }

                            composable("Login") {
                                Login(
                                    navController = navController,
                                    //userViewModel
                                )
                            }

                            composable("Register") {
                                Register(
                                    navController = navController,
                                    //userViewModel
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyBottomNavigation(navController: NavHostController) {

    var selectedItem by remember { mutableStateOf("Home") }

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
                            R.drawable.filled_plane //cambiar por icono de avión relleno
                        } else {
                            R.drawable.outlined_plane // cambiar por icono de avion outlined
                        }
                    ),
                    contentDescription = "MyFlights",
                    tint = Color.White
                )
            },
            label = { Text("MyFlights", color = Color.White) })
        BottomNavigationItem(
            selected = selectedItem == "Home",
            onClick = {
                selectedItem = "Home"
                navController.navigate("Home")
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
                    tint = Color.White
                )
            },
            label = { Text("Home", color = Color.White) })

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
                            R.drawable.filled_search // cambiar por icono de busqueda filled
                        } else {
                            R.drawable.outlined_search //cambiar por icono de busqueda outlined
                        }
                    ),
                    contentDescription = "Search",
                    tint = Color.White
                )
            },
            label = { Text("Search", color = Color.White) })
    }
}