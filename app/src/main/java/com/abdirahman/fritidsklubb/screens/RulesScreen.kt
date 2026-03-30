package com.abdirahman.fritidsklubb.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RulesScreen(aktivitet: String, navController: NavController) {
    val regler = hentRegler(aktivitet)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Tilbake-knapp
        OutlinedButton(
            onClick = { navController.popBackStack() },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = OsloDarkBlue)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Tilbake")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Tilbake")
        }

        Spacer(modifier = Modifier.height(24.dp))

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
                Text(text = regler.emoji, fontSize = 48.sp)
                Text(
                    text = regler.navn,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite
                )
                Text(
                    text = "Regler og forklaring",
                    fontSize = 14.sp,
                    color = OsloTurquoise
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Beskrivelse
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OsloWhite),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "📋 Om aktiviteten",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = OsloText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = regler.beskrivelse,
                    fontSize = 15.sp,
                    color = OsloText,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Regler
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OsloWhite),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "📜 Regler",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = OsloText
                )
                Spacer(modifier = Modifier.height(8.dp))
                regler.regler.forEachIndexed { index, regel ->
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(
                            text = "${index + 1}. ",
                            fontWeight = FontWeight.Bold,
                            color = OsloDarkBlue,
                            fontSize = 15.sp
                        )
                        Text(
                            text = regel,
                            fontSize = 15.sp,
                            color = OsloText,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tips
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "💡 Tips",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = OsloDarkGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = regler.tips,
                    fontSize = 15.sp,
                    color = OsloText,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
        ) {
            Text(
                "← Tilbake til oppsett",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = OsloWhite
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

data class AktivitetRegler(
    val navn: String,
    val emoji: String,
    val beskrivelse: String,
    val regler: List<String>,
    val tips: String
)

fun hentRegler(aktivitet: String): AktivitetRegler {
    return when {
        aktivitet.contains("Fotball", ignoreCase = true) -> AktivitetRegler(
            navn = "Fotball",
            emoji = "⚽",
            beskrivelse = "Fotball er en lagidrett der to lag konkurrerer om å score flest mål. I en turnering spiller alle mot alle, og laget med flest poeng vinner.",
            regler = listOf(
                "To lag spiller mot hverandre.",
                "Målet er å score flest mål.",
                "Ingen bruk av hender — kun føtter og hode.",
                "Vinner får 3 poeng, uavgjort gir 1 poeng til begge.",
                "Tap gir 0 poeng."
            ),
            tips = "Fordel spillere jevnt mellom lagene. Sett en tidsbegrensning per kamp, f.eks. 10 minutter, så rekker dere gjennom hele turneringen."
        )
        aktivitet.contains("Basketball", ignoreCase = true) -> AktivitetRegler(
            navn = "Basketball",
            emoji = "🏀",
            beskrivelse = "Basketball er en lagidrett der to lag prøver å kaste ballen i motstanderens kurv. Perfekt for innendørs turneringer.",
            regler = listOf(
                "To lag spiller mot hverandre.",
                "Ballen scores ved å kaste den i kurven.",
                "Man kan ikke løpe med ballen — man må drible.",
                "Vinner får 3 poeng i tabellen.",
                "Uavgjort gir 1 poeng til begge lag."
            ),
            tips = "Bruk kortere kamptid (5-8 minutter) for å holde energien oppe gjennom hele turneringen."
        )
        aktivitet.contains("Bordtennis", ignoreCase = true) -> AktivitetRegler(
            navn = "Bordtennis",
            emoji = "🏓",
            beskrivelse = "Bordtennis spilles en mot en eller to mot to. Perfekt for innendørs turneringer med mange deltakere.",
            regler = listOf(
                "Spilles til 11 poeng.",
                "Man må vinne med minst 2 poengs margin.",
                "Server byttes etter hvert 2. poeng.",
                "Ballen må treffe bordet på begge sider ved serve.",
                "Vinner av kampen får 3 poeng i turneringen."
            ),
            tips = "Sett opp flere bord parallelt for å spare tid. La taperne spille trøstekamper mens de venter."
        )
        aktivitet.contains("Volleyball", ignoreCase = true) -> AktivitetRegler(
            navn = "Volleyball",
            emoji = "🏐",
            beskrivelse = "Volleyball er en lagidrett der to lag spiller over et nett. Målet er å få ballen til å lande på motstanderens side.",
            regler = listOf(
                "Hvert lag har maks 3 touch før ballen må over nettet.",
                "Man spiller til 25 poeng, men må vinne med 2 poengs margin.",
                "Laget som vinner poenget server neste gang.",
                "Ballen er ute hvis den lander utenfor banen.",
                "Vinner av settet får 3 poeng i turneringen."
            ),
            tips = "For mindre grupper kan dere spille med færre spillere per lag og kortere bane."
        )
        aktivitet.contains("Badminton", ignoreCase = true) -> AktivitetRegler(
            navn = "Badminton",
            emoji = "🏸",
            beskrivelse = "Badminton spilles med fjærball og racket. Kan spilles en mot en eller to mot to over et nett.",
            regler = listOf(
                "Spilles til 21 poeng.",
                "Man må vinne med minst 2 poengs margin.",
                "Fjærballen må ikke treffe bakken på din side.",
                "Server byttes når den som server taper poenget.",
                "Vinner får 3 poeng i turneringen."
            ),
            tips = "Innendørs badminton unngår vindproblemer. Pass på at lokalet er høyt nok til å slå ballen over nettet."
        )
        aktivitet.contains("Kubb", ignoreCase = true) -> AktivitetRegler(
            navn = "Kubb",
            emoji = "🪵",
            beskrivelse = "Kubb er et utendørs spill fra Skandinavia der to lag kaster trepinner for å velte hverandres kubber.",
            regler = listOf(
                "Hvert lag stiller opp 5 kubber på sin baselinje.",
                "Kongen plasseres i midten av banen.",
                "Lagene kaster annenhver tur med kastepiggene.",
                "Kubber som veltes kastes inn på motstanderens halvdel.",
                "Laget som velter alle motstanderens kubber og deretter kongen vinner."
            ),
            tips = "Perfekt for alle aldre! Sett en tidsbegrensning på 15 minutter per kamp hvis dere har mange lag."
        )
        aktivitet.contains("Dart", ignoreCase = true) -> AktivitetRegler(
            navn = "Dart",
            emoji = "🎯",
            beskrivelse = "Dart er et presisjonsspill der man kaster piler mot en dartskive. Spilles en mot en i turneringen.",
            regler = listOf(
                "Hver spiller starter på 301 poeng.",
                "Man trekker fra poengene man scorer hver runde.",
                "Hver runde består av 3 kast.",
                "Man må komme nøyaktig til 0 for å vinne.",
                "Vinner av kampen får 3 poeng i turneringen."
            ),
            tips = "Sørg for at kasteavstanden er markert på gulvet. Ha alltid en dommer som teller poeng."
        )
        aktivitet.contains("Sjakk", ignoreCase = true) -> AktivitetRegler(
            navn = "Sjakk",
            emoji = "♟️",
            beskrivelse = "Sjakk er et strategispill for to spillere. Målet er å sette motstanderens kongen i sjakkmatt.",
            regler = listOf(
                "Hvit spiller alltid først.",
                "Hver brikke beveger seg på sin spesielle måte.",
                "Kongen må beskyttes til enhver tid.",
                "Sjakkmatt betyr at kongen ikke kan flytte seg uten å bli tatt.",
                "Uavgjort (remis) gir 1 poeng til begge spillere."
            ),
            tips = "Bruk sjakkur for å begrense tenketid per spiller. 5 minutter per spiller per kamp er passende for turneringer."
        )
        else -> AktivitetRegler(
            navn = aktivitet.replace(Regex("^\\S+\\s"), ""),
            emoji = "🎮",
            beskrivelse = "En morsom aktivitet for hele gruppen! Bli enige om reglene før dere starter.",
            regler = listOf(
                "Alle deltakere spiller mot hverandre.",
                "Vinner av hver kamp får 3 poeng.",
                "Uavgjort gir 1 poeng til begge.",
                "Tap gir 0 poeng.",
                "Den med flest poeng til slutt vinner turneringen."
            ),
            tips = "Bli enige om reglene før dere starter, og sørg for at alle forstår hvordan man vinner en kamp."
        )
    }
}