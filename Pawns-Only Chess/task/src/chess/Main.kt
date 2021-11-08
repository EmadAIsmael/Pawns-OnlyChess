package chess

fun main() {
    val game = PawnsOnlyChess()
    println("Pawns-Only Chess")
    game.setInitPositions()
    game.displayBoard()
}