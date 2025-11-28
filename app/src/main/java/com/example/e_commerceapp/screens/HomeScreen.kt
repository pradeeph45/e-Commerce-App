package com.example.e_commerceapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun HomeScreen(modifier: Modifier = Modifier,navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(32.dp)
        ,horizontalAlignment = Alignment.CenterHorizontally
        ,verticalArrangement = Arrangement.Center
    ) {
        Text("Home Screen")
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            Firebase.auth.signOut()
            navController.navigate("auth"){
                popUpTo("home"){inclusive = true}
            }
        }) {
            Text("Logout")
        }
    }

}