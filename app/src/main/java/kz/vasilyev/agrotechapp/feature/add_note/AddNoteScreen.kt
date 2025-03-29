package kz.vasilyev.agrotechapp.feature.add_note

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kz.vasilyev.agrotechapp.R
import kz.vasilyev.agrotechapp.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    innerPadding: PaddingValues,
    navController: NavController
) {
    var commentText by remember { mutableStateOf("") }
    var heightText by remember { mutableStateOf("") }
    var lightText by remember { mutableStateOf("") }
    var waterTempText by remember { mutableStateOf("") }
    var airTempText by remember { mutableStateOf("") }
    var humidityText by remember { mutableStateOf("") }

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Назад"
                    )
                }

                Text(
                    text = "Новая запись",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )

                // IoT Upload Button
                Button(
                    onClick = { /* UPLOAD  */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA9D66A)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Данные из IoT")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Metrics
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                MetricInput(
                    label = "Высота",
                    value = heightText,
                    onValueChange = { heightText = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                MetricInput(
                    label = "Освещение",
                    value = lightText,
                    onValueChange = { lightText = it }
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Температура",
                    fontSize = 14.sp,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                MetricInput(
                    label = "Вода",
                    value = waterTempText,
                    onValueChange = { waterTempText = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                MetricInput(
                    label = "Воздух",
                    value = airTempText,
                    onValueChange = { airTempText = it }
                )
                Spacer(modifier = Modifier.height(8.dp))

                MetricInput(
                    label = "Влажность",
                    value = humidityText,
                    onValueChange = { humidityText = it }
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

            // Photo upload
            Text(
                text = "Фотография",
                fontSize = 14.sp,
                color = Color(0xFF757575),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Card(
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable() {
                        photoPicker.launch("image/*")
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEFECEC)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if(selectedImageUri.value != null) {
                        AsyncImage(
                            model = selectedImageUri.value,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text = "Не выбрано",
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Create button
            Button(
                onClick = { /* TODO: Handle create */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA9D66A)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Создать")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MetricInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
            
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = "Не выбрано",
                        color = Color(0xFFA9D66A)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedPlaceholderColor = Color(0xFFA9D66A),
                    focusedPlaceholderColor = Color(0xFFA9D66A)
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    color = Color(0xFF2C2C2C)
                ),
                singleLine = true
            )
        }

    }
}





fun uriToBase64(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()

        if (bytes != null) {
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}