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
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = regler.emoji, fontSize = 48.sp)
                Text(
                    text = regler.navn,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OsloWhite
                )
                Text(text = regler.type, fontSize = 14.sp, color = OsloTurquoise)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Beskrivelse
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = OsloWhite),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("📋 Om aktiviteten", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OsloText)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = regler.beskrivelse, fontSize = 15.sp, color = OsloText, lineHeight = 22.sp)
            }
        }

        // Utstyr
        if (regler.utstyr.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("🎒 Du trenger", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OsloText)
                    Spacer(modifier = Modifier.height(8.dp))
                    regler.utstyr.forEach { utstyr ->
                        Row(modifier = Modifier.padding(vertical = 3.dp)) {
                            Text("• ", color = OsloDarkBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(utstyr, fontSize = 15.sp, color = OsloText)
                        }
                    }
                }
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
                Text("📜 Regler", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OsloText)
                Spacer(modifier = Modifier.height(8.dp))
                regler.regler.forEachIndexed { index, regel ->
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(
                            "${index + 1}. ",
                            fontWeight = FontWeight.Bold,
                            color = OsloDarkBlue,
                            fontSize = 15.sp
                        )
                        Text(text = regel, fontSize = 15.sp, color = OsloText, lineHeight = 22.sp)
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
                Text("💡 Tips", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OsloDarkGreen)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = regler.tips, fontSize = 15.sp, color = OsloText, lineHeight = 22.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = OsloDarkBlue)
        ) {
            Text("← Tilbake til oppsett", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = OsloWhite)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

data class AktivitetRegler(
    val navn: String,
    val emoji: String,
    val type: String,
    val beskrivelse: String,
    val utstyr: List<String>,
    val regler: List<String>,
    val tips: String
)

fun hentRegler(aktivitet: String): AktivitetRegler {
    return when {
        aktivitet.contains("Fotball", ignoreCase = true) -> AktivitetRegler(
            navn = "Fotball",
            emoji = "⚽",
            type = "Ute • Lagsport",
            beskrivelse = "Fotball er en lagidrett der to lag konkurrerer om å score flest mål.",
            utstyr = listOf("Fotball", "Kjegler eller mål"),
            regler = listOf(
                "To lag spiller mot hverandre.",
                "Målet er å score flest mål.",
                "Ingen bruk av hender — kun føtter og hode.",
                "Vinner får 3 poeng, uavgjort gir 1 poeng til begge.",
                "Tap gir 0 poeng."
            ),
            tips = "Sett en tidsbegrensning per kamp, f.eks. 10 minutter, så rekker dere gjennom hele turneringen."
        )
        aktivitet.contains("Basketball", ignoreCase = true) && !aktivitet.contains("kurven", ignoreCase = true) -> AktivitetRegler(
            navn = "Basketball",
            emoji = "🏀",
            type = "Ute • Lagsport",
            beskrivelse = "Basketball er en lagidrett der to lag prøver å kaste ballen i motstanderens kurv.",
            utstyr = listOf("Basketball", "Basketballkurv eller kasse"),
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
            type = "Inne • 1 vs 1",
            beskrivelse = "Bordtennis spilles en mot en eller to mot to over et nett på et bord.",
            utstyr = listOf("Bordtennisbord", "Racketer", "Bordtennisball"),
            regler = listOf(
                "Spilles til 11 poeng.",
                "Man må vinne med minst 2 poengs margin.",
                "Server byttes etter hvert 2. poeng.",
                "Ballen må treffe bordet på begge sider ved serve.",
                "Vinner av kampen får 3 poeng i turneringen."
            ),
            tips = "Sett opp flere bord parallelt for å spare tid."
        )
        aktivitet.contains("Volleyball", ignoreCase = true) -> AktivitetRegler(
            navn = "Volleyball",
            emoji = "🏐",
            type = "Ute • Lagsport",
            beskrivelse = "Volleyball er en lagidrett der to lag spiller over et nett.",
            utstyr = listOf("Volleyball", "Nett eller rep"),
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
            type = "Ute/Inne • 1 vs 1",
            beskrivelse = "Badminton spilles med fjærball og racket over et nett.",
            utstyr = listOf("Badmintonracketer", "Fjærball", "Nett"),
            regler = listOf(
                "Spilles til 21 poeng.",
                "Man må vinne med minst 2 poengs margin.",
                "Fjærballen må ikke treffe bakken på din side.",
                "Server byttes når den som server taper poenget.",
                "Vinner får 3 poeng i turneringen."
            ),
            tips = "Innendørs badminton unngår vindproblemer."
        )
        aktivitet.contains("Kubb", ignoreCase = true) -> AktivitetRegler(
            navn = "Kubb",
            emoji = "🪵",
            type = "Ute • Lagsport",
            beskrivelse = "Kubb er et skandinavisk utespill der to lag kaster trepinner for å velte hverandres kubber.",
            utstyr = listOf("Kubb-sett (kubber, kongen og kastepiggene)"),
            regler = listOf(
                "Hvert lag stiller opp 5 kubber på sin baselinje.",
                "Kongen plasseres i midten av banen.",
                "Lagene kaster annenhver tur med kastepiggene.",
                "Kubber som veltes kastes inn på motstanderens halvdel.",
                "Laget som velter alle motstanderens kubber og deretter kongen vinner."
            ),
            tips = "Perfekt for alle aldre! Sett en tidsbegrensning på 15 minutter per kamp."
        )
        aktivitet.contains("Dart", ignoreCase = true) -> AktivitetRegler(
            navn = "Dart",
            emoji = "🎯",
            type = "Inne • 1 vs 1",
            beskrivelse = "Dart er et presisjonsspill der man kaster piler mot en dartskive.",
            utstyr = listOf("Dartskive", "Dartpiler"),
            regler = listOf(
                "Hver spiller starter på 301 poeng.",
                "Man trekker fra poengene man scorer hver runde.",
                "Hver runde består av 3 kast.",
                "Man må komme nøyaktig til 0 for å vinne.",
                "Vinner av kampen får 3 poeng i turneringen."
            ),
            tips = "Sørg for at kasteavstanden er markert på gulvet."
        )
        aktivitet.contains("Sjakk", ignoreCase = true) -> AktivitetRegler(
            navn = "Sjakk",
            emoji = "♟️",
            type = "Inne • 1 vs 1",
            beskrivelse = "Sjakk er et strategispill for to spillere. Målet er å sette motstanderens kongen i sjakkmatt.",
            utstyr = listOf("Sjakkbrett med brikker"),
            regler = listOf(
                "Hvit spiller alltid først.",
                "Hver brikke beveger seg på sin spesielle måte.",
                "Kongen må beskyttes til enhver tid.",
                "Sjakkmatt betyr at kongen ikke kan flytte seg uten å bli tatt.",
                "Uavgjort (remis) gir 1 poeng til begge spillere."
            ),
            tips = "Bruk sjakkur for å begrense tenketid. 5 minutter per spiller passer godt til turneringer."
        )
        aktivitet.contains("Flipp", ignoreCase = true) -> AktivitetRegler(
            navn = "Flipp the Cup",
            emoji = "🥤",
            type = "Inne • Lag eller 1 vs 1",
            beskrivelse = "Et morsomt stafettspill der man skal flippe en kopp fra kanten av bordet slik at den lander opp-ned.",
            utstyr = listOf("Pappkopper", "Bord"),
            regler = listOf(
                "Bestem om det er lag- eller individuell konkurranse.",
                "Bestem hvor mange ganger man må få koppen til å stå.",
                "Koppen skal stå på bordet med bunnen ned, og flips slik at den lander med toppen ned.",
                "Neste deltaker starter når forrige er ferdig.",
                "Førstemann/første lag til å bli ferdig vinner.",
                "Ved lagkamp: hvis antall deltakere er ujevnt, må en deltaker gjøre oppgaven to ganger."
            ),
            tips = "Start leken når dommeren gir tegn. Ha det på et glatt bord for best effekt!"
        )
        aktivitet.contains("tårn", ignoreCase = true) -> AktivitetRegler(
            navn = "Høyeste tårn",
            emoji = "🧱",
            type = "Inne • 1 vs 1",
            beskrivelse = "Deltakerne konkurrerer om å bygge det høyeste tårnet med byggeklosser innen en tidsbegrensning.",
            utstyr = listOf("Byggeklosser (30-40 stk)", "Mobil for å ta tiden"),
            regler = listOf(
                "Bli enige om hvor lang tid deltakerne har (f.eks. 30 sekunder eller 1 minutt).",
                "Fordel byggeklossene i to like store hauger til hver deltaker.",
                "Deltakerne finner seg plass på gulvet med god avstand fra hverandre.",
                "Når dommeren starter tiden er det om å gjøre å bygge høyest mulig tårn.",
                "Hvis tårnet raser kan man bare starte på nytt.",
                "Tårnet MÅ stå i minst 5 sekunder etter at tiden er ute for å bli godkjent.",
                "Høyeste tårn vinner og går videre."
            ),
            tips = "Ha det på gulvet fremfor et bord — da slipper man at noen skumper borti og velter klossene!"
        )
        aktivitet.contains("kurven", ignoreCase = true) -> AktivitetRegler(
            navn = "Pingpong i kurven",
            emoji = "🏀",
            type = "Inne • Lag eller 1 vs 1",
            beskrivelse = "Deltakerne konkurrerer om å kaste flest pingpongballer i en kurv.",
            utstyr = listOf("Pingpongballer (20-30 stk)", "To kurver"),
            regler = listOf(
                "Bestem avstand fra kurven og streken man kaster fra.",
                "Bestem hvor mange baller hver deltaker/lag skal ha.",
                "Lag: Deltakerne må treffe før neste deltaker kan starte.",
                "1 vs 1: Den som har flest baller i kurven etter alle kast vinner.",
                "Ballen MÅ bli liggende i kurven — spretter den ut er det bom.",
                "Første deltaker/lag som er ferdig vinner."
            ),
            tips = "Tips for lag: Den bakerste i rekka kan hjelpe å samle baller hvis lagspilleren bommer. Kast gjerne samtidig i 1 vs 1 for å unngå kaos!"
        )
        aktivitet.contains("kopp", ignoreCase = true) -> AktivitetRegler(
            navn = "Pingpong i kopp",
            emoji = "🏓",
            type = "Inne • Lag, 1 vs 1 eller på tid",
            beskrivelse = "Deltakerne spretter en pingpongball på bordet og prøver å fange den med en kopp.",
            utstyr = listOf("Pappkopper", "Pingpongballer", "Bord", "Mobil for å ta tiden"),
            regler = listOf(
                "Bli enige om tid eller antall ganger (f.eks. 30 sek, 1 min eller 10-20 ganger).",
                "Lag: Deltakerne fanger ballen x antall ganger før neste deltaker starter.",
                "1 vs 1: Førstemann til å fange ballen et bestemt antall ganger vinner.",
                "På tid: Hvem klarer flest fangster på 1 minutt?",
                "Man har lov å flytte koppen med hånda for å fange ballen.",
                "Det er IKKE lov å løfte koppen opp fra bordet for å fange ballen."
            ),
            tips = "Man kan øke vanskelighetsgraden ved å øke avstanden ballen skal sprettes fra."
        )
        aktivitet.contains("Bomba", ignoreCase = true) -> AktivitetRegler(
            navn = "Bomba",
            emoji = "💣",
            type = "Ute/Inne • Gruppelek",
            beskrivelse = "En morsom gruppelek der deltakerne sender en ball rundt i ring mens en nedteller teller ned. Den som holder ballen når nedtelleren sier stopp er ute!",
            utstyr = listOf("En ball"),
            regler = listOf(
                "Deltakerne stiller seg i en ring. Velg en som står i midten som nedteller.",
                "Nedtelleren lukker øynene og teller ned fra et tall (f.eks. 20). De andre skal ikke vite hvilket tall det telles fra.",
                "Bli enige om hvilken vei ballen sendes rundt.",
                "Halvveis i nedtellingen løfter nedtelleren én hånd for å markere ca. halvparten igjen.",
                "Når nedtelleren har telt ferdig roper de STOPP og løfter begge armene.",
                "Den som holder ballen når stopp ropes er ute og setter seg på huk.",
                "Når sidemannen er ute må man gå BAK sidemannen for å overlevere bomben til nestemann.",
                "Sistemann som står igjen er vinneren!"
            ),
            tips = "Deltakerne MÅ gi ballen rolig videre — IKKE kaste! Dette gjør leken mer spennende og rettferdig."
        )
        aktivitet.contains("Monsterball", ignoreCase = true) -> AktivitetRegler(
            navn = "Monsterball",
            emoji = "🌀",
            type = "Ute/Inne • Gruppelek",
            beskrivelse = "En gruppeaktivitet der deltakerne etablerer et kastmønster og konkurrerer om å gjøre det raskest mulig.",
            utstyr = listOf("En ball", "Mobil for å ta tiden"),
            regler = listOf(
                "Deltakerne stiller seg i en ring.",
                "Gi ballen til en deltaker som kaster til en valgfri person. Den som mottar kaster videre til en ny person. IKKE lov å kaste til en som allerede har hatt ballen.",
                "Gjenta til alle har hatt ballen. Gi ballen tilbake til den som startet — dette er mønsteret.",
                "Øv på mønsteret til alle husker det.",
                "Ta tiden på hvor fort gruppa klarer å kaste seg gjennom mønsteret.",
                "Konkurranse: Hver gruppe har 3-5 sjanser. Beste gruppetid vinner!"
            ),
            tips = "Hvis kasting/mottak er vanskelig kan man trille ballen til hverandre. Man kan forlenge aktiviteten ved å kaste seg gjennom mønsteret 2 ganger!"
        )
        aktivitet.contains("tegner", ignoreCase = true) -> AktivitetRegler(
            navn = "Hva tegner du?",
            emoji = "🖍️",
            type = "Inne • Gruppelek",
            beskrivelse = "En tegnelek der deltakerne skal gjette hva den andre tegner — morsomst når man ikke er så flink å tegne!",
            utstyr = listOf("Whiteboard eller store ark", "Tusjer eller kritt", "Deltakere (6-20 stk)"),
            regler = listOf(
                "Del deltakerne inn i to lag.",
                "En deltaker fra laget får se et ord og skal tegne det på tavlen — UTEN å skrive bokstaver eller snakke.",
                "Laget har en tidsbegrensning på å gjette ordet.",
                "Gjetter laget riktig får de poeng.",
                "Lagene bytter på å tegne.",
                "Laget med flest poeng vinner."
            ),
            tips = "Jo vanskeligere ordene er, jo morsommere blir det! Bruk gjerne temaer som mat, filmer, dyr osv."
        )
        aktivitet.contains("Sheriff", ignoreCase = true) -> AktivitetRegler(
            navn = "Sheriff",
            emoji = "👮",
            type = "Inne/Ute • Gruppelek",
            beskrivelse = "En morsom reaksjonslek der deltakerne skal dukke unna sheriffens skudd og kjempe mot naboene!",
            utstyr = listOf("Deltakere (6-20 stk)"),
            regler = listOf(
                "Velg en sheriff. Deltakerne stiller seg i ring rundt sheriffen.",
                "Sheriffen peker på en deltaker og sier 'pang'. Den som blir pekt på MÅ dukke.",
                "Når deltakeren har dukket skal de to som står nærmest krige — førstemann til å peke på den andre og si 'pang' vinner.",
                "Den som sist peker og sier 'pang' er ute av leken.",
                "Dersom personen som blir pekt på IKKE dukker, er de ute.",
                "Dersom noen andre i ringen sier 'pang', er de ute.",
                "Leken fortsetter til kun to deltakere gjenstår.",
                "Sheriffen finner på et kodeord. Deltakerne står med ryggen mot hverandre og tar et steg for hvert ord som sies.",
                "Først når sheriffen roper kodeordet kan de snu seg og skyte. Førstemann vinner og blir ny sheriff!"
            ),
            tips = "Bruk stein, saks, papir hvis man er usikker på hvem som sa 'pang' først!"
        )
        aktivitet.contains("Kortspill", ignoreCase = true) -> AktivitetRegler(
            navn = "Kortspill — turneringsversjon",
            emoji = "🃏",
            type = "Inne • Lag eller individuelt",
            beskrivelse = "Kortspill i turneringsformat — spill Gris, Uno eller Idiot i grupper der vinneren går videre til en superfinalerunde!",
            utstyr = listOf("Kortstokk (vanlig eller Uno)"),
            regler = listOf(
                "Del inn i grupper på maks 3-4 spillere per gruppe.",
                "Avklar hvor mange runder man spiller før man kårer en vinner.",
                "Vinneren av hver gruppe går videre til en 'superfinalerunde' der alle vinnerne møter hverandre.",
                "I superfinalen spiller man EN finalerunde.",
                "Avklar ALLE regler før man starter — mange spiller med ulike regler!",
                "Gris: Vinneren er den som IKKE har fått alle bokstavene G-R-I-S."
            ),
            tips = "Tips: Bytt ut ordet 'gris' med f.eks. 'sau' eller 'ku' for at det skal gå fortere!"
        )
        aktivitet.contains("Stafett", ignoreCase = true) -> AktivitetRegler(
            navn = "Stafettløp",
            emoji = "🏃",
            type = "Ute • Lagsport",
            beskrivelse = "Klassisk stafettløp der lag konkurrerer om å fullføre løypen raskest.",
            utstyr = listOf("Kjegler for å markere banen", "Stafettstav eller pinne (valgfritt)"),
            regler = listOf(
                "Del deltakerne inn i lag.",
                "Hvert lag stiller seg på rekke.",
                "Én løper av gangen — neste starter når forrige er tilbake.",
                "Laget som fullfører raskest vinner.",
                "Vinner får 3 poeng i turneringen."
            ),
            tips = "Varier løypen med hinderløype-elementer for mer moro!"
        )
        aktivitet.contains("Bowling", ignoreCase = true) -> AktivitetRegler(
            navn = "Bowling",
            emoji = "🎳",
            type = "Inne • 1 vs 1",
            beskrivelse = "Keglesport der man ruller en kule for å velte flest mulig kjegler.",
            utstyr = listOf("Bowlingkule", "Kjegler eller flasker", "Rett gulv"),
            regler = listOf(
                "Sett opp 10 kjegler i trekantformasjon.",
                "Hver spiller har 2 kast per runde.",
                "Spilles over 10 runder.",
                "Flest poeng totalt vinner.",
                "Strike = alle kjegler på ett kast = 10 + neste 2 kasts poeng."
            ),
            tips = "Bruk plastflasker halvfyllt med sand eller vann som kjegler hvis dere ikke har bowlingkjegler!"
        )
        aktivitet.contains("Foosball", ignoreCase = true) -> AktivitetRegler(
            navn = "Foosball",
            emoji = "⚽",
            type = "Inne • 1 vs 1 eller Lag",
            beskrivelse = "Bordspill der to spillere styrer minifotballspillere for å score mål.",
            utstyr = listOf("Foosball-bord"),
            regler = listOf(
                "Spilles til 10 mål.",
                "Ingen spinning av stengene.",
                "Ballen må røres av en spiller før mål er gyldig etter utspark.",
                "Vinner av kampen får 3 poeng i turneringen."
            ),
            tips = "Ved lang kø — la vinneren sitte, taperen bytter med neste i køen."
        )
        aktivitet.contains("Fifa", ignoreCase = true) -> AktivitetRegler(
            navn = "FIFA",
            emoji = "🎮",
            type = "Inne • 1 vs 1",
            beskrivelse = "Fotball-videospill der to spillere konkurrerer mot hverandre.",
            utstyr = listOf("Spillkonsoll (PlayStation eller Xbox)", "FIFA-spillet", "TV-skjerm", "2 kontrollere"),
            regler = listOf(
                "Avtal spillinnstillinger på forhånd (vanskelighetsgrad, kamplengde).",
                "1 kamp per runde i turneringen.",
                "Vinner av kampen går videre.",
                "Ved uavgjort etter ordinær tid: straffesparkskonkurranse.",
                "Vinner får 3 poeng i turneringen."
            ),
            tips = "Sett kamplengde til 4-6 minutter per omgang for at turneringen ikke skal ta for lang tid."
        )
        aktivitet.contains("Hoppetau", ignoreCase = true) -> AktivitetRegler(
            navn = "Hoppetau",
            emoji = "🦘",
            type = "Ute • 1 vs 1",
            beskrivelse = "Konkurransehopping med tau der deltakerne konkurrerer om å hoppe flest eller lengst.",
            utstyr = listOf("Hoppetau"),
            regler = listOf(
                "Bestem konkurranseformat: flest hopp på tid, eller hvem hopper lengst uten å snuble.",
                "Hver deltaker har 3 forsøk.",
                "Beste resultat teller.",
                "Vinner går videre i turneringen."
            ),
            tips = "Varier med dobbelt tau eller gruppehopping for ekstra moro!"
        )
        aktivitet.contains("Straffekonk", ignoreCase = true) -> AktivitetRegler(
            navn = "Straffekonkurranse",
            emoji = "🥅",
            type = "Ute • 1 vs 1",
            beskrivelse = "Klassisk straffekonkurranse der deltakerne skyter mot mål fra straffemerket.",
            utstyr = listOf("Fotball", "Mål eller kjegler som mål", "Keeper (valgfritt)"),
            regler = listOf(
                "Hver deltaker tar 5 straffespark.",
                "Flest mål av 5 vinner.",
                "Ved uavgjort: sudden death — én og én til noen bommer.",
                "Vinner går videre i turneringen."
            ),
            tips = "Bruk kjegler som mål hvis dere ikke har et ordentlig mål. Velg keeper fra publikum for mer spenning!"
        )
        aktivitet.contains("Bocce", ignoreCase = true) -> AktivitetRegler(
            navn = "Bocce",
            emoji = "🎯",
            type = "Ute • Lag eller 1 vs 1",
            beskrivelse = "Et presisjonsspill der man kaster boller så nært en liten målkule som mulig.",
            utstyr = listOf("Bocce-sett (boller og pallino)", "Gresslette eller grusbane"),
            regler = listOf(
                "Kast pallino (den lille hvite ballen) ut på banen.",
                "Deltakerne kaster annenhver tur og prøver å lande nærmest pallino.",
                "Laget/spilleren med bollen nærmest pallino scorer.",
                "Man kan slå motstanderens boller vekk.",
                "Første til 12 poeng vinner."
            ),
            tips = "Perfekt for alle aldre og ferdighetsnivåer — veldig sosialt spill!"
        )
        aktivitet.contains("Frisbee", ignoreCase = true) -> AktivitetRegler(
            navn = "Ultimate Frisbee",
            emoji = "🥏",
            type = "Ute • Lagsport",
            beskrivelse = "Lagspill med frisbee der man skal score ved å kaste frisbeen til en medspiller i endesonen.",
            utstyr = listOf("Frisbee", "Kjegler for å markere endesoner"),
            regler = listOf(
                "To lag på 3-7 spillere.",
                "Score ved å kaste frisbeen til en medspiller i motstanderens endesone.",
                "Man kan ikke løpe med frisbeen — stopp opp og kast.",
                "Ved tapt ball bytter motstanderen ballbesittelse.",
                "Første lag til 7 poeng vinner."
            ),
            tips = "Kortere bane og færre spillere fungerer fint for turneringer med mange lag."
        )
        aktivitet.contains("Hinderløype", ignoreCase = true) -> AktivitetRegler(
            navn = "Hinderløype",
            emoji = "🧗",
            type = "Ute • 1 vs 1",
            beskrivelse = "Deltakerne løper gjennom en løype med hindringer så raskt som mulig.",
            utstyr = listOf("Kjegler", "Hoppetau", "Matter eller tepper", "Annet tilgjengelig utstyr"),
            regler = listOf(
                "Sett opp en løype med ulike hindringer.",
                "Én deltaker løper av gangen — ta tiden.",
                "Alle hindringer MÅ fullføres — ingen snarveier.",
                "Raskeste tid vinner.",
                "Vinner går videre i turneringen."
            ),
            tips = "La ungdommene selv være med å designe løypen for mer engasjement!"
        )
        aktivitet.contains("Ludo", ignoreCase = true) || aktivitet.contains("Brettspill", ignoreCase = true) -> AktivitetRegler(
            navn = "Ludo / Brettspill",
            emoji = "🎲",
            type = "Inne • 2-4 spillere",
            beskrivelse = "Klassisk brettspill der spillerne konkurrerer om å få alle brikkene sine hjem.",
            utstyr = listOf("Ludo-brett eller annet brettspill", "Terninger"),
            regler = listOf(
                "Spilles etter standard regler for det valgte spillet.",
                "Vinneren er den første som fullfører spillets mål.",
                "Vinner går videre i turneringen.",
                "Avklar alle regler før spillet starter."
            ),
            tips = "Sett en tidsbegrensning hvis spillet tar for lang tid — den som er nærmest å vinne vinner!"
        )
        aktivitet.contains("Biljard", ignoreCase = true) -> AktivitetRegler(
            navn = "Biljard",
            emoji = "🎱",
            type = "Inne • 1 vs 1",
            beskrivelse = "Presisjonsspill der man bruker en kølle til å dytte baller i hull på bordet.",
            utstyr = listOf("Biljardbordt", "Biljardkuler", "Køller"),
            regler = listOf(
                "Avtal spillformat på forhånd (8-ball anbefales).",
                "Spilles til én spiller har pottet alle sine baller + 8-ballen.",
                "Man bytter tur når man bommer eller potter feil ball.",
                "Vinner går videre i turneringen."
            ),
            tips = "8-ball er det enkleste formatet for nybegynnere."
        )
        else -> AktivitetRegler(
            navn = aktivitet.replace(Regex("^\\S+\\s"), ""),
            emoji = "🎮",
            type = "Aktivitet",
            beskrivelse = "En morsom aktivitet for hele gruppen! Bli enige om reglene før dere starter.",
            utstyr = listOf("Sjekk hva dere trenger for denne aktiviteten"),
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