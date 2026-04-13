package com.abdirahman.fritidsklubb.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import com.abdirahman.fritidsklubb.screens.TournamentState.antallRunder
import kotlin.math.ceil

@Composable
fun TournamentSetupScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("turnering_prefs", Context.MODE_PRIVATE)
    var visMinimumDialog by remember { mutableStateOf(false) }
    var lagModus by remember { mutableStateOf<String?>(null) } // "tilfeldig" eller "manuell"
    var format by remember { mutableStateOf("elimination") }
    var antallRunder by remember { mutableStateOf(3) }
    val lagredeSpillere = prefs.getString("spillere", "")
        ?.split(",")
        ?.filter { it.isNotBlank() }
        ?: emptyList()

    var players by remember { mutableStateOf(lagredeSpillere) }
    var newPlayer by remember { mutableStateOf("") }
    var groupSize by remember { mutableStateOf(prefs.getInt("gruppestørrelse", 1)) }

    // Manuell lag-bygging
    var lagNavn by remember { mutableStateOf("") }
    var manuelleLag by remember { mutableStateOf(listOf<Pair<String, List<String>>>()) } // lagNavn til spillere
    var valgteLagIndex by remember { mutableStateOf<Int?>(null) }

    fun lagreSpillere(liste: List<String>) {
        prefs.edit()
            .putString("spillere", liste.joinToString(","))
            .putInt("gruppestørrelse", groupSize)
            .apply()
    }

    val antallLag = if (players.isEmpty()) 0 else ceil(players.size.toDouble() / groupSize).toInt()

    if (visMinimumDialog) {
        AlertDialog(
            onDismissRequest = { visMinimumDialog = false },
            title = { Text("For få deltakere") },
            text = { Text("Du må ha minimum 2 lag for å starte en turnering!") },
            confirmButton = {
                Button(
                    onClick = { visMinimumDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
                ) { Text("OK", color = OsloWhite) }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF2F2F2)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(colors = listOf(OsloDarkBlue, OsloWarmBlue)))
                .padding(top = 30.dp, bottom = 32.dp),
        ) {
            // Tilbakeknapp
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tilbake",
                    tint = OsloWhite
                )
            }

            // Sentrert innhold (det du allerede har)
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("👥", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Sett opp deltakere",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite,
                    textAlign = TextAlign.Center
                )
                Text(TournamentState.aktivitet, fontSize = 14.sp, color = OsloTurquoise)
            }
        }

        // Format-valg
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OsloWhite),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Turneringsformat", fontWeight = FontWeight.Bold, color = OsloText, fontSize = 16.sp)
                Text("Velg hvordan turneringen skal fungere", fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (format == "elimination") OsloDarkBlue else Color(0xFFF2F2F2))
                            .clickable { format = "elimination" }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🏆", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Utslagsturnering",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (format == "elimination") OsloWhite else OsloText,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "Tap = ute",
                                fontSize = 11.sp,
                                color = if (format == "elimination") OsloWhite.copy(alpha = 0.8f) else Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (format == "poeng") OsloWarmBlue else Color(0xFFF2F2F2))
                            .clickable { format = "poeng" }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🏅", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Poengbasert",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (format == "poeng") OsloWhite else OsloText,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "Alle spiller alle runder",
                                fontSize = 11.sp,
                                color = if (format == "poeng") OsloWhite.copy(alpha = 0.8f) else Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Antall runder hvis poengbasert
                if (format == "poeng") {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Antall runder", fontWeight = FontWeight.Bold, color = OsloText, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(1, 2, 3, 4, 5, 6).forEach { antall ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (antallRunder == antall) OsloWarmBlue else Color(0xFFF2F2F2))
                                    .clickable { antallRunder = antall }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "$antall",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (antallRunder == antall) OsloWhite else OsloText
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Legg til spillere
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = OsloWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Legg til deltakere", fontWeight = FontWeight.Bold, color = OsloText, fontSize = 16.sp)
                    Text("Skriv inn alle som skal være med", fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newPlayer,
                            onValueChange = { newPlayer = it },
                            label = { Text("Navn på deltaker") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                if (newPlayer.isNotBlank()) {
                                    val oppdatert = players + newPlayer.trim()
                                    players = oppdatert
                                    lagreSpillere(oppdatert)
                                    newPlayer = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OsloDarkGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null, tint = OsloWhite)
                        }
                    }
                }
            }

            // Spillerliste
            if (players.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = OsloWhite),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${players.size} deltakere", fontWeight = FontWeight.Bold, color = OsloText)
                            TextButton(onClick = {
                                players = emptyList()
                                lagreSpillere(emptyList())
                            }) {
                                Text("🗑 Fjern alle", color = OsloWarmBlue, fontSize = 13.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        players.forEachIndexed { index, navn ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFE8F0FE)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("${index + 1}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OsloDarkBlue)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(navn, color = OsloText, fontSize = 15.sp)
                                }
                                IconButton(
                                    onClick = {
                                        val oppdatert = players.toMutableList().also { it.removeAt(index) }
                                        players = oppdatert
                                        lagreSpillere(oppdatert)
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text("✕", color = Color.Gray, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Velg lagmodus
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = OsloWhite),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Hvordan vil dere lage lag?", fontWeight = FontWeight.Bold, color = OsloText, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Tilfeldig
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (lagModus == "tilfeldig") OsloDarkGreen else Color(0xFFF2F2F2))
                                    .border(1.dp, if (lagModus == "tilfeldig") OsloDarkGreen else Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                                    .clickable { lagModus = "tilfeldig" }
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🎲", fontSize = 28.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "Tilfeldig",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (lagModus == "tilfeldig") OsloWhite else OsloText
                                    )
                                    Text(
                                        "Appen fordeler lagene",
                                        fontSize = 11.sp,
                                        color = if (lagModus == "tilfeldig") OsloWhite.copy(alpha = 0.8f) else Color.Gray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            // Manuell
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (lagModus == "manuell") OsloWarmBlue else Color(0xFFF2F2F2))
                                    .border(1.dp, if (lagModus == "manuell") OsloWarmBlue else Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                                    .clickable { lagModus = "manuell" }
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("✋", fontSize = 28.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "Velg selv",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (lagModus == "manuell") OsloWhite else OsloText
                                    )
                                    Text(
                                        "Ungdommene velger lag",
                                        fontSize = 11.sp,
                                        color = if (lagModus == "manuell") OsloWhite.copy(alpha = 0.8f) else Color.Gray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                // Tilfeldig-modus
                if (lagModus == "tilfeldig") {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = OsloWhite),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("Lagstørrelse", fontWeight = FontWeight.Bold, color = OsloText, fontSize = 16.sp)
                            Text("Hvor mange spiller på samme lag?", fontSize = 13.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(1, 2, 3, 4, 5).forEach { størrelse ->
                                    val erValgt = groupSize == størrelse
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (erValgt) OsloDarkBlue else Color(0xFFF2F2F2))
                                            .border(1.dp, if (erValgt) OsloDarkBlue else Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
                                            .clickable {
                                                groupSize = størrelse
                                                lagreSpillere(players)
                                            }
                                            .padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("$størrelse", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = if (erValgt) OsloWhite else OsloText)
                                            Text(if (størrelse == 1) "person" else "pers.", fontSize = 10.sp, color = if (erValgt) OsloWhite.copy(alpha = 0.8f) else Color.Gray)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            val grupper = players.shuffled().chunked(groupSize)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FE))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("📋 Forhåndsvisning — ${grupper.size} lag", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = OsloDarkBlue)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    grupper.forEachIndexed { index, gruppe ->
                                        val navn = if (groupSize == 1) gruppe[0] else "Lag ${index + 1}: ${gruppe.joinToString(", ")}"
                                        Text("• $navn", fontSize = 13.sp, color = OsloText, modifier = Modifier.padding(vertical = 2.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // Manuell-modus
                if (lagModus == "manuell") {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = OsloWhite),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("Lag opp lag", fontWeight = FontWeight.Bold, color = OsloText, fontSize = 16.sp)
                            Text("Trykk på et lag, velg deretter spillere", fontSize = 13.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(12.dp))

                            // Opprett nytt lag
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = lagNavn,
                                    onValueChange = { lagNavn = it },
                                    label = { Text("Lagnavn (valgfritt)") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )
                                Button(
                                    onClick = {
                                        val navn = if (lagNavn.isBlank()) "Lag ${manuelleLag.size + 1}" else lagNavn.trim()
                                        manuelleLag = manuelleLag + Pair(navn, emptyList())
                                        lagNavn = ""
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = OsloWarmBlue),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(56.dp)
                                ) {
                                    Icon(Icons.Filled.Add, contentDescription = null, tint = OsloWhite)
                                }
                            }

                            if (manuelleLag.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(16.dp))

                                // Vis lagene
                                manuelleLag.forEachIndexed { lagIdx, (navn, spillere) ->
                                    val erValgt = valgteLagIndex == lagIdx
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .border(
                                                2.dp,
                                                if (erValgt) OsloWarmBlue else Color.Transparent,
                                                RoundedCornerShape(12.dp)
                                            )
                                            .clickable { valgteLagIndex = if (erValgt) null else lagIdx },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (erValgt) Color(0xFFE3F2FD) else Color(0xFFF9F9F9)
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    navn,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (erValgt) OsloWarmBlue else OsloText,
                                                    fontSize = 15.sp
                                                )
                                                Row {
                                                    if (erValgt) Text("▲ Lukk", fontSize = 12.sp, color = OsloWarmBlue)
                                                    else Text("▼ Velg spillere", fontSize = 12.sp, color = Color.Gray)
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        "✕",
                                                        fontSize = 14.sp,
                                                        color = Color.Gray,
                                                        modifier = Modifier.clickable {
                                                            manuelleLag = manuelleLag.toMutableList().also { it.removeAt(lagIdx) }
                                                            if (valgteLagIndex == lagIdx) valgteLagIndex = null
                                                        }
                                                    )
                                                }
                                            }
                                            if (spillere.isNotEmpty()) {
                                                Text(spillere.joinToString(", "), fontSize = 13.sp, color = Color.Gray)
                                            }

                                            // Velg spillere til dette laget
                                            if (erValgt) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                                val alleredeILag = manuelleLag.flatMap { it.second }.toSet()
                                                players.forEach { spiller ->
                                                    val erILag = spillere.contains(spiller)
                                                    val erIAndre = alleredeILag.contains(spiller) && !erILag
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(vertical = 2.dp)
                                                            .clip(RoundedCornerShape(8.dp))
                                                            .background(
                                                                when {
                                                                    erILag -> Color(0xFFE8F5E9)
                                                                    erIAndre -> Color(0xFFF5F5F5)
                                                                    else -> Color.Transparent
                                                                }
                                                            )
                                                            .clickable(enabled = !erIAndre) {
                                                                val nySpillere = if (erILag) {
                                                                    spillere - spiller
                                                                } else {
                                                                    spillere + spiller
                                                                }
                                                                manuelleLag = manuelleLag.toMutableList().also {
                                                                    it[lagIdx] = Pair(navn, nySpillere)
                                                                }
                                                            }
                                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            if (erILag) "✓" else if (erIAndre) "—" else "○",
                                                            color = when {
                                                                erILag -> OsloDarkGreen
                                                                erIAndre -> Color.LightGray
                                                                else -> Color.Gray
                                                            },
                                                            fontSize = 14.sp
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(
                                                            spiller,
                                                            fontSize = 14.sp,
                                                            color = if (erIAndre) Color.LightGray else OsloText
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (players.size < 2) {
                        visMinimumDialog = true
                    } else {
                        val grupper = when (lagModus) {
                            "manuell" -> manuelleLag.filter { it.second.isNotEmpty() }.map { (navn, spillere) ->
                                if (spillere.size == 1) spillere[0] else "$navn: ${spillere.joinToString(", ")}"
                            }
                            else -> players.shuffled().chunked(groupSize).mapIndexed { index, gruppe ->
                                if (groupSize == 1) gruppe[0] else "Lag ${index + 1}: ${gruppe.joinToString(", ")}"
                            }
                        }
                        if (format == "poeng") {
                            TournamentState.antallRunder = antallRunder
                            TournamentState.startTurnering(grupper)
                            navController.navigate("poeng_turnering")
                        } else {
                            TournamentState.startTurnering(grupper)
                            navController.navigate("bracket")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue),
                enabled = players.isNotEmpty()
            ) {
                Text(
                    when {
                        players.isEmpty() -> "Legg til deltakere først"
                        lagModus == "manuell" -> "▶ Start turnering med ${manuelleLag.filter { it.second.isNotEmpty() }.size} lag"
                        lagModus == "tilfeldig" -> "▶ Start turnering med $antallLag lag"
                        else -> "▶ Start turnering med ${players.size} deltakere"
                    },
                    color = OsloWhite,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}