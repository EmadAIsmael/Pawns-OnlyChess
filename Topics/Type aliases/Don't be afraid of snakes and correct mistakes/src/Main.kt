// here is a mistake!
typealias Snake = Pet.Reptile.Snake
// don't change the code below!
class Pet {
    class Reptile {
        data class Snake(val sound: String)
    }
}