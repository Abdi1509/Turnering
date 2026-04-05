package com.abdirahman.fritidsklubb

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                NavHost(
                    navController = navController,
                    startDestination = if (harSettOnboarding) "oppsett" else "onboarding"
                ) {
                    composable("onboarding") {
                        prefs.edit().putBoolean("har_sett_onboarding", true).apply()
                        OnboardingScreen(navController = navController)
                    }
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
                    composable("poeng_turnering") {
                        PoengTurneringScreen(navController = navController)
                    }
                }
            }
        }
    }
}