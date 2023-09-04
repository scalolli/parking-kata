package parking

import kotlin.math.abs
import kotlin.math.pow

private data class ParkingBay(val bayNumber: Int, val isDisabledBay: Boolean, var car: Char?)

class Parking(
    squareSize: Int,
    private val pedestrianExits: List<Int>,
    private val disabledBays: List<Int>
) {
    private val parkingBays = mutableListOf<ParkingBay>()

    init {
        (0 until squareSize.toDouble().pow(2.toDouble()).toInt()).forEach { index ->
            if (!pedestrianExits.contains(index)) {
                parkingBays.add(ParkingBay(index, disabledBays.contains(index), null))
            }
        }
    }

    fun getAvailableBays(): Int = parkingBays.filter { it.car == null }.size

    fun parkCar(car: Char): Int {
        return when (car) {
            'C',
            'M' -> {
                val emptyBays = parkingBays.filter { it.car == null && !it.isDisabledBay }
                parkCar(emptyBays, car)
            }

            'D' -> {
                val emptyBays = parkingBays.filter { it.car == null && it.isDisabledBay }
                if (emptyBays.isNotEmpty()) parkCar(emptyBays, car) else -1
            }

            else -> -1
        }
    }

    private fun parkCar(emptyBays: List<ParkingBay>, car: Char): Int {
        val bays =
            pedestrianExits
                .flatMap { exit ->
                    emptyBays.map { bay -> abs(bay.bayNumber - exit) to bay }.sortedBy { it.first }
                }
                .sortedBy { it.first }
        val selectedBayNumber = bays.first().second.bayNumber
        val selectedBay = parkingBays.find { it.bayNumber == selectedBayNumber }
        selectedBay?.let {
            parkingBays.remove(selectedBay)
            parkingBays.add(selectedBayNumber, it.copy(car = car))
        }
        return selectedBayNumber
    }

    fun unParkCar(bayNumber: Int): Boolean {
        return parkingBays.find { bay ->
            if (bay.bayNumber == bayNumber) {
                bay.car = null
                true
            } else
                false
        } != null
    }
}
