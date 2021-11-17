package chess

class Player(val name: String = "") {
    fun makeMove(): String {
        val move = readLine()!!
        return move
    }
}