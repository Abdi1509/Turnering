package com.abdirahman.fritidsklubb.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Oslo kommune farger
val OsloDarkBlue = Color(0xFF2A2859)
val OsloWarmBlue = Color(0xFF1F42AA)
val OsloDarkGreen = Color(0xFF034B45)
val OsloTurquoise = Color(0xFF6FE9FF)
val OsloGreen = Color(0xFF43F8B6)
val OsloBackground = Color(0xFFF9F9F9)
val OsloText = Color(0xFF2C2C2C)
val OsloWhite = Color(0xFFFFFFFF)

private val outdoorActivities = listOf(
    "⚽ Football", "🏀 Basketball", "🏐 Volleyball",
    "🏸 Badminton", "🏃 Relay Race", "🪵 Kubb",
    "🥏 Ultimate Frisbee", "🧗 Obstacle Course",
    "🚴 Cycling", "🎯 Bocce"
)

private val indoorActivities = listOf(
    "🏓 Table Tennis", "🎯 Darts", "🎳 Bowling",
    "🎱 Billiards", "♟️ Chess", "🎲 Board Games",
    "🥌 Curling", "🎾 Squash", "⚽ Foosball", "🃏 Card Games"
)

@Composable
fun SetupScreen() {
    var playerCount by remember { mutableStateOf(6) }
    var isOutdoor by remember { mutableStateOf<Boolean?>(null) }
    var suggestion by remember { mutableStateOf("") }

    fun newSuggestion() {
        val list = if (isOutdoor == true) outdoorActivities else indoorActivities
        suggestion = list.random()
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
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🏆",
                    fontSize = 48.sp
                )
                Text(
                    text = "Turnering generator",
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

        Spacer(modifier = Modifier.height(24.dp))

        // Antall spillere
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OsloWhite),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Antall spillere",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = OsloText
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { if (playerCount > 2) playerCount-- },
                        colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("−", fontSize = 22.sp, color = OsloWhite) }

                    Text(
                        text = playerCount.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = OsloDarkBlue
                    )

                    Button(
                        onClick = { if (playerCount < 20) playerCount++ },
                        colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("+", fontSize = 22.sp, color = OsloWhite) }
                }
                Text(
                    text = "${playerCount * (playerCount - 1) / 2} matches total",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ute / Inne
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OsloWhite),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Activity type",
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
                        onClick = { isOutdoor = true; newSuggestion() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isOutdoor == true) OsloDarkGreen else Color.LightGray
                        )
                    ) { Text("🌤 Utendørs", color = if (isOutdoor == true) OsloWhite else OsloText) }

                    Button(
                        onClick = { isOutdoor = false; newSuggestion() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isOutdoor == false) OsloWarmBlue else Color.LightGray
                        )
                    ) { Text("🏠 Innedørs", color = if (isOutdoor == false) OsloWhite else OsloText) }
                }
            }
        }

        // Forslag
        if (suggestion.isNotEmpty()) {
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
                        text = "Suggested activity",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = suggestion,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = OsloText
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { newSuggestion() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OsloDarkBlue)
                    ) { Text("🔀 Try another") }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TournamentScreen kommer */ },
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