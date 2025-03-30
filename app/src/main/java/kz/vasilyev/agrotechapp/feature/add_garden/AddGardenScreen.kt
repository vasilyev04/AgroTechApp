package kz.vasilyev.agrotechapp.feature.add_garden

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kz.vasilyev.agrotechapp.R
import kz.vasilyev.agrotechapp.data.RoomInstance
import kz.vasilyev.agrotechapp.models.Garden
import kz.vasilyev.agrotechapp.ui.theme.BackgroundScreen
import kz.vasilyev.agrotechapp.ui.theme.Primary
import kz.vasilyev.agrotechapp.utils.AlarmScheduler
import kz.vasilyev.agrotechapp.utils.PermissionUtils
import kz.vasilyev.agrotechapp.utils.RequestNotificationPermission
import kotlin.collections.Map.Entry
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGardenScreen(
    innerPadding: PaddingValues,
    navController: NavController
) {
    val context = LocalContext.current
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    var showPermissionRequest by remember { mutableStateOf(false) }
    var pendingGarden by remember { mutableStateOf<Garden?>(null) }

    // Состояния для ошибок
    var titleError by remember { mutableStateOf(false) }
    var plantTypeError by remember { mutableStateOf(false) }
    var substrateError by remember { mutableStateOf(false) }
    var plantDateError by remember { mutableStateOf(false) }
    var harvestDateError by remember { mutableStateOf(false) }
    var wateringTimeError by remember { mutableStateOf(false) }
    var photoError by remember { mutableStateOf(false) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    var title by remember { mutableStateOf("") }
    var plantType by remember { mutableStateOf("") }
    var substrate by remember { mutableStateOf("") }
    
    var plantDate by remember { mutableStateOf<Long?>(null) }
    var harvestDate by remember { mutableStateOf<Long?>(null) }
    var wateringTime by remember { mutableStateOf<Long?>(null) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Если нужно показать запрос разрешения
    if (showPermissionRequest) {
        RequestNotificationPermission {
            pendingGarden?.let { garden ->
                val gardenId = RoomInstance
                    .getInstance(context.applicationContext as Application)
                    .roomDao()
                    .addGarden(garden)

                AlarmScheduler.scheduleWateringAlarm(
                    context = context,
                    gardenId = gardenId,
                    gardenTitle = garden.title,
                    wateringTimeMillis = garden.wateringTime
                )
            }
            navController.popBackStack()
        }
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
                    text = "Создание лота",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 19.dp)
                    .padding(bottom = 90.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(38.dp))

                // Основные поля
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = { 
                        title = it
                        titleError = false 
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = if (titleError) Color.Red else Primary,
                        focusedIndicatorColor = if (titleError) Color.Red else Primary,
                        containerColor = Color.White,
                        focusedTextColor = Color.Black,
                        cursorColor = Primary,
                    ),
                    placeholder = { Text("Название лота") },
                    leadingIcon = {
                        Icon(painterResource(R.drawable.ic_title), contentDescription = "", 
                            tint = if (titleError) Color.Red else Primary)
                    },
                    supportingText = if (titleError) {
                        { Text("Обязательное поле", color = Color.Red) }
                    } else null
                )

                Spacer(Modifier.height(11.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = plantType,
                    onValueChange = { 
                        plantType = it
                        plantTypeError = false 
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = if (plantTypeError) Color.Red else Primary,
                        focusedIndicatorColor = if (plantTypeError) Color.Red else Primary,
                        containerColor = Color.White,
                        focusedTextColor = Color.Black,
                        cursorColor = Primary,
                    ),
                    placeholder = { Text("Вид") },
                    leadingIcon = {
                        Icon(painterResource(R.drawable.ic_home), contentDescription = "", 
                            tint = if (plantTypeError) Color.Red else Primary)
                    },
                    supportingText = if (plantTypeError) {
                        { Text("Обязательное поле", color = Color.Red) }
                    } else null
                )

                Spacer(Modifier.height(11.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = substrate,
                    onValueChange = { 
                        substrate = it
                        substrateError = false 
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = if (substrateError) Color.Red else Primary,
                        focusedIndicatorColor = if (substrateError) Color.Red else Primary,
                        containerColor = Color.White,
                        focusedTextColor = Color.Black,
                        cursorColor = Primary,
                    ),
                    placeholder = { Text("Субстрат") },
                    leadingIcon = {
                        Icon(painterResource(R.drawable.ic_substrate), contentDescription = "", 
                            tint = if (substrateError) Color.Red else Primary)
                    },
                    supportingText = if (substrateError) {
                        { Text("Обязательное поле", color = Color.Red) }
                    } else null
                )

                Spacer(Modifier.height(11.dp))

                // Дата посева
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val calendar = Calendar.getInstance()
                            DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    calendar.set(year, month, day)
                                    plantDate = calendar.timeInMillis
                                    plantDateError = false
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                    value = plantDate?.let { 
                        val date = Date(it)
                        val localDate = date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        localDate.format(dateFormatter)
                    } ?: "",
                    onValueChange = { },
                    enabled = false,
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = if (plantDateError) Color.Red else Primary,
                        focusedIndicatorColor = if (plantDateError) Color.Red else Primary,
                        containerColor = Color.White,
                        disabledTextColor = Color.Black,
                    ),
                    placeholder = { Text("Дата посева") },
                    leadingIcon = {
                        Icon(painterResource(R.drawable.ic_date), contentDescription = "", 
                            tint = if (plantDateError) Color.Red else Primary)
                    },
                    supportingText = if (plantDateError) {
                        { Text("Обязательное поле", color = Color.Red) }
                    } else null
                )

                Spacer(Modifier.height(11.dp))

                // Дата сбора
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val calendar = Calendar.getInstance()
                            DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    calendar.set(year, month, day)
                                    harvestDate = calendar.timeInMillis
                                    harvestDateError = false
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                    value = harvestDate?.let { 
                        val date = Date(it)
                        val localDate = date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        localDate.format(dateFormatter)
                    } ?: "",
                    onValueChange = { },
                    enabled = false,
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = if (harvestDateError) Color.Red else Primary,
                        focusedIndicatorColor = if (harvestDateError) Color.Red else Primary,
                        containerColor = Color.White,
                        disabledTextColor = Color.Black,
                    ),
                    placeholder = { Text("Дата сбора") },
                    leadingIcon = {
                        Icon(painterResource(R.drawable.ic_harvest_date), contentDescription = "", 
                            tint = if (harvestDateError) Color.Red else Primary)
                    },
                    supportingText = if (harvestDateError) {
                        { Text("Обязательное поле", color = Color.Red) }
                    } else null
                )

                Spacer(Modifier.height(11.dp))

                // Время полива
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val calendar = Calendar.getInstance()
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                                    calendar.set(Calendar.MINUTE, minute)
                                    wateringTime = calendar.timeInMillis
                                    wateringTimeError = false
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        },
                    value = wateringTime?.let { 
                        val date = Date(it)
                        val localTime = date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalTime()
                        localTime.format(timeFormatter)
                    } ?: "",
                    onValueChange = { },
                    enabled = false,
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = if (wateringTimeError) Color.Red else Primary,
                        focusedIndicatorColor = if (wateringTimeError) Color.Red else Primary,
                        containerColor = Color.White,
                        disabledTextColor = Color.Black,
                    ),
                    placeholder = { Text("Время полива") },
                    leadingIcon = {
                        Icon(painterResource(R.drawable.ic_clocks), contentDescription = "", 
                            tint = if (wateringTimeError) Color.Red else Primary)
                    },
                    supportingText = if (wateringTimeError) {
                        { Text("Обязательное поле", color = Color.Red) }
                    } else null
                )

                Spacer(Modifier.height(38.dp))

                // Блок фото
                Text(
                    text = "Фотография",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(18.dp))

                Card(
                    modifier = Modifier
                        .height(220.dp)
                        .fillMaxWidth()
                        .clickable { 
                            photoPicker.launch("image/*")
                            photoError = false 
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (photoError) Color(0xFFFFE5E5) else Color(0xFFEFECEC)
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
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Не выбрано",
                                    textAlign = TextAlign.Center,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (photoError) Color.Red else Primary
                                )
                                if (photoError) {
                                    Text(
                                        text = "Обязательное поле",
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        color = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        // Сброс ошибок
                        titleError = false
                        plantTypeError = false
                        substrateError = false
                        plantDateError = false
                        harvestDateError = false
                        wateringTimeError = false
                        photoError = false

                        // Валидация
                        var isValid = true

                        if (title.isBlank()) {
                            titleError = true
                            isValid = false
                        }
                        if (plantType.isBlank()) {
                            plantTypeError = true
                            isValid = false
                        }
                        if (substrate.isBlank()) {
                            substrateError = true
                            isValid = false
                        }
                        if (plantDate == null) {
                            plantDateError = true
                            isValid = false
                        }
                        if (harvestDate == null) {
                            harvestDateError = true
                            isValid = false
                        }
                        if (wateringTime == null) {
                            wateringTimeError = true
                            isValid = false
                        }
                        if (selectedImageUri.value == null) {
                            photoError = true
                            isValid = false
                        }

                        if (isValid) {
                            val garden = Garden(
                                title = title,
                                plantType = plantType,
                                substrate = substrate,
                                plantDate = plantDate!!,
                                harvestDate = harvestDate!!,
                                wateringTime = wateringTime!!,
                                photo = uriToBase64(context, selectedImageUri.value as Uri) ?: ""
                            )

                            if (PermissionUtils.hasNotificationPermission(context)) {
                                val gardenId = RoomInstance
                                    .getInstance(context.applicationContext as Application)
                                    .roomDao()
                                    .addGarden(garden)

                                AlarmScheduler.scheduleWateringAlarm(
                                    context = context,
                                    gardenId = gardenId,
                                    gardenTitle = title,
                                    wateringTimeMillis = wateringTime!!
                                )

                                navController.popBackStack()
                            } else {
                                pendingGarden = garden
                                showPermissionRequest = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA9D66A),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Создать",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
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

