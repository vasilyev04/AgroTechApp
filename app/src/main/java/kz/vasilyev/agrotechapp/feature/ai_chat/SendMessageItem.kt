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
@Preview
fun PreviewSendMessage(){
    SendMessageItem("Test")
}

@Composable
fun SendMessageItem(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 60.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.End
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp))
                .background(Color(0xFFA9D66A))
                .padding(12.dp)
        ) {
            Text(
                text = message,
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}
