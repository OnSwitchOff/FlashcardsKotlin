package flashcards

fun main() {
    menu()
}

fun menu(){
    val flashcards = FlashCards()
    println("Input the number of cards:")
    val n = readLine()!!.toInt()
    for (i in 1..n) {
        println("Card #$i:")
        val front = readLine()!!
        println("The definition for card #$i:")
        val back = readLine()!!
        flashcards.addCard(Card(front, back))
    }
    flashcards.cards.forEach{
        println("Print the definition of \"${it.value.front}\":")
        it.value.checkAnswer(readLine()!!)
    }
}

class Card{
    val front: String
    val back: String
    constructor() {
        this.front = readLine()!!
        this.back = readLine()!!
    }
    constructor(_front: String, _back: String){
        this.front = _front
        this.back = _back
    }
    fun checkAnswer(answer: String) {
        println(if (answer.equals(back, ignoreCase = false)) "Correct!" else "Wrong. The right answer is \"$back\"")
    }
    fun print() {
        println("Card:")
        println(front)
        println("Definition:")
        println(back)
    }
}

class FlashCards() {
    val cards: MutableMap<Int,Card> = mutableMapOf()
    fun addCard(card: Card) {
        cards[cards.size] = card
    }
}