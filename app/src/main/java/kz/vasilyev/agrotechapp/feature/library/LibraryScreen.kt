package kz.vasilyev.agrotechapp.feature.library

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import kz.vasilyev.agrotechapp.R

data class MicrogreenVariety(
    val name: String,
    val description: String,
    val germinationTime: String,
    val temperature: String,
    val lighting: String,
    val isUserAdded: Boolean = false,
    val photoUri: String? = null,
    val defaultPhotoResId: Int? = null
)

@Composable
fun LibraryScreen(innerPadding: PaddingValues) {
    var selectedVariety by remember { mutableStateOf<MicrogreenVariety?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var userVarieties by remember { mutableStateOf(listOf<MicrogreenVariety>()) }
    
    val defaultVarieties = remember {
        listOf(
            MicrogreenVariety(
                "Базилик",
                "Ароматная зелень с ярким вкусом",
                "5-7 д.",
                "20-25°C",
                "12-16 часов",
                defaultPhotoResId = R.drawable.bazilik
            ),
            MicrogreenVariety(
                "Редис",
                "Острый вкус, богат витаминами",
                "4-6 д.",
                "18-22°C",
                "12-14 часов",
                defaultPhotoResId = R.drawable.redis
            ),
            MicrogreenVariety(
                "Горох",
                "Сладкий вкус, богат белком",
                "8-12 д.",
                "18-22°C",
                "12-16 часов",
                defaultPhotoResId = R.drawable.goroh
            ),
            MicrogreenVariety(
                "Подсолнух",
                "Ореховый вкус, богат жирами",
                "8-12 д.",
                "20-25°C",
                "12-16 часов",
                defaultPhotoResId = R.drawable.podsolnuh
            ),
            MicrogreenVariety(
                "Кресс-салат",
                "Острый вкус, богат витаминами",
                "4-6 д.",
                "18-22°C",
                "12-14 часов",
                defaultPhotoResId = R.drawable.kress
            ),
            MicrogreenVariety(
                "Руккола",
                "Острый ореховый вкус",
                "5-7 д.",
                "18-22°C",
                "12-14 часов",
                defaultPhotoResId = R.drawable.rukkola
            )
        )
    }

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Библиотека микрозелени",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(defaultVarieties + userVarieties) { variety ->
                    MicrogreenCard(variety) { selectedVariety = it }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFFA9D66A)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Добавить",
                tint = Color.White
            )
        }
    }

    if (showAddDialog) {
        AddMicrogreenDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { newVariety ->
                userVarieties = userVarieties + newVariety
                showAddDialog = false
            }
        )
    }

    selectedVariety?.let { variety ->
        Dialog(onDismissRequest = { selectedVariety = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = variety.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            variety.photoUri != null -> {
                                AsyncImage(
                                    model = variety.photoUri,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            variety.defaultPhotoResId != null -> {
                                variety.defaultPhotoResId.let { resId ->
                                    Image(
                                        painter = painterResource(id = resId),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            else -> {
                                Text(
                                    text = "Фотография будет добавлена",
                                    color = Color(0xFF5F5F5F),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = variety.description,
                        fontSize = 16.sp,
                        color = Color(0xFF5F5F5F),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Рекомендации по выращиванию:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Время проращивания: ",
                                fontSize = 16.sp
                            )
                            Text(
                                text = variety.germinationTime,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFA9D66A)
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Температура: ",
                                fontSize = 16.sp
                            )
                            Text(
                                text = variety.temperature,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFA9D66A)
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Освещение: ",
                                fontSize = 16.sp
                            )
                            Text(
                                text = variety.lighting,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFA9D66A)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = { selectedVariety = null },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFA9D66A)
                        )
                    ) {
                        Text("Закрыть")
                    }
                }
            }
        }
    }
}

@Composable
fun MicrogreenCard(
    variety: MicrogreenVariety,
    onClick: (MicrogreenVariety) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick(variety) },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFFA9D66A),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = variety.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = variety.description,
                fontSize = 12.sp,
                color = Color(0xFF5F5F5F),
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMicrogreenDialog(
    onDismiss: () -> Unit,
    onAdd: (MicrogreenVariety) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var germinationTime by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    var lighting by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Добавить микрозелень",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название лота") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFA9D66A),
                        focusedBorderColor = Color(0xFFA9D66A),
                        unfocusedLabelColor = Color(0xFFA9D66A),
                        focusedLabelColor = Color(0xFFA9D66A)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Вид") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFA9D66A),
                        focusedBorderColor = Color(0xFFA9D66A),
                        unfocusedLabelColor = Color(0xFFA9D66A),
                        focusedLabelColor = Color(0xFFA9D66A)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = germinationTime,
                    onValueChange = { germinationTime = it },
                    label = { Text("Субстрат") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFA9D66A),
                        focusedBorderColor = Color(0xFFA9D66A),
                        unfocusedLabelColor = Color(0xFFA9D66A),
                        focusedLabelColor = Color(0xFFA9D66A)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = temperature,
                    onValueChange = { temperature = it },
                    label = { Text("Температура") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFA9D66A),
                        focusedBorderColor = Color(0xFFA9D66A),
                        unfocusedLabelColor = Color(0xFFA9D66A),
                        focusedLabelColor = Color(0xFFA9D66A)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = lighting,
                    onValueChange = { lighting = it },
                    label = { Text("Освещение") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFA9D66A),
                        focusedBorderColor = Color(0xFFA9D66A),
                        unfocusedLabelColor = Color(0xFFA9D66A),
                        focusedLabelColor = Color(0xFFA9D66A)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { photoPicker.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA9D66A)
                    )
                ) {
                    Text(if (selectedImageUri != null) "Изменить фото" else "Добавить фото")
                }

                selectedImageUri?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text("Отмена")
                    }
                    Button(
                        onClick = {
                            onAdd(
                                MicrogreenVariety(
                                    name = name,
                                    description = description,
                                    germinationTime = germinationTime,
                                    temperature = temperature,
                                    lighting = lighting,
                                    isUserAdded = true,
                                    photoUri = selectedImageUri?.toString()
                                )
                            )
                        },
                        enabled = name.isNotBlank() && description.isNotBlank() &&
                                germinationTime.isNotBlank() && temperature.isNotBlank() &&
                                lighting.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFA9D66A)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Добавить")
                    }
                }
            }
        }
    }
}