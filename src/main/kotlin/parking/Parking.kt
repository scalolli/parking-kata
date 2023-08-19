package parking

import kotlin.math.abs
import kotlin.math.pow

private data class ParkingBay(val bayNumber: Int, val isDisabledBay: Boolean, val car: Char?)

class Parking(
  private val squareSize: Int,
  private val pedestrianExits: List<Int>,
  private val disabledBays: List<Int>
) {
  private val parkingBays = mutableListOf<ParkingBay>()

  init {
    (1..squareSize.toDouble().pow(2.toDouble()).toInt()).forEach { index ->
      if (!pedestrianExits.contains(index)) {
        parkingBays.add(ParkingBay(index, disabledBays.contains(index), null))
      }
    }
  }

  fun parkCar(car: Char): Int {
    return when (car) {
      'C' -> {
        val emptyBays = parkingBays.filter { it.car != null || !it.isDisabledBay }
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
        selectedBayNumber
      }
      else -> -1
    }
  }

  fun getAvailableBays(): Int = parkingBays.filter { it.car == null }.size
}