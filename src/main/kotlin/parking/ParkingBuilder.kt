package parking


class ParkingBuilder {
    private var squareSize: Int = 0
    private val pedestrianExits = mutableListOf<Int>()
    private val disabledBays = mutableListOf<Int>()

    fun withSquareSize(squareSize: Int): ParkingBuilder {
        this.squareSize = squareSize
        return this
    }

    fun withPedestrianExit(pedestrianExitBayNumber: Int): ParkingBuilder {
        pedestrianExits.add(pedestrianExitBayNumber)
        return this
    }

    fun withDisabledBay(number: Int): ParkingBuilder {
        disabledBays.add(number)
        return this
    }

    fun build() = Parking(squareSize, pedestrianExits, disabledBays)
}