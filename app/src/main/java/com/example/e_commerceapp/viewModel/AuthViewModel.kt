package com.example.e_commerceapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.e_commerceapp.Model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewModel : ViewModel() {

    private val _auth = Firebase.auth
    private val _firestore = Firebase.firestore

    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ){
     _auth.signInWithEmailAndPassword(email,password)
         .addOnCompleteListener {
           if(it.isSuccessful){
               onResult(true, null)
           }else{
               onResult(false, it.exception?.localizedMessage)
           }
         }
    }

    fun register(
        email: String,
        name: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ){
        _auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authTask ->

                if (authTask.isSuccessful) {

                    val userId = authTask.result?.user?.uid

                    if (userId == null) {
                        onResult(false, "User ID is null")
                        return@addOnCompleteListener
                    }

                    val userModel = UserModel(name, email, userId)

                    _firestore.collection("users")
                        .document(userId)
                        .set(userModel)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                onResult(true, null)
                            } else {
                                onResult(false, dbTask.exception?.localizedMessage ?: "Something went wrong")
                            }
                        }

                } else {
                    onResult(false, authTask.exception?.localizedMessage)
                }
            }
    }
}
