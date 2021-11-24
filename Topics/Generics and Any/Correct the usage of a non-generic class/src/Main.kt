class Holder(var value: Any) {

    fun set(newValue: Any) {
        value = newValue
    }

    fun get(): Any {
        return value
    }
}

const val V: Int = 256

fun main() {
    val holder: Holder = Holder(0)
    holder.set(V)

    // correct the line to make the code compile
    val value: Int = holder.get() as Int

    // do not change
    println(value)
}
