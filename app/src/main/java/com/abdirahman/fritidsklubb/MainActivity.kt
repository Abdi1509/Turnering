package com.abdirahman.fritidsklubb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abdirahman.fritidsklubb.screens.BracketScreen
import com.abdirahman.fritidsklubb.screens.RulesScreen
import com.abdirahman.fritidsklubb.screens.SetupScreen
import com.abdirahman.fritidsklubb.screens.TournamentSetupScreen
import com.abdirahman.fritidsklubb.ui.theme.TurneringTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TurneringTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "oppsett") {
                    composable("oppsett") {
                        SetupScreen(navController = navController)
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
                }
            }
        }
    }
}