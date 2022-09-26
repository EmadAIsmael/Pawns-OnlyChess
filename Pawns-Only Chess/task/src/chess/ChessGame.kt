package chess

import kotlin.math.abs

object ChessParams {
    const val RANK_COUNT = 8
    const val FILE_COUNT = 8

    val VALID_FILES = 'a'..'h'
    val VALID_RANKS = 1..8

    const val PAWN_COUNT = 8

    const val WHITE_PAWN_INIT_RANK = 2
    const val BLACK_PAWN_INIT_RANK = 7

    const val WHITE_PAWN_ENPASS_RANK = 4
    const val BLACK_PAWN_ENPASS_RANK = 5

    const val WHITE_PAWN_HIGHEST_RANK = 8
    const val BLACK_PAWN_HIGHEST_RANK = 1
}

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
            FirstPlayer(name = getName("First"), army = board.whitePawns),
            SecondPlayer(name = getName("Second"), army = board.blackPawns)
        )
    }

    private fun promptForMove(turn: Int): String {
        println("${players[turn].name}'s turn:")
        return players[turn].requestMove()
    }

    fun play() {
        println("Pawns-Only Chess")
        getPlayerNames()
        board.display()

        var move = ""
        while (move != "exit") {
            if (players[turn].isStalemate(board)) {
                println("Stalemate!")
                break
            }
            move = promptForMove(turn)
            if (move == "exit") {
                continue
            } else if (!PawnMove.isValidMove(move)) {
                println("Invalid Input")
                continue
            } else {
                if (players[turn].doMove(board, players[turn], PawnMove(move, players[turn].color))) {
                    board.display()

                    if (players[turn].isWinner()) {
                        if (players[turn] is FirstPlayer) {
                            println("White Wins!")
                        } else {
                            println("Black Wins!")
                        }
                        break
                    }
                    turn++
                    turn %= 2
                }
            }
        }
        println("Bye!")
    }
}

object GameLog {
    data class GameLogRecord(
        val fromPiece: Pawn,
        val toPiece: Pawn? = null,
        val move: PawnMove,
        val canBeTakenEnpass: Boolean = false
    )
    private var history = mutableListOf<GameLogRecord>()

    fun lastMovePiece() = history[history.lastIndex].fromPiece
    fun lastMoveMove() = history[history.lastIndex].move
    fun lastMoveWasTwoSteps(): Boolean =
        abs(lastMoveMove().toCell.rank - lastMoveMove().fromCell.rank) == 2

    fun add(fromPiece: Pawn, toPiece: Pawn? = null, pawnMove: PawnMove) {
        var canBeTakenEnpass = false
        if (pawnMove.fromRank == pawnMove.toRank - 2) canBeTakenEnpass = true
        history.add(GameLogRecord(
            fromPiece = fromPiece,
            toPiece = toPiece,
            move = pawnMove,
            canBeTakenEnpass = canBeTakenEnpass)
        )
    }

    fun isLastMoveEnpassant(): Boolean {
        val lastMoveRecord = history.last()
        return lastMoveRecord.canBeTakenEnpass
    }
}
