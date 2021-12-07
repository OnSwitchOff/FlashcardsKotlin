package flashcards

fun main() {
    val card = Card()
    card.checkAnswer(readLine()!!)
}

class Card{
    private val front: String
    private val back: String
    constructor() {
        this.front = readLine()!!
        this.back = readLine()!!
    }
    constructor(_front: String, _back: String){
        this.front = _front
        this.back = _back
    }
    fun checkAnswer(answer: String) {
        println(if (answer.equals(back, ignoreCase = false)) "Your answer is right!" else "Your answer is wrong...")
    }
    fun print() {
        println("Card:")
        println(front)
        println("Definition:")
        println(back)
    }
}

class FlashCards() {
    val cards: MutableList<Card> = mutableListOf()
    fun addCard(card: Card) {
        cards.add(card)
    }
}