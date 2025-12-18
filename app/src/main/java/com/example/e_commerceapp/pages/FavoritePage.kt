package com.example.e_commerceapp.pages

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerceapp.AppUtil
import com.example.e_commerceapp.Model.ProductModel
import com.example.e_commerceapp.components.ProductItemView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun FavoritePage(modifier: Modifier){
    val productsList = remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {

        val favoriteList = AppUtil.getFavouriteList(context)

        if(favoriteList.isEmpty()){
            productsList.value = emptyList()
        } else{
            Firebase.firestore.collection("data").document("stock").collection("products")
                .whereEqualTo("id",favoriteList.toList())
                .get().addOnCompleteListener {
                    if(it.isSuccessful){
                        var resultList = it.result.documents.mapNotNull {
                                doc -> doc.toObject(ProductModel::class.java)
                        }
                        productsList.value = resultList
                    }
                }
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            "Your Favorite",
            style = TextStyle(
                fontStyle = FontStyle.Normal,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(productsList.value.chunked(2)) {rowItems ->
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowItems.forEach {
                        ProductItemView(item = it, modifier = Modifier.weight(1f))
                    }
                    if(rowItems.size == 1){
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }

}