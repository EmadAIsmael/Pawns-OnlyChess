// Posted from EduTools plugin
import java.util.Scanner

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    // put your code here
    var n = scanner.nextInt()

    val d0 = n % 10
    n = n / 10
    val d1 = n % 10
    n = n / 10
    val d2 = n % 10

    println(d0 + d1 + d2)
}