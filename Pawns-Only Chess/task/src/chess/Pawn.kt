package chess

abstract class Pawn {
    abstract val color: String
    abstract val symbol: String
    abstract val initialRank: Int
    abstract val initSquare: Square
    abstract val curSquare: Square

    var isTaken: Boolean = false

    abstract fun isValidMove(to: Square, board: Board): Boolean
    abstract fun isValidCapture(to: Square, board: Board): Boolean
    abstract fun isValidEnPassant(to: Square, board: Board): Boolean
    abstract fun isOpponent(p: Pawn?): Boolean
    override fun toString(): String = symbol
    fun setCurrentSquare(newRank: Int, newFile: Char) {
        curSquare.rank = newRank
        curSquare.file = newFile
    }
}

class WhitePawn(initFile: Char = 'a') : Pawn() {
    override val color = "White"
    override val symbol = "W"
    override val initialRank = 2
    override val initSquare = Square(initFile, initialRank)
    override val curSquare = Square(initFile, initialRank)

    private fun isMovingForward(to: Square): Boolean {
        // e.g. from "a2" to "a3"
        return (curSquare.file == to.file) &&
                (curSquare.rank < to.rank)
    }

    private fun isMovingFromInitPosTwoSteps(to: Square): Boolean {
        return curSquare.rank == initialRank &&
                to.rank > curSquare.rank &&
                to.rank - curSquare.rank == 2
    }

    private fun isMovingOneStep(to: Square): Boolean {
        return to.rank - curSquare.rank == 1
    }

    override fun isValidMove(to: Square, board: Board): Boolean {
        return board.isFree(to) &&
                isMovingForward(to) &&
                (isMovingOneStep(to) ||
                        isMovingFromInitPosTwoSteps(to))
    }

    override fun isValidCapture(to: Square, board: Board): Boolean {
        // find allowed capture squares
        // if "to" square in allowed capture squares
        // and "to" square contains a foe,
        // then return true; you can take foe pawn.
        val attackSqr = mutableListOf(
            Square(curSquare.file - 1, curSquare.rank + 1),
            Square(curSquare.file + 1, curSquare.rank + 1)
        ).filter {
            it.isValidSquare() && isOpponent(board.getSquare(it))
        }.filter {
            it == to
        }
        return (to in attackSqr)
    }

    override fun isValidEnPassant(to: Square, board: Board): Boolean {
        val attackSqr = mutableListOf(
            Square(curSquare.file - 1, curSquare.rank),
            Square(curSquare.file + 1, curSquare.rank)
        ).filter {
            it.isValidSquare() &&
                    isOpponent(board.getSquare(it)) &&
                    ChessGame.lastMovePiece() == board.getSquare(it) &&
                    ChessGame.lastMoveWasTwoSteps() &&
                    (to.file == ChessGame.lastMovePiece().curSquare.file &&
                            to.rank == ChessGame.lastMovePiece().curSquare.rank + 1)
//                            if (ChessGame.lastMovePiece() is WhitePawn) 1 else -1)
        }
        return if (attackSqr.isNotEmpty()) {
            ChessGame.enpassant = EnPassant(this, ChessGame.lastMovePiece(), to)
            true
        } else
            false
    }

    override fun isOpponent(p: Pawn?): Boolean {
        return if (p == null) false
        else
            p::class != this::class
    }
}

class BlackPawn(initFile: Char = 'a') : Pawn() {
    override val color = "Black"
    override val symbol = "B"
    override val initialRank = 7
    override val initSquare = Square(initFile, initialRank)
    override val curSquare = Square(initFile, initialRank)

    private fun isMovingForward(to: Square): Boolean {
        // e.g. from "a7" to "a6"
        return curSquare.file == to.file &&
                curSquare.rank > to.rank
    }

    private fun isMovingFromInitPosTwoSteps(to: Square): Boolean {
        return curSquare.rank == initialRank &&
                to.rank < curSquare.rank &&
                to.rank - curSquare.rank == -2
    }

    private fun isMovingOneStep(to: Square): Boolean {
        return to.rank - curSquare.rank == -1
    }

    override fun isValidMove(to: Square, board: Board): Boolean {
        return board.isFree(to) &&
                isMovingForward(to) &&
                (isMovingOneStep(to) ||
                        isMovingFromInitPosTwoSteps(to))
    }

    override fun isValidCapture(to: Square, board: Board): Boolean {
        // find allowed capture squares
        // if "to" square in allowed capture squares
        // and "to" square contains a foe,
        // then return true; you can take foe pawn.
        val attackSqr = mutableListOf(
            Square(curSquare.file - 1, curSquare.rank - 1),
            Square(curSquare.file + 1, curSquare.rank - 1)
        ).filter {
            it.isValidSquare() && isOpponent(board.getSquare(it))
        }.filter {
            it == to
        }
        return (to in attackSqr)
    }

    override fun isValidEnPassant(to: Square, board: Board): Boolean {
        val attackSqr = mutableListOf(
            Square(curSquare.file - 1, curSquare.rank),
            Square(curSquare.file + 1, curSquare.rank)
        ).filter {
            it.isValidSquare() &&
                    isOpponent(board.getSquare(it)) &&
                    ChessGame.lastMovePiece() == board.getSquare(it) &&
                    ChessGame.lastMoveWasTwoSteps() &&
                    (it.file == ChessGame.lastMovePiece().curSquare.file &&
                            it.rank == ChessGame.lastMovePiece().curSquare.rank - 1)
//                            if (ChessGame.lastMovePiece() is WhitePawn) 1 else -1)
        }
        return attackSqr.isNotEmpty()
    }

    override fun isOpponent(p: Pawn?): Boolean {
        return if (p == null) false
        else
            p::class != this::class
    }
}
