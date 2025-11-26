package com.example.e_commerceapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.e_commerceapp.screens.AuthScreen
import com.example.e_commerceapp.screens.LoginScreen
import com.example.e_commerceapp.screens.RegisterScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "auth"){
        composable("auth") {
            AuthScreen(modifier,navController)
        }
        composable("login") {
            LoginScreen(modifier)
        }
        composable("register") {
            RegisterScreen(modifier)
        }
    }

}