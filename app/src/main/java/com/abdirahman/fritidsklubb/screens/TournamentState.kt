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

        // Hvis odde antall — sett til side én spiller som ekstraspiller
        if (blandet.size % 2 != 0) {
            ekstraSpiller = blandet.removeLastOrNull()
        } else {
            ekstraSpiller = null
        }

        // Generer vanlige kamper
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
        // Velg en tilfeldig ferdig kamp fra denne runden
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

        // Etter at en normal kamp er ferdig, sjekk om ekstraSpiller skal få sin kamp
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
            // Hvis ekstra kamp angres, fjern den og sett ekstraSpiller tilbake
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

        // Samle vinnere — ekstra kamp-vinner erstatter motstanderen sin
        val vinnere = mutableListOf<String>()
        val ekstraKamp = kamper.firstOrNull { it.runde == runde && it.type == KampType.EKSTRA }

        kamper.filter { it.runde == runde && it.type == KampType.NORMAL }.forEach { kamp ->
            val vinner = kamp.vinner ?: return@forEach
            // Sjekk om denne vinneren ble utfordret av ekstraspiller
            if (ekstraKamp != null && ekstraKamp.lag2 == vinner) {
                // Bruk ekstra kamp-vinneren istedet
                vinnere.add(ekstraKamp.vinner ?: vinner)
            } else {
                vinnere.add(vinner)
            }
        }

        val tapere = kamper
            .filter { it.runde == runde && it.type == KampType.NORMAL }
            .filter { it.lag2 != "Bye" }
            .mapNotNull { kamp ->
                if (kamp.vinner == kamp.lag1) kamp.lag2 else kamp.lag1
            }

        when {
            vinnere.size == 2 && tapere.size >= 2 && fase == Fase.NORMAL -> {
                fase = Fase.BRONSEKAMP
                val id = kamper.size
                kamper.add(Kamp(id = id, lag1 = tapere[0], lag2 = tapere[1], runde = runde, type = KampType.BRONSEKAMP))
                kamper.add(Kamp(id = id + 1, lag1 = vinnere[0], lag2 = vinnere[1], runde = runde, type = KampType.FINALE))
            }
            vinnere.size > 2 -> {
                runde++
                genererRunde(vinnere)
            }
        }

        val bronsekamp = kamper.firstOrNull { it.type == KampType.BRONSEKAMP }
        val finale = kamper.firstOrNull { it.type == KampType.FINALE }
        if (bronsekamp?.vinner != null && fase == Fase.BRONSEKAMP) fase = Fase.FINALE
        if (finale?.vinner != null) fase = Fase.FERDIG
    }

    fun nesteKamp(): Kamp? {
        return when (fase) {
            Fase.NORMAL -> {
                // Vanlige kamper først, deretter ekstra kamp
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

    fun turneringsvinner(): String? = kamper.firstOrNull { it.type == KampType.FINALE }?.vinner

    fun andrePlass(): String? {
        val finale = kamper.firstOrNull { it.type == KampType.FINALE } ?: return null
        val vinner = finale.vinner ?: return null
        return if (finale.lag1 == vinner) finale.lag2 else finale.lag1
    }

    fun tredjePlass(): String? = kamper.firstOrNull { it.type == KampType.BRONSEKAMP }?.vinner

    fun reset() {
        lag.clear()
        kamper.clear()
        runde = 1
        fase = Fase.NORMAL
        ekstraSpiller = null
        aktivitet = ""
    }
}