package parking

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class ParkingTest {

  private fun getParking() =
    ParkingBuilder()
      .withSquareSize(5)
      .withPedestrianExit(8)
      .withPedestrianExit(12)
      .withDisabledBay(5)
      .withDisabledBay(10)
      .build()

  @Test
  fun testGetAvailableBays() {
    val parking = getParking()
    assertEquals(23, parking.getAvailableBays())
  }

  @Test
  fun testParkCarVehicleTypeC() {
    val parking = getParking()
    assertEquals(7, parking.parkCar('C'))
    assertEquals(22, parking.getAvailableBays())
  }

  @Test
  fun testParkCarVehicleTypeM() {
    assertEquals(7, getParking().parkCar('M'))
  }
}
