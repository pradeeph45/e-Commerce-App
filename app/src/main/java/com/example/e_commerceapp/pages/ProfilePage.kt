package com.example.e_commerceapp.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerceapp.Model.UserModel
import com.example.e_commerceapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import com.example.e_commerceapp.AppUtil
import com.example.e_commerceapp.GlobalNavigation

@Composable
fun ProfilePage(modifier: Modifier){

    val userModel = remember { mutableStateOf(UserModel()) }

    var addressInput by remember { mutableStateOf(userModel.value.address) }

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    val result = it.result.toObject(UserModel::class.java)
                    if(result!=null){
                        userModel.value = result
                        addressInput = userModel.value.address
                    }
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your Profile",
            style = TextStyle(
                fontStyle = FontStyle.Normal,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Image(painter = painterResource(R.drawable.images),
            contentDescription = "Profile Icon",
            modifier = Modifier.height(200.dp).fillMaxWidth()
            )

        Text(userModel.value.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(20.dp))

        Text("Address:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium)

        TextField(modifier = Modifier.fillMaxWidth()
            ,value = addressInput, onValueChange = {
            addressInput = it
        },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {

                if(addressInput.isNotEmpty()){
                    Firebase.firestore.collection("users")
                        .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                        .update("address",addressInput)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                AppUtil.showToast(context,"Address Updated successfully")
                            }else{
                                AppUtil.showToast(context,"There is some error while updating address")
                            }
                        }
                }

            }))

        Spacer(modifier = Modifier.height(12.dp))

        Text("Email:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium)
        Text(userModel.value.email)

        Spacer(modifier = Modifier.height(12.dp))

        Text("View my orders",
                fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
                .clickable{
                   GlobalNavigation.navController.navigate("orders")
                }.padding(vertical = 16.dp))

        TextButton(onClick = {
            FirebaseAuth.getInstance().signOut()
            val navController = GlobalNavigation.navController
            navController.popBackStack()
               navController.navigate("auth")
        },
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.CenterHorizontally)) {
            Text("Sign Out", fontSize = 18.sp)
        }
    }

}