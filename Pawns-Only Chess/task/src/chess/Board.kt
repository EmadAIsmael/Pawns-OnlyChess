package chess

operator fun Array<Pawn>.get(file: Char): Pawn {
    return this[file - 'a']
}

class Board {

    private val files = ('a'..'h')
    val whitePawns = Array<Pawn>(ChessParams.PAWN_COUNT) { WhitePawn(files.elementAt(it)) }
    val blackPawns = Array<Pawn>(ChessParams.PAWN_COUNT) { BlackPawn(files.elementAt(it)) }
    private val board = Array(ChessParams.RANK_COUNT) { rank ->
        Array(ChessParams.FILE_COUNT) { file ->
            Cell(files.elementAt(file), rank + 1)
        }
    }

    operator fun get(cell: String): Cell {
        if (Cell.isValidCell(cell)
        ) {
            val col = cell[0] - 'a'
            val row = cell[1].toString().toInt() - 1
            return this.board[row][col]
        } else {
            throw Exception("Invalid Board Cell Address!")
        }
    }

    operator fun get(file: Char, rank: Int): Cell? {
        return if (Cell.isValidCell("$file$rank")) {
            val col = file - 'a'
            val row = rank - 1
            this.board[row][col]
        } else {
            null
        }
    }

    operator fun set(cell: String, value: Pawn) {
        if (Cell.isValidCell(cell)) {
            val col = cell[0] - 'a'
            val row = cell[1].toString().toInt() - 1
            this.board[row][col].content = value
        } else {
            throw Exception("Invalid Board Cell Address!")
        }
    }

    operator fun set(file: Char, rank: Int, value: Pawn) {
        if (Cell.isValidCell("$file$rank")) {
            val col = file - 'a'
            val row = rank - 1
            this.board[row][col].content = value
        } else {
            throw Exception("Invalid Board Cell Address!")
        }
    }

    init {
        structurePawns()
    }

    fun getPawnAt(addr: String): Pawn? = this[addr].content

    fun getPawnAt(addr: CellAddress): Pawn? = getPawnAt(addr.addr)

    fun display() {
        println(this.toString())
    }

    override fun toString(): String {
        val files = "   " + files.joinToString("") { c ->
            " $c  "
        }
        var str = ""
        val topBottomBoarder = "  +${"---+".repeat(ChessParams.FILE_COUNT)}\n"

        str += topBottomBoarder
        str += board.reversed().mapIndexed { r, rankRow ->
            "${ChessParams.RANK_COUNT - r} |" +
                    rankRow.mapIndexed { _, p ->
                        " ${if (p.content != null) if (!p.content!!.isTaken) p.content.toString() else " " else " "} |"
                    }.joinToString("") +
                    "\n" + topBottomBoarder
        }.joinToString("")

        str += files
        return str
    }

    private fun structurePawns() {
        whitePawns.forEach {
            this[it.initAddr] = it
        }

        blackPawns.forEach {
            this[it.initAddr] = it
        }
    }
}

class Cell(var file: Char, var rank: Int, var content: Pawn? = null) {
    constructor(cellAddr: CellAddress, content: Pawn? = null) : this(cellAddr.file, cellAddr.rank, content)
    constructor(cellAddr: String, content: Pawn? = null) : this(CellAddress(cellAddr), content)

    val addr = "$file$rank"

    override fun toString(): String = "${this.file}${this.rank}"
    fun receive(piece: Pawn) {
        content = piece
    }

    fun setEmpty() {
        content = null
    }

    fun containsOpponent(to: Pawn): Boolean {
        // return this::class != to::class
        return this.content is Pawn && this.content?.color != to.color
    }

    fun isFree(): Boolean = content == null

    companion object {
        fun isValidCell(cellAddr: String): Boolean {
            return cellAddr.matches("[a-h][1-8]".toRegex()) &&
                    cellAddr[0] in ChessParams.VALID_FILES &&
                    cellAddr[1].toString().toInt() in 1..ChessParams.RANK_COUNT
        }
    }
}

class CellAddress(val addr: String) {
    /**
     * Address of one cell of the playing board
     * two characters in the form: "[a-h][1-8]"
     *
     * rank: is the board row number
     * file: is he board column label
     *
     */
    val rank: Int
        get() = addr.last().toString().toInt()

    val file: Char
        get() = addr.first()
}
