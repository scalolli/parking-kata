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
    val parking = getParking()
    assertEquals(7, parking.parkCar('M'))
    assertEquals(22, parking.getAvailableBays())
  }

  @Test
  fun testParkCarTwoVehicules() {
    val parking = getParking()
    assertEquals(7, parking.parkCar('C'))
    assertEquals(22, parking.getAvailableBays())
    assertEquals(9, parking.parkCar('M'))
    assertEquals(21, parking.getAvailableBays())
  }

  @Test
  fun testParkCarDisabled() {
    val parking = getParking()
    assertEquals(10, parking.parkCar('D'))
    assertEquals(22, parking.getAvailableBays())
    assertEquals(5, parking.parkCar('D'))
    assertEquals(21, parking.getAvailableBays())
    assertEquals(-1, parking.parkCar('D'))
    assertEquals(21, parking.getAvailableBays())
  }
}
