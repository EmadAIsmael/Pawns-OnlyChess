// Posted from EduTools plugin
import java.util.Scanner

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    // put your code here

    val n0 = scanner.nextInt()
    val n1 = scanner.nextInt()
    val n2 = scanner.nextInt()

    val d0 = n0 / 2 + n0 % 2
    val d1 = n1 / 2 + n1 % 2
    val d2 = n2 / 2 + n2 % 2

    println(d0 + d1 + d2)
}