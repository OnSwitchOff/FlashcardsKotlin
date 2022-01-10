package flashcards

import java.io.File
import kotlin.random.Random

fun main() {
    menu2()
}

fun menu2(){
    val flashcards = FlashCards()
    while (true)
    {
        println("Input the action (add, remove, import, export, ask, exit):")
        when (readLine()!!) {
            "add" -> addCommand(flashcards)
            "remove" -> removeCommand(flashcards)
            "import" -> importCommand(flashcards)
            "export" -> exportCommand(flashcards)
            "ask" -> askCommand(flashcards)
            "exit" -> {
                println("Bye bye!")
                return
            }
        }
    }

}

fun askCommand(flashcards: FlashCards) {
    println("How many times to ask?")
    val n = readLine()!!.toInt()
    for (i in 0 until n){
        val card: Card = flashcards.getCardN(i)
        println("Print the definition of \"${card.front}\":")
        val answer = readLine()!!
        val filtred = flashcards.cards.filter { it.value.checkAnswer(answer) }
        when {
            card.back == answer -> println("Correct!")
            filtred.isEmpty() -> println("Wrong. The right answer is \"${card.back}\"")
            else -> filtred.forEach{ println("Wrong. The right answer is \"${card.back}\", but your definition is correct for \"${it.value.front}\".")}
        }
    }
}

fun exportCommand(flashcards: FlashCards) {
    println("File name:")
    val fileName = readLine()!!
    val file = File(fileName)
    flashcards.save(file)
}

fun importCommand(flashcards: FlashCards) {
    println("File name:")
    val fileName = readLine()!!
    val file = File(fileName)
    if (file.exists()){
        flashcards.load(file)
    } else {
        println("File not found.")
    }
}

fun removeCommand(flashcards: FlashCards) {
    println("Which card?")
    val front: String = readLine()!!
    if (flashcards.checkForExistTerm(front)) {
        flashcards.removeCard(front)
        println("The card has been removed.")
    } else {
        println("Can't remove \"$front\": there is no such card.")
    }
}

fun addCommand(flashcards: FlashCards) {
    println("The card:")
    val front: String = readLine()!!
    if (flashcards.checkForExistTerm(front)) {
        println("The card \"$front\" already exists.")
    } else {
        println("The definition of the card:")
        val back: String = readLine()!!
        if (flashcards.checkForExistDefinition(back)){
            println("The definition \"$back\" already exists")
        } else {
            flashcards.addCard(Card(front, back))
            println("The pair (\"$front\":\"$back\") has been added.")
        }
    }
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
    var back: String
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
        cards[(cards.maxByOrNull { it.key }?.key ?: -1) + 1] = card
    }
    fun checkForExistTerm(s: String): Boolean {
        if (cards.any{ it.value.front.equals(s, ignoreCase = false) }) {
            //println("The term \"$s\" already exists. Try again:")
            return true
        }
        return false
    }
    fun checkForExistDefinition(d: String): Boolean {
        if (cards.any{ it.value.back.equals(d, ignoreCase = false) }) {
            //println("The definition \"$d\" already exists. Try again:")
            return true
        }
        return false
    }

    fun removeCard(front: String) {
        cards.remove(cards.filterValues{ it.front == front }.keys.first())
    }

    fun load(file: File) {
        //println(cards.size)
        var j = 0
        val lines = file.readLines()
        for (i in 0 until lines.lastIndex step 2) {
            if (checkForExistTerm(lines[i])) {
                cards.filterValues { it.front == lines[i] }.values.first().back = lines[i + 1]
            } else {
                j++
                addCard(Card(lines[i],lines[i + 1]))
            }
        }
        //lines.forEach { println(it) }
        println("$j cards have been loaded.")
        //println(cards.size)
    }

    fun save(file: File) {
        file.createNewFile()
        cards.forEach{
            file.appendText(it.value.front + "\n")
            file.appendText(it.value.back + "\n")
        }
        println("${cards.size} cards have been saved.")
    }

    fun getRandomCard(): Card {
        val randomGenerator42 = Random(42)
        val k: Int = randomGenerator42.nextInt(cards.size)
        var i = 0
        var result = Card("","")
        for (card in cards){
            if (i == k) {
                result = card.value
            }
            i++
        }
        return result
    }

    fun getCardN(i: Int): Card {
        var j = 0
        var result= Card("","")
        for (card in cards){
            if (i == j) {
                result = card.value
            }
            j++
        }
        return result
    }
}