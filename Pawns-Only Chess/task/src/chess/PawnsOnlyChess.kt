package chess

class PawnsOnlyChess {
    private val rankCount = 8           // rows in chess are called "ranks".
    private val fileCount = 8           // columns in chess are called "files".
    private val ranks = ('1'..'8').toList()
    private val files = ('a'..'h').toList()
    private val WHITE_PAWN_INITIAL_RANK = 2
    private val BLACK_PAWN_INITIAL_RANK = 7
    private val board = Array(8) {
        Array(8) {
            ' '
        }
    }
    private var turn = 0

    private lateinit var players: Array<Player>

    private fun setInitPositions() {
        var rank = WHITE_PAWN_INITIAL_RANK
        files.forEach { file ->
            // board[rank - 1][file - 'a'] = 'W'
            setBoardPosition(rank, file, 'W')
        }
        rank = BLACK_PAWN_INITIAL_RANK
        files.forEach { file ->
            // board[rank - 1][file - 'a'] = 'B'
            setBoardPosition(rank, file, 'B')
        }
    }

    private fun setBoardPosition(rank: Int, file: Char, piece: Char = ' ') {
        board[rank - 1][file - 'a'] = piece
    }

    private fun getBoardPosition(rank: Int, file: Char): Char {
        return board[rank - 1][file - 'a']
    }

    fun play() {
        println("Pawns-Only Chess")
        getPlayerNames()
        setInitPositions()
        displayBoard()

        var move = ""
        // var turn = 0
        while (move != "exit") {
            move = promptForMove(turn)
            if (!isAcceptableMove(move)) {
                println("Invalid Input")
                continue
            } else if (move == "exit") {
                continue
            } else {
                if (movePawn(move)) {
                    displayBoard()
                    turn++
                    turn %= 2
                }
            }
        }

        println("Bye!")
    }

    private fun isValidWhitePawnInitialMove(from: String, to: String): Boolean {
        return (from.last().toString().toInt() == WHITE_PAWN_INITIAL_RANK) &&
                (from.first() == to.first()) &&
                (to.last().toString().toInt() - from.last().toString().toInt() == 2)
    }

    private fun isValidWhitePawnMove(from: String, to: String): Boolean {
        return (from.first() == to.first()) &&
                (to.last().toString().toInt() - from.last().toString().toInt() == 1)
    }

    private fun isValidBlackPawnInitialMove(from: String, to: String): Boolean {
        return (from.last().toString().toInt() == BLACK_PAWN_INITIAL_RANK) &&
                (from.first() == to.first()) &&
                (to.last().toString().toInt() - from.last().toString().toInt() == -2)
    }

    private fun isValidBlackPawnMove(from: String, to: String): Boolean {
        return (from.first() == to.first()) &&
                (to.last().toString().toInt() - from.last().toString().toInt() == -1)
    }

    private fun isFreeDestination(to: String): Boolean {
        val newFile = to.first()
        val newRank = to.last().toString().toInt()
        return getBoardPosition(newRank, newFile) == ' '
    }

    private fun movePawn(move: String): Boolean {
        val from = move.substring(0..1)
        val to = move.substring(2..3)

        val oldFile = from.first()
        val oldRank = from.last().toString().toInt()
        if (getBoardPosition(oldRank, oldFile) != players[turn].piece) {
            println("No ${players[turn].pieceColor} pawn at $from")
            return false
        }
        return when (getBoardPosition(oldRank, oldFile)) {
            'W' -> {
                if (isFreeDestination(to) &&
                    (isValidWhitePawnInitialMove(from, to) ||
                    isValidWhitePawnMove(from, to))) {

                    setBoardPosition(oldRank, oldFile)

                    val newFile = to.first()
                    val newRank = to.last().toString().toInt()
                    setBoardPosition(newRank, newFile, 'W')
                    true
                } else {
                    println("Invalid Input")
                    false
                }
            }
            'B' -> {
                if (isFreeDestination(to) &&
                    (isValidBlackPawnInitialMove(from, to) ||
                    isValidBlackPawnMove(from, to))) {

                    setBoardPosition(oldRank, oldFile)

                    val newFile = to.first()
                    val newRank = to.last().toString().toInt()
                    setBoardPosition(newRank, newFile, 'B')
                    true
                } else {
                    println("Invalid Input")
                    false
                }
            }
            else -> false
        }
    }

    private fun isAcceptableMove(move: String): Boolean {
        return move == "exit" ||
                (move.length == 4 &&
                        move[0] in files &&
                        move[2] in files &&
                        move[1] in ranks &&
                        move[3] in ranks)
    }

    private fun promptForMove(turn: Int): String {
        println("${players[turn].name}'s turn:")
        return players[turn].requestMove()
    }

    private fun getPlayerNames() {
        println("First Player's name:")
        val firstPlayerName = readLine()!!
        println("Second Player's name:")
        val secondPlayerName = readLine()!!
        players = arrayOf(
            Player(name = firstPlayerName, piece = 'W', pieceColor = "White"),
            Player(name = secondPlayerName, piece = 'B', pieceColor = "Black")
        )
    }

    private fun displayBoard() {
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
                        " $p |"
                    }.joinToString("") +
                    "\n" + topBottomBoarder
        }.joinToString("")

        str += files
        return str
    }
}