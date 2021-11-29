// Posted from EduTools plugin
import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    // put your code here
    val dur = scanner.nextInt()
    val food = scanner.nextInt()
    val flight = scanner.nextInt()
    val night = scanner.nextInt()

    val cost = food * dur + 2 * flight + night * (dur - 1)
    println(cost)
}