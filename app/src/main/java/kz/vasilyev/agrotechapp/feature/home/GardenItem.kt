package kz.vasilyev.agrotechapp.feature.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kz.vasilyev.agrotechapp.R
import kz.vasilyev.agrotechapp.models.Garden
import kz.vasilyev.agrotechapp.ui.theme.BackgroundScreen
import kz.vasilyev.agrotechapp.ui.theme.Primary
import androidx.core.net.toUri
import coil.request.ImageRequest

@Composable
fun GardenItem(garden: Garden, position: Int, onClick: (Garden) -> Unit) {
    Column(
        modifier = Modifier.clickable {
            onClick(garden)
        }
    ) {
        val bitmap = base64ToBitmap(garden.photo)
        bitmap?.let {
            Card(
                modifier = Modifier
                    .size(width = 165.dp, height = 125.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BackgroundScreen
                ),
                shape = RoundedCornerShape(15.dp)
            ) {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } ?: Card(
            modifier = Modifier
                .size(width = 165.dp, height = 125.dp),
            colors = CardDefaults.cardColors(
                containerColor = BackgroundScreen
            ),
            shape = RoundedCornerShape(15.dp)
        ) {
        }

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    color = Color.Black,
                    text = garden.plantType,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    color = Color(0xFF5F5F5F),
                    text = "#$position",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(2.dp))

            Text(
                color = Primary,
                text = garden.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            Row {
                Icon(
                    modifier = Modifier.size(15.dp),
                    tint = Color(0xFF5F5F5F),
                    contentDescription = "",
                    painter = painterResource(R.drawable.ic_substrate),
                )

                Spacer(Modifier.width(5.dp))

                Text(
                    color = Color(0xFF5F5F5F),
                    fontSize = 12.sp,
                    text = garden.substrate
                )
            }

            Spacer(Modifier.height(2.dp))

            Row {
                Icon(
                    modifier = Modifier.size(15.dp),
                    tint = Color(0xFF5F5F5F),
                    contentDescription = "",
                    painter = painterResource(R.drawable.ic_date),
                )

                Spacer(Modifier.width(5.dp))

                Text(
                    color = Color(0xFF5F5F5F),
                    fontSize = 12.sp,
                    text = "25.01.2025"
                )
            }
        }
    }
}

fun base64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}