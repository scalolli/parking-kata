package parking

import kotlin.math.abs
import kotlin.math.pow

class Parking(
    squareSize: Int,
    private val pedestrianExits: List<Int>,
    private val disabledBays: List<Int>
) {
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

    fun getAvailableBays(): Int = parkingBays.filter {
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
        val bays =
            pedestrianExits
                .flatMap { exit ->
                    emptyBays.map { bay -> abs(bay.bayNumber - exit) to bay }.sortedBy { it.first }
                }
                .sortedBy { it.first }
        val selectedBayNumber = bays.first().second.bayNumber
        val selectedBay = parkingBays.find { it.bayNumber == selectedBayNumber }
        selectedBay?.let {
            when (it) {
                is RegularParkingBay -> {
                    it.car = car
                    true
                }

                is DisabledParkingBay -> {
                    it.car = 'D'
                    true
                }

                else -> null
            }
        }
        return selectedBayNumber
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

//    todo: WIP
//    override fun toString(): String {
//        val stringBuffer = StringBuffer()
//
//        parkingBays.withIndex().forEach { it ->
//            when (it.value.car) {
//                'C',
//                'M' -> stringBuffer.append(it.value.car!!)
//
//            }
//
//        }
//
//        return super.toString()
//    }

    sealed class Bay(open val bayNumber: Int)
    sealed class ParkingBay(override val bayNumber: Int, open var car: Char?) : Bay(bayNumber)
    class DisabledParkingBay(override val bayNumber: Int, override var car: Char?) : ParkingBay(bayNumber, car)
    class RegularParkingBay(override val bayNumber: Int, override var car: Char?) : ParkingBay(bayNumber, car)
    class Exit(override val bayNumber: Int) : Bay(bayNumber)
}
