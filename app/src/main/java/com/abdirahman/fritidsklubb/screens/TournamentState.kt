package com.abdirahman.fritidsklubb.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object TournamentState {
    var aktivitet: String = ""
    var lag: MutableList<String> = mutableListOf()
    var kamper = mutableStateListOf<Kamp>()
    var runde: Int by mutableStateOf(1)
    var fase: Fase by mutableStateOf(Fase.NORMAL)
    var ekstraSpiller: String? by mutableStateOf(null)
    var sisteForslag: String by mutableStateOf("")
    var erUte: Boolean? by mutableStateOf(null)
    var antallRunder: Int by mutableStateOf(3)
    var visModus: String? by mutableStateOf(null)

    enum class Fase {
        NORMAL, BRONSEKAMP, FINALE, FERDIG
    }

    data class Kamp(
        val id: Int,
        val lag1: String,
        val lag2: String,
        var vinner: String? = null,
        val runde: Int,
        val type: KampType = KampType.NORMAL,
        val erEkstraKamp: Boolean = false,
        val notat: String = ""
    )

    enum class KampType {
        NORMAL, BRONSEKAMP, FINALE, EKSTRA
    }

    fun oppdaterNotat(kampId: Int, notat: String) {
        val index = kamper.indexOfFirst { it.id == kampId }
        if (index != -1) {
            kamper[index] = kamper[index].copy(notat = notat)
        }
    }

    fun startTurnering(lagListe: List<String>) {
        lag = lagListe.toMutableList()
        runde = 1
        fase = Fase.NORMAL
        ekstraSpiller = null
        kamper.clear()
        genererRunde(lag)
    }

    private fun genererRunde(deltakere: List<String>) {
        val blandet = deltakere.shuffled().toMutableList()
        val startId = kamper.size

        if (blandet.size % 2 != 0) {
            ekstraSpiller = blandet.removeLastOrNull()
        } else {
            ekstraSpiller = null
        }

        for (i in blandet.indices step 2) {
            kamper.add(
                Kamp(
                    id = startId + i / 2,
                    lag1 = blandet[i],
                    lag2 = blandet[i + 1],
                    runde = runde
                )
            )
        }
    }

    private fun leggTilEkstraKamp() {
        val ekstra = ekstraSpiller ?: return
        val ferdigeKamper = kamper.filter {
            it.runde == runde &&
                    it.type == KampType.NORMAL &&
                    it.vinner != null
        }
        val tilfeldigKamp = ferdigeKamper.randomOrNull() ?: return
        val motstander = tilfeldigKamp.vinner ?: return

        kamper.add(
            Kamp(
                id = kamper.size,
                lag1 = ekstra,
                lag2 = motstander,
                runde = runde,
                type = KampType.EKSTRA,
                erEkstraKamp = true
            )
        )
        ekstraSpiller = null
    }

    fun registrerVinner(kampId: Int, vinner: String) {
        val index = kamper.indexOfFirst { it.id == kampId }
        if (index != -1) {
            kamper[index] = kamper[index].copy(vinner = vinner)
        }

        val alleVanligeFerdig = kamper
            .filter { it.runde == runde && it.type == KampType.NORMAL }
            .all { it.vinner != null }

        if (alleVanligeFerdig && ekstraSpiller != null) {
            leggTilEkstraKamp()
            return
        }

        sjekkOgOppdaterFase()
    }

    fun angreVinner(kampId: Int) {
        val index = kamper.indexOfFirst { it.id == kampId }
        if (index != -1) {
            val kamp = kamper[index]
            if (kamp.type == KampType.EKSTRA) {
                ekstraSpiller = kamp.lag1
                kamper.removeAt(index)
                return
            }
            kamper[index] = kamp.copy(vinner = null)
            if (fase == Fase.FINALE || fase == Fase.BRONSEKAMP) {
                fase = Fase.BRONSEKAMP
            }
        }
    }

    private fun sjekkOgOppdaterFase() {
        val kamperIDenneRunden = kamper.filter {
            it.runde == runde && (it.type == KampType.NORMAL || it.type == KampType.EKSTRA)
        }
        val alleKlare = kamperIDenneRunden.all { it.vinner != null }
        if (!alleKlare) return

        val ekstraKamp = kamper.firstOrNull { it.runde == runde && it.type == KampType.EKSTRA }

        val vinnereNormal = mutableListOf<String>()
        val tapereNormal = mutableListOf<String>()

        kamper.filter { it.runde == runde && it.type == KampType.NORMAL }.forEach { kamp ->
            val vinner = kamp.vinner ?: return@forEach
            val taper = if (vinner == kamp.lag1) kamp.lag2 else kamp.lag1
            vinnereNormal.add(vinner)
            if (taper != "Bye") tapereNormal.add(taper)
        }

        val endeligeVinnere = mutableListOf<String>()
        val ekstraTaper = mutableListOf<String>()

        if (ekstraKamp != null) {
            val ekstraVinner = ekstraKamp.vinner ?: return
            val ekstraTaperNavn = if (ekstraVinner == ekstraKamp.lag1) ekstraKamp.lag2 else ekstraKamp.lag1

            vinnereNormal.forEach { vinner ->
                if (vinner == ekstraKamp.lag2) {
                    endeligeVinnere.add(ekstraVinner)
                } else {
                    endeligeVinnere.add(vinner)
                }
            }
            ekstraTaper.add(ekstraTaperNavn)
        } else {
            endeligeVinnere.addAll(vinnereNormal)
        }

        when {
            endeligeVinnere.size == 1 -> {
                fase = Fase.FERDIG
            }
            endeligeVinnere.size == 2 -> {
                val bronse1 = tapereNormal.getOrNull(0)
                val bronse2 = tapereNormal.getOrNull(1) ?: ekstraTaper.getOrNull(0)

                fase = Fase.BRONSEKAMP
                val id = kamper.size
                if (bronse1 != null && bronse2 != null) {
                    kamper.add(Kamp(id = id, lag1 = bronse1, lag2 = bronse2, runde = runde, type = KampType.BRONSEKAMP))
                    kamper.add(Kamp(id = id + 1, lag1 = endeligeVinnere[0], lag2 = endeligeVinnere[1], runde = runde, type = KampType.FINALE))
                } else {
                    kamper.add(Kamp(id = id, lag1 = endeligeVinnere[0], lag2 = endeligeVinnere[1], runde = runde, type = KampType.FINALE))
                    fase = Fase.FINALE
                }
            }
            endeligeVinnere.size == 3 -> {
                val blandet = endeligeVinnere.shuffled()
                val ekstraSpillerNeste = blandet[0]
                val semifinaleSpillere = listOf(blandet[1], blandet[2])

                runde++
                kamper.add(
                    Kamp(
                        id = kamper.size,
                        lag1 = semifinaleSpillere[0],
                        lag2 = semifinaleSpillere[1],
                        runde = runde
                    )
                )
                ekstraSpiller = ekstraSpillerNeste
            }
            endeligeVinnere.size > 3 -> {
                runde++
                genererRunde(endeligeVinnere)
            }
        }

        val bronsekamp = kamper.firstOrNull { it.type == KampType.BRONSEKAMP }
        val finale = kamper.firstOrNull { it.type == KampType.FINALE }
        if (bronsekamp?.vinner != null && fase == Fase.BRONSEKAMP) fase = Fase.FINALE
        if (finale?.vinner != null && fase != Fase.FERDIG) fase = Fase.FERDIG
    }

    fun nesteKamp(): Kamp? {
        return when (fase) {
            Fase.NORMAL -> {
                kamper.firstOrNull {
                    it.runde == runde &&
                            it.type == KampType.NORMAL &&
                            it.vinner == null
                } ?: kamper.firstOrNull {
                    it.runde == runde &&
                            it.type == KampType.EKSTRA &&
                            it.vinner == null
                }
            }
            Fase.BRONSEKAMP -> kamper.firstOrNull { it.type == KampType.BRONSEKAMP && it.vinner == null }
            Fase.FINALE -> kamper.firstOrNull { it.type == KampType.FINALE && it.vinner == null }
            Fase.FERDIG -> null
        }
    }

    fun ventendeSpillere(): List<String> {
        return kamper.filter { it.runde == runde }.mapNotNull { it.vinner }
    }

    fun turneringFerdig(): Boolean = fase == Fase.FERDIG

    fun turneringsvinner(): String? {
        return kamper.firstOrNull { it.type == KampType.FINALE }?.vinner
            ?: if (fase == Fase.FERDIG) kamper.lastOrNull { it.vinner != null }?.vinner else null
    }

    fun andrePlass(): String? {
        val finale = kamper.firstOrNull { it.type == KampType.FINALE }
        if (finale?.vinner != null) {
            return if (finale.lag1 == finale.vinner) finale.lag2 else finale.lag1
        }
        val sisteEkstra = kamper.lastOrNull { it.type == KampType.EKSTRA && it.vinner != null }
        if (sisteEkstra != null) {
            return if (sisteEkstra.lag1 == sisteEkstra.vinner) sisteEkstra.lag2 else sisteEkstra.lag1
        }
        return null
    }


    fun tredjePlass(): String? {
        val bronsekamp = kamper.firstOrNull { it.type == KampType.BRONSEKAMP }
        if (bronsekamp?.vinner != null) return bronsekamp.vinner

        val sisteEkstra = kamper.lastOrNull { it.type == KampType.EKSTRA && it.vinner != null }
        if (sisteEkstra != null) {
            val semifinale = kamper
                .filter { it.type == KampType.NORMAL && it.runde == sisteEkstra.runde && it.vinner != null }
                .firstOrNull()
            if (semifinale != null) {
                return if (semifinale.vinner == semifinale.lag1) semifinale.lag2 else semifinale.lag1
            }
        }

        val sisteNormale = kamper
            .filter { it.type == KampType.NORMAL && it.vinner != null }
            .maxByOrNull { it.runde }
        if (sisteNormale != null) {
            return if (sisteNormale.vinner == sisteNormale.lag1) sisteNormale.lag2 else sisteNormale.lag1
        }

        return null
    }

    fun lagreResultat(context: android.content.Context, antallGavekort: Int = 0) {
        val prefs = context.getSharedPreferences("turnering_prefs", android.content.Context.MODE_PRIVATE)
        val totaltGavekort = prefs.getInt("totalt_gavekort", 0) + antallGavekort
        prefs.edit()
            .putString("siste_aktivitet", aktivitet)
            .putInt("siste_antall", lag.size)
            .putInt("siste_gavekort", antallGavekort)
            .putInt("totalt_gavekort", totaltGavekort)
            .putInt("antall_turneringer", prefs.getInt("antall_turneringer", 0) + 1)
            .apply()
    }

    fun reset() {
        lag.clear()
        kamper.clear()
        runde = 1
        fase = Fase.NORMAL
        ekstraSpiller = null
        aktivitet = ""
        sisteForslag = ""
        erUte = null
        antallRunder = 3
        visModus = null
    }
}