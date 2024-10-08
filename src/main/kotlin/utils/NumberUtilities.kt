package utils

fun validRange(numberToCheck: Int, min: Int, max: Int): Boolean {
    return numberToCheck in min..max
}
