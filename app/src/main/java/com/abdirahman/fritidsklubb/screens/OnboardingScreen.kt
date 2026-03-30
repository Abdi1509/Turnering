package com.abdirahman.fritidsklubb.screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class OnboardingSteg(
    val emoji: String,
    val tittel: String,
    val beskrivelse: String
)

val onboardingSteg = listOf(
    OnboardingSteg(
        emoji = "👋",
        tittel = "Velkommen!",
        beskrivelse = "Dette er turneringsgeneratoren for Oslo kommune fritidsklubb. Vi hjelper deg å lage morsomme turneringer raskt og enkelt!"
    ),
    OnboardingSteg(
        emoji = "🌤",
        tittel = "Velg aktivitet",
        beskrivelse = "Velg om dere skal være ute eller inne, så foreslår appen en tilfeldig aktivitet. Ikke fornøyd? Trykk på 'Prøv en annen'!"
    ),
    OnboardingSteg(
        emoji = "📖",
        tittel = "Les reglene",
        beskrivelse = "Usikker på reglene? Trykk på 'Se regler' for å få en enkel forklaring av aktiviteten — perfekt for de som aldri har prøvd før!"
    ),
    OnboardingSteg(
        emoji = "👥",
        tittel = "Legg inn deltakere",
        beskrivelse = "Skriv inn navnene på alle som skal være med. Du kan velge hvor mange som skal være på hvert lag, og legge til flere underveis!"
    ),
    OnboardingSteg(
        emoji = "🏆",
        tittel = "Start turneringen!",
        beskrivelse = "Appen genererer automatisk en bracket. Trykk på vinneren etter hver kamp for å gå videre. Lykke til!"
    )
)

@Composable
fun OnboardingScreen(navController: NavController) {
    var nåværendeSteg by remember { mutableStateOf(0) }
    val steg = onboardingSteg[nåværendeSteg]
    val erSiste = nåværendeSteg == onboardingSteg.size - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Innhold
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Oslo kommune header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = OsloDarkBlue)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(steg.emoji, fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = steg.tittel,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = OsloWhite,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = OsloWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Text(
                    text = steg.beskrivelse,
                    fontSize = 16.sp,
                    color = OsloText,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Prikker
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                onboardingSteg.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .size(if (index == nåværendeSteg) 12.dp else 8.dp)
                            .background(
                                color = if (index == nåværendeSteg) OsloDarkBlue else Color.LightGray,
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        // Knapper
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (erSiste) {
                        navController.navigate("oppsett") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    } else {
                        nåværendeSteg++
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
            ) {
                Text(
                    if (erSiste) "🚀 Kom i gang!" else "Neste →",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite
                )
            }

            if (!erSiste) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        navController.navigate("oppsett") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Hopp over", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}