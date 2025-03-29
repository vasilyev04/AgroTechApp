package kz.vasilyev.agrotechapp.feature.tips

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TipItem(
    val question: String,
    val answer: String
)

@Composable
fun TipsScreen(innerPadding: PaddingValues) {
    var expandedItem by remember { mutableStateOf<Int?>(null) }

    val tips = remember {
        listOf(
            TipItem(
                "Как правильно выбрать семена для микрозелени?",
                "Выбирайте органические семена, специально предназначенные для выращивания микрозелени. Избегайте обработанных семян, так как они могут содержать химикаты. Лучше всего подходят семена редиса, подсолнечника, гороха и горчицы."
            ),
            TipItem(
                "Какой субстрат лучше использовать?",
                "Для микрозелени идеально подходит кокосовый субстрат или специальные маты для микрозелени. Они хорошо удерживают влагу и обеспечивают оптимальные условия для роста. Избегайте использования обычной почвы, так как она может содержать патогены."
            ),
            TipItem(
                "Как часто нужно поливать микрозелень?",
                "Микрозелень требует регулярного полива, но важно не переувлажнять. Поливайте 1-2 раза в день, используя пульверизатор. Субстрат должен быть влажным, но не мокрым. Избегайте застоя воды."
            ),
            TipItem(
                "Какое освещение необходимо?",
                "Микрозелень нуждается в ярком, но рассеянном свете. Оптимально использовать специальные фитолампы или размещать контейнеры на подоконнике с восточной или западной стороны. Световой день должен составлять 12-16 часов."
            ),
            TipItem(
                "Когда собирать урожай?",
                "Микрозелень готова к сбору, когда у растений появляются первые настоящие листочки (обычно через 7-14 дней после посева). Срезайте растения ножницами чуть выше уровня субстрата. Употребляйте в пищу сразу после сбора."
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Daily Tip Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFA9D66A)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Совет дня",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Для лучшего роста микрозелени поддерживайте температуру воздуха в помещении на уровне 20-25°C. При более низкой температуре рост замедляется, а при более высокой может появиться плесень.",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        item {
            Text(
                text = "Частые вопросы",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(tips.indices.toList()) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedItem = if (expandedItem == index) null else index },
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tips[index].question,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (expandedItem == index) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (expandedItem == index) "Свернуть" else "Развернуть"
                        )
                    }

                    AnimatedVisibility(visible = expandedItem == index) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = tips[index].answer,
                                fontSize = 14.sp,
                                color = Color(0xFF5F5F5F)
                            )
                        }
                    }
                }
            }
        }
    }
}