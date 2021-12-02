package chess

abstract class Player(val name: String) {
    abstract val color: String
    abstract var pieces: List<Pawn>
    val files = ('a'..'h')

    fun requestMove(): String {
        return readLine()!!
    }

    fun movePiece(board: Board, player: Player, move: String): Boolean {
        val m = Move(move)

        val from = m.curSquare
        val oldFile = m.from.file
        val oldRank = m.from.rank

        val newFile = m.to.file
        val newRank = m.to.rank

        val piece = board.getSquare(oldRank, oldFile)
        if (piece !in pieces) {
            println("No ${player.color} pawn at $from")
            return false
        }
        return when {
            piece?.isValidMove(m.to, board) == true -> {
                // remove pawn from current square
                // move pawn to new square
                // set pawn's current square to new square
                board.resetSquare(oldRank, oldFile)
                piece.setCurrentSquare(newRank, newFile)
                board.setSquare(newRank, newFile, piece)
                true
            }
            piece?.isValidCapture(m.to, board)!! -> {
                // set opponent pawn's state to taken
                // to remove it from board
                // remove pawn from current square
                // move pawn to new square.
                val opponent = board.getSquare(m.to)
                opponent?.isTaken = true
                board.resetSquare(opponent!!.curSquare.rank, opponent.curSquare.file)
                board.resetSquare(piece.curSquare.rank, piece.curSquare.file)
                piece.setCurrentSquare(newRank, newFile)
                board.setSquare(newRank, newFile, piece)
                true
            }
            piece.isValidEnPassant(m.to, board) -> {
                val ep = ChessGame.enpassant
                ep.takenPawn.isTaken = true
                board.resetSquare(ep.takenPawn.curSquare.rank, ep.takenPawn.curSquare.file)

                board.resetSquare(ep.takingPawn.curSquare.rank, ep.takingPawn.curSquare.file)
                ep.takingPawn.setCurrentSquare(ep.newSquare.rank, ep.newSquare.file)
                board.setSquare(ep.newSquare.rank, ep.newSquare.file, ep.takingPawn)
                true
            }
            else -> {
                println("Invalid Input")
                false
            }
        }
    }
}

class FirstPlayer(name: String) : Player(name) {
    override val color = "White"
    override var pieces: List<Pawn> = files.map {
        WhitePawn(it)
    }
}

class SecondPlayer(name: String) : Player(name) {
    override val color = "Black"
    override var pieces: List<Pawn> = files.map {
        BlackPawn(it)
    }
}
