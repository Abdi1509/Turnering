package com.abdirahman.fritidsklubb

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abdirahman.fritidsklubb.screens.*
import com.abdirahman.fritidsklubb.ui.theme.TurneringTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val harSettOnboarding = prefs.getBoolean("har_sett_onboarding", false)

        setContent {
            TurneringTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                // Skjerm der vi IKKE viser bottom nav
                val skjulNavBar = listOf(
                    "onboarding",
                    "regler/{aktivitet}",
                    "tournament_setup",
                    "bracket",
                    "poeng_turnering"
                ).any { currentRoute == it || currentRoute?.startsWith("regler/") == true }

                Scaffold(
                    bottomBar = {
                        if (!skjulNavBar) {
                            NavigationBar(
                                containerColor = Color(0xFF2A2859),
                                contentColor = Color.White
                            ) {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.PlayArrow, contentDescription = "Ny turnering") },
                                    label = { Text("Turnering") },
                                    selected = currentRoute == "oppsett",
                                    onClick = {
                                        navController.navigate("oppsett") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF6FE9FF),
                                        selectedTextColor = Color(0xFF6FE9FF),
                                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                        unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                        indicatorColor = Color.White.copy(alpha = 0.1f)
                                    )
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Star, contentDescription = "Statistikk") },
                                    label = { Text("Statistikk") },
                                    selected = currentRoute == "statistikk",
                                    onClick = {
                                        navController.navigate("statistikk") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF6FE9FF),
                                        selectedTextColor = Color(0xFF6FE9FF),
                                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                        unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                        indicatorColor = Color.White.copy(alpha = 0.1f)
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (harSettOnboarding) "oppsett" else "onboarding",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("onboarding") {
                            prefs.edit().putBoolean("har_sett_onboarding", true).apply()
                            OnboardingScreen(navController = navController)
                        }
                        composable("hjem") {
                            HjemScreen(navController = navController)
                        }
                        composable("oppsett") {
                            SetupScreen(navController = navController)
                        }
                        composable("statistikk") {
                            StatistikkScreen(navController = navController)
                        }
                        composable("regler/{aktivitet}") { backStackEntry ->
                            val aktivitet = backStackEntry.arguments?.getString("aktivitet") ?: ""
                            RulesScreen(aktivitet = aktivitet, navController = navController)
                        }
                        composable("tournament_setup") {
                            TournamentSetupScreen(navController = navController)
                        }
                        composable("bracket") {
                            BracketScreen(navController = navController)
                        }
                        composable("poeng_turnering") {
                            PoengTurneringScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}