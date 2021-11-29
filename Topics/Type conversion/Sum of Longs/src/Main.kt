fun main() {
    // write your code here
    /*val x = readLine()!!.toLong()
    val y = readLine()!!.toLong()
    println(x + y)*/
    println(
        Array(2) {
            readLine()!!.toLong()
        }.sum()
    )
}