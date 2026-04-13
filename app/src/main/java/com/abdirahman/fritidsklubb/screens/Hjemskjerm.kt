package com.abdirahman.fritidsklubb.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun HjemScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("turnering_prefs", android.content.Context.MODE_PRIVATE)
    val sisteAktivitet = prefs.getString("siste_aktivitet", null)
    val sisteAntall = prefs.getInt("siste_antall", 0)
    val sisteGavekort = prefs.getInt("siste_gavekort", 0)
    val totaltGavekort = prefs.getInt("totalt_gavekort", 0)
    val antallTurneringer = prefs.getInt("antall_turneringer", 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(colors = listOf(OsloDarkBlue, OsloWarmBlue)))
                .padding(top = 56.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🏆", fontSize = 56.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Team B fritidsklubb",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite,
                    textAlign = TextAlign.Center
                )
                Text("Turneringsgenerator", fontSize = 14.sp, color = OsloTurquoise)
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {

            // Start turnering-knapp
            Button(
                onClick = { navController.navigate("oppsett") },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OsloDarkGreen)
            ) {
                Text(
                    "▶ Start ny turnering",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Statistikk
            if (antallTurneringer > 0) {
                Text("📊 Statistikk", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = OsloText)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatKortHjem(
                        emoji = "🏆",
                        verdi = "$antallTurneringer",
                        label = "Turneringer",
                        modifier = Modifier.weight(1f)
                    )
                    StatKortHjem(
                        emoji = "🎁",
                        verdi = "$totaltGavekort",
                        label = "Gavekort totalt",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Siste turnering
            if (sisteAktivitet != null && sisteAntall > 0) {
                Text("📋 Siste turnering", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = OsloText)
                Spacer(modifier = Modifier.height(12.dp))

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
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFE8F0FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📋", fontSize = 28.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Siste aktivitet", fontSize = 12.sp, color = Color.Gray)
                            Text(sisteAktivitet, fontWeight = FontWeight.Bold, color = OsloText, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("👥 $sisteAntall deltakere", fontSize = 13.sp, color = Color.Gray)
                                Text("🎁 $sisteGavekort gavekort", fontSize = 13.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }

            if (antallTurneringer == 0) {
                Spacer(modifier = Modifier.height(48.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎉", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Ingen turneringer ennå!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = OsloText
                    )
                    Text(
                        "Trykk på 'Start ny turnering' for å komme i gang",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun StatKortHjem(emoji: String, verdi: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = OsloWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(verdi, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = OsloDarkBlue)
            Text(label, fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}