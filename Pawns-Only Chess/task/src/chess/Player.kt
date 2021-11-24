package chess

class Player(val name: String = "", val piece: Char = 'W', val pieceColor: String = "White") {
    fun requestMove(): String {
        return readLine()!!
    }
}