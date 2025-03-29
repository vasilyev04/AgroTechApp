package kz.vasilyev.agrotechapp.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kz.vasilyev.agrotechapp.models.Garden

@Composable
fun HomeScreen(
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 21.dp, top = 14.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            )
        ) {
            LazyVerticalGrid(
                modifier = Modifier.padding(top = 30.dp, start = 24.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(2) {
                    GardenItem(
                        garden = Garden(
                            title = "Название лота",
                            substrate = "Cубстрат",
                            plantType = "Sunflower",
                            harvestDate = 0L,
                            photo = "",
                            plantDate = 0L
                        ),
                        position = it + 1
                    )
                }
            }
        }

    }
}