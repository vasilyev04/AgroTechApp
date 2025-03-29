package kz.vasilyev.agrotechapp.feature.note_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NoteItem(
    val date: String,
    val height: String,
    val lighting: String,
    val temperature: String,
    val humidity: String,
    val photoUri: String? = null
)

@Composable
fun NoteDetailsScreen(
    innerPadding: PaddingValues,
    note: NoteItem = NoteItem(
        date = "29.03.2025",
        height = "314мм",
        lighting = "22lux",
        temperature = "24,3°C",
        humidity = "12%"
    )
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0xFFF5F5F5))
    ) {
        // Date
        Text(
            text = note.date,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Metrics
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            MetricItem(
                label = "Высота",
                value = note.height,
                color = Color(0xFF4CAF50)
            )

            MetricItem(
                label = "Освещение",
                value = note.lighting,
                color = Color(0xFFFFC107)
            )

            MetricItem(
                label = "Температура",
                value = note.temperature,
                color = Color(0xFFE91E63)
            )

            MetricItem(
                label = "Влажность",
                value = note.humidity,
                color = Color(0xFF2196F3)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = { /* TODO: Handle photo */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF2C2C2C)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Фотография")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* TODO: Handle comparison */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA9D66A)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Сравнить")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* TODO: Handle GIF creation */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA9D66A)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("GIF")
            }
        }
    }
}


@Composable
private fun MetricItem(
    label: String,
    value: String,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF757575)
        )

        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}