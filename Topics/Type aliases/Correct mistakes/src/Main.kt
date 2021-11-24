typealias User = Triple<String, String, Int>
const val YEAR = 2021

fun fetchUser(): User {
    return User("Hyperskill", "User", YEAR)
}