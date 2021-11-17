package chess

class PawnsOnlyChess {
    private val rankCount = 8           // rows in chess are called "ranks".
    private val fileCount = 8           // columns in chess are called "files".
    private val ranks = ('1'..'8').toList()
    private val files = ('a'..'h').toList()
    private val board = Array(8) {
        Array(8) {
            ' '
        }
    }

    private lateinit var players: Array<Player>

    private fun setInitPositions() {
        var rank = 2
        files.forEach { file ->
            board[rank - 1][file - 'a'] = 'W'
        }
        rank = 7
        files.forEach { file ->
            board[rank - 1][file - 'a'] = 'B'
        }
    }

    fun play() {
        println("Pawns-Only Chess")
        getPlayerNames()
        setInitPositions()
        displayBoard()

        var move = ""
        var turn = 0
        while (move != "exit") {
            move = promptForMove(turn)
            if (!isValidMove(move)) {
                println("Invalid Input")
                continue
            }
            turn++
            turn %= 2
        }

        println("Bye!")
    }

    private fun isValidMove(move: String): Boolean {
        return move == "exit" ||
                (move.length == 4 &&
                        move[0] in files &&
                        move[2] in files &&
                        move[1] in ranks &&
                        move[3] in ranks)
    }

    private fun promptForMove(turn: Int): String {
        println("${players[turn].name}'s turn:")
        return players[turn].makeMove()
    }

    private fun getPlayerNames() {
        println("First Player's name:")
        val firstPlayerName = readLine()!!
        println("Second Player's name:")
        val secondPlayerName = readLine()!!
        players = arrayOf(
            Player(firstPlayerName),
            Player(secondPlayerName)
        )
    }

    private fun displayBoard() {
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