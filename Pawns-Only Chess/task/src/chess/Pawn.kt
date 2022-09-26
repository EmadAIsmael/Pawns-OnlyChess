package chess

abstract class Pawn {
    abstract val color: String
    abstract val symbol: String
    abstract val initFile: Char
    abstract val initRank: Int
    abstract val initAddr: String
    abstract var curAddr: String

    abstract var curFile: Char
    abstract var curRank: Int

    abstract val opponentInitRank: Int
    abstract val opponentEnpassRank: Int

    var isTaken: Boolean = false

    abstract fun canMoveOneStep(board: Board): Boolean
    abstract fun cannotMoveOneStep(board: Board): Boolean

    abstract fun canMoveTwoSteps(board: Board): Boolean
    abstract fun cannotMoveTwoSteps(board: Board): Boolean

    abstract fun canTakeLeft(board: Board): Boolean
    abstract fun cannotTakeLeft(board: Board): Boolean
    abstract fun canTakeRight(board: Board): Boolean
    abstract fun cannotTakeRight(board: Board): Boolean

    abstract fun canTakeEnpassantLeft(board: Board): Boolean
    abstract fun cannotTakeEnpassantLeft(board: Board): Boolean
    abstract fun canTakeEnpassantRight(board: Board): Boolean
    abstract fun cannotTakeEnpassantRight(board: Board): Boolean

    abstract fun isValidOneStepMove(to: Cell, board: Board): Boolean
    abstract fun isValidTwoStepsMove(to: Cell, board: Board): Boolean
    abstract fun isValidCapture(to: Cell, board: Board): Boolean
    abstract fun isValidEnPassant(to: Cell, board: Board): Boolean
    fun isOpponentTo(p: Pawn?): Boolean {
        return if (p == null) false
        else
        // p::class != this::class
            p.color != this.color
    }

    override fun toString(): String = symbol

    fun moveTo(newFile: Char, newRank: Int) {
        curAddr = "$newFile$newRank"
        curFile = newFile
        curRank = newRank
    }

    fun removeFromBoard() {
        this.curRank = -1
        this.curFile = 'z'
        this.curAddr = "$curFile$curRank"
    }

    fun cannotMove(board: Board): Boolean {
        return cannotMoveOneStep(board) &&
                cannotMoveTwoSteps(board) &&
                cannotTakeLeft(board) &&
                cannotTakeRight(board) &&
                cannotTakeEnpassantLeft(board) &&
                cannotTakeEnpassantRight(board)
    }
}

class WhitePawn(override val initFile: Char) : Pawn() {
    override val color = "White"
    override val symbol = "W"
    override val initRank = ChessParams.WHITE_PAWN_INIT_RANK
    override val initAddr = "$initFile$initRank"
    override var curAddr = "$initFile$initRank"

    override var curFile = initFile
    override var curRank = initRank
    override val opponentInitRank: Int
        get() = ChessParams.BLACK_PAWN_INIT_RANK
    override val opponentEnpassRank: Int
        get() = ChessParams.BLACK_PAWN_ENPASS_RANK

    override fun canMoveOneStep(board: Board): Boolean {
        val newCell = board[curFile, curRank + 1]
        return PawnMove.isValidMove("${curFile}${curRank}${curFile}${curRank + 1}") &&
                newCell!!.isFree()
    }

    override fun cannotMoveOneStep(board: Board): Boolean = !canMoveOneStep(board)

    override fun canMoveTwoSteps(board: Board): Boolean {
        val newCellOneStep = board[curFile, curRank + 1]
        val newCellTwoStep = board[curFile, curRank + 2]
        return PawnMove.isValidMove("${curFile}${curRank}${newCellOneStep?.addr}") &&
                PawnMove.isValidMove("${curFile}${curRank}${newCellTwoStep?.addr}") &&
                curRank == initRank &&
                newCellOneStep!!.isFree() &&
                newCellTwoStep!!.isFree()
    }

    override fun cannotMoveTwoSteps(board: Board) = !canMoveTwoSteps(board)

    override fun canTakeLeft(board: Board): Boolean {
        val newCell = board[curFile - 1, curRank + 1]
        return newCell != null &&
                PawnMove.isValidMove("${curFile}${curRank}${newCell.addr}") &&
                newCell.containsOpponent(this)
    }

    override fun cannotTakeLeft(board: Board) = !canTakeLeft(board)

    override fun canTakeRight(board: Board): Boolean {
        val newCell = board[curFile + 1, curRank + 1]
        return newCell != null &&
                PawnMove.isValidMove("${curFile}${curRank}${newCell.addr}") &&
                newCell.containsOpponent(this)
    }

    override fun cannotTakeRight(board: Board) = !canTakeRight(board)

