package com.example.e_commerceapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import network.DebugCoil

@Composable
fun BannerView(modifier: Modifier = Modifier) {
    var bannerList by remember { mutableStateOf<List<String>>(emptyList()) }
    val context = LocalContext.current
    val debugImageLoader = remember { DebugCoil.createDebugImageLoader(context) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("banners")
            .get()
            .addOnCompleteListener {
                bannerList = (it.result?.get("urls") as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            }
    }

    // if you want the dots centered under pager, use Box or Column but keep spacing small
    Column(modifier = modifier) {
        val pagerState = rememberPagerState(initialPage = 0) { bannerList.size }

        HorizontalPager(
            state = pagerState,
            pageSpacing = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp) // fixed height
        ) { page ->
            AsyncImage(
                model = bannerList.getOrNull(page) ?: "",
                imageLoader = debugImageLoader,
                contentDescription = "banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        DotsIndicator(
            dotCount = bannerList.size,
            type = ShiftIndicatorType(DotGraphic(color = MaterialTheme.colorScheme.primary, size = 6.dp)),
            pagerState = pagerState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
