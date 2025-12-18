package com.example.e_commerceapp

import android.content.Context
import android.widget.Toast
import com.example.e_commerceapp.Model.OrderModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID
import androidx.core.content.edit

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

    fun clearCartAndAddToOrders(){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if(it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val order = OrderModel(
                    id = "ORD_" + UUID.randomUUID().toString().replace("-","").take(10).uppercase(),
                    userId = FirebaseAuth.getInstance().currentUser?.uid!!,
                     date = Timestamp.now(),
                    items = currentCart,
                    status = "ORDERED",
                    address = it.result.get("address") as String
                )
                Firebase.firestore.collection("orders")
                    .document(order.id).set(order)
                    .addOnCompleteListener {
                        if(it.isSuccessful)
                            userDoc.update("cartItems", FieldValue.delete())
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

    fun formatDate(timestamp: Timestamp) : String{
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a",Locale.getDefault())
        return sdf.format(timestamp.toDate().time)
    }

    private const val _PREF_NAME = "favourite_pref"
    private const val KEY_FAVOURITES = "favourite_list"


    fun addOrRemoveFromFavourite(context: Context,productId: String){
     val list = getFavouriteList(context).toMutableSet()
        if(list.contains(productId)){
            list.remove(productId)
            showToast(context,"Item removed from Favourites")
        }else{
            list.add(productId)
            showToast(context,"Item added to Favourites")
        }
        val prefs = context.getSharedPreferences(_PREF_NAME, Context.MODE_PRIVATE)

        prefs.edit{
            putStringSet(KEY_FAVOURITES,list)
        }
    }

    fun checkFavourite(context: Context,productId: String) : Boolean{
        if(getFavouriteList(context).contains(productId))
            return true
    return false
    }

    fun getFavouriteList(context: Context) : Set<String>{
    val prefs = context.getSharedPreferences(_PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_FAVOURITES,emptySet()) ?: emptySet()

    }
}