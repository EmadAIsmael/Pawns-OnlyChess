class ListUtils {
    companion object Info {

        // define info method here
        fun <T> info(list: List<T>): String {
            return "[${list.joinToString()}]"
        }

    }
}
