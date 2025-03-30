package kz.vasilyev.agrotechapp.feature.journal_garden

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kz.vasilyev.agrotechapp.data.RoomInstance
import kz.vasilyev.agrotechapp.feature.home.base64ToBitmap
import kz.vasilyev.agrotechapp.models.Garden
import kz.vasilyev.agrotechapp.models.Note
import kz.vasilyev.agrotechapp.navigation.Screen
import kz.vasilyev.agrotechapp.ui.theme.BackgroundScreen

@Composable
fun JournalGarden(innerPadding: PaddingValues, navController: NavController, gardenId: Long) {
    val context = LocalContext.current

    val garden = remember {
        RoomInstance
            .getInstance(context.applicationContext as Application)
            .roomDao()
            .getGarden(gardenId)
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
                    text = garden.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { innerPaddings ->
        Column(
            modifier = Modifier.padding(innerPaddings)
        ) {
            PlantInfoCard(garden, navController)
            Spacer(Modifier.height(17.dp))
            DashboardMenu(navController, gardenId)
        }
    }
}

@Composable
fun PlantInfoCard(garden: Garden, navController: NavController) {
    val context = LocalContext.current
    val notes = remember {
        RoomInstance
            .getInstance(context.applicationContext as Application)
            .roomDao()
            .getNotesForGarden(garden.id)
    }

    var showGallery by remember { mutableStateOf(false) }
    var showCompare by remember { mutableStateOf(false) }

    if (showGallery) {
        GalleryBottomSheet(
            navController = navController,
            gardenId = garden.id,
            onDismiss = { showGallery = false }
        )
    }

    if (showCompare && notes.isNotEmpty()) {
        CompareBottomSheet(
            initialPhoto = garden.photo,
            lastPhoto = notes.last().photo,
            onDismiss = { showCompare = false }
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Добавьте запись",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF88C057)
                )
            }
        } else {
            val lastNote = notes.last()
            val currentDate = System.currentTimeMillis()
            val totalDays = ((garden.harvestDate - garden.plantDate) / (24 * 60 * 60 * 1000)).toInt()
            val passedDays = ((currentDate - garden.plantDate) / (24 * 60 * 60 * 1000)).toInt()
            val remainingDays = totalDays - passedDays

            Row(modifier = Modifier.fillMaxWidth()) {
                Column {
                    val bitmap = base64ToBitmap(lastNote.photo)
                    bitmap?.let {
                        Card(
                            modifier = Modifier
                                .size(width = 157.dp, height = 128.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = BackgroundScreen
                            ),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } ?: Card(
                        modifier = Modifier
                            .size(width = 165.dp, height = 125.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = BackgroundScreen
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier.width(157.dp)
                    ) {
                        Button(
                            onClick = { showCompare = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF88C057)),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Сравнение",
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { showGallery = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF88C057)),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Галерея",
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "День $passedDays",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "/ $totalDays",
                            color = Color(0xFF88C057),
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Column {
                        Text("Информация", fontSize = 12.sp, color = Color.Gray)
                        InfoRow(label = "Высота", value = "${lastNote.height}мм", valueColor = Color(0xFF88C057))
                        InfoRow(label = "Освещение", value = "${lastNote.light}lux", valueColor = Color(0xFF88C057))

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Температура", fontSize = 12.sp, color = Color.Gray)
                        InfoRow(label = "Вода", value = "${lastNote.waterTemp}°C", valueColor = Color(0xFF88C057))
                        InfoRow(label = "Воздух", value = "${lastNote.airTemp}°C", valueColor = Color(0xFF88C057))

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Влажность", fontSize = 12.sp, color = Color.Gray)
                        InfoRow(label = "Почва", value = "${lastNote.humidity}%", valueColor = Color(0xFF88C057))
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text("$label: ", fontWeight = FontWeight.Bold)
        Text(value, color = valueColor)
    }
}

@Composable
fun DashboardMenu(navController: NavController, gardenId: Long) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate(Screen.Analytics.route.replace(
                oldValue = "{gardenId}",
                newValue = gardenId.toString()
            )) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA6D86D)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Аналитика", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Записи",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { navController.navigate(
                Screen.AddNote.route.replace(
                    oldValue = "{gardenId}",
                    newValue = gardenId.toString()
                )
            ) },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, Color(0xFFA6D86D)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Новая запись", color = Color(0xFFA6D86D), fontWeight = FontWeight.Medium, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
        // Список записей
        val notes = remember {
            RoomInstance
                .getInstance(context.applicationContext as Application)
                .roomDao()
                .getNotesForGarden(gardenId)
                .sortedByDescending { it.createdAt }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { 
                            navController.navigate(
                                Screen.DetailsNote.route.replace(
                                    oldValue = "{noteId}",
                                    newValue = note.id.toString()
                                )
                            )
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = note.getFormattedDate(),
                                fontSize = 18.sp,
                                color = Color(0xFF88C057),
                                fontWeight = FontWeight.Bold
                            )
                            if (note.comment.isNotEmpty()) {
                                Text(
                                    text = note.comment,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Подробнее",
                            tint = Color(0xFF88C057)
                        )
                    }
                }
            }
        }
    }
}

