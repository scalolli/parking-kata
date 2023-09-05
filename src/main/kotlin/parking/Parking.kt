package parking

import kotlin.math.abs
import kotlin.math.pow

class Parking(
    private val squareSize: Int,
    private val pedestrianExits: List<Int>,
    private val disabledBays: List<Int>
) {
    sealed class Bay(open val bayNumber: Int)
    sealed class ParkingBay(override val bayNumber: Int, open val state: ParkingBayState) : Bay(bayNumber)
    private data class DisabledParkingBay(override val bayNumber: Int, override val state: ParkingBayState) :
        ParkingBay(bayNumber, state) {
        override fun toString() =
            when (state) {
                is Empty -> "@"
                is Occupied -> state.car.toString()
            }
    }

    private data class RegularParkingBay(override val bayNumber: Int, override val state: ParkingBayState) :
        ParkingBay(bayNumber, state) {
        override fun toString() =
            when (state) {
                is Empty -> "U"
                is Occupied -> state.car.toString()
            }
    }

    private class Exit(override val bayNumber: Int) : Bay(bayNumber)

    sealed class ParkingBayState
    object Empty : ParkingBayState()
    data class Occupied(val car: Char) : ParkingBayState()

    private val parkingBays = mutableListOf<Bay>()

    init {
        (0 until totalNumberOfParkingBays()).forEach { index ->
            if (pedestrianExits.contains(index)) {
                parkingBays.add(Exit(index))
            } else if (disabledBays.contains(index)) {
                parkingBays.add(DisabledParkingBay(index, Empty))
            } else
                parkingBays.add(RegularParkingBay(index, Empty))
        }
    }

    private fun totalNumberOfParkingBays() = squareSize.toDouble().pow(2.toDouble()).toInt()

    fun getAvailableBays(): Int = getEmptyParkingBays().size

    fun parkCar(car: Char): Int {
        return when (car) {
            'C', 'M' -> parkCar(getEmptyParkingBays(), car)
            'D' -> if (getEmptyDisabledParkingBays().isNotEmpty()) parkCar(getEmptyDisabledParkingBays(), car) else -1
            else -> -1
        }
    }

    private fun getEmptyParkingBays(): List<ParkingBay> {
        return parkingBays.filter { it is ParkingBay && it.state is Empty }.map { it as ParkingBay }
    }

    private fun getEmptyDisabledParkingBays(): List<ParkingBay> {
        return parkingBays.filter { it is DisabledParkingBay && it.state is Empty }.map { it as ParkingBay }
    }

    private fun parkCar(emptyBays: List<ParkingBay>, car: Char): Int {
        val suitableBaysOrderedByClosetToExit =
            pedestrianExits
                .flatMap { exit -> emptyBays.map { bay -> abs(bay.bayNumber - exit) to bay } }
                .sortedBy { it.first }
        val bay = suitableBaysOrderedByClosetToExit.firstOrNull()?.second
        bay?.let {
            parkingBays.remove(it)
            val updatedBay = when (it) {
                is DisabledParkingBay -> it.copy(state = Occupied(car))
                is RegularParkingBay -> it.copy(state = Occupied(car))
            }
            parkingBays.add(updatedBay)
        }

        return bay?.bayNumber ?: -1
    }

    fun unParkCar(bayNumber: Int): Boolean {
        val foundBay = parkingBays.find { it is ParkingBay && it.bayNumber == bayNumber } as? ParkingBay
        foundBay?.let {
            parkingBays.remove(it)
            val updatedBay = when (it) {
                is DisabledParkingBay -> it.copy(state = Empty)
                is RegularParkingBay -> it.copy(state = Empty)
            }
            parkingBays.add(updatedBay)
        }
        return foundBay != null
    }

    override fun toString(): String {
        return parkingBays.mapIndexed { index, bay ->
            val stringPart = when (bay) {
                is RegularParkingBay -> bay.toString()
                is DisabledParkingBay -> bay.toString()
                is Exit -> "="
            }

            val separator = if (index != 0 && index != (parkingBays.size - 1) && (index + 1) % squareSize == 0)
                "\n"
            else ""
            stringPart + separator
        }.joinToString(separator = "")
    }
}

