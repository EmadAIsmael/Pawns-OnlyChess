data class Box<T>(val item: T)

fun <T> Box<T>.guessBox() {
    println("In this box you have: ${this.item}")
}