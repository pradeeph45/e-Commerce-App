package com.example.e_commerceapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.e_commerceapp.pages.CartPage
import com.example.e_commerceapp.pages.FavoritePage
import com.example.e_commerceapp.pages.HomePage
import com.example.e_commerceapp.pages.ProfilePage


@Composable
fun HomeScreen(modifier: Modifier = Modifier,navController: NavController){

    val navItems = listOf(
        NavItems("Home",Icons.Default.Home),
        NavItems("Favorite",Icons.Default.Favorite),
        NavItems("Cart",Icons.Default.ShoppingCart),
        NavItems("Profile",Icons.Default.Person)
    )

    var selectedIndex by remember() { mutableStateOf(0) }

    Scaffold(
       bottomBar = {
           NavigationBar {
              navItems.forEachIndexed { index,navItem ->
                  NavigationBarItem(
                      selected = index == selectedIndex,
                      onClick = {
                          selectedIndex = index
                      },
                      icon = {
                          Icon(imageVector = navItem.item, contentDescription = navItem.label)
                      },
                      label = {Text(navItem.label)})
              }
           }
       }
    ){
     ContentScreen(modifier = Modifier.padding(it),selectedIndex)
}
}


@Composable
fun ContentScreen(modifier: Modifier,selectedIndex: Int){
    when(selectedIndex){
        0 -> HomePage(modifier)
        1 -> FavoritePage(modifier)
        2 -> CartPage(modifier)
        3 -> ProfilePage(modifier)
    }

}

data class NavItems(
    val label : String,
    val item: ImageVector
)