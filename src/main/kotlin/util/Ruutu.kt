package luolastogeneraattori.util

/**
 * Ruutuun voidaan tallentaa [suunta], [arvo] ja [naapurit].
 */
open class Ruutu (
    var suunta: String?,
    // var suunnat: Array<String> = arrayOf(),
    var arvo: Int = 0,
    var naapurit: Array<Triple<Int, Int, Ruutu>> = arrayOf()
)
