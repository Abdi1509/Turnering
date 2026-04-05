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
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

val OsloDarkBlue = Color(0xFF2A2859)
val OsloWarmBlue = Color(0xFF1F42AA)
val OsloDarkGreen = Color(0xFF034B45)
val OsloTurquoise = Color(0xFF6FE9FF)
val OsloText = Color(0xFF2C2C2C)
val OsloWhite = Color(0xFFFFFFFF)

private val uteAktiviteter = listOf(
    "⚽ Fotball", "🏀 Basketball",
    "🏸 Badminton", "🏃 Stafettløp", "🪵 Kubb",
    "🥏 Frisbee", "🧗 Hinderløype",
    "🎯 Bocce", "🦘 Hoppetau",
    "🥅 Straffekonk"
)

private val inneAktiviteter = listOf(
    "🏓 Bordtennis", "🎯 Dart", "🎳 Bowling",
    "🎱 Biljard", "♟️ Sjakk", "🎲 Ludo",
    "⚽ Foosball", "🃏 Kortspill", "🎮 Fifa"
)

@Composable
fun SetupScreen(navController: NavController) {
    var forslag by remember { mutableStateOf(TournamentState.sisteForslag) }
    var erUte by remember { mutableStateOf(TournamentState.erUte) }
    var visModus by remember { mutableStateOf<String?>(null) } // "forslag" eller "direkte"

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("turnering_prefs", android.content.Context.MODE_PRIVATE)
    val sisteAktivitet = prefs.getString("siste_aktivitet", null)
    val sisteAntall = prefs.getInt("siste_antall", 0)
    val totaltGavekort = prefs.getInt("totalt_gavekort", 0)
    val sisteGavekort = prefs.getInt("siste_gavekort", 0)
    val antallTurneringer = prefs.getInt("antall_turneringer", 0)
    val scope = rememberCoroutineScope()
    var visSpinner by remember { mutableStateOf(false) }
    var spinnerTekst by remember { mutableStateOf("") }
    val rotasjon = remember { Animatable(0f) }

    fun nyttForslag() {
        scope.launch {
            visSpinner = true
            val liste = if (erUte == true) uteAktiviteter else inneAktiviteter


            launch {
                repeat(20) {
                    spinnerTekst = liste.random()
                    delay(100) // hvor fort navnene bytter
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
    fun lagStatistikkPdf(
        context: android.content.Context,
        antallTurneringer: Int,
        sisteGavekort: Int,
        totaltGavekort: Int,
        sisteAktivitet: String?,
        sisteAntall: Int
    ) {
        val pdf = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdf.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        // Bakgrunn tittel
        paint.color = android.graphics.Color.rgb(42, 40, 89)
        canvas.drawRect(0f, 0f, 595f, 120f, paint)

        // Tittel tekst
        paint.color = android.graphics.Color.WHITE
        paint.textSize = 28f
        paint.isFakeBoldText = true
        canvas.drawText("Turneringsstatistikk", 40f, 60f, paint)

        paint.textSize = 14f
        paint.isFakeBoldText = false
        canvas.drawText("Team B fritidsklubb", 40f, 90f, paint)

        // Dato
        val dato = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
            .format(java.util.Date())
        paint.textSize = 12f
        paint.color = android.graphics.Color.rgb(180, 180, 180)
        canvas.drawText("Generert: $dato", 40f, 108f, paint)

        // Statistikk
        paint.color = android.graphics.Color.rgb(44, 44, 44)
        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText("📊 Statistikk", 40f, 170f, paint)

        paint.color = android.graphics.Color.rgb(200, 200, 200)
        canvas.drawLine(40f, 180f, 555f, 180f, paint)

        paint.color = android.graphics.Color.rgb(44, 44, 44)
        paint.textSize = 14f
        paint.isFakeBoldText = false

        canvas.drawText("Antall turneringer gjennomført:", 40f, 210f, paint)
        paint.isFakeBoldText = true
        canvas.drawText("$antallTurneringer", 400f, 210f, paint)

        paint.isFakeBoldText = false
        canvas.drawText("Gavekort delt ut siste turnering:", 40f, 240f, paint)
        paint.isFakeBoldText = true
        canvas.drawText("$sisteGavekort", 400f, 240f, paint)

        paint.isFakeBoldText = false
        canvas.drawText("Gavekort delt ut totalt:", 40f, 270f, paint)
        paint.isFakeBoldText = true
        canvas.drawText("$totaltGavekort", 400f, 270f, paint)

        // Siste turnering
        if (sisteAktivitet != null) {
            paint.color = android.graphics.Color.rgb(44, 44, 44)
            paint.textSize = 18f
            paint.isFakeBoldText = true
            canvas.drawText("📋 Siste turnering", 40f, 330f, paint)

            paint.color = android.graphics.Color.rgb(200, 200, 200)
            canvas.drawLine(40f, 340f, 555f, 340f, paint)

            paint.color = android.graphics.Color.rgb(44, 44, 44)
            paint.textSize = 14f
            paint.isFakeBoldText = false
            canvas.drawText("Aktivitet:", 40f, 370f, paint)
            paint.isFakeBoldText = true
            canvas.drawText(sisteAktivitet, 200f, 370f, paint)

            paint.isFakeBoldText = false
            canvas.drawText("Antall deltakere:", 40f, 400f, paint)
            paint.isFakeBoldText = true
            canvas.drawText("$sisteAntall", 200f, 400f, paint)
        }

        // Footer
        paint.color = android.graphics.Color.rgb(42, 40, 89)
        canvas.drawRect(0f, 800f, 595f, 842f, paint)
        paint.color = android.graphics.Color.WHITE
        paint.textSize = 11f
        paint.isFakeBoldText = false
        canvas.drawText("Oslo kommune — Team B fritidsklubb", 40f, 826f, paint)

        pdf.finishPage(page)

        // Lagre og åpne
        val fil = File(context.cacheDir, "statistikk_$dato.pdf")
        pdf.writeTo(FileOutputStream(fil))
        pdf.close()

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            fil
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Åpne PDF"))
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFF2F2F2)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gradient header
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
                Text(text = "🏆", fontSize = 56.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Turneringsgenerator",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Team B fritidsklubb",
                    fontSize = 14.sp,
                    color = OsloTurquoise
                )
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hva vil du gjøre?
            Text(
                "Hva vil du gjøre idag?",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = OsloText,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // To store valg-kort
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
                    elevation = CardDefaults.cardElevation(2.dp),
                    onClick = {
                        visModus = "forslag"
                        forslag = ""

                        scope.launch {
                            delay(100) // gi UI tid til å tegne boksen
                            scrollState.animateScrollTo(600) // juster denne!
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
                    elevation = CardDefaults.cardElevation(2.dp),
                    onClick = {
                        visModus = "direkte"
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
                                },
                                modifier = Modifier.weight(1f).height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (erUte == true) OsloDarkGreen else Color.LightGray
                                )
                            ) { Text("🌤 Ute", color = OsloWhite, fontWeight = FontWeight.Bold) }

                            Button(
                                onClick = {
                                    erUte = false
                                    TournamentState.erUte = false
                                    nyttForslag()
                                },
                                modifier = Modifier.weight(1f).height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (erUte == false) OsloWarmBlue else Color.LightGray
                                )
                            ) { Text("🏠 Inne", color = OsloWhite, fontWeight = FontWeight.Bold) }
                        }
                    }
                }

// Spin-animasjon state

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
                                // Spinner animasjon
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .rotate(rotasjon.value),
                                    contentAlignment = Alignment.Center
                                ) { /*🎡*/
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
                                onClick = {
                                    scope.launch {
                                        visSpinner = true
                                        // Spinner snurrer og bytter aktiviteter raskt
                                        val liste = if (erUte == true) uteAktiviteter else inneAktiviteter
                                        // Lander på endelig forslag
                                        val endelig = liste.random()
                                        forslag = endelig
                                        TournamentState.sisteForslag = endelig
                                        visSpinner = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = OsloDarkBlue)
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
                                    modifier = Modifier.fillMaxWidth().height(52.dp),
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
            // Statistikk
            if (antallTurneringer > 0) {
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
                            StatKolonne("🏆", "$antallTurneringer", "Turneringer")
                            StatKolonne("🎁", "$sisteGavekort", "Gavekort sist")
                            StatKolonne("🎁", "$totaltGavekort", "Gavekort totalt")
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(
                            onClick = {
                                /*lagStatistikkPdf(
                                    context,
                                    antallTurneringer,
                                    sisteGavekort,
                                    totaltGavekort,
                                    sisteAktivitet,
                                    sisteAntall
                                )*/
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("📄 Last ned statistikk som PDF", color = OsloDarkBlue, fontSize = 13.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Siste turnering
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
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE8F0FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📋", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Siste turnering", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                sisteAktivitet,
                                fontWeight = FontWeight.Bold,
                                color = OsloText,
                                fontSize = 15.sp
                            )
                            Text("$sisteAntall deltakere", fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
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