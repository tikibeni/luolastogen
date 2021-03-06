package luolastogeneraattori.algo

import luolastogeneraattori.util.*

class Wilson {
    private var kaydyt = arrayOf<Array<Solmu>>()
    private var suunnat = arrayOf("ylos", "alas", "oikea", "vasen")
    private var vastakkainen = mapOf("oikea" to "vasen", "vasen" to "oikea", "ylos" to "alas", "alas" to "ylos")
    private var deltaX = mapOf("ylos" to 0, "alas" to 0, "oikea" to 1, "vasen" to -1)
    private var deltaY = mapOf("ylos" to -1, "alas" to 1, "oikea" to 0, "vasen" to 0)

    /**
     * Muodostetaan kuljetusta reitistä monikkomuotoinen taulukko
     */
    private fun rakennaReittiMonikko(alkuX: Int, alkuY: Int): Array<Triple<Int, Int, String>> {
        // Alustetaan reittimonikkolista alkaen alkukoordinaatista
        var reitti = arrayOf<Triple<Int, Int, String>>()
        var x = alkuX
        var y = alkuY

        // Navigoidaan aina reitin seuraavaan solmuun apulistojen avulla
        while (true) {
            val solmusuunta = kaydyt[y][x].suunta
            if (solmusuunta !== null) {
                reitti += Triple(y, x, solmusuunta)
                y += deltaY[solmusuunta]!!
                x += deltaX[solmusuunta]!!
            } else break
        }

        return reitti
    }

    /**
     * Wilsonin algoritmin satunnaiskävely
     */
    private fun randomKavely(ruudukko: Array<Array<Solmu>>): Array<Triple<Int, Int, String>> {
        while (true) {
            // Y-akselin koordinaatti (rivi)
            var cy = ruudukko.indices.random()
            // X-akselin koordinaatti (sarake)
            var cx = ruudukko[0].indices.random()

            // Jos ruudukon arvo on jo labyrintissa, haetaan uusi koordinaatti
            if (ruudukko[cy][cx].arvo != 0) continue

            // Otetaan reitin alkukoordinaatit talteen
            val alkuY = cy
            val alkuX = cx

            var randomKavely: Boolean

            // Aloitetaan kävely
            do {
                // Oletetaan, että tällä kierroksella osutaan maaliin
                randomKavely = false
                suunnat.shuffle()
                for (suunta in suunnat) {
                    // Lasketaan suunnan osoittaman ruudun koordinaatit
                    val ny = cy + deltaY[suunta]!!
                    val nx = cx + deltaX[suunta]!!

                    // Onko naapurisolmu validi (ruudukon sisällä)
                    if (nx >= 0 && ny >= 0 && ny < ruudukko.size && nx < ruudukko[ny].size) {
                        // Validin naapurin löydettyä asetetaan se poistumisvektoriksi
                        kaydyt[cy][cx].suunta = suunta

                        // Jos naapuriruudussa on jo käyty, niin poistutaan kävelystä.
                        if (ruudukko[ny][nx].arvo == 0) {
                            // Muussa tapauksessa jatketaan kävelyä (kunnes törmätään jo käytyyn solmuun)
                            // Asetetaan uudet koordinaatit ja indikoidaan kävelyn jatkuvuus
                            cy = ny
                            cx = nx
                            randomKavely = true
                        }

                        break
                    }
                }
            } while (randomKavely)

            // Lopulta rakennetaan ja palautetaan reittimonikko
            return rakennaReittiMonikko(alkuX, alkuY)
        }
    }

    /**
     * Algoritmin ajaminen niin kauan kunnes kaikki ruudut on käyty läpi
     */
    fun muunnaLabyrintiksi(ruudukko: Array<Array<Solmu>>): Array<Array<Solmu>> {
        // Alustetaan erillinen käyntejä seuraava taulukko
        kaydyt = alustaKaydyt(ruudukko)

        // Valitaan loppupisteeksi satunnainen piste
        val satunnaisX = ruudukko.indices.random()
        val satunnaisY = ruudukko[0].indices.random()

        // Asetetaan loppupisteen arvoksi 2, joka indikoi maalisolmua
        ruudukko[satunnaisX][satunnaisY].arvo = 2

        var solmujaJaljella = ruudukko.size * ruudukko[0].size - 1
        while (solmujaJaljella > 0) {
            for (it in randomKavely(ruudukko)) {
                if (ruudukko[it.first][it.second].arvo == 0) {
                    // Suunnan osoittama y-koordinaatti (rivi)
                    val ny = it.first + deltaY[it.third]!!
                    // Suunnan osoittama x-koordinaatti (sarake)
                    val nx = it.second + deltaX[it.third]!!

                    ruudukko[it.first][it.second].suunnat += it.third
                    ruudukko[it.first][it.second].arvo = 1
                    ruudukko[ny][nx].suunnat += vastakkainen[it.third]!!
                    solmujaJaljella -= 1

                    if (solmujaJaljella == 0) break
                }
            }
        }

        return ruudukko
    }
}
