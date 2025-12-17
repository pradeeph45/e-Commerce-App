package com.example.e_commerceapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerceapp.AppUtil
import com.example.e_commerceapp.Model.OrderModel

@Composable
fun OrderView(orderItem : OrderModel,modifier: Modifier = Modifier){
    Card(
        modifier = Modifier.padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("OrderID : "+orderItem.id,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(4.dp))

            Text(AppUtil.formatDate(orderItem.date),
                fontSize = 14.sp,
                )

            Spacer(modifier = Modifier.height(4.dp))

            Text(orderItem.status,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(4.dp))

            Text(orderItem.items.size.toString() + " items",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold)

        }
    }
}