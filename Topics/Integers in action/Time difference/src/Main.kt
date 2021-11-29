// Posted from EduTools plugin
import java.util.Scanner

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    // put your code here
    val h0 = scanner.nextInt()
    val m0 = scanner.nextInt()
    val s0 = scanner.nextInt()

    val h1 = scanner.nextInt()
    val m1 = scanner.nextInt()
    val s1 = scanner.nextInt()

    var seconds = (h1 - h0) * 60 * 60
    seconds = seconds + (m1 - m0) * 60
    seconds = seconds + (s1 - s0)

    println(seconds)

}