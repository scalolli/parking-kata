package parking


class Parking(val squareSize: Int) {
    fun getAvailableBays(): Int = squareSize * 4
}

class ParkingBuilder {
    private var squareSize: Int = 0

    fun withSquareSize(squareSize: Int): ParkingBuilder {
        this.squareSize = squareSize
        return this
    }

    fun build() = Parking(squareSize)
}