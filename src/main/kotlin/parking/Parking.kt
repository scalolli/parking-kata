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
    class DisabledParkingBay(override val bayNumber: Int, override var car: Char?) : ParkingBay(bayNumber, car)
    class RegularParkingBay(override val bayNumber: Int, override var car: Char?) : ParkingBay(bayNumber, car)
    class Exit(override val bayNumber: Int) : Bay(bayNumber)

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

    private fun getEmptyParkingBays(): List<Bay> {
        return parkingBays.filter {
            when (it) {
                is ParkingBay -> it.car == null
                else -> false
            }
        }
    }

    private fun getEmptyDisabledParkingBays(): List<Bay> {
        return parkingBays.filter {
            when (it) {
                is DisabledParkingBay -> it.car == null
                else -> false
            }
        }
    }

    private fun parkCar(emptyBays: List<Bay>, car: Char): Int {
        val suitableBaysOrderedByClosetToExit =
            pedestrianExits
                .flatMap { exit -> emptyBays.map { bay -> abs(bay.bayNumber - exit) to bay } }
                .sortedBy { it.first }
        val bay = suitableBaysOrderedByClosetToExit.firstOrNull()?.second
        return bay?.let {
            return when (it) {
                is ParkingBay -> {
                    it.car = car
                    it.bayNumber
                }
                else -> -1
            }
        } ?: -1
    }

    fun unParkCar(bayNumber: Int): Boolean {
        return parkingBays.find {
            when (it) {
                is ParkingBay ->
                    if (it.bayNumber == bayNumber) {
                        it.car = null
                        true
                    } else false

                else -> false
            }
        } != null
    }

    override fun toString(): String {
        val stringBuffer = StringBuffer()

        parkingBays.withIndex().forEach { (index, bay) ->
            when (bay) {
                is RegularParkingBay -> bay.car?.let { stringBuffer.append(it) } ?: stringBuffer.append("U")
                is DisabledParkingBay -> bay.car?.let { stringBuffer.append(it) } ?: stringBuffer.append("@")
                is Exit -> stringBuffer.append("=")
            }

            if (index != 0 && index != (parkingBays.size - 1) && (index + 1) % squareSize == 0)
                stringBuffer.append("\n")
        }

        return stringBuffer.toString()
    }
}

