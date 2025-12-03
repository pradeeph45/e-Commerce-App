package com.example.e_commerceapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.e_commerceapp.GlobalNavigation
import com.example.e_commerceapp.Model.CategoryModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import network.DebugCoil

@Composable
fun CategoriesView(modifier: Modifier = Modifier){

    val categoriesList = remember {
        mutableStateOf<List<CategoryModel>>(emptyList())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock").collection("categories")
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    var resultList = it.result.documents.mapNotNull {
                        doc -> doc.toObject(CategoryModel::class.java)
                    }
                    categoriesList.value = resultList
                }
            }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      items(categoriesList.value) { item ->
          CategoryItem(category = item)
      }
    }
}

@Composable
fun CategoryItem(category : CategoryModel){
    val context = LocalContext.current
    val debugImageLoader = remember {
        DebugCoil.createDebugImageLoader(context)
    }
    Card(
        modifier = Modifier.size(90.dp)
            .clickable(){
              GlobalNavigation.navController.navigate("category-products/"+category.id)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.surface)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ){
            AsyncImage(
                model = category.imageUrl,
                imageLoader = debugImageLoader,
                contentDescription = category.name,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }

}