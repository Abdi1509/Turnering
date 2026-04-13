package com.abdirahman.fritidsklubb.screens

import androidx.compose.animation.core.*
import androidx.compose.ui.draw.rotate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

val OsloDarkBlue = Color(0xFF2A2859)
val OsloWarmBlue = Color(0xFF1F42AA)
val OsloDarkGreen = Color(0xFF034B45)
val OsloTurquoise = Color(0xFF6FE9FF)
val OsloText = Color(0xFF2C2C2C)
val OsloWhite = Color(0xFFFFFFFF)
val OsloLightGreen = Color(0xFFC7F6C9)

private val uteAktiviteter = listOf(
    "⚽ Fotball", "🏀 Basketball",
    "🏸 Badminton", "🏃 Stafettløp", "🪵 Kubb",
    "🥏 Frisbee", "🧗 Hinderløype",
    "🎯 Bocce", "🦘 Hoppetau",
    "🥅 Straffekonk", "💣 Bomba",
    "🌀 Monsterball"
)

private val inneAktiviteter = listOf(
    "🏓 Bordtennis", "🎯 Dart", "🎳 Bowling",
    "🎱 Biljard", "♟️ Sjakk", "🎲 Ludo",
    "⚽ Foosball", "🃏 Kortspill", "🎮 Fifa",
    "🥤 Flipp the Cup", "🧱 Høyeste tårn",
    "🏀 Pingpong i kurven", "🏓 Pingpong i kopp",
    "🖍️ Hva tegner du?", "👮 Sheriff"
)

@Composable
fun SetupScreen(navController: NavController) {
    var forslag by remember { mutableStateOf(TournamentState.sisteForslag) }
    var erUte by remember { mutableStateOf(TournamentState.erUte) }
    var visModus by remember { mutableStateOf(TournamentState.visModus) }
    val scope = rememberCoroutineScope()
    var visSpinner by remember { mutableStateOf(false) }
    var spinnerTekst by remember { mutableStateOf("") }
    val rotasjon = remember { Animatable(0f) }
    val scrollState = rememberScrollState()

    fun nyttForslag() {
        scope.launch {
            visSpinner = true
            val liste = if (erUte == true) uteAktiviteter else inneAktiviteter

            launch {
                repeat(20) {
                    spinnerTekst = liste.random()
                    delay(100)
                }
            }

            rotasjon.animateTo(
                targetValue = rotasjon.value + 3600f,
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = FastOutSlowInEasing
                )
            )

            val endelig = liste.random()
            forslag = endelig
            TournamentState.sisteForslag = endelig
            visSpinner = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFF2F2F2)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(OsloDarkBlue, OsloWarmBlue)
                    )
                )
                .padding(top = 56.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🎮", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Klar for en turnering?",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Velg aktivitet eller start direkte",
                    fontSize = 14.sp,
                    color = OsloTurquoise
                )
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Hva vil du gjøre idag?",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = OsloText,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Valg-kort
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Forslag-kort
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(130.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (visModus == "forslag") OsloDarkGreen else OsloWhite
                    ),
                    elevation = CardDefaults.cardElevation(if (visModus == "forslag") 6.dp else 2.dp),
                    onClick = {
                        visModus = "forslag"
                        TournamentState.visModus = "forslag"
                        if (forslag.isEmpty()) {
                            // ikke nullstill forslag hvis det allerede finnes
                        }
                        scope.launch {
                            delay(100)
                            scrollState.animateScrollTo(600)
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("🎲", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Få forslag",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (visModus == "forslag") OsloWhite else OsloText,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "La appen velge aktivitet",
                            fontSize = 11.sp,
                            color = if (visModus == "forslag") OsloWhite.copy(alpha = 0.8f) else Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Direkte-kort
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(130.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (visModus == "direkte") OsloDarkBlue else OsloWhite
                    ),
                    elevation = CardDefaults.cardElevation(if (visModus == "direkte") 6.dp else 2.dp),
                    onClick = {
                        visModus = "direkte"
                        TournamentState.visModus = "direkte"
                        TournamentState.aktivitet = "Egendefinert turnering"
                        navController.navigate("tournament_setup")
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("▶️", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Start direkte",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (visModus == "direkte") OsloWhite else OsloText,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Hopp rett til turneringen",
                            fontSize = 11.sp,
                            color = if (visModus == "direkte") OsloWhite.copy(alpha = 0.8f) else Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Forslag-seksjon
            if (visModus == "forslag") {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = OsloWhite),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Hvor skal dere være?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = OsloText
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    erUte = true
                                    TournamentState.erUte = true
                                    nyttForslag()
                                    scope.launch {
                                        delay(150) // gir UI tid til å vise kortet
                                        scrollState.animateScrollTo(scrollState.maxValue)
                                    }
                                },
                                modifier = Modifier.weight(1f).height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (erUte == true) OsloDarkGreen else OsloLightGreen
                                )
                            ) {
                                Text(
                                    "🌤 Ute",
                                    color = if (erUte == true) OsloWhite else OsloText,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = {
                                    erUte = false
                                    TournamentState.erUte = false
                                    nyttForslag()
                                    scope.launch {
                                        delay(150) // gir UI tid til å vise kortet
                                        scrollState.animateScrollTo(scrollState.maxValue)
                                    }
                                },
                                modifier = Modifier.weight(1f).height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (erUte == false) OsloWarmBlue else OsloLightGreen
                                )
                            ) {
                                Text(
                                    "🏠 Inne",
                                    color = if (erUte == false) OsloWhite else OsloText,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                if (forslag.isNotEmpty() || visSpinner) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = OsloWhite),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Foreslått aktivitet", color = Color.Gray, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(16.dp))

                            if (visSpinner) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .rotate(rotasjon.value),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val spinnerEmojis = listOf("🎡", "🎰", "🎲", "🌀")
                                    Text(spinnerEmojis.random(), fontSize = 64.sp)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    spinnerTekst,
                                    fontSize = 18.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = forslag,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OsloText,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            OutlinedButton(
                                onClick = { nyttForslag() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = OsloDarkBlue),
                                enabled = !visSpinner
                            ) {
                                Icon(Icons.Filled.Refresh, contentDescription = null, tint = OsloDarkBlue)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (visSpinner) "Spinner..." else "Prøv en annen")
                            }

                            if (!visSpinner) {
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedButton(
                                    onClick = {
                                        val aktivitetNavn = forslag.replace("/", "-")
                                        navController.navigate("regler/$aktivitetNavn")
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = OsloWarmBlue)
                                ) { Text("📖 Se regler") }

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        TournamentState.aktivitet = forslag
                                        navController.navigate("tournament_setup")
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
                                ) {
                                    Text(
                                        "▶ Start turneringen",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OsloWhite
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StatKolonne(emoji: String, verdi: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 28.sp)
        Text(verdi, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = OsloDarkBlue)
        Text(label, fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
    }
}