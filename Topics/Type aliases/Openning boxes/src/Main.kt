class BiggerBox {
    class InnerBox<T>(private var items: List<T>) {
        fun getSomethingFromBox(): List<T> {
            return items
        }
    }
}
typealias Box<T> = BiggerBox.InnerBox<T>
fun main() {
    val box = Box(listOf("lion", "rose"))
    // print the number of items
    println(box.getSomethingFromBox().size)
}