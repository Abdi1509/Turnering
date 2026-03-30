package com.abdirahman.fritidsklubb.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun BracketScreen(navController: NavController) {
    val kamper = TournamentState.kamper
    val runde = TournamentState.runde
    val fase = TournamentState.fase
    val ferdig = TournamentState.turneringFerdig()
    val nesteKamp = TournamentState.nesteKamp()
    var visAngreDialog by remember { mutableStateOf<TournamentState.Kamp?>(null) }

    // Angre-dialog
    if (visAngreDialog != null) {
        AlertDialog(
            onDismissRequest = { visAngreDialog = null },
            title = { Text("Angre resultat?") },
            text = {
                Text("Vil du nullstille resultatet for ${visAngreDialog!!.lag1} vs ${visAngreDialog!!.lag2}?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        TournamentState.angreVinner(visAngreDialog!!.id)
                        visAngreDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
                ) { Text("Ja, angre", color = OsloWhite) }
            },
            dismissButton = {
                OutlinedButton(onClick = { visAngreDialog = null }) {
                    Text("Avbryt")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
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
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🏆", fontSize = 36.sp)
                Text(
                    text = when (fase) {
                        TournamentState.Fase.FERDIG -> "Turnering ferdig!"
                        TournamentState.Fase.BRONSEKAMP -> "Bronsekamp 🥉"
                        TournamentState.Fase.FINALE -> "Finale 🏆"
                        else -> "Runde $runde"
                    },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite
                )
                Text(TournamentState.aktivitet, fontSize = 13.sp, color = OsloTurquoise)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

// Ekstraspiller-boks
        val ekstraSpiller = TournamentState.ekstraSpiller
        if (ekstraSpiller != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⏳", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Venter på ekstra kamp",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100),
                            fontSize = 14.sp
                        )
                        Text(
                            "$ekstraSpiller spiller mot vinneren av en tilfeldig kamp når alle kamper er ferdige",
                            color = Color(0xFFE65100),
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

// Pall
        if (ferdig) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Sluttresultat", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OsloText)
                    Spacer(modifier = Modifier.height(16.dp))
                    PallRad("🥇", "1. plass", TournamentState.turneringsvinner(), Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(8.dp))
                    PallRad("🥈", "2. plass", TournamentState.andrePlass(), Color(0xFFB0BEC5))
                    Spacer(modifier = Modifier.height(8.dp))
                    PallRad("🥉", "3. plass", TournamentState.tredjePlass(), Color(0xFFCD7F32))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    TournamentState.reset()
                    navController.navigate("oppsett") {
                        popUpTo("oppsett") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
            ) { Text("🔄 Ny turnering", color = OsloWhite, fontWeight = FontWeight.Bold) }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Neste kamp
        if (nesteKamp != null && !ferdig) {
            val kampTittel = when (nesteKamp.type) {
                TournamentState.KampType.BRONSEKAMP -> "🥉 Bronsekamp — trykk på vinneren"
                TournamentState.KampType.FINALE -> "🏆 Finale — trykk på vinneren"
                TournamentState.KampType.EKSTRA -> "⚠️ Ekstra kamp — trykk på vinneren"
                else -> "⚔️ Neste kamp — trykk på vinneren"
            }

            Text(kampTittel, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OsloText, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (nesteKamp.type == TournamentState.KampType.EKSTRA)
                        Color(0xFFFFF3E0) else OsloWhite
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                if (nesteKamp.type == TournamentState.KampType.EKSTRA) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE65100), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "⚠️ Denne spilleren måtte spille en ekstra kamp fordi antallet var ujevnt",
                            color = OsloWhite,
                            fontSize = 12.sp
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { TournamentState.registrerVinner(nesteKamp.id, nesteKamp.lag1) },
                        modifier = Modifier.weight(1f).height(64.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OsloDarkGreen)
                    ) {
                        Text(nesteKamp.lag1, color = OsloWhite, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 13.sp)
                    }
                    Text("VS", fontWeight = FontWeight.Bold, color = OsloDarkBlue, fontSize = 16.sp)
                    Button(
                        onClick = { TournamentState.registrerVinner(nesteKamp.id, nesteKamp.lag2) },
                        modifier = Modifier.weight(1f).height(64.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OsloWarmBlue)
                    ) {
                        Text(nesteKamp.lag2, color = OsloWhite, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 13.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Kamper denne runden
        Text("📋 Runde $runde", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OsloText, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        kamper.filter { it.runde == runde && it.type == TournamentState.KampType.NORMAL }.forEach { kamp ->
            KampKort(
                kamp = kamp,
                erNeste = kamp.id == nesteKamp?.id,
                kanAngre = kamp.vinner != null && fase == TournamentState.Fase.NORMAL,
                onAngre = { visAngreDialog = kamp }
            )
        }

        val bronsekamp = kamper.firstOrNull { it.type == TournamentState.KampType.BRONSEKAMP }
        val finale = kamper.firstOrNull { it.type == TournamentState.KampType.FINALE }

        if (bronsekamp != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("🥉 Bronsekamp", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OsloText, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            KampKort(
                kamp = bronsekamp,
                erNeste = bronsekamp.id == nesteKamp?.id,
                kanAngre = bronsekamp.vinner != null && fase == TournamentState.Fase.BRONSEKAMP,
                onAngre = { visAngreDialog = bronsekamp }
            )
        }

        if (finale != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("🏆 Finale", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OsloText, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            KampKort(
                kamp = finale,
                erNeste = finale.id == nesteKamp?.id,
                kanAngre = finale.vinner != null && fase == TournamentState.Fase.FINALE,
                onAngre = { visAngreDialog = finale }
            )
        }

        // Tidligere runder
        if (runde > 1) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("📜 Tidligere runder", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OsloText, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            (1 until runde).forEach { r ->
                Text("Runde $r", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
                kamper.filter { it.runde == r }.forEach { kamp ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${kamp.lag1} vs ${kamp.lag2}", fontSize = 13.sp, color = OsloText)
                            Text("✓ ${kamp.vinner}", fontSize = 13.sp, color = OsloDarkGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(
            onClick = { navController.navigate("tournament_setup") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = OsloDarkBlue)
        ) { Text("➕ Legg til flere spillere") }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PallRad(emoji: String, plass: String, navn: String?, farge: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = farge.copy(alpha = 0.2f))
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(plass, fontSize = 12.sp, color = Color.Gray)
                Text(navn ?: "—", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OsloText)
            }
        }
    }
}

@Composable
fun KampKort(
    kamp: TournamentState.Kamp,
    erNeste: Boolean,
    kanAngre: Boolean = false,
    onAngre: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(if (erNeste) Modifier.border(2.dp, OsloWarmBlue, RoundedCornerShape(12.dp)) else Modifier),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                kamp.vinner != null -> Color(0xFFE8F5E9)
                erNeste -> Color(0xFFE3F2FD)
                else -> OsloWhite
            }
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    kamp.lag1,
                    fontWeight = if (kamp.vinner == kamp.lag1) FontWeight.Bold else FontWeight.Normal,
                    color = if (kamp.vinner == kamp.lag1) OsloDarkGreen else OsloText,
                    fontSize = 14.sp
                )
                Text(
                    if (kamp.erEkstraKamp) "vs\n⚠️ Ekstra kamp" else "vs",
                    fontSize = 11.sp,
                    color = if (kamp.erEkstraKamp) Color(0xFFF9A825) else Color.Gray
                )
                Text(
                    kamp.lag2,
                    fontWeight = if (kamp.vinner == kamp.lag2) FontWeight.Bold else FontWeight.Normal,
                    color = if (kamp.vinner == kamp.lag2) OsloDarkGreen else OsloText,
                    fontSize = 14.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                when {
                    kamp.lag2 == "Bye" -> Text("Bye ✓", color = Color.Gray, fontSize = 13.sp)
                    erNeste -> Text("▶ Nå", color = OsloWarmBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    kamp.vinner != null -> Text("✓ ${kamp.vinner}", color = OsloDarkGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    else -> Text("Venter", color = Color.Gray, fontSize = 13.sp)
                }

                if (kanAngre) {
                    IconButton(onClick = onAngre, modifier = Modifier.size(32.dp)) {
                        Text("↩️", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}