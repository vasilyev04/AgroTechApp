package kz.vasilyev.agrotechapp.feature.note_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
@Preview
fun PreviewNoteDetails(){
    NoteDetailsScreen(
        innerPadding = PaddingValues(bottom = 80.dp),
        note = NoteItem(
            date = "29.03.2025",
            height = "314мм",
            lighting = "22lux",
            temperature = "24,3°C",
            humidity = "12%"
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
    var commentText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        // Date
        Text(
            text = note.date,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        )

        // Metrics
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            MetricItem(
                label = "Высота",
                value = note.height
            )
            
            MetricItem(
                label = "Освещение",
                value = note.lighting
            )
            
            MetricItem(
                label = "Температура",
                value = note.temperature
            )
            
            MetricItem(
                label = "Влажность",
                value = note.humidity
            )
        }

        // Comment
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { 
                        if (it.length <= 150) commentText = it 
                    },
                    placeholder = { 
                        Text(
                            text = "Комментарий...",
                            color = Color(0xFF757575)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    ),
                    maxLines = 5
                )
                
                Text(
                    text = "${commentText.length}/150",
                    fontSize = 12.sp,
                    color = Color(0xFF757575),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        // Photo placeholder
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE0E0E0)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Фотография",
                    color = Color(0xFF757575)
                )
            }
        }

        // Bottom buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = { /* TODO: Handle comparison */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA9D66A)
                ),
                shape = RoundedCornerShape(24.dp)
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
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("GIF")
            }
        }
    }
}

@Composable
private fun MetricItem(
    label: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
                color = Color(0xFFA9D66A),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}