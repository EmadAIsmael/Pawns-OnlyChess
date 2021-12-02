package chess

import kotlin.math.abs

class ChessGame {
    private lateinit var players: Array<Player>
    private val board: Board = Board()
    private var turn = 0

    private fun getName(order: String): String {
        println("$order Player's name:")
        return readLine()!!
    }

    private fun getPlayerNames() {
        players = arrayOf(
            FirstPlayer(name = getName("First")),
            SecondPlayer(name = getName("Second"))
        )
    }

    private fun setInitSquares() {
        board.structurePieces(players)
    }

    private fun promptForMove(turn: Int): String {
        println("${players[turn].name}'s turn:")
        return players[turn].requestMove()
    }

    fun play() {
        println("Pawns-Only Chess")
        getPlayerNames()
        setInitSquares()
        board.display()

        var move = ""
        while (move != "exit") {
            move = promptForMove(turn)
            if (!board.isAcceptableMove(move)) {
                println("Invalid Input")
                continue
            } else if (move == "exit") {
                continue
            } else {
                if (players[turn].movePiece(board, players[turn], move)) {
                    val m = Move(move)
                    val piece = board.getSquare(m.to.rank, m.to.file)
                    history.add(HistoryRecord(piece!!, Move(move)))
                    board.display()
                    turn++
                    turn %= 2
                }
            }
        }
        println("Bye!")
    }

    companion object {
        data class HistoryRecord(val piece: Pawn, val move: Move)

        var history = mutableListOf<HistoryRecord>()
        lateinit var enpassant: EnPassant
        fun lastMovePiece() = history[history.lastIndex].piece
        fun lastMoveMove() = history[history.lastIndex].move
        fun lastMoveWasTwoSteps(): Boolean =
            abs(lastMoveMove().to.rank - lastMoveMove().from.rank) == 2
    }
}

data class EnPassant(var takingPawn: Pawn, var takenPawn: Pawn, var newSquare: Square)