    override fun canTakeEnpassantLeft(board: Board): Boolean {
        val leftSideCell = board[curFile - 1, curRank]
        return leftSideCell != null &&
                PawnMove.isValidMove("$${curFile}${curRank}${leftSideCell.addr}") &&
                curRank == opponentEnpassRank &&
                //  leftSideCell.content is BlackPawn &&
                leftSideCell.content!!.isOpponentTo(this) &&
                GameLog.lastMovePiece() == leftSideCell.content &&
                GameLog.lastMoveWasTwoSteps()
    }

    override fun cannotTakeEnpassantLeft(board: Board) = !canTakeEnpassantLeft(board)

    override fun canTakeEnpassantRight(board: Board): Boolean {
        val rightSideCell = board[curFile + 1, curRank]
        return rightSideCell != null &&
                PawnMove.isValidMove("${curFile}${curRank}${rightSideCell.addr}") &&
                curRank == opponentEnpassRank &&
                // rightSideCell.content is BlackPawn &&
                rightSideCell.content!!.isOpponentTo(this) &&
                GameLog.lastMovePiece() == rightSideCell.content &&
                GameLog.lastMoveWasTwoSteps()
    }

    override fun cannotTakeEnpassantRight(board: Board) = !canTakeEnpassantRight(board)

    override fun isValidOneStepMove(to: Cell, board: Board): Boolean {
        return canMoveOneStep(board)
    }

    override fun isValidTwoStepsMove(to: Cell, board: Board): Boolean {
        return canMoveTwoSteps(board)
    }

    override fun isValidCapture(to: Cell, board: Board): Boolean {
        return canTakeLeft(board) || canTakeRight(board) ||
                canTakeEnpassantLeft(board) ||
                canTakeEnpassantRight(board)
    }

    override fun isValidEnPassant(to: Cell, board: Board): Boolean {
        return canTakeEnpassantLeft(board) || canTakeEnpassantRight(board)
    }
}

class BlackPawn(override val initFile: Char) : Pawn() {
    override val color = "Black"
    override val symbol = "B"
    override val initRank = ChessParams.BLACK_PAWN_INIT_RANK
    override val initAddr = "$initFile$initRank"
    override var curAddr = "$initFile$initRank"

    override var curFile = initFile
    override var curRank = initRank
    override val opponentInitRank: Int
        get() = ChessParams.WHITE_PAWN_INIT_RANK
    override val opponentEnpassRank: Int
        get() = ChessParams.WHITE_PAWN_ENPASS_RANK

    override fun canMoveOneStep(board: Board): Boolean {
        return if (Cell.isValidCell("$curFile$curRank")) {
            board[curFile, curRank - 1]!!.isFree()
        } else {
            false
        }
    }

    override fun cannotMoveOneStep(board: Board): Boolean = !canMoveOneStep(board)

    override fun canMoveTwoSteps(board: Board): Boolean {
        return if (Cell.isValidCell("$curFile${curRank - 1}") &&
            Cell.isValidCell("$curFile${curRank - 2}")
        ) {
            curRank == initRank &&
                    board[curFile, curRank - 1]!!.isFree() &&
                    board[curFile, curRank - 2]!!.isFree()
        } else {
            false
        }
    }

    override fun cannotMoveTwoSteps(board: Board) = !canMoveTwoSteps(board)

    override fun canTakeLeft(board: Board): Boolean {
        val newCell = board[curFile - 1, curRank - 1]
        return newCell != null &&
                PawnMove.isValidMove("${this.curAddr}${newCell.addr}") &&
                newCell.containsOpponent(this)
    }

    override fun cannotTakeLeft(board: Board) = !canTakeLeft(board)

    override fun canTakeRight(board: Board): Boolean {
        val newCell = board[curFile + 1, curRank - 1]
        return newCell != null &&
                PawnMove.isValidMove("${this.curAddr}${newCell.addr}") &&
                newCell.containsOpponent(this)
    }

    override fun cannotTakeRight(board: Board) = !canTakeRight(board)

    override fun canTakeEnpassantLeft(board: Board): Boolean {
        val cellAddr = "${curFile - 1}$curRank"
        if (! Cell.isValidCell(cellAddr))
            return false
        // val leftSideCell = board[curFile - 1, curRank]
        val leftSideCell = board[cellAddr]
        val opponent = board.getPawnAt(cellAddr) ?: return false
        return PawnMove.isValidMove("${this.curAddr}${leftSideCell.addr}") &&
                curRank == opponentEnpassRank &&
                opponent.isOpponentTo(this) &&
                GameLog.lastMovePiece() == opponent &&
                GameLog.lastMoveWasTwoSteps()
    }

    override fun cannotTakeEnpassantLeft(board: Board) = !canTakeEnpassantLeft(board)

