package com.abdirahman.fritidsklubb.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TournamentSetupScreen(navController: NavController) {
    var players by remember { mutableStateOf(listOf<String>()) }
    var newPlayer by remember { mutableStateOf("") }
    var groupSize by remember { mutableStateOf(1) }

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
                Text("Sett opp lag", fontWeight = FontWeight.Bold, color = OsloWhite)
                Text("Velg grupper og spillere", color = OsloTurquoise)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Gruppestørrelse
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OsloWhite)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Spillere per gruppe", fontWeight = FontWeight.Bold, color = OsloText)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { if (groupSize > 1) groupSize-- },
                        colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
                    ) { Text("−", color = OsloWhite) }

                    Text("$groupSize", fontSize = 28.sp, color = OsloDarkBlue)

                    Button(
                        onClick = { groupSize++ },
                        colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
                    ) { Text("+", color = OsloWhite) }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legg til spiller
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OsloWhite)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                OutlinedTextField(
                    value = newPlayer,
                    onValueChange = { newPlayer = it },
                    label = { Text("Spillernavn") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (newPlayer.isNotBlank()) {
                            players = players + newPlayer
                            newPlayer = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = OsloDarkGreen)
                ) {
                    Text("➕ Legg til spiller", color = OsloWhite)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (players.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = OsloWhite)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Deltakere", fontWeight = FontWeight.Bold, color = OsloText)

                    players.forEach {
                        Text("• $it", color = OsloText)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val grupper = players.chunked(groupSize)
                val lagNavn = grupper.mapIndexed { index, gruppe ->
                    "Lag ${index + 1}: ${gruppe.joinToString(", ")}"
                }

                TournamentState.startTurnering(lagNavn)
                navController.navigate("bracket")
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
        ) {
            Text("▶ Generer turnering", color = OsloWhite)
        }
    }
}