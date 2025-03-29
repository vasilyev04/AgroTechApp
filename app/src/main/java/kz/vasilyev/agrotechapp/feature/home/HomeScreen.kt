package kz.vasilyev.agrotechapp.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kz.vasilyev.agrotechapp.models.Garden
import kz.vasilyev.agrotechapp.navigation.Screen
import kz.vasilyev.agrotechapp.ui.theme.Primary

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 8.dp)
    ) {
        MySearchBar(textFieldValue = "", {}) { }

        Spacer(Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 21.dp, top = 14.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(top = 30.dp, start = 24.dp, end = 24.dp, bottom = 80.dp),
                    columns = GridCells.Fixed(2)
                ) {
                    items(2) {
                        GardenItem(
                            garden = Garden(
                                title = "Название лота",
                                substrate = "Cубстрат",
                                plantType = "Sunflower",
                                harvestDate = "",
                                photo = "",
                                plantDate = "",
                                wateringInterval = 12
                            ),
                            position = it + 1
                        )
                    }
                }

                FloatingActionButton(
                    onClick = { navController.navigate(route = Screen.AddGarden.route) },
                    containerColor = Primary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        }
    }
}