package chess

abstract class Player(val name: String, army: Array<Pawn>) {

    abstract val color: String
    abstract val highestRank: Int

    val pieces = army
    private val takenPawns = mutableListOf<Pawn>()

    fun isWinner(): Boolean {
        return haveReachedHighestRank() ||
                haveTakenAllOpponentPawns()
    }

    fun isStalemate(board: Board): Boolean {
        return cannotMove(board)
    }

    private fun cannotMove(board: Board): Boolean =
        pieces.all {
            it.cannotMove(board)
        }

    fun haveReachedHighestRank(): Boolean {
        return pieces.any {
            it.curRank == highestRank
        }
    }

    fun haveTakenAllOpponentPawns(): Boolean {
        return takenPawns.size == 8 &&
                takenPawns.all {
                    it.isTaken
                }
    }

    fun requestMove(): String {
        return readLine()!!
    }

    fun movePawnOneStep(board: Board, m: PawnMove): Boolean {
        val piece = board.getPawnAt(m.from)
        GameLog.add(piece!!, null, pawnMove = m)

        board[m.from].setEmpty()
        piece.moveTo(m.toFile, m.toRank)
        board[m.to].content = piece
        return true
    }

    fun movePawnTwoSteps(board: Board, m: PawnMove): Boolean {
        val piece = board.getPawnAt(m.from)
        GameLog.add(piece!!, null, pawnMove = m)

        board[m.from].setEmpty()
        piece.moveTo(m.toFile, m.toRank)
        board[m.to].content = piece
        return true
    }

    fun capturePawn(board: Board, m: PawnMove): Boolean {

        val opponent = board.getPawnAt(m.to)
        val piece = board.getPawnAt(m.from)
        GameLog.add(piece!!, opponent, pawnMove = m)

        board[opponent!!.curFile, opponent.curRank]!!.setEmpty()
        opponent.isTaken = true
        opponent.removeFromBoard()

        board[piece.curFile, piece.curRank]!!.setEmpty()
        piece.moveTo(m.toFile, m.toRank)
        this.takenPawns.add(opponent)
        // board[piece.curFile, piece.curRank].content = piece
        board[piece.curFile, piece.curRank]!!.receive(piece)
        board[m.toFile, m.toRank] = piece
        return true
    }

    fun takeEnpassant(board: Board, m: PawnMove): Boolean {
        val opponent = board.getPawnAt(m.to)
        val piece = board.getPawnAt(m.from)
        GameLog.add(piece!!, opponent, pawnMove = m)

        opponent!!.isTaken = true
        board[opponent.curFile, opponent.curRank]!!.setEmpty()

        piece.moveTo(m.toFile, m.toRank)
        board[m.toFile, m.toRank]!!.receive(piece)
        this.takenPawns.add(opponent)
        return true
    }

    fun doMove(board: Board, player: Player, m: PawnMove): Boolean {
        val piece = board.getPawnAt(m.from)
        if (piece == null) {
            println("No ${player.color} pawn at ${m.from}")
            return false
        }
        return when {
            m.moveType == MoveType.ILLEGAL_MOVE -> {
                println("Invalid Input")
                false
            }

            piece.isValidOneStepMove(board[m.to], board) &&
                    (m.moveType == MoveType.BLACK_ONE_STEP ||
                            m.moveType == MoveType.WHITE_ONE_STEP) -> {
                // remove pawn from current square
                // move pawn to new square
                // set pawn's current square to new square
                return movePawnOneStep(board, m)
            }

            piece.isValidTwoStepsMove(board[m.to], board) &&
                    (m.moveType == MoveType.BLACK_TWO_STEPS ||
                            m.moveType == MoveType.WHITE_TWO_STEPS) -> {
                // remove pawn from current square
                // move pawn to new square
                // set pawn's current square to new square
                return movePawnTwoSteps(board, m)
            }

            piece.isValidCapture(board[m.to], board) -> {
                // set opponent pawn's state to taken
                // to remove it from board
                // remove pawn from current square
                // move pawn to new square.
                //
                return capturePawn(board, m)
            }

            piece.isValidEnPassant(board[m.to], board) -> {
                return takeEnpassant(board, m)
            }

            else -> {
                println("Invalid Input")
                false
            }
        }
    }

}

class FirstPlayer(name: String, army: Array<Pawn>) : Player(name, army) {

    override val color = "White"
    override val highestRank: Int
        get() = ChessParams.WHITE_PAWN_HIGHEST_RANK
}

class SecondPlayer(name: String, army: Array<Pawn>) : Player(name, army) {

    override val color = "Black"
    override val highestRank: Int
        get() = ChessParams.BLACK_PAWN_HIGHEST_RANK
}
