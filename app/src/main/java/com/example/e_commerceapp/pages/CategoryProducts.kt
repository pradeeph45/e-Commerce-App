package com.example.e_commerceapp.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CategoryProduct(modifier: Modifier = Modifier,categoryId : String){
    Text("Category Products Page :: ${categoryId}")
}