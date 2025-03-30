package kz.vasilyev.agrotechapp.feature.add_note

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kz.vasilyev.agrotechapp.data.RoomInstance
import kz.vasilyev.agrotechapp.models.Note
import kz.vasilyev.agrotechapp.ui.theme.BackgroundScreen
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import java.util.*
import kotlinx.coroutines.launch
import kz.vasilyev.agrotechapp.components.LoadingDots
import kz.vasilyev.agrotechapp.feature.ai_chat.api.RetrofitClient
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import kotlin.math.roundToInt
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    innerPadding: PaddingValues,
    navController: NavController,
    gardenId: Long
) {
    var height by remember { mutableStateOf("") }
    var light by remember { mutableStateOf("") }
    var waterTemp by remember { mutableStateOf("") }
    var airTemp by remember { mutableStateOf("") }
    var humidity by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showHealthDialog by remember { mutableStateOf(false) }
    var healthyPercent by remember { mutableStateOf(0) }
    var analyzedImageBase64 by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Состояния для отображения ошибок
    var heightError by remember { mutableStateOf(false) }
    var lightError by remember { mutableStateOf(false) }
    var waterTempError by remember { mutableStateOf(false) }
    var airTempError by remember { mutableStateOf(false) }
    var humidityError by remember { mutableStateOf(false) }
    var photoError by remember { mutableStateOf(false) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> 
            uri?.let {
                selectedImageUri = uri
                photoError = false
                
                scope.launch {
                    isLoading = true
                    try {
                        val file = createTempFileFromUri(context, uri)
                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                        
                        val response = RetrofitClient.microgreensApi.analyzeImage(body)
                        if (response.isSuccessful) {
                            response.body()?.let { analysisResponse ->
                                healthyPercent = (analysisResponse.healthy * 100).roundToInt()
                                analyzedImageBase64 = analysisResponse.image_base64
                                
                                if (healthyPercent <= 50) {
                                    showHealthDialog = true
                                }
                            }
                        }
                    } catch (e: Exception) {
                        // Обработка ошибки
                    } finally {
                        isLoading = false
                    }
                }
            }
        }
    )

    // Функция для генерации новых данных на основе предыдущих записей
    fun generateDataFromIoT() {
        val previousNotes = RoomInstance
            .getInstance(context.applicationContext as Application)
            .roomDao()
            .getNotesForGarden(gardenId)
            .sortedBy { it.createdAt }

        if (previousNotes.isNotEmpty()) {
            val lastNote = previousNotes.last()
            
            // Увеличиваем высоту на случайное значение от 5 до 15 мм
            val heightIncrease = (5..15).random()
            height = (lastNote.height + heightIncrease).toString()
            
            // Генерируем освещенность в пределах ±10% от предыдущего значения
            val lightVariation = lastNote.light * (0.9f + Math.random().toFloat() * 0.2f)
            light = "%.1f".format(Locale.US, lightVariation)
            
            // Температура воды ±0.5 градуса
            val waterTempVariation = lastNote.waterTemp + (-0.5f + Math.random().toFloat())
            waterTemp = "%.1f".format(Locale.US, waterTempVariation)
            
            // Температура воздуха ±1 градус
            val airTempVariation = lastNote.airTemp + (-1f + Math.random().toFloat() * 2f)
            airTemp = "%.1f".format(Locale.US, airTempVariation)
            
            // Влажность ±5%
            val humidityVariation = lastNote.humidity + (-5f + Math.random().toFloat() * 10f)
            humidity = "%.1f".format(Locale.US, humidityVariation.coerceIn(0f, 100f))
            
            comment = "Данные получены с IoT устройства"
        } else {
            // Если это первая запись, генерируем начальные значения
            height = "50" // Начальная высота 50мм
            light = "1000" // Освещенность 1000 люкс
            waterTemp = "20.0" // Температура воды 20°C
            airTemp = "22.0" // Температура воздуха 22°C
            humidity = "70.0" // Влажность 70%
            comment = "Первичные данные с IoT устройства"
        }

        // Сбрасываем ошибки после заполнения полей
        heightError = false
        lightError = false
        waterTempError = false
        airTempError = false
        humidityError = false
    }

    Scaffold(
        containerColor = BackgroundScreen,
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
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Photo selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(200.dp)
                    .clickable { 
                        photoPicker.launch("image/*")
                        photoError = false 
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (photoError) Color(0xFFFFE5E5) else Color(0xFFEFECEC)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        LoadingDots()
                    } else {
                        selectedImageUri?.let { uri ->
                            Column {
                                context.contentResolver.openInputStream(uri)?.use { stream ->
                                    val bitmap = BitmapFactory.decodeStream(stream)
                                    if (bitmap != null) {
                                        Image(
                                            bitmap = bitmap.asImageBitmap(),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .weight(1f),
                                            contentScale = ContentScale.Crop
                                        )
                                        if (healthyPercent > 0) {
                                            Text(
                                                text = "Здоровье растения: $healthyPercent%",
                                                color = if (healthyPercent > 50) Color(0xFF88C057) else Color.Red,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color.White)
                                                    .padding(8.dp),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = "Ошибка загрузки изображения",
                                            color = Color.Red,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        } ?: Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Добавить фото",
                                color = if (photoError) Color.Red else Color(0xFF88C057),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            if (photoError) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Обязательное поле",
                                    color = Color.Red,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Metrics
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Добавляем кнопку получения данных с IoT
                    Button(
                        onClick = { generateDataFromIoT() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF88C057)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Получить данные с IoT",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    MetricInput(
                        "Высота",
                        height,
                        { newValue -> 
                            height = newValue
                            heightError = false
                        },
                        heightError
                    )
                    MetricInput(
                        "Освещение",
                        light,
                        { newValue -> 
                            light = newValue
                            lightError = false
                        },
                        lightError
                    )
                    MetricInput(
                        "Температура воды",
                        waterTemp,
                        { newValue -> 
                            waterTemp = newValue
                            waterTempError = false
                        },
                        waterTempError
                    )
                    MetricInput(
                        "Температура воздуха",
                        airTemp,
                        { newValue -> 
                            airTemp = newValue
                            airTempError = false
                        },
                        airTempError
                    )
                    MetricInput(
                        "Влажность",
                        humidity,
                        { newValue -> 
                            humidity = newValue
                            humidityError = false
                        },
                        humidityError
                    )
                }
            }

            // Comment
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Комментарий",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF88C057)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFA9D66A),
                            focusedBorderColor = Color(0xFFA9D66A)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3
                    )
                }
            }

            // Save button
            Button(
                onClick = {
                    // Сброс ошибок
                    photoError = false

                    // Проверка фото
                    if (selectedImageUri == null) {
                        photoError = true
                        return@Button
                    }

                    // Проверка числовых полей
                    var hasError = false
                    
                    if (height.isEmpty() || height.toFloatOrNull() == null) {
                        heightError = true
                        hasError = true
                    }
                    if (light.isEmpty() || light.toFloatOrNull() == null) {
                        lightError = true
                        hasError = true
                    }
                    if (waterTemp.isEmpty() || waterTemp.toFloatOrNull() == null) {
                        waterTempError = true
                        hasError = true
                    }
                    if (airTemp.isEmpty() || airTemp.toFloatOrNull() == null) {
                        airTempError = true
                        hasError = true
                    }
                    if (humidity.isEmpty() || humidity.toFloatOrNull() == null) {
                        humidityError = true
                        hasError = true
                    }

                    if (hasError) {
                        return@Button
                    }

                    val note = Note(
                        height = height.toFloat(),
                        light = light.toFloat(),
                        waterTemp = waterTemp.toFloat(),
                        airTemp = airTemp.toFloat(),
                        humidity = humidity.toFloat(),
                        comment = comment,
                        photo = selectedImageUri?.let { uri ->
                            context.contentResolver.openInputStream(uri)?.use { stream ->
                                val bytes = stream.readBytes()
                                Base64.encodeToString(bytes, Base64.DEFAULT)
                            }
                        } ?: "",
                        gardenId = gardenId
                    )

                    RoomInstance
                        .getInstance(context.applicationContext as Application)
                        .roomDao()
                        .addNote(note)

                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF88C057)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Сохранить",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (showHealthDialog) {
            Dialog(onDismissRequest = { showHealthDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Внимание!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = """
                                Обнаружены проблемы со здоровьем растения!
                                
                                Процент здоровых участков: $healthyPercent%
                                
                                На изображении отмечены проблемные зоны синим цветом.
                                Рекомендуется проверить условия выращивания и при необходимости обратиться к специалисту.
                            """.trimIndent(),
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        analyzedImageBase64?.let { base64 ->
                            // Удаляем префикс data:image/jpeg;base64, если он есть
                            val cleanBase64 = base64.substringAfter("base64,")
                            val imageBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Fit
                                )
                            } else {
                                Text(
                                    text = "Ошибка отображения результата анализа",
                                    color = Color.Red,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { showHealthDialog = false },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF88C057)
                            )
                        ) {
                            Text("Понятно")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = if (isError) Color.Red else Color.Gray
        )
        
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                // Разрешаем только цифры, точку и минус
                if (newValue.isEmpty() || newValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = if (isError) Color.Red else Color(0xFFA9D66A),
                focusedBorderColor = if (isError) Color.Red else Color(0xFFA9D66A),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = isError,
            supportingText = if (isError) {
                { Text("Введите числовое значение") }
            } else null
        )
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

private fun createTempFileFromUri(context: Context, uri: Uri): File {
    val file = File.createTempFile("image", ".jpg", context.cacheDir)
    
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    } ?: throw IllegalStateException("Не удалось открыть изображение")
    
    return file
}