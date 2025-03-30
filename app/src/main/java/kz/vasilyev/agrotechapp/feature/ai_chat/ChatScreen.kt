package kz.vasilyev.agrotechapp.feature.ai_chat

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kz.vasilyev.agrotechapp.components.LoadingDots
import kz.vasilyev.agrotechapp.feature.ai_chat.api.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

data class Message(
    val text: String,
    val isFromUser: Boolean,
    val imageBase64: String? = null
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
    var isLoading by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val geminiService = remember { GeminiService() }
    val context = LocalContext.current
    
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                scope.launch {
                    isLoading = true
                    try {
                        val file = createTempFileFromUri(context, uri)
                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                        
                        val response = RetrofitClient.microgreensApi.analyzeImage(body)
                        if (response.isSuccessful) {
                            response.body()?.let { analysisResponse ->
                                val healthyPercent = (analysisResponse.healthy * 100).roundToInt()
                                val unhealthyPercent = (analysisResponse.unhealthy * 100).roundToInt()
                                
                                val analysisText = """
                                    Анализ фотографии микрозелени:
                                    
                                    🌱 Здоровые растения: $healthyPercent%
                                    🚫 Нездоровые участки: $unhealthyPercent%
                                    
                                    На изображении отмечены проблемные зоны синим цветом.
                                    """.trimIndent()
                                
                                messages = messages + Message(
                                    text = analysisText,
                                    isFromUser = false,
                                    imageBase64 = analysisResponse.image_base64
                                )
                            }
                        } else {
                            messages = messages + Message(
                                text = "Не удалось проанализировать изображение. Попробуйте еще раз.",
                                isFromUser = false
                            )
                        }
                    } catch (e: Exception) {
                        messages = messages + Message(
                            text = "Произошла ошибка при обработке изображения: ${e.message}",
                            isFromUser = false
                        )
                    } finally {
                        isLoading = false
                    }
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        if (messages.isEmpty()) {
            messages = listOf(Message(
                text = """
                    Привет! Я ваш ИИ ассистент по выращиванию микрозелени. 
                    
                    Я могу помочь вам с:
                    🌱 Анализом состояния растений по фото
                    📋 Рекомендациями по выращиванию
                    🔍 Диагностикой проблем
                    
                    Отправьте фото или задайте вопрос!
                """.trimIndent(),
                isFromUser = false
            ))
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Чат с ИИ ассистентом",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(
                    onClick = { photoPicker.launch("image/*") }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(kz.vasilyev.agrotechapp.R.drawable.ic_camera),
                        contentDescription = "Открыть галерею",
                        tint = Color(0xFFA9D66A)
                    )
                }
            }
        },
        bottomBar = {
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
                        shape = RoundedCornerShape(20.dp),
                        enabled = !isLoading
                    )

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank() && !isLoading) {
                                val userMessage = inputText
                                messages = messages + Message(userMessage, true)
                                inputText = ""
                                isLoading = true

                                scope.launch {
                                    val response = geminiService.makeRequest(userMessage)
                                    messages = messages + Message(response, false)
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier
                            .background(
                                color = if (isLoading) Color.Gray else Color(0xFFA9D66A),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .size(48.dp),
                        enabled = !isLoading
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = listState,
            reverseLayout = false
        ) {
            items(messages) { message ->
                if (message.isFromUser) {
                    SendMessageItem(message = message.text)
                } else {
                    ReceiveMessageItem(
                        message = message.text,
                        imageBase64 = message.imageBase64
                    )
                }
            }
            
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .widthIn(max = 340.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            LoadingDots(
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                )
                            )
                        }
                    }
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

private fun createTempFileFromUri(context: Context, uri: Uri): File {
    val stream = context.contentResolver.openInputStream(uri)
    val file = File.createTempFile("image", ".jpg", context.cacheDir)
    
    FileOutputStream(file).use { output ->
        stream?.copyTo(output)
    }
    
    return file
}