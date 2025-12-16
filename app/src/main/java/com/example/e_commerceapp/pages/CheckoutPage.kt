package com.example.e_commerceapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerceapp.AppUtil
import com.example.e_commerceapp.GlobalNavigation
import com.example.e_commerceapp.Model.ProductModel
import com.example.e_commerceapp.Model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun CheckoutPage(modifier: Modifier = Modifier){

    var userModel by remember { mutableStateOf(UserModel()) }

    val productList = remember { mutableStateListOf(ProductModel()) }

    val subTotal = remember { mutableStateOf(0f) }

    val discount = remember { mutableStateOf(0f) }

    val tax = remember { mutableStateOf(0f) }

    val total = remember { mutableStateOf(0f) }

    fun calculateAndAssign(){
      productList.forEach {
          if(it.actualPrice.isNotEmpty()){
              val qty = userModel.cartItems[it.id] ?: 0
              subTotal.value += it.actualPrice.toFloat() * qty
          }

          discount.value = subTotal.value * AppUtil.getDiscountPercentage()
          tax.value = subTotal.value * AppUtil.getTaxPercentage()

          total.value = "%.2f".format(subTotal.value - discount.value + tax.value).toFloat()
      }
    }

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    val result = it.result.toObject(UserModel::class.java)
                    if(result != null){
                        userModel = result

                        Firebase.firestore.collection("data")
                            .document("stock").collection("products")
                            .whereIn("id",userModel.cartItems.keys.toList())
                            .get().addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    val resultProducts = task.result.toObjects(ProductModel::class.java)
                                    productList.clear()
                                    productList.addAll(resultProducts)
                                    calculateAndAssign()
                                }
                            }
                    }
                }
            }

    }

    Column(modifier = Modifier.fillMaxSize()
        .padding(16.dp)) {
       Text("Checkout", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Deliver To", fontWeight = FontWeight.SemiBold)
        Text(userModel.name.toString())
        Text(userModel.address.toString())
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        RowCheckOutPage("SubTotal",subTotal.value.toString())
        Spacer(modifier = Modifier.height(8.dp))
        RowCheckOutPage("Discount (-)",discount.value.toString())
        Spacer(modifier = Modifier.height(8.dp))
        RowCheckOutPage("Tax (+)",tax.value.toString())
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        RowCheckOutPage("Total",total.value.toString())
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
              GlobalNavigation.navController.navigate("order")
            },
            modifier = Modifier.fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Pay Now", fontSize = 16.sp)
        }
    }
}

@Composable
fun RowCheckOutPage(title : String,value : String){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
     Text(title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Text("$"+value, fontSize = 18.sp)
    }

}