package kz.vasilyev.agrotechapp.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.vasilyev.agrotechapp.R
import kz.vasilyev.agrotechapp.models.Garden
import kz.vasilyev.agrotechapp.ui.theme.BackgroundScreen
import kz.vasilyev.agrotechapp.ui.theme.Primary

@Composable
fun GardenItem(garden: Garden, position: Int) {
    Column {
        Card(
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