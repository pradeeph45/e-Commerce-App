package com.example.e_commerceapp

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

object AppUtil {

    fun showToast(context: Context,message: String){
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    }

    fun addToCart(productId: String,context: Context){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if(it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity  = currentCart[productId] ?: 0
                val updatedQuantity = currentQuantity + 1

                val updatedCart = mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                          showToast(context,"Item Successfully added to cart")
                        }else{
                            showToast(context,"Failed adding item to cart")
                        }
                    }
            }
        }
    }

    fun removeFromCart(productId: String,context: Context,removeAll : Boolean = false){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if(it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity  = currentCart[productId] ?: 0
                val updatedQuantity = currentQuantity - 1

                val updatedCart =
                    if(updatedQuantity <= 0 || removeAll)
                         mapOf("cartItems.$productId" to FieldValue.delete())
                    else
                         mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            showToast(context,"Item Successfully removed from cart")
                        }else{
                            showToast(context,"Failed removing item from cart")
                        }
                    }
            }
        }
    }

    fun getDiscountPercentage() : Float{
        return 0.1f;
    }

    fun getTaxPercentage() : Float{
        return 0.18f;
    }
}