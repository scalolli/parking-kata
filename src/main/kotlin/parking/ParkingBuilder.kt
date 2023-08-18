package parking

import kotlin.math.pow


class Parking(private val squareSize: Int, private val pedestrianExits: List<Int>) {
    fun getAvailableBays(): Int = squareSize.toDouble().pow(2.toDouble()).toInt() - (pedestrianExits.size)
}

class ParkingBuilder {
    private var squareSize: Int = 0
    private val pedestrianExits = mutableListOf<Int>()

    fun withSquareSize(squareSize: Int): ParkingBuilder {
        this.squareSize = squareSize
        return this
    }

    fun withPedestrianExit(pedestrianExitBayNumber: Int): ParkingBuilder {
        pedestrianExits.add(pedestrianExitBayNumber)
        return this
    }

    fun build() = Parking(squareSize, pedestrianExits)
}