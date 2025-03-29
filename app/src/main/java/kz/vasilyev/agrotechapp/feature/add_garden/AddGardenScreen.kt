package kz.vasilyev.agrotechapp.feature.add_garden

import android.app.Application
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
import kotlin.collections.Map.Entry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGardenScreen(
    innerPadding: PaddingValues,
    navController: NavController
) {
    val context = LocalContext.current

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    val fieldStates = remember {
        mutableStateListOf(
            mutableStateOf(""), // Название лота
            mutableStateOf(""), // Вид
            mutableStateOf(""), // Субстрат
            mutableStateOf(""), // Дата посева
            mutableStateOf("")  // Дата сбора
        )
    }

    val placeholderFields = listOf(
        Pair("Название лота", R.drawable.ic_title),
        Pair("Вид", R.drawable.ic_home),
        Pair("Субстрат", R.drawable.ic_substrate),
        Pair("Дата посева", R.drawable.ic_date),
        Pair("Дата сбора", R.drawable.ic_harvest_date)
    )

    var wateringIntervalText by remember { mutableStateOf("") }

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

                placeholderFields.forEachIndexed { index, (placeholder, iconRes) ->
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = fieldStates[index].value,
                        onValueChange = { fieldStates[index].value = it },
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Primary,
                            focusedIndicatorColor = Primary,
                            disabledIndicatorColor = Primary,
                            containerColor = Color.White,
                            focusedTextColor = Color.Black,
                            cursorColor = Primary,
                        ),
                        placeholder = { Text(text = placeholder) },
                        leadingIcon = {
                            Icon(painterResource(iconRes), contentDescription = "", tint = Primary)
                        }
                    )
                    if(index != fieldStates.size - 1)  Spacer(Modifier.height(11.dp))
                }

                Spacer(Modifier.height(11.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = wateringIntervalText,
                    onValueChange = { wateringIntervalText = it },
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = Primary,
                        focusedIndicatorColor = Primary,
                        disabledIndicatorColor = Primary,
                        containerColor = Color.White,
                        focusedTextColor = Color.Black,
                        cursorColor = Primary,
                    ),
                    placeholder = { Text(text = "Интервал полива") },
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.ic_clocks),
                            contentDescription = "",
                            tint = Primary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                Spacer(Modifier.height(11.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    value = "",
                    onValueChange = { },
                    placeholder = { Text("Комментарий...") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedIndicatorColor = Primary,
                        focusedIndicatorColor = Primary,
                        cursorColor = Primary
                    )
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
            }

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        val garden = Garden(
                            title = fieldStates[0].value,
                            plantType = fieldStates[1].value,
                            substrate = fieldStates[2].value,
                            harvestDate = fieldStates[3].value,
                            plantDate = fieldStates[4].value,
                            wateringInterval = wateringIntervalText.toIntOrNull() ?: 0,
                            photo = uriToBase64(context, selectedImageUri.value as Uri) ?: ""
                        )

                        RoomInstance
                            .getInstance(context.applicationContext as Application)
                            .roomDao()
                            .addGarden(garden)

                        navController.popBackStack()
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

