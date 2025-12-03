package com.example.e_commerceapp.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerceapp.components.BannerView
import com.example.e_commerceapp.components.CategoriesView
import com.example.e_commerceapp.components.HeaderView


@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp) // smaller, symmetric padding
    ) {
        HeaderView(modifier)
        Spacer(modifier = Modifier.height(8.dp))
        BannerView(modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Categories",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(15.dp))

        CategoriesView(modifier = Modifier.fillMaxWidth())
    }
}

