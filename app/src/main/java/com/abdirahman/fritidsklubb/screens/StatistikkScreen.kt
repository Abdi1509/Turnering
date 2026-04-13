package com.abdirahman.fritidsklubb.screens

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import java.io.File
import java.io.FileOutputStream

@Composable
fun StatistikkScreen(navController: NavController) {
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(colors = listOf(OsloDarkBlue, OsloWarmBlue)))
                .padding(top = 56.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📊", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Statistikk",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite,
                    textAlign = TextAlign.Center
                )
                Text("Team B fritidsklubb", fontSize = 14.sp, color = OsloTurquoise)
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {

            if (antallTurneringer == 0) {
                Spacer(modifier = Modifier.height(48.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📭", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Ingen statistikk ennå", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = OsloText)
                    Text("Gjennomfør en turnering for å se statistikk her", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
                }
            } else {

                // Oversiktskort
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatKortHjem("🏆", "$antallTurneringer", "Turneringer", Modifier.weight(1f))
                    StatKortHjem("🎁", "$totaltGavekort", "Gavekort totalt", Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatKortHjem("🎁", "$sisteGavekort", "Gavekort sist", Modifier.weight(1f))
                    StatKortHjem("👥", "$sisteAntall", "Deltakere sist", Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Siste turnering
                if (sisteAktivitet != null) {
                    Text("📋 Siste turnering", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = OsloText)
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = OsloWhite),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(sisteAktivitet, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = OsloText)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("👥 $sisteAntall deltakere", fontSize = 14.sp, color = Color.Gray)
                            Text("🎁 $sisteGavekort gavekort delt ut", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // PDF-knapp
                Button(
                    onClick = {
                        lagStatistikkPdf(
                            context, antallTurneringer, sisteGavekort,
                            totaltGavekort, sisteAktivitet, sisteAntall
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
                ) {
                    Text("📄 Last ned statistikk som PDF", color = OsloWhite, fontWeight = FontWeight.Bold)
                }
            }
        }
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

    paint.color = android.graphics.Color.rgb(42, 40, 89)
    canvas.drawRect(0f, 0f, 595f, 120f, paint)

    paint.color = android.graphics.Color.WHITE
    paint.textSize = 28f
    paint.isFakeBoldText = true
    canvas.drawText("Turneringsstatistikk", 40f, 60f, paint)

    paint.textSize = 14f
    paint.isFakeBoldText = false
    canvas.drawText("Team B fritidsklubb", 40f, 90f, paint)

    val dato = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()).format(java.util.Date())
    paint.textSize = 12f
    paint.color = android.graphics.Color.rgb(180, 180, 180)
    canvas.drawText("Generert: $dato", 40f, 108f, paint)

    paint.color = android.graphics.Color.rgb(44, 44, 44)
    paint.textSize = 18f
    paint.isFakeBoldText = true
    canvas.drawText("Statistikk", 40f, 170f, paint)

    paint.color = android.graphics.Color.rgb(200, 200, 200)
    canvas.drawLine(40f, 180f, 555f, 180f, paint)

    paint.color = android.graphics.Color.rgb(44, 44, 44)
    paint.textSize = 14f
    paint.isFakeBoldText = false
    canvas.drawText("Antall turneringer:", 40f, 210f, paint)
    paint.isFakeBoldText = true
    canvas.drawText("$antallTurneringer", 400f, 210f, paint)

    paint.isFakeBoldText = false
    canvas.drawText("Gavekort siste turnering:", 40f, 240f, paint)
    paint.isFakeBoldText = true
    canvas.drawText("$sisteGavekort", 400f, 240f, paint)

    paint.isFakeBoldText = false
    canvas.drawText("Gavekort totalt:", 40f, 270f, paint)
    paint.isFakeBoldText = true
    canvas.drawText("$totaltGavekort", 400f, 270f, paint)

    if (sisteAktivitet != null) {
        paint.color = android.graphics.Color.rgb(44, 44, 44)
        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText("Siste turnering", 40f, 330f, paint)

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

    paint.color = android.graphics.Color.rgb(42, 40, 89)
    canvas.drawRect(0f, 800f, 595f, 842f, paint)
    paint.color = android.graphics.Color.WHITE
    paint.textSize = 11f
    paint.isFakeBoldText = false
    canvas.drawText("Oslo kommune — Team B fritidsklubb", 40f, 826f, paint)

    pdf.finishPage(page)

    val fil = File(context.cacheDir, "statistikk_$dato.pdf")
    pdf.writeTo(FileOutputStream(fil))
    pdf.close()

    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", fil)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Åpne PDF"))
}