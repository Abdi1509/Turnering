package com.abdirahman.fritidsklubb.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TournamentSetupScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("turnering_prefs", Context.MODE_PRIVATE)

    // Last inn lagrede spillere
    val lagredeSpillere = prefs.getString("spillere", "")
        ?.split(",")
        ?.filter { it.isNotBlank() }
        ?: emptyList()

    var players by remember { mutableStateOf(lagredeSpillere) }
    var newPlayer by remember { mutableStateOf("") }
    var groupSize by remember { mutableStateOf(prefs.getInt("gruppestørrelse", 1)) }

    fun lagreSpillere(liste: List<String>) {
        prefs.edit()
            .putString("spillere", liste.joinToString(","))
            .putInt("gruppestørrelse", groupSize)
            .apply()
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
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("👥", fontSize = 48.sp)
                Text("Sett opp lag", fontWeight = FontWeight.Bold, color = OsloWhite, fontSize = 20.sp)
                Text("Velg grupper og spillere", color = OsloTurquoise, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Gruppestørrelse
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OsloWhite),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Spillere per gruppe", fontWeight = FontWeight.Bold, color = OsloText)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            if (groupSize > 1) {
                                groupSize--
                                lagreSpillere(players)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("−", color = OsloWhite, fontSize = 18.sp) }

                    Text("$groupSize", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = OsloDarkBlue)

                    Button(
                        onClick = {
                            groupSize++
                            lagreSpillere(players)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("+", color = OsloWhite, fontSize = 18.sp) }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legg til spiller
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OsloWhite),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Legg til deltaker", fontWeight = FontWeight.Bold, color = OsloText)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = newPlayer,
                    onValueChange = { newPlayer = it },
                    label = { Text("Navn") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (newPlayer.isNotBlank()) {
                            val oppdatert = players + newPlayer.trim()
                            players = oppdatert
                            lagreSpillere(oppdatert)
                            newPlayer = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = OsloDarkGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("➕ Legg til", color = OsloWhite)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Spillerliste
        if (players.isNotEmpty()) {
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
                        Text(
                            "Deltakere (${players.size})",
                            fontWeight = FontWeight.Bold,
                            color = OsloText
                        )
                        TextButton(
                            onClick = {
                                players = emptyList()
                                lagreSpillere(emptyList())
                            }
                        ) {
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
                            Text("${index + 1}. $navn", color = OsloText, fontSize = 15.sp)
                            IconButton(
                                onClick = {
                                    val oppdatert = players.toMutableList().also { it.removeAt(index) }
                                    players = oppdatert
                                    lagreSpillere(oppdatert)
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Text("✕", color = OsloWarmBlue, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (players.isNotEmpty()) {
                    val grupper = players.chunked(groupSize)
                    val lagNavn = grupper.mapIndexed { index, gruppe ->
                        if (groupSize == 1) gruppe[0]
                        else "Lag ${index + 1}: ${gruppe.joinToString(", ")}"
                    }
                    TournamentState.startTurnering(lagNavn)
                    navController.navigate("bracket")
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (players.isNotEmpty()) OsloDarkBlue else OsloDarkBlue.copy(alpha = 0.4f)
            ),
            enabled = players.isNotEmpty()
        ) {
            Text("▶ Generer turnering (${players.size} deltakere)", color = OsloWhite, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}