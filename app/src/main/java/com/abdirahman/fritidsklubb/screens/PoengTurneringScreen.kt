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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class PoengSpiller(
    val navn: String,
    val rundePoeng: MutableList<Int> = mutableListOf()
) {
    val totalPoeng: Int get() = rundePoeng.sum()
}

@Composable
fun PoengTurneringScreen(navController: NavController) {
    val spillere = remember {
        TournamentState.lag.map { PoengSpiller(navn = it) }.toMutableStateList()
    }

    var nåværendeRunde by remember { mutableStateOf(1) }
    var antallRunder by remember { mutableStateOf(TournamentState.antallRunder) }
    var poengInput by remember { mutableStateOf(spillere.map { "" }.toMutableStateList()) }
    var visResultat by remember { mutableStateOf(false) }
    var visAngreDialog by remember { mutableStateOf(false) }

    if (visAngreDialog) {
        AlertDialog(
            onDismissRequest = { visAngreDialog = false },
            title = { Text("Angre siste runde?") },
            text = { Text("Dette vil fjerne poengene fra runde ${nåværendeRunde - 1}.") },
            confirmButton = {
                Button(
                    onClick = {
                        spillere.forEach { it.rundePoeng.removeLastOrNull() }
                        nåværendeRunde--
                        poengInput = spillere.map { "" }.toMutableStateList()
                        visAngreDialog = false
                        visResultat = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
                ) { Text("Ja, angre", color = OsloWhite) }
            },
            dismissButton = {
                OutlinedButton(onClick = { visAngreDialog = false }) { Text("Avbryt") }
            }
        )
    }

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
                Text("🏅", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    if (visResultat) "Sluttresultat!" else "Runde $nåværendeRunde av $antallRunder",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite,
                    textAlign = TextAlign.Center
                )
                Text(TournamentState.aktivitet, fontSize = 14.sp, color = OsloTurquoise)

                if (!visResultat) {
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { (nåværendeRunde - 1).toFloat() / antallRunder.toFloat() },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = Color(0xFFFFD600),
                        trackColor = Color(0x33FFFFFF)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {

            if (visResultat) {
                // Sluttresultat
                val sortert = spillere.sortedByDescending { it.totalPoeng }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🏆 Sluttresultat", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = OsloText)
                        Spacer(modifier = Modifier.height(16.dp))

                        sortert.forEachIndexed { index, spiller ->
                            val medalje = when (index) {
                                0 -> "🥇"
                                1 -> "🥈"
                                2 -> "🥉"
                                else -> "${index + 1}."
                            }
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = when (index) {
                                        0 -> Color(0xFFFFF9C4)
                                        1 -> Color(0xFFF5F5F5)
                                        2 -> Color(0xFFFBE9E7)
                                        else -> OsloWhite
                                    }
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(medalje, fontSize = 24.sp)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(spiller.navn, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OsloText)
                                            Text(
                                                spiller.rundePoeng.mapIndexed { i, p -> "R${i + 1}: $p" }.joinToString(" · "),
                                                fontSize = 11.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                    Text(
                                        "${spiller.totalPoeng} p",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = OsloDarkBlue
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (nåværendeRunde > 1) {
                    OutlinedButton(
                        onClick = { visAngreDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OsloDarkBlue)
                    ) { Text("↩️ Angre siste runde") }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        TournamentState.reset()
                        navController.navigate("oppsett") {
                            popUpTo("oppsett") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
                ) { Text("🔄 Ny turnering", color = OsloWhite, fontWeight = FontWeight.Bold) }

            } else {
                // Løpende tabell
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = OsloWhite),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("📊 Tabell så langt", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OsloText)
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Color(0xFFE0E0E0))
                        Spacer(modifier = Modifier.height(8.dp))

                        if (spillere.all { it.rundePoeng.isEmpty() }) {
                            Text("Ingen runder spilt ennå", color = Color.Gray, fontSize = 14.sp)
                        } else {
                            spillere.sortedByDescending { it.totalPoeng }.forEachIndexed { index, spiller ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            "${index + 1}.",
                                            fontWeight = FontWeight.Bold,
                                            color = OsloDarkBlue,
                                            fontSize = 14.sp,
                                            modifier = Modifier.width(24.dp)
                                        )
                                        Text(spiller.navn, fontSize = 14.sp, color = OsloText)
                                    }
                                    Text(
                                        "${spiller.totalPoeng} p",
                                        fontWeight = FontWeight.Bold,
                                        color = OsloDarkBlue,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Poengregistrering
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = OsloWhite),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Skriv inn poeng for runde $nåværendeRunde",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = OsloText
                        )
                        Text("Fyll inn poengene hver deltaker fikk denne runden", fontSize = 13.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))

                        spillere.forEachIndexed { index, spiller ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    spiller.navn,
                                    modifier = Modifier.weight(1f),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OsloText
                                )
                                OutlinedTextField(
                                    value = poengInput[index],
                                    onValueChange = { ny ->
                                        if (ny.all { it.isDigit() } && ny.length <= 4) {
                                            poengInput[index] = ny
                                        }
                                    },
                                    label = { Text("Poeng") },
                                    modifier = Modifier.width(100.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Angre knapp
                if (nåværendeRunde > 1) {
                    OutlinedButton(
                        onClick = { visAngreDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OsloDarkBlue)
                    ) { Text("↩️ Angre forrige runde") }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        val alleUtfylt = poengInput.all { it.isNotBlank() }
                        if (alleUtfylt) {
                            spillere.forEachIndexed { index, spiller ->
                                spiller.rundePoeng.add(poengInput[index].toInt())
                            }
                            poengInput = spillere.map { "" }.toMutableStateList()

                            if (nåværendeRunde >= antallRunder) {
                                visResultat = true
                            } else {
                                nåværendeRunde++
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (poengInput.all { it.isNotBlank() }) OsloDarkBlue
                        else OsloDarkBlue.copy(alpha = 0.4f)
                    ),
                    enabled = poengInput.all { it.isNotBlank() }
                ) {
                    Text(
                        if (nåværendeRunde >= antallRunder) "🏆 Vis sluttresultat"
                        else "➡ Neste runde (${nåværendeRunde + 1} av $antallRunder)",
                        color = OsloWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}