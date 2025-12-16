package com.example.e_commerceapp.pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerceapp.AppUtil
import com.example.e_commerceapp.GlobalNavigation
import kotlinx.coroutines.delay

@Composable
fun OrderPage(modifier: Modifier = Modifier) {

    val paymentOptions = listOf(
        "Cash on Delivery",
        "UPI",
        "Credit / Debit Card",
        "Net Banking",
        "Wallet"
    )

    var selectedOption by remember { mutableStateOf("") }
    var orderPlaced by remember { mutableStateOf(false) }

    if (orderPlaced) {
        LaunchedEffect(Unit) {
            delay(5000)
            GlobalNavigation.navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (orderPlaced) {
           AppUtil.clearCartAndAddToOrders()
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Order Placed Successfully!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        } else {

            Text(
                text = "Select Payment Method",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            paymentOptions.forEach { option ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { selectedOption = option },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (selectedOption == option) 8.dp else 2.dp
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = selectedOption == option,
                            onClick = { selectedOption = option }
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = option,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { orderPlaced = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = selectedOption.isNotEmpty()
            ) {
                Text(text = "Place Order")
            }
        }
    }
}
