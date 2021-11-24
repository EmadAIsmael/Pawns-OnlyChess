class SomeCollection<T>(val list: List<T>) {
    // write your code here
    fun invert() = println("[${list.reversed().joinToString()}]")
}