package kz.vasilyev.agrotechapp.feature.journal_garden

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kz.vasilyev.agrotechapp.ui.theme.BackgroundScreen

@Composable
fun JournalGarden(innerPadding: PaddingValues, navController: NavController, gardenId: Long) {

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
                    text = "Название лота",
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
            PlantInfoCard()
            Spacer(Modifier.height(17.dp))
            DashboardMenu()
        }
    }
}

@Composable
fun PlantInfoCard() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray, RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "День X",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "/ осталось X",
                        color = Color(0xFF88C057),
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column {
                    Text("Информация", fontSize = 12.sp, color = Color.Gray)
                    InfoRow(label = "Высота", value = "314мм")
                    InfoRow(label = "Освещение", value = "2.2lux", valueColor = Color(0xFF88C057))

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Температура", fontSize = 12.sp, color = Color.Gray)
                    InfoRow(label = "Вода", value = "22.5°C", valueColor = Color(0xFF88C057))
                    InfoRow(label = "Воздух", value = "24.3°C", valueColor = Color(0xFF88C057))

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Влажность", fontSize = 12.sp, color = Color.Gray)
                    InfoRow(label = "Почва", value = "12%", valueColor = Color(0xFF88C057))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF88C057)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("До / после")
            }

            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF88C057)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Галерея")
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
fun DashboardMenu() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5)) // светлый фон
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Кнопка "Аналитика"
        Button(
            onClick = { /* TODO */ },
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
            onClick = { /* TODO */ },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, Color(0xFFA6D86D)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Новая запись", color = Color(0xFFA6D86D), fontWeight = FontWeight.Medium, fontSize = 18.sp)
        }
    }
}

