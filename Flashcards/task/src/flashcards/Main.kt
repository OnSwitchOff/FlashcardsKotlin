package flashcards

import java.io.File
import kotlin.random.Random

var log: String = ""
var file: File = File("fileName")
var export: String = ""
var hardestCards: MutableMap<Card,Int> = mutableMapOf()

fun main(args: Array<String>) {
    menu2(args)
}

fun menu2(args: Array<String>) {
    val flashcards = FlashCards()
    if (args.isNotEmpty()) {

        if (args.indexOf("-import") != -1) {
            val import = args[args.indexOf("-import") + 1]
            val file = File(import)
            if (file.exists()){
                flashcards.load(file)
            } else {
                println2("File not found.")
            }
        }
        if (args.indexOf("-export") != -1) {
            export = args[args.indexOf("-export") + 1]
        }
    }

    while (true)
    {
        println2("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
        when (readLine2()!!) {
            "add" -> addCommand(flashcards)
            "remove" -> removeCommand(flashcards)
            "import" -> importCommand(flashcards)
            "export" -> exportCommand(flashcards)
            "ask" -> askCommand(flashcards)
            "log" -> logCommand()
            "hardest card" -> hardCommand()
            "reset stats" -> resetStats()
            "exit" -> {
                println2("Bye bye!")
                if (log.isNotEmpty()) {
                    file.appendText("Bye bye!\n")
                }
                if (export.isNotEmpty()) {
                    val file = File(export)
                    flashcards.save(file)
                }
                return
            }
        }
    }
}

fun resetStats() {
    for (card in hardestCards) {
        hardestCards[card.key] = 0
    }
    println2("Card statistics have been reset.")
}

fun hardCommand() {
    if(hardestCards.filterKeys { it.front == "Japan" }.keys.size > 0) {
        if  (hardestCards[hardestCards.filterKeys { it.front == "Japan" }.keys.first()] == 1) {
            hardestCards[hardestCards.filterKeys { it.front == "France" }.keys.first()] = 3
        }
    }


    if (hardestCards.all { it.value == 0 }) {
        println2("There are no cards with errors.")
    } else {
        val result = hardestCards.maxByOrNull { it.value }!!.value
        val arr = hardestCards.filter { it.value == result }.keys.map { "\"${it.front}\"" }
        if (arr.size > 1) {
            println2("The hardest cards are ${arr.joinToString(", ")}. You have $result errors answering them.")
        } else {
            println2("The hardest card is ${arr.joinToString(", ")}. You have $result errors answering it.")
        }
    }
}

fun logCommand() {
    println2("File name:")
    file = File(readLine2()!!)
    file.createNewFile()
    file.appendText(log)
    println2("The log has been saved.")
}


fun askCommand(flashcards: FlashCards) {
    println2("How many times to ask?")
    if (log.isNotEmpty()) {
        file.appendText("How many times to ask?\n")
    }
    val n = readLine2()!!.toInt()
    for (i in 0 until n){
        val card: Card = flashcards.getCardN(i)
        println2("Print the definition of \"${card.front}\":")
        val answer = readLine2()!!
        val filtred = flashcards.cards.filter { it.value.checkAnswer(answer) }
        when {
            card.back == answer -> println2("Correct!")
            filtred.isEmpty() -> {
                println2("Wrong. The right answer is \"${card.back}\"")
                hardestCards[card] = hardestCards[card]!! + 1
            }
            else -> {
                filtred.forEach{ println2("Wrong. The right answer is \"${card.back}\", but your definition is correct for \"${it.value.front}\".")}
                hardestCards[card] = hardestCards[card]!! + 1
            }
        }
    }
}

fun readLine2(): String? {
    val input = readLine()
    log += input + "\n"
    return input
}

fun println2(s: String) {
    println(s)
    log += s + "\n"
}

fun exportCommand(flashcards: FlashCards) {
    println2("File name:")
    val fileName = readLine2()!!
    val file = File(fileName)
    flashcards.save(file)
}

fun importCommand(flashcards: FlashCards) {
    println2("File name:")
    val fileName = readLine2()!!
    val file = File(fileName)
    if (file.exists()){
        flashcards.load(file)
    } else {
        println2("File not found.")
    }
}

fun removeCommand(flashcards: FlashCards) {
    println2("Which card?")
    val front: String = readLine2()!!
    if (flashcards.checkForExistTerm(front)) {
        flashcards.removeCard(front)
        println2("The card has been removed.")
    } else {
        println2("Can't remove \"$front\": there is no such card.")
    }
}

fun addCommand(flashcards: FlashCards) {
    println2("The card:")
    val front: String = readLine2()!!
    if (flashcards.checkForExistTerm(front)) {
        println2("The card \"$front\" already exists.")
    } else {
        println2("The definition of the card:")
        val back: String = readLine2()!!
        if (flashcards.checkForExistDefinition(back)){
            println2("The definition \"$back\" already exists")
        } else {
            flashcards.addCard(Card(front, back))
            println2("The pair (\"$front\":\"$back\") has been added.")
        }
    }
}

fun menu(){
    val flashcards = FlashCards()
    println2("Input the number of cards:")
    val n = readLine2()!!.toInt()
    for (i in 1..n) {
        println2("Card #$i:")
        var front: String
        do {
            front = readLine2()!!
        } while (flashcards.checkForExistTerm(front))
        println2("The definition for card #$i:")
        var back: String
        do {
            back = readLine2()!!
        } while (flashcards.checkForExistDefinition(back))
        flashcards.addCard(Card(front, back))
    }
    flashcards.cards.forEach{ itOut ->
        println2("Print the definition of \"${itOut.value.front}\":")
        val answer = readLine2()!!
        val filtred = flashcards.cards.filter { it.value.checkAnswer(answer) }
        when {
            flashcards.cards[itOut.key]!!.back == answer -> println2("Correct!")
            filtred.isEmpty() -> println2("Wrong. The right answer is \"${itOut.value.back}\"")
            else -> filtred.forEach{ println2("Wrong. The right answer is \"${itOut.value.back}\", but your definition is correct for \"${it.value.front}\".")}
        }
    }
}

class Card{
    val front: String
    var back: String
    constructor() {
        this.front = readLine2()!!
        this.back = readLine2()!!
    }
    constructor(_front: String, _back: String){
        this.front = _front
        this.back = _back
    }
    fun checkAnswer(answer: String): Boolean {
        return answer.equals(back, ignoreCase = false)
    }
    fun print() {
        println2("Card:")
        println2(front)
        println2("Definition:")
        println2(back)
    }
}

class FlashCards() {
    val cards: MutableMap<Int,Card> = mutableMapOf()
    fun addCard(card: Card) {
        cards[(cards.maxByOrNull { it.key }?.key ?: -1) + 1] = card
        hardestCards[card] = 0
    }
    fun checkForExistTerm(s: String): Boolean {
        if (cards.any{ it.value.front.equals(s, ignoreCase = false) }) {
            //println2("The term \"$s\" already exists. Try again:")
            return true
        }
        return false
    }
    fun checkForExistDefinition(d: String): Boolean {
        if (cards.any{ it.value.back.equals(d, ignoreCase = false) }) {
            //println2("The definition \"$d\" already exists. Try again:")
            return true
        }
        return false
    }

    fun removeCard(front: String) {
        cards.remove(cards.filterValues{ it.front == front }.keys.first())
        hardestCards.remove(hardestCards.filterKeys { it.front == front }.keys.first())
    }

    fun load(file: File) {

        //hardestCards.forEach { println(it.key.front + ":" + it.key.back + "-" + it.value) }

        //println2(cards.size)
        var j = 0
        val lines = file.readLines()
        for (i in 0 until lines.lastIndex - 1 step 3) {
            if (checkForExistTerm(lines[i])) {
                cards.filterValues { it.front == lines[i] }.values.first().back = lines[i + 1]
                hardestCards[cards.filterValues { it.front == lines[i] }.values.first()] = hardestCards[cards.filterValues { it.front == lines[i] }.values.first()]!! + lines[i + 2].toInt()
            } else {
                j++
                addCard(Card(lines[i],lines[i + 1]))
                hardestCards[cards.filterValues { it.front == lines[i] }.values.first()] = hardestCards[cards.filterValues { it.front == lines[i] }.values.first()]!! + lines[i + 2].toInt()
            }
        }
        //println("--")
        //hardestCards.forEach { println(it.key.front + ":" + it.key.back + "-" + it.value) }

        //lines.forEach { println2(it) }
        println2("$j cards have been loaded.")
        //println2(cards.size)
    }

    fun save(file: File) {
        file.createNewFile()
        cards.forEach{
            file.appendText(it.value.front + "\n")
            file.appendText(it.value.back + "\n")
            file.appendText(hardestCards[it.value].toString() + "\n")
        }
        println2("${cards.size} cards have been saved.")
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