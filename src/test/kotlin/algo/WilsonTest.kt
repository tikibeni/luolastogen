package algo

import luolastogeneraattori.algo.Wilson
import luolastogeneraattori.util.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class WilsonTest {
    @Test
    fun kaveleeLoppuun() {
        // Yksinkertainen testi algoritmin läpivedolle. AssertNotNull on enemmänkin muodollisuus, eikä mittaa mitään
        // palautusarvoon liittyvää. Epäonnistuessa tämä testi jäisi ikuiseen luuppiin.
        val wilson = Wilson()
        val ruudukko = rakennaRuudukko(3, 3)
        val laby = wilson.muunnaLabyrintiksi(ruudukko)
        assertNotNull(laby)
    }

    @Test
    fun labyrinttiKaikistaRuuduista() {
        // Tarkistaa, ettei lopulliseen ruudukkoon jää yhtään ruutua, jonka arvo olisi 0 tai jolla ei olisi suuntaa
        val wilson = Wilson()
        val ruudukko = rakennaRuudukko(5, 5)
        val laby = wilson.muunnaLabyrintiksi(ruudukko)
        for (rivi in laby) {
            for (ruutu in rivi) {
                assertNotEquals(ruutu.arvo, 0)
                assertNotEquals(ruutu.suunnat.size, 0)
            }
        }
    }

    @Test
    fun kestoTesti() {
        // Testaa kaatuuko ohjelma erilaisilla syötteillä
        var counter = 0
        var leveys = 0
        var korkeus = 0

        print("Testataan Wilsonia eri syötteillä: ")
        try {
            while (counter < 20) {
                val wilson = Wilson()
                leveys = (3..50).random()
                korkeus = (3..50).random()
                val verkko = rakennaRuudukko(leveys, korkeus)
                wilson.muunnaLabyrintiksi(verkko)
                counter++
            }
        } catch (e: Exception) { println("\nWilson kaatui syötteillä: $leveys, $korkeus\nVirheviesti: ${e.message}\n") }
        print("Ok! (${counter}/20)")
    }
}
