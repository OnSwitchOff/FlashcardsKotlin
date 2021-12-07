package flashcards

fun main() {
    Card("Yes?", "Yes!!!").print()
}

class Card(_front: String, _back: String) {
    private val front = _front
    private val back = _back
    fun print() {
        println("Card:")
        println(front)
        println("Definition:")
        println(back)
    }
}