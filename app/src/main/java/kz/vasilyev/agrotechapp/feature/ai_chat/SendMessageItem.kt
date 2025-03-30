package kz.vasilyev.agrotechapp.feature.ai_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun SendMessageItem(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .widthIn(min = 0.dp, max = 280.dp)
                .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp))
                .background(Color(0xFFA9D66A))
                .padding(12.dp)
        ) {
            Text(
                text = message,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

