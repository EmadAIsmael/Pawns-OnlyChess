package chess

class PawnsOnlyChess {
    private val rankCount = 8           // rows in chess are called "ranks".
    private val fileCount = 8           // columns in chess are called "files".
    private val ranks = (1..8).toList()
    private val files = ('a'..'h').toList()
    private val board = Array(8) {
        Array(8) {
            ' '
        }
    }

    fun setInitPositions() {
        var rank = 2
        files.forEach { file ->
            board[rank - 1][file - 'a'] = 'W'
        }
        rank = 7
        files.forEach { file ->
            board[rank - 1][file - 'a'] = 'B'
        }
    }

    fun displayBoard() {
        println(this.toString())
    }

    override fun toString(): String {
        val files = "   " + ('a'..'h').joinToString("") { c ->
            " $c  "
        }
        var str = ""
        val topBottomBoarder = "  +${"---+".repeat(this.fileCount)}\n"

        str += topBottomBoarder
        str += board.reversed().mapIndexed { r, rankRow ->
            "${rankCount - r} |" +
            rankRow.mapIndexed { _, p ->
                " $p |"
            }.joinToString("") +
                    "\n" + topBottomBoarder
        }.joinToString("")

        str += files

        return str
    }
}