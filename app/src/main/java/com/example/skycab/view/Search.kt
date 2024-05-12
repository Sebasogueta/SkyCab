package com.example.skycab.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skycab.models.UserViewModel
import com.example.skycab.ui.theme.FontTitle
import com.example.skycab.ui.theme.text


//val cityInfoList = mutableStateListOf<CityInfo>()


@Composable
fun Search(
    navController: NavHostController,
    userViewModel: UserViewModel,
    //cityViewModel: CityViewModel,
) {
    //val weatherViewModel: WeatherViewModel = viewModel()
    //var cityInput by remember { mutableStateOf("") }
    //cityInput = cityViewModel.getCity()
    //cityViewModel.setCity("")
    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Text(
                    text = "Search now!",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(16.dp),
                    color = text,
                    fontFamily = FontTitle
                )
            }
            //item { WeatherInfo(userViewModel, cityInput, weatherViewModel) }
            item { WeatherInfo(userViewModel) }
        }

    }

}


@SuppressLint("SuspiciousIndentation", "UnrememberedMutableState")
@Composable
fun WeatherInfo(
    userViewModel: UserViewModel,
    //cityParameter: String,
    //weatherViewModel: WeatherViewModel
) {

    var logged by remember { mutableStateOf(false) }

    if (userViewModel.auth.currentUser != null) {
        logged = true
    }

    Text(text = "Search")
}