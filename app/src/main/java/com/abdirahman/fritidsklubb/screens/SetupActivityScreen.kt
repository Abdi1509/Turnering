package com.abdirahman.fritidsklubb.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

val OsloDarkBlue = Color(0xFF2A2859)
val OsloWarmBlue = Color(0xFF1F42AA)
val OsloDarkGreen = Color(0xFF034B45)
val OsloTurquoise = Color(0xFF6FE9FF)
val OsloText = Color(0xFF2C2C2C)
val OsloWhite = Color(0xFFFFFFFF)

private val uteAktiviteter = listOf(
    "⚽ Fotball", "🏀 Basketball", "🏐 Volleyball",
    "🏸 Badminton", "🏃 Stafettløp", "🪵 Kubb",
    "🥏 Ultimate Frisbee", "🧗 Hinderløype",
    "🚴 Sykling", "🎯 Bocce"
)

private val inneAktiviteter = listOf(
    "🏓 Bordtennis", "🎯 Dart", "🎳 Bowling",
    "🎱 Biljard", "♟️ Sjakk", "🎲 Brettspill",
    "🥌 Curling", "🎾 Squash", "⚽ Foosball", "🃏 Kortspill"
)

@Composable
fun SetupScreen(navController: NavController) {
    var erUte by remember { mutableStateOf<Boolean?>(null) }
    var forslag by remember { mutableStateOf("") }

    // Siste turnering
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("turnering_prefs", android.content.Context.MODE_PRIVATE)
    val sisteAktivitet = prefs.getString("siste_aktivitet", null)
    val sisteAntall = prefs.getInt("siste_antall", 0)

    fun nyttForslag() {
        val liste = if (erUte == true) uteAktiviteter else inneAktiviteter
        forslag = liste.random()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OsloDarkBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🏆", fontSize = 48.sp)
                Text(
                    text = "Turneringsgenerator",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite
                )
                Text(
                    text = "Oslo kommune fritidsklubb",
                    fontSize = 14.sp,
                    color = OsloTurquoise
                )
            }
        }

        val totaltGavekort = prefs.getInt("totalt_gavekort", 0)
        val sisteGavekort = prefs.getInt("siste_gavekort", 0)
        val antallTurneringer = prefs.getInt("antall_turneringer", 0)

        if (antallTurneringer > 0) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = OsloWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "📊 Statistikk",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = OsloText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🏆", fontSize = 28.sp)
                            Text(
                                "$antallTurneringer",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = OsloDarkBlue
                            )
                            Text("Turneringer", fontSize = 12.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎁", fontSize = 28.sp)
                            Text(
                                "$sisteGavekort",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = OsloDarkBlue
                            )
                            Text("Gavekort sist", fontSize = 12.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎁", fontSize = 28.sp)
                            Text(
                                "$totaltGavekort",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = OsloDarkBlue
                            )
                            Text("Gavekort Totalt", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (sisteAktivitet != null && sisteAntall > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = OsloWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📋", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Siste turnering", fontSize = 12.sp, color = Color.Gray)
                        Text(sisteAktivitet, fontWeight = FontWeight.Bold, color = OsloText, fontSize = 15.sp)
                        Text("$sisteAntall deltakere", fontSize = 13.sp, color = OsloText)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }




        // Ute / Inne
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OsloWhite),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Hvor skal dere være idag?",
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
                        onClick = { erUte = true; nyttForslag() },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (erUte == true) OsloDarkGreen else Color.LightGray
                        )
                    ) { Text("🌤 Ute", color = OsloWhite, fontWeight = FontWeight.Bold) }

                    Button(
                        onClick = { erUte = false; nyttForslag() },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (erUte == false) OsloWarmBlue else Color.LightGray
                        )
                    ) { Text("🏠 Inne", color = OsloWhite, fontWeight = FontWeight.Bold) }
                }
            }
        }

        // Forslag
        if (forslag.isNotEmpty()) {
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
                    Text(
                        text = "Foreslått aktivitet",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = forslag,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = OsloText
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { nyttForslag() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OsloDarkBlue)
                    ) {  Icon(Icons.Filled.Refresh, contentDescription = null, tint = OsloDarkBlue)
                        Text("Prøv en annen") }

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
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    TournamentState.aktivitet = forslag
                    navController.navigate("tournament_setup")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
            ) {
                Text(
                    "▶ Start turneringen",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}