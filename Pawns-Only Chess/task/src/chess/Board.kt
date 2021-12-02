package chess

class Board {
    private val rankCount = 8           // rows in chess are called "ranks".
    private val fileCount = 8           // columns in chess are called "files".
    // private val ranks = ('1'..'8')
    private val files = ('a'..'h')
    private val board = Array(8) {
        Array<Pawn?>(8) {
            null
        }
    }

    fun setSquare(rank: Int, file: Char, piece: Pawn? = null) {
        board[rank - 1][file - 'a'] = piece
    }

    fun resetSquare(rank: Int, file: Char) {
        board[rank - 1][file - 'a'] = null
    }

    fun getSquare(rank: Int, file: Char): Pawn? = board[rank - 1][file - 'a']

    fun getSquare(sqr: Square): Pawn? = board[sqr.rank - 1][sqr.file - 'a']

    fun isFree(to: Square): Boolean {
        return getSquare(to.rank, to.file) == null
    }

    fun display() {
        println(this.toString())
    }

    override fun toString(): String {
        val files = "   " + files.joinToString("") { c ->
            " $c  "
        }
        var str = ""
        val topBottomBoarder = "  +${"---+".repeat(this.fileCount)}\n"

        str += topBottomBoarder
        str += board.reversed().mapIndexed { r, rankRow ->
            "${rankCount - r} |" +
                    rankRow.mapIndexed { _, p ->
                        " ${if (p != null) if (!p.isTaken) p.toString() else " " else " "} |"
                    }.joinToString("") +
                    "\n" + topBottomBoarder
        }.joinToString("")

        str += files
        return str
    }

    fun structurePieces(players: Array<Player>) {
        players.forEach { player ->
            player.pieces.forEach { pawn ->
                setSquare(pawn.initialRank, pawn.initSquare.file, pawn)
            }
        }
    }

    fun isAcceptableMove(move: String): Boolean {
        /*return move == "exit" ||
                (move.length == 4 &&
                        move[0] in files &&
                        move[2] in files &&
                        move[1] in ranks &&
                        move[3] in ranks)*/
        return move.matches("(exit)|([a-h][1-8][a-h][1-8])".toRegex())
    }
}

data class Square(var file: Char, var rank: Int) {
    fun isValidSquare(): Boolean = this.toString().matches("[a-h][1-8]".toRegex())
    override fun toString(): String = this.file.toString() + this.rank.toString()
}

class Move(move: String) {
    val curSquare = move.substring(0..1)
    private val newSquare = move.substring(2..3)

    val from: Square
        get() = Square(curSquare.first(), curSquare.last().toString().toInt())

    val to: Square
        get() = Square(newSquare.first(), newSquare.last().toString().toInt())
}
