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
        var front: String
        do {
            front = readLine()!!
        } while (flashcards.checkForExistTerm(front))
        println("The definition for card #$i:")
        var back: String
        do {
            back = readLine()!!
        } while (flashcards.checkForExistDefinition(back))
        flashcards.addCard(Card(front, back))
    }
    flashcards.cards.forEach{ itOut ->
        println("Print the definition of \"${itOut.value.front}\":")
        val answer = readLine()!!
        val filtred = flashcards.cards.filter { it.value.checkAnswer(answer) }
        when {
            flashcards.cards[itOut.key]!!.back == answer -> println("Correct!")
            filtred.isEmpty() -> println("Wrong. The right answer is \"${itOut.value.back}\"")
            else -> filtred.forEach{ println("Wrong. The right answer is \"${itOut.value.back}\", but your definition is correct for \"${it.value.front}\".")}
        }
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
    fun checkAnswer(answer: String): Boolean {
        return answer.equals(back, ignoreCase = false)
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
    fun checkForExistTerm(s: String): Boolean {
        if (cards.any{ it.value.front.equals(s, ignoreCase = false) }) {
            println("The term \"$s\" already exists. Try again:")
            return true
        }
        return false
    }
    fun checkForExistDefinition(d: String): Boolean {
        if (cards.any{ it.value.back.equals(d, ignoreCase = false) }) {
            println("The definition \"$d\" already exists. Try again:")
            return true
        }
        return false
    }
}