    override fun canTakeEnpassantRight(board: Board): Boolean {
        val cellAddr = "${curFile + 1}$curRank"
        if (! Cell.isValidCell(cellAddr))
            return false
        val rightSideCell = board[cellAddr]
        val opponent = board.getPawnAt(cellAddr) ?: return false
        return PawnMove.isValidMove("${this.curAddr}${rightSideCell.addr}") &&
                curRank == opponentEnpassRank &&
                opponent.isOpponentTo(this) &&
                GameLog.lastMovePiece() == opponent &&
                GameLog.lastMoveWasTwoSteps()
    }

    override fun cannotTakeEnpassantRight(board: Board) = !canTakeEnpassantRight(board)

    override fun isValidOneStepMove(to: Cell, board: Board): Boolean {
        return canMoveOneStep(board)
    }

    override fun isValidTwoStepsMove(to: Cell, board: Board): Boolean {
        return canMoveTwoSteps(board)
    }

    override fun isValidCapture(to: Cell, board: Board): Boolean {
        return canTakeLeft(board) || canTakeRight(board)
    }

    override fun isValidEnPassant(to: Cell, board: Board): Boolean {
        return canTakeEnpassantLeft(board) || canTakeEnpassantRight(board)
    }
}

class PawnMove(private val move: String, color: String) {
    /**
     * Pawn move in algebraic notation
     * a four character string in the form: "[a-h][1-8][a-h][1-8]"
     *
     * first two characters: address of from cell
     * second two characters: address of to cell
     *
     */
    val from: String                // address of from cell
        get() = move.from()
    val to: String                  // address of to cell
        get() = move.to()
    val fromCell: CellAddress       // source cell of move
        get() = CellAddress(from)
    val toCell: CellAddress         // destination cell of move
        get() = CellAddress(to)
    val isAcceptable: Boolean       // is properly formed legal move
        get() = move.matches("[a-h][1-8][a-h][1-8]".toRegex())

    val moveType: MoveType
        get() = parseMove()
    val fromFile: Char
        get() = from[0]
    val fromRank: Int
        get() = from[1].toString().toInt()
    val toFile: Char
        get() = to[0]
    val toRank: Int
        get() = to[1].toString().toInt()
    val movingPawnColor = color

    override fun toString(): String = move
    private fun String.from() = this.substring(0..1)
    private fun String.to() = this.substring(2..3)

    private fun parseMove(): MoveType {
        return when (movingPawnColor) {
            "White" -> {
                when {
                    fromFile == toFile && toRank == fromRank + 1 -> MoveType.WHITE_ONE_STEP
                    fromRank == ChessParams.WHITE_PAWN_INIT_RANK &&
                            fromFile == toFile &&
                            toRank == fromRank + 2 -> MoveType.WHITE_TWO_STEPS

                    toFile == fromFile - 1 && toRank == fromRank + 1 -> MoveType.WHITE_TAKE_LEFT
                    toFile == fromFile + 1 && toRank == fromRank + 1 -> MoveType.WHITE_TAKE_RIGHT
                    else -> MoveType.ILLEGAL_MOVE
                }
            }

            "Black" -> {
                when {
                    fromFile == toFile && toRank == fromRank - 1 -> MoveType.BLACK_ONE_STEP
                    fromRank == ChessParams.BLACK_PAWN_INIT_RANK &&
                            fromFile == toFile &&
                            toRank == fromRank - 2 -> MoveType.BLACK_TWO_STEPS

                    toFile == fromFile - 1 && toRank == fromRank - 1 -> MoveType.BLACK_TAKE_LEFT
                    toFile == fromFile + 1 && toRank == fromRank - 1 -> MoveType.BLACK_TAKE_RIGHT
                    else -> MoveType.ILLEGAL_MOVE
                }
            }

            else -> MoveType.ILLEGAL_MOVE
        }
    }

    companion object {
        fun isValidMove(move: String): Boolean {
            return move.matches("[a-h][1-8][a-h][1-8]".toRegex()) &&
                    move[0] in ChessParams.VALID_FILES &&
                    move[1].toString().toInt() in ChessParams.VALID_RANKS &&
                    move[2] in ChessParams.VALID_FILES &&
                    move[3].toString().toInt() in ChessParams.VALID_RANKS
        }
    }
}

enum class MoveType {
    WHITE_ONE_STEP,
    BLACK_ONE_STEP,
    WHITE_TWO_STEPS,
    BLACK_TWO_STEPS,
    WHITE_TAKE_LEFT,        // regular or enpassant
    BLACK_TAKE_LEFT,
    WHITE_TAKE_RIGHT,       // regular or enpassant
    BLACK_TAKE_RIGHT,
    ILLEGAL_MOVE;
}
