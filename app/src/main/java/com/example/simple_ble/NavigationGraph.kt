package com.example.simple_ble

import GraphScreen
import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(navController: NavHostController, bleViewModel: BleViewModel) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(bleViewModel, navController)
        }
        composable("graph") {
            GraphScreen(bleViewModel, navController)
        }
    }
}

