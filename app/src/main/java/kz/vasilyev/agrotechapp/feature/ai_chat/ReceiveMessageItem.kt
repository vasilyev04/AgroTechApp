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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
@Preview
fun PreviewReceiveMessage(){
    ReceiveMessageItem("Test")
}


@Composable
fun ReceiveMessageItem(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 60.dp, top = 8.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp))
                .background(Color.White)
                .padding(12.dp)
        ) {
            Text(
                text = message,
                color = Color(0xFF2C2C2C),
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}
