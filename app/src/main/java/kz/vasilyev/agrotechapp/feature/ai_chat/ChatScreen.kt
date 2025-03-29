package kz.vasilyev.agrotechapp.feature.ai_chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.vasilyev.agrotechapp.navigation.Screen

data class Message(
    val text: String,
    val isFromUser: Boolean
)

@Composable
@Preview
fun PreviewChatScreen(){
    ChatScreen(innerPadding = PaddingValues())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(innerPadding: PaddingValues) {
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Text(
            text = "Чат с ИИ ассистентом",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        
        // Messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState,
            reverseLayout = false
        ) {
            items(messages) { message ->
                if (message.isFromUser) {
                    SendMessageItem(message = message.text)
                } else {
                    ReceiveMessageItem(message = message.text)
                }
            }
        }
        
        // Input field
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    placeholder = { Text("Введите сообщение") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFA9D66A),
                        focusedBorderColor = Color(0xFFA9D66A),
                        unfocusedLabelColor = Color(0xFFA9D66A),
                        focusedLabelColor = Color(0xFFA9D66A)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            messages = messages + Message(inputText, true)
                            // Add AI response (you can replace this with actual AI response logic)
                            messages = messages + Message("Это тестовый ответ от ИИ ассистента.", false)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .background(
                            color = Color(0xFFA9D66A),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    }
    
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
}