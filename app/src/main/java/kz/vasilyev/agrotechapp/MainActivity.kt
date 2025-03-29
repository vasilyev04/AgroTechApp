package kz.vasilyev.agrotechapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kz.vasilyev.agrotechapp.navigation.NavHost
import kz.vasilyev.agrotechapp.navigation.Screen
import kz.vasilyev.agrotechapp.ui.theme.AgroTechAppTheme
import kz.vasilyev.agrotechapp.ui.theme.BackgroundScreen
import kz.vasilyev.agrotechapp.ui.theme.Primary
import kz.vasilyev.agrotechapp.ui.theme.Secondary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry = navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry.value?.destination

            AgroTechAppTheme {
                Scaffold(
                    containerColor = BackgroundScreen,
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if(Screen.BottomBar.isBottomBarScreen(currentDestination?.route)) {
                            BottomAppBar(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                containerColor = Primary,
                                content = {
                                    NavigationBarItem(
                                        selected = false,
                                        onClick = {
                                            navController.navigate(Screen.BottomBar.Home.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        label = null,
                                        icon = {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Icon(
                                                    modifier = Modifier.size(32.dp),
                                                    painter = painterResource(R.drawable.ic_home),
                                                    contentDescription = "Home",
                                                    tint = if(currentDestination != null && currentDestination.route == Screen.BottomBar.Home.route) {
                                                        Color.White
                                                    } else {
                                                        Secondary
                                                    }
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Главная",
                                                    fontSize = 12.sp,
                                                    color = if(currentDestination != null && currentDestination.route == Screen.BottomBar.Home.route) {
                                                        Color.White
                                                    } else {
                                                        Secondary
                                                    }
                                                )
                                            }
                                        }
                                    )

                                    NavigationBarItem(
                                        selected = false,
                                        onClick = {
                                            navController.navigate(Screen.BottomBar.Tips.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        label = null,
                                        icon = {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Icon(
                                                    modifier = Modifier.size(32.dp),
                                                    painter = painterResource(R.drawable.ic_journal),
                                                    contentDescription = "",
                                                    tint = if(currentDestination != null && currentDestination.route == Screen.BottomBar.Tips.route) {
                                                        Color.White
                                                    } else {
                                                        Secondary
                                                    }
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Советы",
                                                    fontSize = 12.sp,
                                                    color = if(currentDestination != null && currentDestination.route == Screen.BottomBar.Tips.route) {
                                                        Color.White
                                                    } else {
                                                        Secondary
                                                    }
                                                )
                                            }
                                        }
                                    )

                                    NavigationBarItem(
                                        selected = false,
                                        onClick = {
                                            navController.navigate(Screen.BottomBar.Chat.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        label = null,
                                        icon = {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Icon(
                                                    modifier = Modifier.size(32.dp),
                                                    painter = painterResource(R.drawable.ic_chat),
                                                    contentDescription = "Home",
                                                    tint = if(currentDestination != null && currentDestination.route == Screen.BottomBar.Chat.route) {
                                                        Color.White
                                                    } else {
                                                        Secondary
                                                    }
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Чат",
                                                    fontSize = 12.sp,
                                                    color = if(currentDestination != null && currentDestination.route == Screen.BottomBar.Chat.route) {
                                                        Color.White
                                                    } else {
                                                        Secondary
                                                    }
                                                )
                                            }
                                        }
                                    )

                                    NavigationBarItem(
                                        selected = false,
                                        onClick = {   navController.navigate(Screen.BottomBar.Library.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        } },
                                        label = null,
                                        icon = {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Icon(
                                                    modifier = Modifier.size(32.dp),
                                                    painter = painterResource(R.drawable.ic_library),
                                                    contentDescription = "Home",
                                                    tint = if(currentDestination != null && currentDestination.route == Screen.BottomBar.Library.route) {
                                                        Color.White
                                                    } else {
                                                        Secondary
                                                    }
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Библиоетка",
                                                    fontSize = 12.sp,
                                                    color = if(currentDestination != null && currentDestination.route == Screen.BottomBar.Library.route) {
                                                        Color.White
                                                    } else {
                                                        Secondary
                                                    }
                                                )
                                            }
                                        }
                                    )
                                },
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        innerPadding = innerPadding,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AgroTechAppTheme {
        Greeting("Android")
    }
}