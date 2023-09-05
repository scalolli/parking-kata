package parking

import kotlin.math.abs
import kotlin.math.pow

class Parking(
    private val squareSize: Int,
    private val pedestrianExits: List<Int>,
    private val disabledBays: List<Int>
) {
    sealed class Bay(open val bayNumber: Int)
    sealed class ParkingBay(override val bayNumber: Int, open var car: Char?) : Bay(bayNumber)
    private class DisabledParkingBay(override val bayNumber: Int, override var car: Char?) : ParkingBay(bayNumber, car)
    private class RegularParkingBay(override val bayNumber: Int, override var car: Char?) : ParkingBay(bayNumber, car)
    private class Exit(override val bayNumber: Int) : Bay(bayNumber)

    private val parkingBays = mutableListOf<Bay>()

    init {
        (0 until squareSize.toDouble().pow(2.toDouble()).toInt()).forEach { index ->
            if (pedestrianExits.contains(index)) {
                parkingBays.add(Exit(index))
            } else if (disabledBays.contains(index)) {
                parkingBays.add(DisabledParkingBay(index, null))
            } else
                parkingBays.add(RegularParkingBay(index, null))
        }
    }

    fun getAvailableBays(): Int =
        parkingBays.filter {
            when (it) {
                is ParkingBay -> it.car == null
                else -> false
            }
        }.size

    fun parkCar(car: Char): Int {
        return when (car) {
            'C',
            'M' -> {
                val emptyBays = getEmptyParkingBays()
                parkCar(emptyBays, car)
            }

            'D' -> {
                val emptyBays = getEmptyDisabledParkingBays()
                if (emptyBays.isNotEmpty()) parkCar(emptyBays, car) else -1
            }

            else -> -1
        }
    }

    private fun getEmptyParkingBays(): List<ParkingBay> {
        return parkingBays.filter { it is ParkingBay && it.car == null }.map { it as ParkingBay }
    }

    private fun getEmptyDisabledParkingBays(): List<ParkingBay> {
        return parkingBays.filter { it is DisabledParkingBay && it.car == null }.map { it as ParkingBay }
    }

    private fun parkCar(emptyBays: List<ParkingBay>, car: Char): Int {
        val suitableBaysOrderedByClosetToExit =
            pedestrianExits
                .flatMap { exit -> emptyBays.map { bay -> abs(bay.bayNumber - exit) to bay } }
                .sortedBy { it.first }
        val bay = suitableBaysOrderedByClosetToExit.firstOrNull()?.second
        bay?.car = car
        return bay?.bayNumber ?: -1
    }

    fun unParkCar(bayNumber: Int): Boolean {
        val foundBay = parkingBays.find { it is ParkingBay && it.bayNumber == bayNumber } as? ParkingBay
        foundBay?.car = null
        return foundBay != null
    }

    override fun toString(): String {
        return parkingBays.mapIndexed { index, bay ->
            val stringPart = when (bay) {
                is RegularParkingBay -> bay.car?.toString() ?: "U"
                is DisabledParkingBay -> bay.car?.toString() ?: "@"
                is Exit -> "="
            }

            val separator = if (index != 0 && index != (parkingBays.size - 1) && (index + 1) % squareSize == 0)
                "\n"
            else ""
            stringPart + separator
        }.joinToString(separator = "")
    }
}

