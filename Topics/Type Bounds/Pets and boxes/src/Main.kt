class Box<T : Animal> {
    var content: T? = null
    fun add(obj: T) {
        content = obj
    }
}

// Don't change the class below
open class Animal
class Cat : Animal()