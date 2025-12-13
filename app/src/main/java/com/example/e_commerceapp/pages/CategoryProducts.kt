package com.example.e_commerceapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.e_commerceapp.Model.ProductModel
import com.example.e_commerceapp.components.ProductItemView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoryProduct(modifier: Modifier = Modifier,categoryId : String){
    val productsList = remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock").collection("products")
            .whereEqualTo("category",categoryId)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    var resultList = it.result.documents.mapNotNull {
                            doc -> doc.toObject(ProductModel::class.java)
                    }
                    productsList.value = resultList
                }
            }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
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