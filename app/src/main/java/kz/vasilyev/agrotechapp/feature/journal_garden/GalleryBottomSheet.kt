package kz.vasilyev.agrotechapp.feature.journal_garden

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kz.vasilyev.agrotechapp.navigation.Screen
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryBottomSheet(
    gardenId: Long,
    navController: NavController,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val notes = remember {
        RoomInstance
            .getInstance(context.applicationContext as Application)
            .roomDao()
            .getNotesForGarden(gardenId)
            .filter { it.photo.isNotEmpty() }
            .groupBy { note ->
                Date(note.createdAt).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }
            .toSortedMap(compareByDescending { it })
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Галерея",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            notes.forEach { (date, dateNotes) ->
                item {
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF88C057),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(dateNotes) { note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        val bitmap = base64ToBitmap(note.photo)
                        bitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
} 